package cz.zk.tools;

import cz.zk.frmMain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logging {
    private frmMain parent;

    public Logging(frmMain parent) {
        this.parent = parent;
    }

    public void logMessageString(String msg) {
        parent.dlmLog.addElement(msg);
    }

    /**
     * @param stitle
     * @param data
     */
    public void logMessageBytes(String stitle, byte[] data) {
        String ss;
        parent.dlmLog.addElement(stitle);
        String spom = "";
        for (int i = 0; i < data.length; i++) {
            if (i == 0) ss = String.format("%02X", data[i]);
            else ss = String.format(":%02X", data[i]);
            spom += ss;
        }
        parent.dlmLog.addElement(spom);
    }

    public void logMessageBytesRaw(byte[] data) {
        String spom = "";
        for (int i = 0; i < data.length; i++) {
            String ss = String.format("%02X:", data[i]);
            spom += ss;
        }
        parent.dlmLog.addElement(spom);
    }

    public void SaveLog() {

        try {
            PrintWriter vystup = new PrintWriter(new BufferedWriter(new FileWriter("CAN.log", true)));
            for (int i = 0; i < parent.dlmLog.getSize(); i++) {
                vystup.println(parent.dlmLog.get(i));
            }
            vystup.close();
        } catch (IOException e) {

        }

    }


}
