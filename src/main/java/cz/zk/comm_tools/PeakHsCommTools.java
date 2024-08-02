package cz.zk.comm_tools;

import cz.zk.TouchType;
import cz.zk.frmMain;

import javax.swing.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PeakHsCommTools {
    private frmMain parent;

    public PeakHsCommTools(frmMain parent) {
        this.parent = parent;
    }

    public void SimulateTouch(int x, int y) {

        String msgId = parent.txAbtMessageId.getText();

        byte[] txBuff = ComposeTouchMessage(x, y, TouchType.PREPARE, msgId);
        int iRes = SendMessage(parent.txPeakHsIpAddress.getText(),
                Integer.parseInt(parent.txPeakHsPort.getText()), txBuff);
        txBuff = ComposeTouchMessage(x, y, TouchType.PRESS, msgId);
        iRes = SendMessage(parent.txPeakHsIpAddress.getText(),
                Integer.parseInt(parent.txPeakHsPort.getText()), txBuff);
        Delay(150);
        txBuff = ComposeTouchMessage(x, y, TouchType.RELEASE, msgId);
        iRes = SendMessage(parent.txPeakHsIpAddress.getText(),
                Integer.parseInt(parent.txPeakHsPort.getText()), txBuff);
    }

    private void Delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {

        }
    }

    public byte[] ComposeTouchMessage(int x, int y, TouchType touchType, String id) {
        String preTouchConstant = "";
        String postTouchConstant = "";
        byte[] outBuffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = paddString(id, 8);

        outBuffer[0] = (byte) (getHexValue(sId.substring(6, 7)) * 16 + getHexValue(sId.substring(7, 8)));
        outBuffer[1] = (byte) (getHexValue(sId.substring(4, 5)) * 16 + getHexValue(sId.substring(5, 6)));
        outBuffer[2] = (byte) (getHexValue(sId.substring(2, 3)) * 16 + getHexValue(sId.substring(3, 4)));
        outBuffer[3] = (byte) (getHexValue(sId.substring(0, 1)) * 16 + getHexValue(sId.substring(1, 2)));
        outBuffer[4] = (byte) (8);

        if (touchType == TouchType.PRESS) {
            preTouchConstant = getMessageBody("PRE_TOUCH_PRESS");
            postTouchConstant = getMessageBody("POST_TOUCH_PRESS");
        } else if (touchType == TouchType.RELEASE) {
            preTouchConstant = getMessageBody("PRE_TOUCH_RELEASE");
            postTouchConstant = getMessageBody("POST_TOUCH_RELEASE");
        } else if (touchType == TouchType.PREPARE) {
            preTouchConstant = getMessageBody("PRE_TOUCH_PRESS");
            postTouchConstant = "FD";
        }

        // divide x coords otherwise it overflows maximum bit size.
        x /= 2;

        // pad binary string with zeros. Length of the string must be 10.
        //String convertedY = padZeros(Integer.toBinaryString(y));
        //String convertedX = padZeros(Integer.toBinaryString(x));

        // 1. byte. Constant value matched to press or release type.
        // Defines strength (or distance from display) of the touch if supported by unit
        //String finalHexData = postTouchConstant;
        outBuffer[12] = (byte) convertByte(postTouchConstant);
        // 2. byte. last 8 bits of Y coords
        //finalHexData += binaryToHex(convertedY.substring(2, 10));
        outBuffer[11] = (byte) (y & 0xff);
        // 3. byte. last 6 bits of X coords and first 2 bits of Y coords
        //finalHexData += binaryToHex(convertedX.substring(4, 10) + convertedY.substring(0, 2));
        outBuffer[10] = (byte) (((x & 0x3f) << 2) | ((y & 0x300) >> 8));
        // 4. byte. 0001 prefix + first 4 bits of X coords
        //finalHexData += binaryToHex(getTouchConstant("X_COORDS_PREFIX", platform) + convertedX.substring(0, 4));
        outBuffer[9] = (byte) (0x10 | ((x & 0x3c0) >> 6));
        // 5. byte. Constant value matched to press or release type
        //finalHexData += preTouchConstant;
        outBuffer[8] = (byte) convertByte(preTouchConstant);
        // 6. - 8. byte. Constant value
        //finalHexData += getTouchConstant("TOUCH_PREFIX_GLOBAL", platform);
        String touchPrefixGlobal = getMessageBody("TOUCH_PREFIX_GLOBAL");
        outBuffer[5] = (byte) (convertByte(touchPrefixGlobal.substring(0, 2)));
        outBuffer[6] = (byte) (convertByte(touchPrefixGlobal.substring(2, 4)));
        outBuffer[7] = (byte) (convertByte(touchPrefixGlobal.substring(4)));

        parent.logging.logMessageString(String.format("ComposeToucMsg: ID=%s TYPE=%s  X=%d  Y=%d  ",
                id, touchType, x, y));
        parent.logging.logMessageBytesRaw(outBuffer);
        return outBuffer;
    }

    /****************************************************************************
     *
     * @param ipAddress
     * @param port
     * @param data
     * @return
     ****************************************************************************/
    public static int SendMessage(String ipAddress, int port, byte [] data) {
        DatagramSocket socket;
        InetAddress address;

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ipAddress);

            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            return(0);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            return(1);
        }
    }



}
