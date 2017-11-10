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
package org.medinvention.rasp.service;

import java.util.Random;

import org.medinvention.rasp.utility.system.SystemInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SystemService {

    static Logger log = LoggerFactory.getLogger(SystemService.class);

    @Autowired
    private MessageSendingOperations<String> messagingTemplate;

    @Scheduled(fixedDelay = 3000)
    public void sendInformation() throws Exception {
        SystemInformation information = this.get();

        ObjectMapper mapper = new ObjectMapper();

        this.messagingTemplate.convertAndSend("/ws/status", mapper.writeValueAsString(information));
    }

    public SystemInformation get() {
        try {
            return SystemInformation.build();
        }
        catch (Exception ex) {
            // log.error("Cannot get system information , simulate status with
            // random data");
            return SystemService.randomize();
        }
    }

    public static SystemInformation randomize() {
        String information = "{\"platform\":{\"name\":\"Raspberry Pi\",\"id\":\"raspberrypi\"},\"hardware\":{\"serialNumber\":\"00000000e341aaec\",\"cpu\":{\"revision\":\"4\",\"architecture\":\"7\",\"part\":\"0xd03\",\"temperature\":54.8,\"coreVoltage\":1.2875,\"modelName\":\"ARMv7 Processor rev 4 (v7l)\"},\"memory\":{\"total\":968204288,\"used\":350916608,\"free\":617263104,\"shared\":12091392,\"buffers\":39936000,\"cached\":154460160},\"processor\":\"3\",\"hardware\":\"BCM2835\",\"revision\":\"a02082\",\"bordType\":\"RaspberryPi_3B\",\"hardFloat\":true}}";
        Random rand = new Random();

        String randomTemperature = (rand.nextInt((75 - 50) + 1) + 50) + "." + (rand.nextInt(10));
        information = information.replace("54.8", randomTemperature);

        String randomVoltage = "1." + rand.nextInt(7100);
        information = information.replace("1.2875", randomVoltage);

        int randomUsedMemory = rand.nextInt(1024 * 1024 * 1024);
        information = information.replace("350916608", String.valueOf(randomUsedMemory));

        int randomCachedMemory = rand.nextInt(1024 * 1024 * 1024);
        information = information.replace("154460160", String.valueOf(randomCachedMemory));

        int randomSharedMemory = rand.nextInt(1024 * 1024 * 1024);
        information = information.replace("12091392", String.valueOf(randomSharedMemory));

        int randomBufferMemory = rand.nextInt(1024 * 1024 * 1024);
        information = information.replace("39936000", String.valueOf(randomBufferMemory));

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(information, SystemInformation.class);
        }
        catch (Exception e) {
            return new SystemInformation();
        }
    }
}
