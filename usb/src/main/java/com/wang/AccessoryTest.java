package com.wang;

import org.usb4java.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class AccessoryTest {
    private final static byte REQUEST_TYPE_READ = (byte) 0xC0;
    private final static byte END_POINT_IN = (byte) 0x81;
    private final static byte END_POINT_OUT = (byte) 0x03;

    //下边两个参数为系统中usb设备的VID和PID 需要自行配置
    private static short idVendor = (short) 0x2207;
    private static short idProduct = (short) 0x0011;
    private static short idProduct2 = (short) 0x2d01;
    private static boolean isAccessory = false;


    private static Context context;
    private static Device device;
    private static DeviceHandle handle;

    public static void main(String[] args) {
        try {
            init();

            if (!isAccessory) {
                int result = setupAccessory("Google, Inc.", "AccessoryChat", "Accessory Chat", "1.0", "http://www.android.com",
                        "0123456789");
                if (result != LibUsb.SUCCESS)
                    throw new LibUsbException("Unable to setup Accessory", result);
            }

//            device = findDevice(idProduct2);
//            result = LibUsb.open(device, handle);
//            if (result < 0)
//                throw new LibUsbException("Unable to open USB device", result);
//
//            result = LibUsb.claimInterface(handle, 0);
//            if (result != LibUsb.SUCCESS)
//                throw new LibUsbException("Unable to claim interface", result);


            writeAndRead();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LibUsb.releaseInterface(handle, 0);
            LibUsb.resetDevice(handle);
            LibUsb.close(handle);
            LibUsb.exit(context);
        }

    }

    private static void writeAndRead() {
        String question = "Hello Android I'll be your host today, how are you?";
        byte[] questionBuffer = question.getBytes();
        ByteBuffer questionData = BufferUtils.allocateByteBuffer(questionBuffer.length);
        questionData.put(questionBuffer);
        IntBuffer transferred = IntBuffer.allocate(1);

        int result = 0;
        //THIS IS THE PART WHERE IT FAILS!
        result = LibUsb.bulkTransfer(handle, END_POINT_OUT, questionData, transferred, 30000);

        if (result < 0) {
            throw new LibUsbException("Bulk write error!", result);
        }
    }

    private static void init() {
        context = new Context();
        int result = LibUsb.init(context);
        if (result != LibUsb.SUCCESS)
            throw new LibUsbException("Unable to initialize libusb.", result);

        device = findDevice(idProduct);

        handle = new DeviceHandle();

        result = LibUsb.open(device, handle);
        if (result < 0)
            throw new LibUsbException("Unable to open USB device", result);

        result = LibUsb.claimInterface(handle, 0);
        if (result != LibUsb.SUCCESS)
            throw new LibUsbException("Unable to claim interface", result);
    }

    private static Device findDevice(short productId) {
        // Read the USB device list
        DeviceList list = new DeviceList();
        int result = LibUsb.getDeviceList(context, list);
        if (result < 0)
            throw new LibUsbException("Unable to get device list", result);

        try {
            // Iterate over all devices and scan for the right one
            for (Device device : list) {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result != LibUsb.SUCCESS)
                    throw new LibUsbException("Unable to read device descriptor", result);
                if (descriptor.idProduct() == productId) {
                    return device;
                } else if (descriptor.idProduct() == idProduct2) {
                    isAccessory = true;
                    return device;
                }
            }
        } finally {
            // Ensure the allocated device list is freed
            LibUsb.freeDeviceList(list, true);
        }

        // Device not found
        return null;
    }

    private static int setupAccessory(String vendor, String model, String description, String version, String url,
                                      String serial) throws LibUsbException {

        int response = 0;

        // Setup setup token
        response = transferSetupPacket((short) 2, REQUEST_TYPE_READ, (byte) 51);

        // Setup data packet
        response = transferAccessoryDataPacket(vendor, (short) 0);
        response = transferAccessoryDataPacket(model, (short) 1);
        response = transferAccessoryDataPacket(description, (short) 2);
        response = transferAccessoryDataPacket(version, (short) 3);
        response = transferAccessoryDataPacket(url, (short) 4);
        response = transferAccessoryDataPacket(serial, (short) 5);

        // Setup handshake packet
        response = transferSetupPacket((short) 0, (byte) (LibUsb.REQUEST_TYPE_VENDOR | LibUsb.ENDPOINT_OUT), (byte) 53);

        // LibUsb.releaseInterface(handle, 0);
        return response;
    }

    private static int transferSetupPacket(short bufferLength, byte requestType, byte request) throws LibUsbException {
        int response = 0;
        byte[] bytebuff = new byte[bufferLength];
        ByteBuffer data = BufferUtils.allocateByteBuffer(bytebuff.length);
        data.put(bytebuff);

        final short wValue = 0;
        final short wIndex = 0;
        final long timeout = 1000;

        data.rewind();
        response = LibUsb.controlTransfer(handle, requestType, request, wValue, wIndex,
                data, timeout);

        if (response < 0)
            throw new LibUsbException("Unable to transfer setup packet ", response);

        return response;
    }

    private static int transferAccessoryDataPacket(String param, short index) {
        int response;
        byte[] byteArray = param.getBytes();
        ByteBuffer data = BufferUtils.allocateByteBuffer(byteArray.length);
        data.put(byteArray);
        final byte bRequest = (byte) 52;
        final short wValue = 0;
        final long timeout = 0;
        response = LibUsb.controlTransfer(handle, LibUsb.REQUEST_TYPE_VENDOR, bRequest, wValue, index,
                data, timeout);
        if (response < 0)
            throw new LibUsbException("Unable to control transfer.", response);
        return response;
    }
}
