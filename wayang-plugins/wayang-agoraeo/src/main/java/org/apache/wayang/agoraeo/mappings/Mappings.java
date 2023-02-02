/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.wayang.agoraeo.mappings;

import org.apache.wayang.agoraeo.mappings.java.Sen2CorWrapperJavaMapping;
import org.apache.wayang.agoraeo.mappings.java.SentinelSourceJavaMapping;
import org.apache.wayang.agoraeo.mappings.spark.Sen2CorWrapperSparkMapping;
import org.apache.wayang.agoraeo.mappings.spark.SentinelSourceSparkMapping;
import org.apache.wayang.core.mapping.Mapping;

import java.util.Arrays;
import java.util.Collection;

public class Mappings {

    public static Collection<Mapping> BASIC_MAPPINGS = Arrays.asList(
//            new SentinelSourceJavaMapping(),
//            new Sen2CorWrapperJavaMapping(),
            new SentinelSourceSparkMapping(),
            new Sen2CorWrapperSparkMapping()
    );

}
