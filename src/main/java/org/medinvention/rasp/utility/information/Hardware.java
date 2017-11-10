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

public class Hardware implements AbstractInformation {

    private String serialNumber;

    private CPU cpu;

    private Memory memory;

    private String processor;

    private String hardware;

    private String revision;

    private boolean isHardFloat;

    private String bordType;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public boolean isHardFloat() {
        return isHardFloat;
    }

    public void setHardFloat(boolean isHardFloat) {
        this.isHardFloat = isHardFloat;
    }

    public String getBordType() {
        return bordType;
    }

    public void setBordType(String bordType) {
        this.bordType = bordType;
    }

    public static Hardware build() {
        Hardware instance = new Hardware();

        instance.setCpu(CPU.build());
        instance.setMemory(Memory.build());
        // instance.setHardFloat(SystemInfo.isHardFloatAbi());
        // because it's contain print stack exception
        instance.setHardFloat(false);

        try {
            instance.setSerialNumber(SystemInfo.getSerial());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setProcessor(SystemInfo.getProcessor());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setHardware(SystemInfo.getHardware());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setRevision(SystemInfo.getRevision());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setBordType(SystemInfo.getBoardType().name());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }
        return instance;
    }
}
