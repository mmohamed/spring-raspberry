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

public class Memory implements AbstractInformation {

    private long total;

    private long used;

    private long free;

    private long shared;

    private long buffers;

    private long cached;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public long getShared() {
        return shared;
    }

    public void setShared(long shared) {
        this.shared = shared;
    }

    public long getBuffers() {
        return buffers;
    }

    public void setBuffers(long buffers) {
        this.buffers = buffers;
    }

    public long getCached() {
        return cached;
    }

    public void setCached(long cached) {
        this.cached = cached;
    }

    public static Memory build() {
        Memory instance = new Memory();

        try {
            instance.setTotal(SystemInfo.getMemoryTotal());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setUsed(SystemInfo.getMemoryUsed());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setFree(SystemInfo.getMemoryFree());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setShared(SystemInfo.getMemoryShared());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setBuffers(SystemInfo.getMemoryBuffers());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        try {
            instance.setCached(SystemInfo.getMemoryCached());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException e) {
        }

        return instance;
    }
}
