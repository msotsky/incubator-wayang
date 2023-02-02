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

package org.apache.wayang.agoraeo.operators.spark;

import org.apache.commons.io.FileUtils;
import org.apache.spark.api.java.function.FlatMapFunction;

import java.io.*;
import java.util.Iterator;
import java.util.stream.Stream;
import java.nio.file.*;

public class RunSen2Cor implements Serializable, FlatMapFunction<String, String> {

    private final String sen2cor;
    private final String l2a_location;

    public RunSen2Cor(String sen2cor, String l2a_location) {

        this.sen2cor = sen2cor;
        this.l2a_location = l2a_location;
    }

    @Override
    public Iterator<String> call(String s) throws Exception {
        try {
            String command = sen2cor + " " +
                    s + " " +
                    " --output_dir " + l2a_location;
            Process process = Runtime.getRuntime().exec(command);

            // TODO: ErrorStream should be redirected here

            Iterator<String> input = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream()
                    )
            ).lines().iterator();

//            File f = new File("/Users/rodrigopardomeza/tu-berlin/agoraeo/agoraeo/sen2cor_logs" + s.substring(0, s.length()-5) + ".log");
//            Files.copy(l, f, StandardCopyOption.REPLACE_EXISTING);
//            FileUtils.copyInputStreamToFile(l, f);

            return input;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}