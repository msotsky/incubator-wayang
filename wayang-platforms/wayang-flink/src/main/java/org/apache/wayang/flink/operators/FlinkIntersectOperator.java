package org.apache.incubator.wayang.flink.operators;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.incubator.wayang.basic.operators.IntersectOperator;
import org.apache.incubator.wayang.core.function.TransformationDescriptor;
import org.apache.incubator.wayang.core.optimizer.OptimizationContext;
import org.apache.incubator.wayang.core.plan.wayangplan.ExecutionOperator;
import org.apache.incubator.wayang.core.platform.ChannelDescriptor;
import org.apache.incubator.wayang.core.platform.ChannelInstance;
import org.apache.incubator.wayang.core.platform.lineage.ExecutionLineageNode;
import org.apache.incubator.wayang.core.types.DataSetType;
import org.apache.incubator.wayang.core.util.Tuple;
import org.apache.incubator.wayang.flink.channels.DataSetChannel;
import org.apache.incubator.wayang.flink.execution.FlinkExecutor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Flink implementation of the {@link IntersectOperator}.
 */
public class FlinkIntersectOperator<Type> extends IntersectOperator<Type> implements FlinkExecutionOperator {


    public FlinkIntersectOperator(DataSetType<Type> dataSetType) {
        super(dataSetType);
    }

    public FlinkIntersectOperator(Class<Type> typeClass) {
        super(typeClass);
    }

    /**
     * Copies an instance (exclusive of broadcasts).
     *
     * @param that that should be copied
     */
    public FlinkIntersectOperator(IntersectOperator<Type> that) {
        super(that);
    }

    @Override
    public Tuple<Collection<ExecutionLineageNode>, Collection<ChannelInstance>> evaluate(
            ChannelInstance[] inputs,
            ChannelInstance[] outputs,
            FlinkExecutor flinkExecutor,
            OptimizationContext.OperatorContext operatorContext) {
        assert inputs.length == this.getNumInputs();
        assert outputs.length == this.getNumOutputs();

        final DataSetChannel.Instance input0 = (DataSetChannel.Instance) inputs[0];
        final DataSetChannel.Instance input1 = (DataSetChannel.Instance) inputs[1];
        final DataSetChannel.Instance output = (DataSetChannel.Instance) outputs[0];


        final DataSet<Type> dataSetInput0 = input0.provideDataSet();
        final DataSet<Type> dataSetInput1 = input1.provideDataSet();

        Class type_class = dataSetInput0.getType().getTypeClass();

        TransformationDescriptor<Type, Type> descriptor = new TransformationDescriptor<Type, Type>(
                type -> type,
                type_class,
                type_class
        );

        KeySelector<Type, Type> keySelector = flinkExecutor.compiler.compileKeySelector(descriptor);

        final DataSet<Type> dataSetOutput = dataSetInput0.join(dataSetInput1)
                .where(
                        keySelector
                ).equalTo(
                        keySelector
                ).distinct()
                .map(
                        new MapFunction<Tuple2<Type, Type>, Type>() {
                            @Override
                            public Type map(Tuple2<Type, Type> typeTypeTuple2) throws Exception {
                                return typeTypeTuple2.f0;
                            }
                        }
                );

        output.accept(dataSetOutput, flinkExecutor);

        return ExecutionOperator.modelLazyExecution(inputs, outputs, operatorContext);
    }

    @Override
    protected ExecutionOperator createCopy() {
        return new FlinkIntersectOperator<>(this.getType());
    }

    @Override
    public String getLoadProfileEstimatorConfigurationKey() {
        return "wayang.flink.intersect.load";
    }

    @Override
    public List<ChannelDescriptor> getSupportedInputChannels(int index) {
        assert index <= this.getNumInputs() || (index == 0 && this.getNumInputs() == 0);
        return Arrays.asList(DataSetChannel.DESCRIPTOR, DataSetChannel.DESCRIPTOR_MANY);
    }

    @Override
    public List<ChannelDescriptor> getSupportedOutputChannels(int index) {
        assert index <= this.getNumOutputs() || (index == 0 && this.getNumOutputs() == 0);
        return Arrays.asList(DataSetChannel.DESCRIPTOR, DataSetChannel.DESCRIPTOR_MANY);
    }

    @Override
    public boolean containsAction() {
        return false;
    }
}