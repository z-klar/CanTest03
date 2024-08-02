package cz.zk.comm_tools;

import cz.zk.AbtMessage;
import cz.zk.Tools;
import cz.zk.TouchType;
import cz.zk.frmMain;

import javax.swing.*;

import static cz.zk.Tools.getHexValue;

public class CanlancCommTools {
    private frmMain parent;

    public CanlancCommTools(frmMain parent) {
        this.parent = parent;
    }

    public void SimulateTouch(int x, int y) {

        String msgId = parent.txAbtMessageId.getText();

        byte[] txBuff = ComposeTouchMessage(x, y, TouchType.PREPARE, msgId);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff);
        txBuff = ComposeTouchMessage(x, y, TouchType.PRESS, msgId);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff);
        Delay(150);
        txBuff = ComposeTouchMessage(x, y, TouchType.RELEASE, msgId);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff);
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

        String sId = Tools.paddString(id, 8);

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
        outBuffer[12] = (byte) Tools.convertByte(postTouchConstant);
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
        outBuffer[8] = (byte) Tools.convertByte(preTouchConstant);
        // 6. - 8. byte. Constant value
        //finalHexData += getTouchConstant("TOUCH_PREFIX_GLOBAL", platform);
        String touchPrefixGlobal = getMessageBody("TOUCH_PREFIX_GLOBAL");
        outBuffer[5] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(0, 2)));
        outBuffer[6] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(2, 4)));
        outBuffer[7] = (byte) (Tools.convertByte(touchPrefixGlobal.substring(4)));

        parent.logging.logMessageString(String.format("ComposeToucMsg: ID=%s TYPE=%s  X=%d  Y=%d  ",
                id, touchType, x, y));
        parent.logging.logMessageBytesRaw(outBuffer);
        return outBuffer;
    }

    private String getMessageBody(String msgName) {

        for (AbtMessage msg : parent.canMessages) {
            if (msg.getName().equals(msgName)) {
                return (msg.getBody());
            }
        }
        return ("");
    }
    public void LaunchSequence() {
        int delay = Integer.parseInt(parent.txDelay.getText());
        String msgBody1 = getMessageBody(parent.cbMessageType10.getSelectedItem().toString());
        String msgBody2 = getMessageBody(parent.cbMessageType11.getSelectedItem().toString());
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody1, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG #1: IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMessage(msgId, msgBody2, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG #2: IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    //------------------------------------------------------------
    public void LaunchSequence(String type1, String type2) {
        int delay = Integer.parseInt(parent.txDelay.getText());
        String msgBody1 = getMessageBody(type1);
        String msgBody2 = getMessageBody(type2);
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody1, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG %s - #1: IP addr: %s   PORT: %s",
                type1, parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMessage(msgId, msgBody2, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG %s - #2: IP addr: %s   PORT: %s",
                type2, parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }
    //------------------------------------------------------------
    public void SimulateIcas4Touch(int x, int y) {

        String msgId = parent.txAbtMessageId.getText();
        int delay;
        try {
            delay = Integer.parseInt(parent.txIcas4Delay.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid delay value !");
            return;
        }
        if (delay > 1000) {
            JOptionPane.showMessageDialog(null, "Invalid delay value - valid = 0 to 1000 !");
            return;
        }

        byte[] txBuff = parent.icas4.ComposeIcas4Message(x, y, "PRESS", msgId,
                parent.chkIcas4Fd.isSelected(), parent.chkIcas4Bsr.isSelected(), parent.chkIcas4Ext.isSelected());
        int iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        if (parent.chkIcas4SendReleaseMessage.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeIcas4Message(x, y, "RELEASE", msgId,
                    parent.chkIcas4Fd.isSelected(), parent.chkIcas4Bsr.isSelected(), parent.chkIcas4Ext.isSelected());
            iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                    Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        }
        if (parent.chkIcas4SendUpdate.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeIcas4Message(x, y, "UPDATE", msgId,
                    parent.chkIcas4Fd.isSelected(), parent.chkIcas4Bsr.isSelected(), parent.chkIcas4Ext.isSelected());
            iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                    Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        }
    }

    //------------------------------------------------------------
    public void SimulateAudiTouch(int x, int y) {

        int msgId = Integer.parseInt(parent.txAudiAbtMessageId.getText());
        int delay;
        try {
            delay = Integer.parseInt(parent.txAudiDelay.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid delay value !");
            return;
        }
        if (delay > 1000) {
            JOptionPane.showMessageDialog(null, "Invalid delay value - valid = 0 to 1000 !");
            return;
        }

        byte[] txBuff = parent.icas4.ComposeAudiTouchMessage(x, y, "PRESS", msgId,
                parent.chkAudiFD.isSelected(), parent.chkAudiBRS.isSelected(), parent.chkAudiEXT.isSelected());
        int iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        if (parent.chkAudiSendRelease.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeAudiTouchMessage(x, y, "RELEASE", msgId,
                    parent.chkAudiFD.isSelected(), parent.chkAudiBRS.isSelected(), parent.chkAudiEXT.isSelected());
            iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                    Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        }
    }

    public void SendAudiFreeFormMsg() {

        int msgId = Integer.parseInt(parent.txAudiAbtMessageId.getText());
        String[] sData = parent.txAudiFreeFormMsg.getText().split(" ");
        int msgLen = sData.length;
        int dlcCode = parent.icas4.GetDlcCode(msgLen);
        if (dlcCode == -1) {
            JOptionPane.showMessageDialog(null, "Invalid data length !");
            return;
        }

        byte[] txBuff = parent.icas4.ComposeAudiFreeFormMessage(dlcCode, msgId,
                parent.chkAudiFD.isSelected(), parent.chkAudiBRS.isSelected(), parent.chkAudiEXT.isSelected(), sData);
        int iRes = Tools.SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
    }

    /**
     *
     */
    public void SendMflSequence() {
        int delay = Integer.parseInt(parent.txMflMessageDelay.getText());
        String msgBody1 = getMflMessageBody(parent.cbMflMessage1.getSelectedItem().toString());
        String msgBody2 = getMflMessageBody(parent.cbMflMessage2.getSelectedItem().toString());
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;

        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody1, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("***** SendMflSeq #1 ******* : IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMflMessage(msgId, msgBody2, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("***** SendMflSeq #2 ******* : IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    private String getMflMessageBody(String msgName) {

        for (AbtMessage msg : parent.mflMessages) {
            if (msg.getName().equals(msgName)) {
                return (msg.getBody());
            }
        }
        return ("");
    }

    public void SendSingleMflMsg() {
        String msgBody = getMflMessageBody(parent.cbMflMsgType.getSelectedItem().toString());
        if (msgBody.length() == 0) {
            JOptionPane.showMessageDialog(null, "Message not found !!!!!");
            return;
        }
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("****** SendSingleMflMsg ******   IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendMflRawMsg() {
        String msgBody = parent.txMflRawMsgValue.getText();
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawMflMsg******  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendRawSequence() {
        String msgBody = parent.txMflRawMsgValue1.getText();
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawSequence - #1 *****  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        int delay = Integer.parseInt(parent.txMflMessageDelay2.getText());
        Delay(delay);

        msgBody = parent.txMflRawMsgValue2.getText();
        msgId = parent.txMflButtonsMsgId.getText();
        msgLen = 4;
        outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawSequence - #2 *****  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendMessage1() {
        String msgBody = getMessageBody(parent.cbMessageType1.getSelectedItem().toString());
        if (msgBody.length() == 0) {
            JOptionPane.showMessageDialog(null, "Message not found !!!!!");
            return;
        }
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;
        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody, msgLen);
        parent.logging.logMessageString(String.format("IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }



}
