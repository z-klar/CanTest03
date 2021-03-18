package cz.zk;

import javax.swing.*;
import java.util.ArrayList;

import static cz.zk.Tools.getHexValue;

public class Icas4 {

    private ArrayList<AbtMessage> icas4Messages = new ArrayList<>();
    private DefaultListModel<String> dlmLog;

    public Icas4(ArrayList<AbtMessage> msg, DefaultListModel<String> log) {
        this.icas4Messages = msg;
        this.dlmLog = log;
    }

    /**
     *
     * @param x
     * @param y
     * @param type
     * @return
     */
    public byte [] ComposeIcas4Message(int x, int y, String type, String id,
                                       boolean isFd, boolean bsr, boolean ext) {
        byte[] outBuffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = Tools.paddString(id, 8);
        int ourx = x / 2;
        int oury = y;

        outBuffer[0] = (byte) (getHexValue(sId.substring(6, 7)) * 16 + getHexValue(sId.substring(7, 8)));
        outBuffer[1] = (byte) (getHexValue(sId.substring(4, 5)) * 16 + getHexValue(sId.substring(5, 6)));
        outBuffer[2] = (byte) (getHexValue(sId.substring(2, 3)) * 16 + getHexValue(sId.substring(3, 4)));
        outBuffer[3] = (byte) (getHexValue(sId.substring(0, 1)) * 16 + getHexValue(sId.substring(1, 2)));
        outBuffer[3] &= 0x1f;
        if(isFd) outBuffer[3] |= 0x40;
        if(bsr) outBuffer[3] |= 0x20;
        if(ext) outBuffer[3] |= 0x80;

        outBuffer[4] = (byte) (8);   // message length

        String touchPrefixGlobal = getMessage("MSG_PREFIX");
        outBuffer[5] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(0, 2)));
        outBuffer[6] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(2, 4)));
        outBuffer[7] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(4, 6)));

        String preTouchMsg = getMessage("PRE_TOUCH_" + type);
        outBuffer[8] = (byte) (Tools.convertByte(preTouchMsg));
        // byte[4]: 0001 XXXX, where XXXX top 4 bits of X
        outBuffer[9] = (byte) (0x10 + (ourx >> 6) & 0x0f);
        // byte[5]: XXXX XXYY - lower 6 bits of X + 2 top bits of Y
        outBuffer[10] = (byte) (((ourx & 0x3f) << 2) + ((oury >> 8) & 0x03));
        // byte[6]:  lower 8 bits ox Y
        outBuffer[11] = (byte) (oury & 0xff);
        String postTouchMsg = getMessage("POST_TOUCH_" + type);
        outBuffer[12] = (byte) (Tools.convertByte(postTouchMsg));

        logMessageString(String.format("ComposeIcas4Msg: ID=%s TYPE=%s  X=%d  Y=%d  ",
                id, type, x, y));
        logMessageBytesRaw(outBuffer);

        return outBuffer;
    }

    /**
     *
     * @param type
     * @return
     */
    private String getMessage(String type) {
        for(AbtMessage msg : icas4Messages) {
            if(msg.getName().equals(type)) {
                return msg.getBody();
            }
        }
        return "ER";
    }
    /**
     * @param msg
     */
    private void logMessageString(String msg) {
        dlmLog.addElement(msg);
    }

    /**
     * @param stitle
     * @param data
     */
    private void logMessageBytes(String stitle, byte[] data) {
        String ss;
        dlmLog.addElement(stitle);
        String spom = "";
        for (int i = 0; i < data.length; i++) {
            if (i == 0) ss = String.format("%02X", data[i]);
            else ss = String.format(":%02X", data[i]);
            spom += ss;
        }
        dlmLog.addElement(spom);
    }

    private void logMessageBytesRaw(byte[] data) {
        String spom = "";
        for (int i = 0; i < data.length; i++) {
            String ss = String.format("%02X:", data[i]);
            spom += ss;
        }
        dlmLog.addElement(spom);
    }

}
