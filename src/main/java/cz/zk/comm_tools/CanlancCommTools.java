package cz.zk.comm_tools;

import cz.zk.AbtMessage;
import cz.zk.TouchType;
import cz.zk.frmMain;

import javax.swing.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CanlancCommTools {
    private frmMain parent;

    public CanlancCommTools(frmMain parent) {
        this.parent = parent;
    }

    public void SimulateTouch(int x, int y) {

        String msgId = parent.txAbtMessageId.getText();

        byte[] txBuff = ComposeTouchMessage(x, y, TouchType.PREPARE, msgId);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff);
        txBuff = ComposeTouchMessage(x, y, TouchType.PRESS, msgId);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff);
        Delay(150);
        txBuff = ComposeTouchMessage(x, y, TouchType.RELEASE, msgId);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
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

        String sId = parent.commonTools.paddString(id, 8);

        outBuffer[0] = (byte) (parent.commonTools.getHexValue(sId.substring(6, 7)) * 16 + parent.commonTools.getHexValue(sId.substring(7, 8)));
        outBuffer[1] = (byte) (parent.commonTools.getHexValue(sId.substring(4, 5)) * 16 + parent.commonTools.getHexValue(sId.substring(5, 6)));
        outBuffer[2] = (byte) (parent.commonTools.getHexValue(sId.substring(2, 3)) * 16 + parent.commonTools.getHexValue(sId.substring(3, 4)));
        outBuffer[3] = (byte) (parent.commonTools.getHexValue(sId.substring(0, 1)) * 16 + parent.commonTools.getHexValue(sId.substring(1, 2)));
        outBuffer[4] = (byte) (8);

        byte [] canMessage = parent.communicationTools.ComposeTouchMessage(x, y, touchType);
        for(int i=0; i<8; i++) outBuffer[5+i] = canMessage[i];

        parent.logging.logMessageString(String.format("ComposeToucMsg: ID=%s TYPE=%s  X=%d  Y=%d  ",
                id, touchType, x, y));
        parent.logging.logMessageBytesRaw(outBuffer);
        return outBuffer;
    }

    public void LaunchSequence() {
        int delay = Integer.parseInt(parent.txDelay.getText());
        String msgBody1 = parent.commonTools.getMessageBody(parent.cbMessageType10.getSelectedItem().toString());
        String msgBody2 = parent.commonTools.getMessageBody(parent.cbMessageType11.getSelectedItem().toString());
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = ComposeMessage(msgId, msgBody1, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG #1: IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = ComposeMessage(msgId, msgBody2, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG #2: IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    //------------------------------------------------------------
    public void LaunchSequence(String type1, String type2) {
        int delay = Integer.parseInt(parent.txDelay.getText());
        String msgBody1 = parent.commonTools.getMessageBody(type1);
        String msgBody2 = parent.commonTools.getMessageBody(type2);
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = ComposeMessage(msgId, msgBody1, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG %s - #1: IP addr: %s   PORT: %s",
                type1, parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = ComposeMessage(msgId, msgBody2, msgLen);
        parent.logging.logMessageString(String.format("Sending MSG %s - #2: IP addr: %s   PORT: %s",
                type2, parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
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
        int iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        if (parent.chkIcas4SendReleaseMessage.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeIcas4Message(x, y, "RELEASE", msgId,
                    parent.chkIcas4Fd.isSelected(), parent.chkIcas4Bsr.isSelected(), parent.chkIcas4Ext.isSelected());
            iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                    Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        }
        if (parent.chkIcas4SendUpdate.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeIcas4Message(x, y, "UPDATE", msgId,
                    parent.chkIcas4Fd.isSelected(), parent.chkIcas4Bsr.isSelected(), parent.chkIcas4Ext.isSelected());
            iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
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
        int iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()), txBuff, parent.dlmLog);
        if (parent.chkAudiSendRelease.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = parent.icas4.ComposeAudiTouchMessage(x, y, "RELEASE", msgId,
                    parent.chkAudiFD.isSelected(), parent.chkAudiBRS.isSelected(), parent.chkAudiEXT.isSelected());
            iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
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
        int iRes = SendMessageWithLog(parent.txCanlancIpAddress.getText(),
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

        byte[] outBuff = ComposeMflMessage(msgId, msgBody1, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("***** SendMflSeq #1 ******* : IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = ComposeMflMessage(msgId, msgBody2, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("***** SendMflSeq #2 ******* : IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
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
        byte[] outBuff = ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("****** SendSingleMflMsg ******   IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendMflRawMsg() {
        String msgBody = parent.txMflRawMsgValue.getText();
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawMflMsg******  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendRawSequence() {
        String msgBody = parent.txMflRawMsgValue1.getText();
        String msgId = parent.txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawSequence - #1 *****  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);

        int delay = Integer.parseInt(parent.txMflMessageDelay2.getText());
        Delay(delay);

        msgBody = parent.txMflRawMsgValue2.getText();
        msgId = parent.txMflButtonsMsgId.getText();
        msgLen = 4;
        outBuff = ComposeMflMessage(msgId, msgBody, msgLen, parent.chkMflCanFD.isSelected(),
                parent.chkMflBrs.isSelected(), parent.chkMflExt.isSelected());
        parent.logging.logMessageString(String.format("**** SendRawSequence - #2 *****  IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }

    public void SendMessage1() {
        String msgBody = parent.commonTools.getMessageBody(parent.cbMessageType1.getSelectedItem().toString());
        if (msgBody.length() == 0) {
            JOptionPane.showMessageDialog(null, "Message not found !!!!!");
            return;
        }
        String msgId = parent.txAbtMessageId.getText();
        int msgLen = 8;
        byte[] outBuff = ComposeMessage(msgId, msgBody, msgLen);
        parent.logging.logMessageString(String.format("IP addr: %s   PORT: %s",
                parent.txCanlancIpAddress.getText(), parent.txCanlancPort.getText()));
        parent.logging.logMessageBytes("Message sent:", outBuff);
        int iRes = SendMessage(parent.txCanlancIpAddress.getText(),
                Integer.parseInt(parent.txCanlancPort.getText()),
                outBuff);
    }


    /************************************************************************
     *
     * @param id
     * @param body
     * @param length
     * @return
     ************************************************************************/
    private byte[] ComposeMessage(String id, String body, int length) {
        byte [] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = parent.commonTools.paddString(id, 8);
        String sBody = parent.commonTools.paddString(body, 16);

        buffer[0] = (byte)(parent.commonTools.getHexValue(sId.substring(6,7)) * 16 + parent.commonTools.getHexValue(sId.substring(7,8)));
        buffer[1] = (byte)(parent.commonTools.getHexValue(sId.substring(4,5)) * 16 + parent.commonTools.getHexValue(sId.substring(5,6)));
        buffer[2] = (byte)(parent.commonTools.getHexValue(sId.substring(2,3)) * 16 + parent.commonTools.getHexValue(sId.substring(3,4)));
        buffer[3] = (byte)(parent.commonTools.getHexValue(sId.substring(0,1)) * 16 + parent.commonTools.getHexValue(sId.substring(1,2)));
        buffer[4] = (byte)(length & 0x0f);
        for(int i = 0; i<length; i++) {
            int ival1 = parent.commonTools.getHexValue(body.substring(2*i,2*i+1));     // HI nibble
            int ival2 = parent.commonTools.getHexValue(body.substring(2*i+1,2*i+2));   // LO nibble
            buffer[5+i] = (byte)(16 * ival1 + ival2);
        }

        return(buffer);
    }
    /**
     *
     * @param id
     * @param body
     * @param length
     * @param isFd
     * @param bsr
     * @return
     */
    public byte[] ComposeMflMessage(String id, String body, int length,
                                           boolean isFd, boolean bsr, boolean ext) {
        byte [] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = parent.commonTools.paddString(id, 8);
        String sBody = parent.commonTools.paddString(body, 8);

        buffer[0] = (byte)(parent.commonTools.getHexValue(sId.substring(6,7)) * 16 + parent.commonTools.getHexValue(sId.substring(7,8)));
        buffer[1] = (byte)(parent.commonTools.getHexValue(sId.substring(4,5)) * 16 + parent.commonTools.getHexValue(sId.substring(5,6)));
        buffer[2] = (byte)(parent.commonTools.getHexValue(sId.substring(2,3)) * 16 + parent.commonTools.getHexValue(sId.substring(3,4)));
        buffer[3] = (byte)(parent.commonTools.getHexValue(sId.substring(0,1)) * 16 + parent.commonTools.getHexValue(sId.substring(1,2)));
        buffer[3] &= 0x1f;
        if(isFd) buffer[3] |= 0x40;
        if(bsr) buffer[3] |= 0x20;
        if(ext) buffer[3] |= 0x80;
        buffer[4] = (byte)(length & 0x0f);
        for(int i = 0; i<length; i++) {
            int ival1 = parent.commonTools.getHexValue(body.substring(2*i,2*i+1));     // HI nibble
            int ival2 = parent.commonTools.getHexValue(body.substring(2*i+1,2*i+2));   // LO nibble
            buffer[5+i] = (byte)(16 * ival1 + ival2);
        }
        return(buffer);
    }
    /****************************************************************************
     *
     * @param ipAddress
     * @param port
     * @param data
     * @return
     ****************************************************************************/
    public  int SendMessage(String ipAddress, int port, byte [] data) {
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

    /**
     *
     * @param ipAddress
     * @param port
     * @param data
     * @param dlm
     * @return
     */
    public static int SendMessageWithLog(String ipAddress, int port, byte [] data, DefaultListModel<String> dlm) {
        DatagramSocket socket;
        InetAddress address;

        String sData = "";
        for(int i=0; i<data.length; i++)
            sData += String.format("%02X ", data[i]);
        dlm.addElement(String.format("SendMsg:  Addr=%s  Port=%d  Data=[%s]",
                ipAddress, port, sData));
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
    /**
     *
     * @param msg
     */
    private static  void logujem (String msg) {

        try {
            PrintWriter vystup = new PrintWriter(new BufferedWriter(new FileWriter("CAN.log", true)));
            vystup.println(msg);
            vystup.close();
        }
        catch(IOException e) {

        }
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     *
     * @param literal
     * @return
     */
    public int convertWord(String literal) {
        String spom = parent.commonTools.paddString(literal, 4);
        int ipom = 4096 * parent.commonTools.getHexValue(spom.substring(0,1));
        ipom += 256 * parent.commonTools.getHexValue(spom.substring(1,2));
        ipom += 16 * parent.commonTools.getHexValue(spom.substring(2,3));
        ipom += parent.commonTools.getHexValue(spom.substring(3));
        return(ipom);
    }


}
