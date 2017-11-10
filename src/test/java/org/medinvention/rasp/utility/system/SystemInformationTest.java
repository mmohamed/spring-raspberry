/*******************************************************************************
 * Copyright 2017 Marouan MOHAMED
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.medinvention.rasp.utility.system;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.medinvention.rasp.BaseTestUnit;
import org.springframework.util.Assert;

public class SystemInformationTest extends BaseTestUnit {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testPrint() throws Exception {
        SystemInformation.print();
        Assert.isTrue(outContent.toString().length() > 0, "Has console output");
        Assert.isTrue(errContent.toString().length() == 0, "Execute witout error output");
    }

    @Test
    public void testBuild() throws Exception {
        SystemInformation information = SystemInformation.build();
        Assert.isInstanceOf(SystemInformation.class, information);
        Assert.isTrue(errContent.toString().length() == 0, "Execute witout error output");
    }
}
