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
package org.medinvention.rasp.utility.information;

import java.io.IOException;

import org.medinvention.rasp.utility.system.AbstractInformation;

import com.pi4j.system.SystemInfo;

public class CPU implements AbstractInformation {

    private String revision;

    private String architecture;

    private String part;

    private float temperature;

    private float coreVoltage;

    private String modelName;

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getCoreVoltage() {
        return coreVoltage;
    }

    public void setCoreVoltage(float coreVoltage) {
        this.coreVoltage = coreVoltage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public static CPU build() {
        CPU instance = new CPU();

        try {
            instance.setRevision(SystemInfo.getCpuRevision());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        try {
            instance.setPart(SystemInfo.getCpuPart());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        try {
            instance.setArchitecture(SystemInfo.getCpuArchitecture());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        try {
            instance.setTemperature(SystemInfo.getCpuTemperature());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        try {
            instance.setCoreVoltage(SystemInfo.getCpuVoltage());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        try {
            instance.setModelName(SystemInfo.getModelName());
        }
        catch (IOException | InterruptedException | UnsupportedOperationException e) {
        }

        return instance;
    }
}
