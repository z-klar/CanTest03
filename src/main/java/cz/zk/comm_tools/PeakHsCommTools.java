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
        byte[] outBuffer = {0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0,};

        String sId = parent.commonTools.paddString(id, 8);

        outBuffer[1] = 36;              // frame length
        outBuffer[3] = (byte)0x80;      // msg type
        outBuffer[20] = 0;              // channel #
        outBuffer[21] = 8;              // DLC
        outBuffer[23] = 2;              // extended IDs
        outBuffer[24] = (byte) (parent.commonTools.getHexValue(sId.substring(0, 1)) * 16 + parent.commonTools.getHexValue(sId.substring(1, 2)));
        outBuffer[24] |= 0x80;          // extended IDs
        outBuffer[25] = (byte) (parent.commonTools.getHexValue(sId.substring(2, 3)) * 16 + parent.commonTools.getHexValue(sId.substring(3, 4)));
        outBuffer[26] = (byte) (parent.commonTools.getHexValue(sId.substring(4, 5)) * 16 + parent.commonTools.getHexValue(sId.substring(5, 6)));
        outBuffer[27] = (byte) (parent.commonTools.getHexValue(sId.substring(6, 7)) * 16 + parent.commonTools.getHexValue(sId.substring(7, 8)));

        byte [] msg = parent.communicationTools.ComposeTouchMessage(x, y, touchType);
        for(int i=0; i<8; i++) outBuffer[28+i] = msg[i];

        parent.logging.logMessageString(String.format("ComposeTouchMsgPEAK: ID=%s TYPE=%s  X=%d  Y=%d  ",
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
