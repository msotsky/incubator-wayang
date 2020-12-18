package org.apache.incubator.wayang.giraph.operators;

import org.apache.incubator.wayang.core.optimizer.OptimizationContext;
import org.apache.incubator.wayang.core.plan.wayangplan.ExecutionOperator;
import org.apache.incubator.wayang.core.platform.ChannelInstance;
import org.apache.incubator.wayang.core.platform.lineage.ExecutionLineageNode;
import org.apache.incubator.wayang.core.util.Tuple;
import org.apache.incubator.wayang.giraph.execution.GiraphExecutor;

import java.util.Collection;

/**
 * {@link ExecutionOperator} that can be run by the {@link GiraphExecutor}.
 */
public interface GiraphExecutionOperator  extends ExecutionOperator{
    /**
     * Evaluates this operator. Takes a set of inputs and produces a set of outputs.
     * <p>In addition, this method should give feedback of what this instance was doing by wiring the
     * {@link org.apache.incubator.wayang.core.platform.lineage.LazyExecutionLineageNode}s of input and ouput {@link ChannelInstance}s and
     * providing a {@link Collection} of executed {@link OptimizationContext.OperatorContext}s.</p>
     *
     * @param inputs          {@link ChannelInstance}s that satisfy the inputs of this operator
     * @param outputs         {@link ChannelInstance}s that collect the outputs of this operator
     * @param operatorContext {@link OptimizationContext.OperatorContext} of this instance
     * @return {@link Collection}s of what has been executed and produced
     */
    Tuple<Collection<ExecutionLineageNode>, Collection<ChannelInstance>> execute(ChannelInstance[] inputs,
                                                                                 ChannelInstance[] outputs,
                                                                                 GiraphExecutor giraphExecutor,
                                                                                 OptimizationContext.OperatorContext operatorContext);
}