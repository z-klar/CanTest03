package cz.zk;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Tools {

    private static String [] hexChars = {"0","1","2","3","4","5","6","7",
                                         "8","9","A","B","C","D","E","F"};


    /************************************************************************
     *
     * @param id
     * @param body
     * @param length
     * @return
     ************************************************************************/
    public static byte[] ComposeMessage(String id, String body, int length) {
        byte [] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = paddString(id, 8);
        String sBody = paddString(body, 16);

        buffer[0] = (byte)(getHexValue(sId.substring(6,7)) * 16 + getHexValue(sId.substring(7,8)));
        buffer[1] = (byte)(getHexValue(sId.substring(4,5)) * 16 + getHexValue(sId.substring(5,6)));
        buffer[2] = (byte)(getHexValue(sId.substring(2,3)) * 16 + getHexValue(sId.substring(3,4)));
        buffer[3] = (byte)(getHexValue(sId.substring(0,1)) * 16 + getHexValue(sId.substring(1,2)));
        buffer[4] = (byte)(length & 0x0f);
        for(int i = 0; i<length; i++) {
            int ival1 = getHexValue(body.substring(2*i,2*i+1));     // HI nibble
            int ival2 = getHexValue(body.substring(2*i+1,2*i+2));   // LO nibble
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
    public static byte[] ComposeMflMessage(String id, String body, int length,
                                           boolean isFd, boolean bsr, boolean ext) {
        byte [] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0};

        String sId = paddString(id, 8);
        String sBody = paddString(body, 8);

        buffer[0] = (byte)(getHexValue(sId.substring(6,7)) * 16 + getHexValue(sId.substring(7,8)));
        buffer[1] = (byte)(getHexValue(sId.substring(4,5)) * 16 + getHexValue(sId.substring(5,6)));
        buffer[2] = (byte)(getHexValue(sId.substring(2,3)) * 16 + getHexValue(sId.substring(3,4)));
        buffer[3] = (byte)(getHexValue(sId.substring(0,1)) * 16 + getHexValue(sId.substring(1,2)));
        buffer[3] &= 0x1f;
        if(isFd) buffer[3] |= 0x40;
        if(bsr) buffer[3] |= 0x20;
        if(ext) buffer[3] |= 0x80;
        buffer[4] = (byte)(length & 0x0f);
        for(int i = 0; i<length; i++) {
            int ival1 = getHexValue(body.substring(2*i,2*i+1));     // HI nibble
            int ival2 = getHexValue(body.substring(2*i+1,2*i+2));   // LO nibble
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

    /*******************************************************************************
     *
     * @param input
     * @param length
     * @return
     *******************************************************************************/
    public static String paddString(String input, int length) {
        String spom = input;
        while(spom.length() < length) spom = "0" + spom;
        return(spom);
    }

    /**
     *
     * @param literal
     * @return
     */
    public static int getHexValue(String literal) {

        for(int i=0; i<16; i++) {
            if(hexChars[i].equals(literal.toUpperCase())) {
                return(i);
            }
        }
        return(0);
    }

    /**
     *
     * @param literal
     * @return
     */
    public static int convertByte(String literal) {
        String spom = paddString(literal, 2);
        return(16*getHexValue(spom.substring(0,1)) + getHexValue(spom.substring(1)));
    }

    /**
     *
     * @param literal
     * @return
     */
    public static int convertWord(String literal) {
        String spom = paddString(literal, 4);
        int ipom = 4096 * getHexValue(spom.substring(0,1));
        ipom += 256 * getHexValue(spom.substring(1,2));
        ipom += 16 * getHexValue(spom.substring(2,3));
        ipom += getHexValue(spom.substring(3));
        return(ipom);
    }

}
