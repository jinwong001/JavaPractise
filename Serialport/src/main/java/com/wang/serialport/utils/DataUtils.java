package com.wang.serialport.utils;


import java.util.ArrayList;
import java.util.List;

/**
 * 串口数据转换工具类
 * Created by Administrator on 2016/6/2.
 */
public class DataUtils {
    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //-------------------------------------------------------
    //Hex字符串转int
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static String IntToHex(int intHex) {
        return Integer.toHexString(intHex);
    }

    //-------------------------------------------------------
    //Hex字符串转byte
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    //1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串
    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串，可选长度
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 按照指定长度切割字符串
     *
     * @param inputString 需要切割的源字符串
     * @param length      指定的长度
     * @return
     */
    public static List<String> getDivLines(String inputString, int length) {
        List<String> divList = new ArrayList<String>();
        int remainder = (inputString.length()) % length;
        // 一共要分割成几段
        int number = (int) Math.floor((inputString.length()) / length);
        for (int index = 0; index < number; index++) {
            String childStr = inputString.substring(index * length, (index + 1) * length);
            divList.add(childStr);
        }
        if (remainder > 0) {
            String cStr = inputString.substring(number * length, inputString.length());
            divList.add(cStr);
        }
        return divList;
    }

    /**
     * 计算长度，两个字节长度
     *
     * @param val value
     * @return 结果
     */
    public static String twoByte(String val) {
        if (val.length() > 4) {
            val = val.substring(0, 4);
        } else {
            int l = 4 - val.length();
            for (int i = 0; i < l; i++) {
                val = "0" + val;
            }
        }
        return val;
    }

    /**
     * 校验和
     *
     * @param cmd 指令
     * @return 结果
     */
    public static String sum(String cmd) {
        List<String> cmdList = DataUtils.getDivLines(cmd, 2);
        int sumInt = 0;
        for (String c : cmdList) {
            sumInt += DataUtils.HexToInt(c);
        }
        String sum = DataUtils.IntToHex(sumInt);
        sum = DataUtils.twoByte(sum);
        cmd += sum;
        return cmd.toUpperCase();
    }

    public static byte[] int2ByteArray(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }

    public static int bytesToInt(byte[] src, int offset) {
        return (int) ((src[offset] & 0xFF) << 8) | (src[offset + 1] & 0xFF);
    }


    public static byte CONFIG_COD = (byte) 0xB1;    // 配置
    public static byte QUERY_COD = (byte) 0xB2;    // 状态查询
    public static byte PAY_COD = (byte) 0xB3;  // 订单支付
    public static byte MEMBER_COD = (byte) 0xB4;  // 会员识别

    public static byte[] parse(String content, byte type) {
        byte[] data = content.getBytes();
        int length = data.length + 9;

        byte[] result = new byte[length];

        result[0] = (byte) 0x3A;
        result[1] = (byte) 0x55;

        byte[] len = int2ByteArray(length - 8);
        System.arraycopy(len, 0, result, 2, 2);
        result[4] = type;

        System.arraycopy(data, 0, result, 5, length - 9);

        byte[] temp = new byte[length - 4];

        System.arraycopy(result, 0, temp, 0, length - 4);
        int crcInt = CRC16.CRC16_MODBUS(temp);

        byte[] crc = int2ByteArray(crcInt);

        System.arraycopy(crc, 0, result, length - 4, 2);

        result[length - 2] = (byte) 0x0D;
        result[length - 1] = (byte) 0x0A;
        return result;
    }

    public static String getData(byte[] raw) {
        if (raw == null || raw.length < 9) {
            return null;
        }

        int length = raw.length - 9;
        byte[] data = new byte[length];
        System.arraycopy(raw, 5, data, 0, length);
        return new String(data);
    }

    public static String getDataCheck(byte[] raw, byte type) {
        if (raw == null || raw.length < 9) {
            return null;
        }

        if (raw[0] != (byte) 0x3A) {
            return null;
        }

        if (raw[1] != (byte) 0x55) {
            return null;
        }

        if (raw[raw.length - 2] != (byte) 0x0D) {
            return null;
        }

        if (raw[raw.length - 1] != (byte) 0x0A) {
            return null;
        }

        if (raw[4] != type) {
            return null;
        }


        byte[] temp = new byte[raw.length - 4];
        System.arraycopy(raw, 0, temp, 0, raw.length - 4);


        int crc = CRC16.CRC16_MODBUS(temp);
        int crc2 = bytesToInt(raw, raw.length - 4);
        if (crc != crc2) {
            return null;
        }

        byte[] data = new byte[raw.length - 9];
        System.arraycopy(raw, 5, data, 0, raw.length - 9);
        System.out.println("--------------------");
        System.out.println(ByteArrToHex(data));
        System.out.println("--------------------");
        return new String(data);
    }
}
