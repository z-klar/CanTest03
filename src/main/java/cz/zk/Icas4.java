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
     * MESSAGE:
     *   00   09   10   00   TT   TT   XX   XX   YY   YY   00   AA
     *   -- prefix --   | -touch type-  --coordinates--   -postfix-
     *              timestamp: delay from last action in 10ms
     *  TouchType:  TOUCH   = 11  10
     *              UPDATE  = 11  11
     *              RELEASE = 11  12
     *  X, Y coordinates: coords in BIG Endian
     *  DLC code for CAN-FD frame with 12 bytes is 9
     *  MSG_ID = 483 402 347 = 0x1CD0 226B
     * @param x
     * @param y
     * @param type
     * @param id
     * @param isFd
     * @param bsr
     * @param ext
     * @return
     */
    public byte [] ComposeAudiTouchMessage(int x, int y, String type, int id,
                                       boolean isFd, boolean bsr, boolean ext) {
        byte[] outBuffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        int ourx = x;
        int oury = y;

        outBuffer[0] = (byte) (id & 0xff);
        outBuffer[1] = (byte) ((id >> 8) & 0xff);
        outBuffer[2] = (byte) ((id >> 16) & 0xff);
        outBuffer[3] = (byte) ((id >> 24) & 0x1f);
        outBuffer[3] &= 0x1f;
        if(isFd) outBuffer[3] |= 0x40;
        if(bsr) outBuffer[3] |= 0x20;
        if(ext) outBuffer[3] |= 0x80;

        outBuffer[4] = (byte) (9);   // message length

        String touchPrefixGlobal = getMessage("MSG_PREFIX");
        outBuffer[5] = (byte) (0x00);   // prefix
        outBuffer[6] = (byte) (0x09);   // prefix
        outBuffer[7] = (byte) (0x10);   // prefix
        outBuffer[8] = (byte) (0x00);   // timestamp
        if(type.equals("PRESS")) {
            outBuffer[9] = (byte) (0x11);
            outBuffer[10] = (byte) (0x10);
        }
        else {
            outBuffer[9] = (byte) (0x11);
            outBuffer[10] = (byte) (0x12);
        }
        outBuffer[11] = (byte)((x >> 8) & 0xff);  // HI byte of X
        outBuffer[12] = (byte)(x & 0xff);         // LO byte of X
        outBuffer[13] = (byte)((y >> 8) & 0xff);  // HI byte of Y
        outBuffer[14] = (byte)(y & 0xff);         // LO byte of Y
        outBuffer[15] = (byte) (0x00);            // postfix
        outBuffer[16] = (byte) (0x0a);            // postfix

        logMessageString(String.format("AudiTouchMsg: ID=%d TYPE=%s  X=%d  Y=%d  ",
                id, type, x, y));
        logMessageBytesRaw(outBuffer);

        return outBuffer;
    }

    public byte [] ComposeAudiFreeFormMessage(int dlcCode, int id, boolean isFd, boolean bsr,
                                              boolean ext, String [] data) {

        ArrayList<Byte> alBuff = new ArrayList<Byte>();

        alBuff.add((byte) (id & 0xff));
        alBuff.add((byte) ((id >> 8) & 0xff));
        alBuff.add((byte) ((id >> 16) & 0xff));
        byte bb = (byte) ((id >> 24) & 0x1f);
        if(isFd) bb |= 0x40;
        if(bsr) bb |= 0x20;
        if(ext) bb |= 0x80;
        alBuff.add(bb);

        alBuff.add((byte) (dlcCode));   // message length
        for (String sByte : data) {
            alBuff.add(ConvertStringToByte(sByte));
        }
        byte [] outBuffer = new byte[alBuff.size()];
        for(int i=0; i<alBuff.size(); i++) outBuffer[i] = alBuff.get(i);

        logMessageString(String.format("AudiFreeFormMsg: ID=%d ",  id));
        logMessageBytesRaw(outBuffer);

        return outBuffer;
    }

    private byte ConvertStringToByte(String sByte) {
        byte bRes;
        try {
            bRes = (byte)Integer.parseInt(sByte, 16);
            return bRes;
        }
        catch (Exception ex) {
            return 0;
        }
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

    private int [] MsgAllowedLengtg = {0,1,2,3,4,5,6,7,8,12,16,20,24,32,48,64};
    private int [] DlcCodes         = {0,1,2,3,4,5,6,7,8,9, 10,11,12,13,14,15};

    public int GetDlcCode(int MsgLen) {
        for(int i = 0; i<MsgAllowedLengtg.length; i++) {
            if(MsgAllowedLengtg[i] == MsgLen) return DlcCodes[i];
        }
        return -1;
    }

}
