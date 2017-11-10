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

import java.io.IOException;
import java.text.ParseException;

import org.medinvention.rasp.utility.information.Hardware;
import org.medinvention.rasp.utility.information.Platform;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

public class SystemInformation {

    private Platform platform;

    private Hardware hardware;

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Hardware getHardware() {
        return hardware;
    }

    public void setHardware(Hardware hardware) {
        this.hardware = hardware;
    }

    public static SystemInformation build() {
        SystemInformation instance = new SystemInformation();

        instance.setPlatform(Platform.build());
        instance.setHardware(Hardware.build());

        return instance;
    }

    public static void print() {
        // display a few of the available system information properties
        System.out.println("----------------------------------------------------");
        System.out.println("PLATFORM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel());
        }
        catch (UnsupportedOperationException ex) {
        }
        try {
            System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId());
        }
        catch (UnsupportedOperationException ex) {
        }
        System.out.println("----------------------------------------------------");
        System.out.println("HARDWARE INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        }
        catch (UnsupportedOperationException | IOException | NumberFormatException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
        }
        catch (UnsupportedOperationException | IOException | NumberFormatException | InterruptedException ex) {
        }
        try {
            System.out.println("CPU Model Name    :  " + SystemInfo.getModelName());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Processor         :  " + SystemInfo.getProcessor());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Hardware          :  " + SystemInfo.getHardware());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }

        // System.out.println("Is Hard Float ABI : " +
        // SystemInfo.isHardFloatAbi());
        // because it's contain print stack exception
        System.out.println("Is Hard Float ABI :  false");

        try {
            System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("MEMORY INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
        }
        catch (UnsupportedOperationException | IOException | NumberFormatException | InterruptedException ex) {
        }
        try {
            System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
        }
        catch (UnsupportedOperationException | IOException | NumberFormatException | InterruptedException ex) {
        }
        try {
            System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());
        }
        catch (UnsupportedOperationException | IOException | NumberFormatException | InterruptedException ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("OPERATING SYSTEM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("OS Name           :  " + SystemInfo.getOsName());
        }
        catch (UnsupportedOperationException ex) {
        }
        try {
            System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
        }
        catch (UnsupportedOperationException ex) {
        }
        try {
            System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
        }
        catch (UnsupportedOperationException ex) {
        }
        try {
            System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException | ParseException ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("JAVA ENVIRONMENT INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
        System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
        System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

        System.out.println("----------------------------------------------------");
        System.out.println("NETWORK INFO");
        System.out.println("----------------------------------------------------");
        try {
            // display some of the network information
            System.out.println("Hostname          :  " + NetworkInfo.getHostname());
            for (String ipAddress : NetworkInfo.getIPAddresses())
                System.out.println("IP Addresses      :  " + ipAddress);
            for (String fqdn : NetworkInfo.getFQDNs())
                System.out.println("FQDN              :  " + fqdn);
            for (String nameserver : NetworkInfo.getNameservers())
                System.out.println("Nameserver        :  " + nameserver);
        }
        catch (NullPointerException | IOException | InterruptedException e) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CODEC INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CLOCK INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }
        try {
            System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
        }
        catch (UnsupportedOperationException | IOException | InterruptedException ex) {
        }

        System.out.println();
        System.out.println();
        System.out.println("Finish SystemInformation");
    }
}
