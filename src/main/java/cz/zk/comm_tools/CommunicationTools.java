package cz.zk.comm_tools;

import cz.zk.TouchType;
import cz.zk.frmMain;

public class CommunicationTools {
    private frmMain parent;

    public CommunicationTools(frmMain parent) {
        this.parent = parent;
    }

    public byte[] ComposeTouchMessage(int x, int y, TouchType touchType) {
        String preTouchConstant = "";
        String postTouchConstant = "";
        byte[] outBuffer = {0, 0, 0, 0, 0, 0, 0, 0};

        if (touchType == cz.zk.TouchType.PRESS) {
            preTouchConstant = parent.commonTools.getMessageBody("PRE_TOUCH_PRESS");
            postTouchConstant = parent.commonTools.getMessageBody("POST_TOUCH_PRESS");
        } else if (touchType == cz.zk.TouchType.RELEASE) {
            preTouchConstant = parent.commonTools.getMessageBody("PRE_TOUCH_RELEASE");
            postTouchConstant = parent.commonTools.getMessageBody("POST_TOUCH_RELEASE");
        } else if (touchType == cz.zk.TouchType.PREPARE) {
            preTouchConstant = parent.commonTools.getMessageBody("PRE_TOUCH_PRESS");
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
        outBuffer[0] = (byte) parent.commonTools.convertByte(postTouchConstant);
        // 2. byte. last 8 bits of Y coords
        //finalHexData += binaryToHex(convertedY.substring(2, 10));
        outBuffer[1] = (byte) (y & 0xff);
        // 3. byte. last 6 bits of X coords and first 2 bits of Y coords
        //finalHexData += binaryToHex(convertedX.substring(4, 10) + convertedY.substring(0, 2));
        outBuffer[2] = (byte) (((x & 0x3f) << 2) | ((y & 0x300) >> 8));
        // 4. byte. 0001 prefix + first 4 bits of X coords
        //finalHexData += binaryToHex(getTouchConstant("X_COORDS_PREFIX", platform) + convertedX.substring(0, 4));
        outBuffer[3] = (byte) (0x10 | ((x & 0x3c0) >> 6));
        // 5. byte. Constant value matched to press or release type
        //finalHexData += preTouchConstant;
        outBuffer[4] = (byte) parent.commonTools.convertByte(preTouchConstant);
        // 6. - 8. byte. Constant value
        //finalHexData += getTouchConstant("TOUCH_PREFIX_GLOBAL", platform);
        String touchPrefixGlobal = parent.commonTools.getMessageBody("TOUCH_PREFIX_GLOBAL");
        outBuffer[5] = (byte) (parent.commonTools.convertByte(touchPrefixGlobal.substring(0, 2)));
        outBuffer[6] = (byte) (parent.commonTools.convertByte(touchPrefixGlobal.substring(2, 4)));
        outBuffer[7] = (byte) (parent.commonTools.convertByte(touchPrefixGlobal.substring(4)));

        parent.logging.logMessageString(String.format("ComposeToucMsg: TYPE=%s  X=%d  Y=%d  ",
                touchType, x, y));
        parent.logging.logMessageBytesRaw(outBuffer);
        return outBuffer;
    }

}
