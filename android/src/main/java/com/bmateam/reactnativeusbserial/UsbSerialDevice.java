package com.bmateam.reactnativeusbserial;

import com.facebook.react.bridge.Promise;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

public class UsbSerialDevice {
    private UsbSerialPort port;
    private static final int SERIAL_TIMEOUT = 1000;

    public UsbSerialDevice(UsbSerialPort port) {
        this.port = port;
    }

    public void writeAsync(String value, Promise promise) {

        if (port != null) {

            try {
                port.write(value.getBytes(), SERIAL_TIMEOUT);

                promise.resolve(value);
            } catch (IOException e) {
                promise.reject(e);
            }

        } else {
            promise.reject(getNoPortErrorMessage());
        }
    }

    public void readAsync(Promise promise) {
        byte buffer[] = new byte[1000];
        try {
            if (port != null) {
                String result = "";
                int numBytesRead = port.read(buffer, 1000);
                while (numBytesRead != 0) {
                    result += new String(buffer);
                    numBytesRead = port.read(buffer, 1000);
                } 
                
                promise.resolve(result);
            } else {
                promise.resolve(getNoPortErrorMessage());
            }
        } catch (IOException e) {
            promise.reject(e);
        }
        
    }

    private Exception getNoPortErrorMessage() {
        return new Exception("No port present for the UsbSerialDevice instance");
    }
}
