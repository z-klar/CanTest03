package cz.zk;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

import static cz.zk.Tools.getHexValue;


public class frmMain {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txCanlancIpAddress;
    private JTextField txCanlancPort;
    private JComboBox cbMessageType1;
    private JButton btnSendMessage1;
    private JTextField txAbtMessageId;
    private JButton btnClearLog;
    private JList lbLog;
    private JComboBox cbMessageType10;
    private JTextField txDelay;
    private JComboBox cbMessageType11;
    private JButton btnStartSequence;
    private JTextField txXCoord;
    private JTextField txYCoord;
    private JButton btnSimulateTouch;
    private JButton btnHome;
    private JButton btnMenu;
    private JButton btnPower;
    private JPanel panelMain;
    private JButton btnSaveLog;
    private JLabel lbMousePosition;
    private JTextField txUdpRepetitionsNo;
    private JButton btnRunUdpTests;
    private JTextField txUdpResult;
    private JTextField txMflButtonsMsgId;
    private JCheckBox chkMflCanFD;
    private JComboBox cbMflMsgType;
    private JButton btnSendMflMessage;
    private JComboBox cbMflMessage1;
    private JTextField txMflMessageDelay;
    private JComboBox cbMflMessage2;
    private JButton btnMflSendSequence;
    private JTextField txMflRawMsgValue;
    private JButton btnMflSendRawMsg;
    private JCheckBox chkMflBrs;
    private JCheckBox chkMflExt;
    private JTextField txMflRawMsgValue1;
    private JTextField txMflRawMsgValue2;
    private JTextField txMflMessageDelay2;
    private JButton btnMflSendRawSequence;
    private JTextField txIcas4X;
    private JTextField txIcas4Y;
    private JButton btnIcas4Send;
    private JCheckBox chkIcas4Fd;
    private JCheckBox chkIcas4Ext;
    private JCheckBox chkIcas4Bsr;
    private JCheckBox chkIcas4SendUpdate;
    private JTextArea notesIPAddressCanLANcTextArea;
    private JTextField txIcas4Delay;
    private JCheckBox chkIcas4SendReleaseMessage;
    private JTextField txAudiX;
    private JTextField txAudiY;
    private JCheckBox chkAudiFD;
    private JCheckBox chkAudiBRS;
    private JCheckBox chkAudiEXT;
    private JButton btnAudiSend;
    private JTextField txAudiAbtMessageId;
    private JTextField txAudiDelay;
    private JCheckBox chkAudiSendRelease;
    private JTextField txAudiFreeFormMsg;
    private JButton btnAudiSendFreeFormMsg;

    private final ArrayList<AbtMessage> canMessages = new ArrayList<>();
    private final ArrayList<AbtMessage> mflMessages = new ArrayList<>();
    private final ArrayList<AbtMessage> icas4Messages = new ArrayList<>();
    DefaultListModel<String> dlmLog = new DefaultListModel<>();

    private final Icas4 icas4;

    public frmMain() {
        $$$setupUI$$$();

        canMessages.add(new AbtMessage("HOME_PRESSED", "04306601AAAAAAAA"));
        canMessages.add(new AbtMessage("HOME_RELEASED", "04306600AAAAAAAA"));
        canMessages.add(new AbtMessage("MENU_PRESSED", "04301A0100AAAAAA"));
        canMessages.add(new AbtMessage("MENU_RELEASED", "04301A0010AAAAAA"));
        canMessages.add(new AbtMessage("POWER_PRESSED", "04303801AAAAAAAA"));
        canMessages.add(new AbtMessage("POWER_RELEASED", "04303800AAAAAAAA"));

        canMessages.add(new AbtMessage("PRE_TOUCH_PRESS", "11"));
        canMessages.add(new AbtMessage("PRE_TOUCH_RELEASE", "10"));
        canMessages.add(new AbtMessage("POST_TOUCH_PRESS", "20"));
        canMessages.add(new AbtMessage("POST_TOUCH_RELEASE", "FF"));
        canMessages.add(new AbtMessage("TOUCH_PREFIX_GLOBAL", "07A000"));
        canMessages.add(new AbtMessage("X_COORDS_PREFIX", "0001"));  // orig

        mflMessages.add(new AbtMessage("END_COMMAND", "000000A3"));
        mflMessages.add(new AbtMessage("KEY_RELEASED_NO_KEY", "000001A3"));
        mflMessages.add(new AbtMessage("CONTEXT_MENU", "010001A3"));
        mflMessages.add(new AbtMessage("MENU_UP_NEXT_SCREEN", "020001A3"));
        mflMessages.add(new AbtMessage("MENU_DOWN_NEXT_SCREEN", "030001A3"));
        mflMessages.add(new AbtMessage("UP", "040001A3"));
        mflMessages.add(new AbtMessage("DOWN", "050001A3"));
        mflMessages.add(new AbtMessage("UP_THUMBWHEEL", "060001A3"));
        mflMessages.add(new AbtMessage("DOWN_THUMBWHEEL", "06000FA3"));
        mflMessages.add(new AbtMessage("OK_THUMBWHEEL_BUTTON", "070001A3"));
        mflMessages.add(new AbtMessage("CANCEL_ESCAPE", "080001A3"));
        mflMessages.add(new AbtMessage("MAIN_MENU", "090001A3"));
        mflMessages.add(new AbtMessage("SIDE_MENU_LEFT", "0A0001A3"));
        mflMessages.add(new AbtMessage("SIDE_MENU_RIGHT", "0B0001A3"));
        mflMessages.add(new AbtMessage("FAS_MENU", "0C0001A3"));
        mflMessages.add(new AbtMessage("LEFT_RIGHT_THUMBWHEEL", "0D0001A3"));
        mflMessages.add(new AbtMessage("VOLUME_UP", "100001A3"));
        mflMessages.add(new AbtMessage("VOLUME_DOWN", "110001A3"));
        mflMessages.add(new AbtMessage("VOLUME_UP_THUMBWHEEL", "120001A3"));
        mflMessages.add(new AbtMessage("VOLUME_DOWN_THUMBWHEEL", "12000FA3"));
        mflMessages.add(new AbtMessage("VOLUME_THUMBWHEEL_BUTTON", "130001A3"));
        mflMessages.add(new AbtMessage("AUDIO_SOURCE", "140001A3"));
        mflMessages.add(new AbtMessage("ARROW_A_UP_RIGHT", "150001A3"));
        mflMessages.add(new AbtMessage("ARROW_A_DOWN_LEFT", "160001A3"));
        mflMessages.add(new AbtMessage("ARROW_B_UP_RIGHT", "170001A3"));
        mflMessages.add(new AbtMessage("ARROW_B_DOWN_LEFT", "180001A3"));
        mflMessages.add(new AbtMessage("PTT_PUSHTOTALK", "190001A3"));
        mflMessages.add(new AbtMessage("PTT_CANCEL", "1A0001A3"));
        mflMessages.add(new AbtMessage("ROUT_INFO", "1B0001A3"));
        mflMessages.add(new AbtMessage("HOOK", "1C0001A3"));
        mflMessages.add(new AbtMessage("HANG_UP", "1D0001A3"));
        mflMessages.add(new AbtMessage("OFF_HOOK", "1E0001A3"));
        mflMessages.add(new AbtMessage("LIGHT_ON_OFF", "1F0001A3"));
        mflMessages.add(new AbtMessage("MUTE", "200001A3"));
        mflMessages.add(new AbtMessage("JOKER1", "210001A3"));

        icas4Messages.add(new AbtMessage("PRE_TOUCH_PRESS", "11"));
        icas4Messages.add(new AbtMessage("PRE_TOUCH_RELEASE", "10"));
        icas4Messages.add(new AbtMessage("PRE_TOUCH_UPDATE", "11"));
        icas4Messages.add(new AbtMessage("POST_TOUCH_PRESS", "FD"));
        icas4Messages.add(new AbtMessage("POST_TOUCH_RELEASE", "FE"));
        icas4Messages.add(new AbtMessage("POST_TOUCH_UPDATE", "FF"));
        icas4Messages.add(new AbtMessage("MSG_PREFIX", "07A000"));


        lbLog.setModel(dlmLog);

        cbMessageType1.removeAllItems();
        cbMessageType10.removeAllItems();
        cbMessageType11.removeAllItems();
        for (AbtMessage msg : canMessages) {
            cbMessageType1.addItem(msg.getName());
            cbMessageType10.addItem(msg.getName());
            cbMessageType11.addItem(msg.getName());
        }

        cbMflMsgType.removeAllItems();
        cbMflMessage1.removeAllItems();
        cbMflMessage2.removeAllItems();
        for (AbtMessage msg : mflMessages) {
            cbMflMsgType.addItem(msg.getName());
            cbMflMessage1.addItem(msg.getName());
            cbMflMessage2.addItem(msg.getName());
        }

        btnSendMessage1.addActionListener(e -> SendMessage1());
        btnClearLog.addActionListener(e -> dlmLog.clear());
        btnStartSequence.addActionListener(e -> LaunchSequence());
        btnSimulateTouch.addActionListener(e -> SimulateTouch());
        btnHome.addActionListener(e -> SendHomeButton());
        btnMenu.addActionListener(e -> SendMenuButton());
        btnPower.addActionListener(e -> SendPOwerButton());

        MyListener listener = new MyListener();
        panelMain.addMouseListener(listener);
        panelMain.addMouseMotionListener(listener);

        btnRunUdpTests.addActionListener(e -> RunUdpTests());

        btnSendMflMessage.addActionListener(e -> SendSingleMflMsg());
        btnMflSendRawMsg.addActionListener(e -> SendMflRawMsg());
        btnMflSendSequence.addActionListener(e -> SendMflSequence());
        btnMflSendRawSequence.addActionListener(e -> SendRawSequence());

        btnSaveLog.addActionListener(e -> SaveLog());

        icas4 = new Icas4(icas4Messages, dlmLog);
        btnIcas4Send.addActionListener(e -> SimulateIcas4Touch());
        btnAudiSend.addActionListener(e -> SimulateAudiTouch());
        btnAudiSendFreeFormMsg.addActionListener(e -> SendAudiFreeFormMsg());
    }

    private class MyListener extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            mainClick(e);
            super.mouseClicked(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            canvasMoved(e);
            super.mouseMoved(e);
        }
    }

    /**
     * @param e
     */
    private void canvasMoved(MouseEvent e) {
        double coordX = e.getX();
        double coordY = e.getY();
        double panelWidth = panelMain.getWidth();
        double panelHeight = panelMain.getHeight();

        double ABT_WIDTH = 1740;
        double ABT_HEIGHT = 720;

        double finalX = (ABT_WIDTH / panelWidth) * coordX;
        double finalY = (ABT_HEIGHT / panelHeight) * coordY;

        String spom = String.format("X: %d   Y: %d", (int) finalX, (int) finalY);
        lbMousePosition.setText(spom);
    }

    /**
     *
     */
    private void RunUdpTests() {

        LocalDateTime start, end;

        String ipAddr = txCanlancIpAddress.getText();
        int port = Integer.parseInt(txCanlancPort.getText());
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int opak = Integer.parseInt(txUdpRepetitionsNo.getText());
        start = LocalDateTime.now();
        for (int i = 0; i < opak; i++) {
            Tools.SendMessage(ipAddr, port, buffer);
        }
        end = LocalDateTime.now();
        long milliseconds = ChronoUnit.MILLIS.between(start, end);
        txUdpResult.setText(String.format("Duration = %d ms", milliseconds));
    }

    /**
     *
     */
    private void SaveLog() {

        try {
            PrintWriter vystup = new PrintWriter(new BufferedWriter(new FileWriter("CAN.log", true)));
            for (int i = 0; i < dlmLog.getSize(); i++) {
                vystup.println(dlmLog.get(i));
            }
            vystup.close();
        } catch (IOException e) {

        }

    }

    /*

     */
    private void mainClick(MouseEvent e) {
        double coordX = e.getX();
        double coordY = e.getY();
        double panelWidth = panelMain.getWidth();
        double panelHeight = panelMain.getHeight();

        double ABT_WIDTH = 1740;
        double ABT_HEIGHT = 720;

        double finalX = (ABT_WIDTH / panelWidth) * coordX;
        double finalY = (ABT_HEIGHT / panelHeight) * coordY;

        /*
        JOptionPane.showMessageDialog(null,
                String.format("Real / ABT: X = %f / %f   Y = %f / %f ",
                                      coordX, finalX, coordY, finalY));
        */
        SimulateTouch((int) finalX, (int) finalY);
    }

    //------------------------------------------------------------
    private void SendHomeButton() {
        LaunchSequence("HOME_PRESSED", "HOME_RELEASED");
    }

    //------------------------------------------------------------
    private void SendMenuButton() {
        LaunchSequence("MENU_PRESSED", "MENU_RELEASED");
    }

    //------------------------------------------------------------
    private void SendPOwerButton() {
        LaunchSequence("POWER_PRESSED", "POWER_RELEASED");
    }

    //------------------------------------------------------------
    private void SimulateTouch() {
        int x = Integer.parseInt(txXCoord.getText());
        int y = Integer.parseInt(txYCoord.getText());
        SimulateTouch(x, y);
    }

    //------------------------------------------------------------
    private void SimulateTouch(int x, int y) {

        String msgId = txAbtMessageId.getText();

        byte[] txBuff = ComposeTouchMessage(x, y, TouchType.PREPARE, msgId);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff);
        txBuff = ComposeTouchMessage(x, y, TouchType.PRESS, msgId);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff);
        Delay(150);
        txBuff = ComposeTouchMessage(x, y, TouchType.RELEASE, msgId);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff);
    }

    //------------------------------------------------------------
    private void SimulateIcas4Touch() {
        int x = Integer.parseInt(txIcas4X.getText());
        int y = Integer.parseInt(txIcas4Y.getText());
        SimulateIcas4Touch(x, y);
    }

    //------------------------------------------------------------
    private void SimulateAudiTouch() {
        int x = Integer.parseInt(txAudiX.getText());
        int y = Integer.parseInt(txAudiY.getText());
        SimulateAudiTouch(x, y);
    }

    //------------------------------------------------------------
    private void SimulateIcas4Touch(int x, int y) {

        String msgId = txAbtMessageId.getText();
        int delay;
        try {
            delay = Integer.parseInt(txIcas4Delay.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid delay value !");
            return;
        }
        if (delay > 1000) {
            JOptionPane.showMessageDialog(null, "Invalid delay value - valid = 0 to 1000 !");
            return;
        }

        byte[] txBuff = icas4.ComposeIcas4Message(x, y, "PRESS", msgId,
                chkIcas4Fd.isSelected(), chkIcas4Bsr.isSelected(), chkIcas4Ext.isSelected());
        int iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
        if (chkIcas4SendReleaseMessage.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = icas4.ComposeIcas4Message(x, y, "RELEASE", msgId,
                    chkIcas4Fd.isSelected(), chkIcas4Bsr.isSelected(), chkIcas4Ext.isSelected());
            iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                    Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
        }
        if (chkIcas4SendUpdate.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = icas4.ComposeIcas4Message(x, y, "UPDATE", msgId,
                    chkIcas4Fd.isSelected(), chkIcas4Bsr.isSelected(), chkIcas4Ext.isSelected());
            iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                    Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
        }
    }

    //------------------------------------------------------------
    private void SimulateAudiTouch(int x, int y) {

        int msgId = Integer.parseInt(txAudiAbtMessageId.getText());
        int delay;
        try {
            delay = Integer.parseInt(txAudiDelay.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid delay value !");
            return;
        }
        if (delay > 1000) {
            JOptionPane.showMessageDialog(null, "Invalid delay value - valid = 0 to 1000 !");
            return;
        }

        byte[] txBuff = icas4.ComposeAudiTouchMessage(x, y, "PRESS", msgId,
                chkAudiFD.isSelected(), chkAudiBRS.isSelected(), chkAudiEXT.isSelected());
        int iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
        if (chkAudiSendRelease.isSelected()) {
            if (delay > 0) Delay(delay);
            txBuff = icas4.ComposeAudiTouchMessage(x, y, "RELEASE", msgId,
                    chkAudiFD.isSelected(), chkAudiBRS.isSelected(), chkAudiEXT.isSelected());
            iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                    Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
        }
    }

    //------------------------------------------------------------
    private void SendAudiFreeFormMsg() {

        int msgId = Integer.parseInt(txAudiAbtMessageId.getText());
        String[] sData = txAudiFreeFormMsg.getText().split(" ");
        int msgLen = sData.length;
        int dlcCode = icas4.GetDlcCode(msgLen);
        if (dlcCode == -1) {
            JOptionPane.showMessageDialog(null, "Invalid data length !");
            return;
        }

        byte[] txBuff = icas4.ComposeAudiFreeFormMessage(dlcCode, msgId,
                chkAudiFD.isSelected(), chkAudiBRS.isSelected(), chkAudiEXT.isSelected(), sData);
        int iRes = Tools.SendMessageWithLog(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()), txBuff, dlmLog);
    }

    /**
     *
     */
    private void SendMflSequence() {
        int delay = Integer.parseInt(txMflMessageDelay.getText());
        String msgBody1 = getMflMessageBody(cbMflMessage1.getSelectedItem().toString());
        String msgBody2 = getMflMessageBody(cbMflMessage2.getSelectedItem().toString());
        String msgId = txMflButtonsMsgId.getText();
        int msgLen = 4;

        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody1, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("***** SendMflSeq #1 ******* : IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMflMessage(msgId, msgBody2, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("***** SendMflSeq #2 ******* : IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
    }

    //------------------------------------------------------------
    private void LaunchSequence() {
        int delay = Integer.parseInt(txDelay.getText());
        String msgBody1 = getMessageBody(cbMessageType10.getSelectedItem().toString());
        String msgBody2 = getMessageBody(cbMessageType11.getSelectedItem().toString());
        String msgId = txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody1, msgLen);
        logMessageString(String.format("Sending MSG #1: IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMessage(msgId, msgBody2, msgLen);
        logMessageString(String.format("Sending MSG #2: IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
    }

    //------------------------------------------------------------
    private void LaunchSequence(String type1, String type2) {
        int delay = Integer.parseInt(txDelay.getText());
        String msgBody1 = getMessageBody(type1);
        String msgBody2 = getMessageBody(type2);
        String msgId = txAbtMessageId.getText();
        int msgLen = 8;

        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody1, msgLen);
        logMessageString(String.format("Sending MSG %s - #1: IP addr: %s   PORT: %s",
                type1, txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);

        Delay(delay);

        outBuff = Tools.ComposeMessage(msgId, msgBody2, msgLen);
        logMessageString(String.format("Sending MSG %s - #2: IP addr: %s   PORT: %s",
                type2, txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
    }


    //------------------------------------------------------------
    private void Delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {

        }
    }

    /**
     *
     */
    private void SendSingleMflMsg() {
        String msgBody = getMflMessageBody(cbMflMsgType.getSelectedItem().toString());
        if (msgBody.length() == 0) {
            JOptionPane.showMessageDialog(null, "Message not found !!!!!");
            return;
        }
        String msgId = txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("****** SendSingleMflMsg ******   IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
    }

    /**
     *
     */
    private void SendMflRawMsg() {
        String msgBody = txMflRawMsgValue.getText();
        String msgId = txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("**** SendRawMflMsg******  IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
    }

    /**
     *
     */
    private void SendRawSequence() {
        String msgBody = txMflRawMsgValue1.getText();
        String msgId = txMflButtonsMsgId.getText();
        int msgLen = 4;
        byte[] outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("**** SendRawSequence - #1 *****  IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);

        int delay = Integer.parseInt(txMflMessageDelay2.getText());
        Delay(delay);

        msgBody = txMflRawMsgValue2.getText();
        msgId = txMflButtonsMsgId.getText();
        msgLen = 4;
        outBuff = Tools.ComposeMflMessage(msgId, msgBody, msgLen, chkMflCanFD.isSelected(),
                chkMflBrs.isSelected(), chkMflExt.isSelected());
        logMessageString(String.format("**** SendRawSequence - #2 *****  IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);


    }

    //------------------------------------------------------------
    private void SendMessage1() {
        String msgBody = getMessageBody(cbMessageType1.getSelectedItem().toString());
        if (msgBody.length() == 0) {
            JOptionPane.showMessageDialog(null, "Message not found !!!!!");
            return;
        }
        String msgId = txAbtMessageId.getText();
        int msgLen = 8;
        byte[] outBuff = Tools.ComposeMessage(msgId, msgBody, msgLen);
        logMessageString(String.format("IP addr: %s   PORT: %s",
                txCanlancIpAddress.getText(), txCanlancPort.getText()));
        logMessageBytes("Message sent:", outBuff);
        int iRes = Tools.SendMessage(txCanlancIpAddress.getText(),
                Integer.parseInt(txCanlancPort.getText()),
                outBuff);
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

    /**
     * @param x
     * @param y
     * @param touchType
     * @return
     */
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

        logMessageString(String.format("ComposeToucMsg: ID=%s TYPE=%s  X=%d  Y=%d  ",
                id, touchType, x, y));
        logMessageBytesRaw(outBuffer);
        return outBuffer;
    }


    //##################################################################
    private String getMessageBody(String msgName) {

        for (AbtMessage msg : canMessages) {
            if (msg.getName().equals(msgName)) {
                return (msg.getBody());
            }
        }
        return ("");
    }

    //##################################################################
    private String getMflMessageBody(String msgName) {

        for (AbtMessage msg : mflMessages) {
            if (msg.getName().equals(msgName)) {
                return (msg.getBody());
            }
        }
        return ("");
    }


    /************************************************************
     *
     ************************************************************/
    public static void main(String[] args) {
        JFrame frame = new JFrame("CAN Test 03");
        frame.setContentPane(new frmMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1300, 800);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 8, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setForeground(new Color(-5987164));
        tabbedPane1.addTab("ABT Tests", panel2);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Message Type:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cbMessageType1 = new JComboBox();
        panel2.add(cbMessageType1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnSendMessage1 = new JButton();
        btnSendMessage1.setText("Send Message");
        panel2.add(btnSendMessage1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Message Type 1:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMessageType10 = new JComboBox();
        panel2.add(cbMessageType10, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, -1, 14, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Delay [ms]:");
        panel2.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txDelay = new JTextField();
        Font txDelayFont = this.$$$getFont$$$(null, -1, 14, txDelay.getFont());
        if (txDelayFont != null) txDelay.setFont(txDelayFont);
        txDelay.setText("100");
        panel2.add(txDelay, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, -1, 14, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Message Type 2:");
        panel2.add(label4, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMessageType11 = new JComboBox();
        panel2.add(cbMessageType11, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnStartSequence = new JButton();
        btnStartSequence.setText("Start Sequence");
        panel2.add(btnStartSequence, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, -1, 14, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("X Coord:");
        panel2.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txXCoord = new JTextField();
        Font txXCoordFont = this.$$$getFont$$$(null, -1, 14, txXCoord.getFont());
        if (txXCoordFont != null) txXCoord.setFont(txXCoordFont);
        txXCoord.setText("0");
        panel2.add(txXCoord, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, -1, 14, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Y Coord:");
        panel2.add(label6, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txYCoord = new JTextField();
        Font txYCoordFont = this.$$$getFont$$$(null, -1, 14, txYCoord.getFont());
        if (txYCoordFont != null) txYCoord.setFont(txYCoordFont);
        txYCoord.setText("0");
        panel2.add(txYCoord, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        btnSimulateTouch = new JButton();
        btnSimulateTouch.setText("Simulate Touch");
        panel2.add(btnSimulateTouch, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Version: 1.1.0.0");
        panel2.add(label7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnHome = new JButton();
        Font btnHomeFont = this.$$$getFont$$$(null, Font.BOLD, 18, btnHome.getFont());
        if (btnHomeFont != null) btnHome.setFont(btnHomeFont);
        btnHome.setForeground(new Color(-14672672));
        btnHome.setText("HOME");
        panel2.add(btnHome, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnMenu = new JButton();
        Font btnMenuFont = this.$$$getFont$$$(null, Font.BOLD, 18, btnMenu.getFont());
        if (btnMenuFont != null) btnMenu.setFont(btnMenuFont);
        btnMenu.setForeground(new Color(-14672672));
        btnMenu.setText("MENU");
        panel2.add(btnMenu, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPower = new JButton();
        Font btnPowerFont = this.$$$getFont$$$(null, Font.BOLD, 18, btnPower.getFont());
        if (btnPowerFont != null) btnPower.setFont(btnPowerFont);
        btnPower.setForeground(new Color(-14672672));
        btnPower.setText("POWER");
        panel2.add(btnPower, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBackground(new Color(-14324714));
        panel2.add(panelMain, new GridConstraints(4, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lbMousePosition = new JLabel();
        lbMousePosition.setText("N/A");
        panel2.add(lbMousePosition, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("MFL Buttons", panel3);
        final JLabel label8 = new JLabel();
        label8.setText("MFL Buttons Message ID:");
        panel3.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txMflButtonsMsgId = new JTextField();
        Font txMflButtonsMsgIdFont = this.$$$getFont$$$(null, -1, 14, txMflButtonsMsgId.getFont());
        if (txMflButtonsMsgIdFont != null) txMflButtonsMsgId.setFont(txMflButtonsMsgIdFont);
        txMflButtonsMsgId.setText("5BF");
        panel3.add(txMflButtonsMsgId, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 30), null, 0, false));
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, -1, 14, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Message Type:");
        panel3.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMsgType = new JComboBox();
        panel3.add(cbMflMsgType, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 38), null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, -1, 14, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Message Type 1:");
        panel3.add(label10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMessage1 = new JComboBox();
        panel3.add(cbMflMessage1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 38), null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, -1, 14, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Delay [ms]:");
        panel3.add(label11, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflMessageDelay = new JTextField();
        Font txMflMessageDelayFont = this.$$$getFont$$$(null, -1, 14, txMflMessageDelay.getFont());
        if (txMflMessageDelayFont != null) txMflMessageDelay.setFont(txMflMessageDelayFont);
        txMflMessageDelay.setText("100");
        panel3.add(txMflMessageDelay, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, -1, 14, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Message Type 2:");
        panel3.add(label12, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMessage2 = new JComboBox();
        panel3.add(cbMflMessage2, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 38), null, 0, false));
        btnMflSendSequence = new JButton();
        btnMflSendSequence.setText("Send Message");
        panel3.add(btnMflSendSequence, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkMflCanFD = new JCheckBox();
        chkMflCanFD.setText("CAN FD");
        panel3.add(chkMflCanFD, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, -1, 14, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Raw Message Value:");
        panel3.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflRawMsgValue = new JTextField();
        Font txMflRawMsgValueFont = this.$$$getFont$$$(null, -1, 14, txMflRawMsgValue.getFont());
        if (txMflRawMsgValueFont != null) txMflRawMsgValue.setFont(txMflRawMsgValueFont);
        txMflRawMsgValue.setText("00112233");
        panel3.add(txMflRawMsgValue, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 30), null, 0, false));
        btnSendMflMessage = new JButton();
        btnSendMflMessage.setText("Send Message");
        panel3.add(btnSendMflMessage, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnMflSendRawMsg = new JButton();
        btnMflSendRawMsg.setText("Send Message");
        panel3.add(btnMflSendRawMsg, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkMflBrs = new JCheckBox();
        chkMflBrs.setText("BRS");
        panel3.add(chkMflBrs, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkMflExt = new JCheckBox();
        chkMflExt.setText("EXT");
        panel3.add(chkMflExt, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, -1, 14, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Raw Message Value:");
        panel3.add(label14, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflRawMsgValue1 = new JTextField();
        Font txMflRawMsgValue1Font = this.$$$getFont$$$(null, -1, 14, txMflRawMsgValue1.getFont());
        if (txMflRawMsgValue1Font != null) txMflRawMsgValue1.setFont(txMflRawMsgValue1Font);
        txMflRawMsgValue1.setText("00112233");
        panel3.add(txMflRawMsgValue1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 30), null, 0, false));
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, -1, 14, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("Delay [ms]:");
        panel3.add(label15, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflMessageDelay2 = new JTextField();
        Font txMflMessageDelay2Font = this.$$$getFont$$$(null, -1, 14, txMflMessageDelay2.getFont());
        if (txMflMessageDelay2Font != null) txMflMessageDelay2.setFont(txMflMessageDelay2Font);
        txMflMessageDelay2.setText("100");
        panel3.add(txMflMessageDelay2, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, -1, 14, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setText("Raw Message Value:");
        panel3.add(label16, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflRawMsgValue2 = new JTextField();
        Font txMflRawMsgValue2Font = this.$$$getFont$$$(null, -1, 14, txMflRawMsgValue2.getFont());
        if (txMflRawMsgValue2Font != null) txMflRawMsgValue2.setFont(txMflRawMsgValue2Font);
        txMflRawMsgValue2.setText("00112233");
        panel3.add(txMflRawMsgValue2, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 30), null, 0, false));
        btnMflSendRawSequence = new JButton();
        btnMflSendRawSequence.setText("Send Message");
        panel3.add(btnMflSendRawSequence, new GridConstraints(4, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(7, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("ICAS Touches", panel4);
        final JLabel label17 = new JLabel();
        label17.setText("X Coordinate (WIDTH):");
        panel4.add(label17, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txIcas4X = new JTextField();
        txIcas4X.setText("0");
        panel4.add(txIcas4X, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label18 = new JLabel();
        label18.setText("Y Coordinate (HEIGHT):");
        panel4.add(label18, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txIcas4Y = new JTextField();
        txIcas4Y.setText("0");
        panel4.add(txIcas4Y, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnIcas4Send = new JButton();
        btnIcas4Send.setText("Send Message");
        panel4.add(btnIcas4Send, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkIcas4Fd = new JCheckBox();
        chkIcas4Fd.setText("FD");
        chkIcas4Fd.setToolTipText("Set the Flexible Date Rate bit");
        panel4.add(chkIcas4Fd, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkIcas4Bsr = new JCheckBox();
        chkIcas4Bsr.setText("BRS");
        chkIcas4Bsr.setToolTipText("Set the Bit Rate Switch bit");
        panel4.add(chkIcas4Bsr, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkIcas4Ext = new JCheckBox();
        chkIcas4Ext.setText("EXT");
        chkIcas4Ext.setToolTipText("Set the Error State Indicator bit");
        panel4.add(chkIcas4Ext, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        panel4.add(panel5, new GridConstraints(6, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, panel5.getFont()), null));
        notesIPAddressCanLANcTextArea = new JTextArea();
        Font notesIPAddressCanLANcTextAreaFont = this.$$$getFont$$$("Courier New", -1, 12, notesIPAddressCanLANcTextArea.getFont());
        if (notesIPAddressCanLANcTextAreaFont != null)
            notesIPAddressCanLANcTextArea.setFont(notesIPAddressCanLANcTextAreaFont);
        notesIPAddressCanLANcTextArea.setText("Notes:\n- IP address & CanLANc port is shared from Configuration Tab\n");
        panel5.add(notesIPAddressCanLANcTextArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("Delay between messages [ms]");
        panel4.add(label19, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txIcas4Delay = new JTextField();
        txIcas4Delay.setText("20");
        panel4.add(txIcas4Delay, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chkIcas4SendUpdate = new JCheckBox();
        chkIcas4SendUpdate.setSelected(true);
        chkIcas4SendUpdate.setText("Send UPDATE Message");
        chkIcas4SendUpdate.setToolTipText("Send the UPDATE message at the end (for MQB37W and ICAS3 only)");
        panel4.add(chkIcas4SendUpdate, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkIcas4SendReleaseMessage = new JCheckBox();
        chkIcas4SendReleaseMessage.setSelected(true);
        chkIcas4SendReleaseMessage.setText("Send RELEASE Message");
        chkIcas4SendReleaseMessage.setToolTipText("Send the UPDATE message at the end (for MQB37W and ICAS3 only)");
        panel4.add(chkIcas4SendReleaseMessage, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(8, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Audi HCP ABT", panel6);
        final JLabel label20 = new JLabel();
        label20.setText("ABT Message ID (Decimal):");
        panel6.add(label20, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiAbtMessageId = new JTextField();
        Font txAudiAbtMessageIdFont = this.$$$getFont$$$(null, -1, 14, txAudiAbtMessageId.getFont());
        if (txAudiAbtMessageIdFont != null) txAudiAbtMessageId.setFont(txAudiAbtMessageIdFont);
        txAudiAbtMessageId.setText("483402347");
        txAudiAbtMessageId.setToolTipText("DECIMAL value of the Message ID !!!!");
        panel6.add(txAudiAbtMessageId, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label21 = new JLabel();
        label21.setText("X Coordinate (WIDTH):");
        panel6.add(label21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiX = new JTextField();
        txAudiX.setText("0");
        panel6.add(txAudiX, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("Y Coordinate (WIDTH):");
        panel6.add(label22, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiY = new JTextField();
        txAudiY.setText("0");
        panel6.add(txAudiY, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chkAudiFD = new JCheckBox();
        chkAudiFD.setSelected(true);
        chkAudiFD.setText("FD");
        chkAudiFD.setToolTipText("Set the Flexible Date Rate bit");
        panel6.add(chkAudiFD, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkAudiBRS = new JCheckBox();
        chkAudiBRS.setText("BRS");
        chkAudiBRS.setToolTipText("Set the Bit Rate Switch bit");
        panel6.add(chkAudiBRS, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkAudiEXT = new JCheckBox();
        chkAudiEXT.setText("EXT");
        chkAudiEXT.setToolTipText("Set the Error State Indicator bit");
        panel6.add(chkAudiEXT, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnAudiSend = new JButton();
        btnAudiSend.setText("Send Message");
        panel6.add(btnAudiSend, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(11, 324), null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("Delay between messages [ms]");
        panel6.add(label23, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiDelay = new JTextField();
        txAudiDelay.setText("20");
        panel6.add(txAudiDelay, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chkAudiSendRelease = new JCheckBox();
        chkAudiSendRelease.setSelected(true);
        chkAudiSendRelease.setText("Send RELEASE Message");
        chkAudiSendRelease.setToolTipText("Send the UPDATE message at the end (for MQB37W and ICAS3 only)");
        panel6.add(chkAudiSendRelease, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("FreeForm Message");
        panel6.add(label24, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiFreeFormMsg = new JTextField();
        txAudiFreeFormMsg.setText("");
        txAudiFreeFormMsg.setToolTipText("Provide sequence of Hexadecimal numbers separated by a space");
        panel6.add(txAudiFreeFormMsg, new GridConstraints(4, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnAudiSendFreeFormMsg = new JButton();
        btnAudiSendFreeFormMsg.setText("Send Message");
        panel6.add(btnAudiSendFreeFormMsg, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(2, 2, 2, 2), -1, -1));
        panel6.add(panel7, new GridConstraints(7, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JTextArea textArea1 = new JTextArea();
        Font textArea1Font = this.$$$getFont$$$("Courier New", -1, 12, textArea1.getFont());
        if (textArea1Font != null) textArea1.setFont(textArea1Font);
        textArea1.setText("Notes:\n- IP address & CanLANc port is shared from Configuration Tab\n");
        panel7.add(textArea1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel8.setForeground(new Color(-6513508));
        tabbedPane1.addTab("Configuration", panel8);
        final JLabel label25 = new JLabel();
        label25.setText("CanLANc IP Address:");
        panel8.add(label25, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel8.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        txCanlancIpAddress = new JTextField();
        Font txCanlancIpAddressFont = this.$$$getFont$$$(null, -1, 14, txCanlancIpAddress.getFont());
        if (txCanlancIpAddressFont != null) txCanlancIpAddress.setFont(txCanlancIpAddressFont);
        txCanlancIpAddress.setText("192.168.4.10");
        panel8.add(txCanlancIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("CanLANc PORT:");
        panel8.add(label26, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCanlancPort = new JTextField();
        Font txCanlancPortFont = this.$$$getFont$$$(null, -1, 14, txCanlancPort.getFont());
        if (txCanlancPortFont != null) txCanlancPort.setFont(txCanlancPortFont);
        txCanlancPort.setText("7777");
        panel8.add(txCanlancPort, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label27 = new JLabel();
        label27.setText("ABT Message ID:");
        panel8.add(label27, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAbtMessageId = new JTextField();
        Font txAbtMessageIdFont = this.$$$getFont$$$(null, -1, 14, txAbtMessageId.getFont());
        if (txAbtMessageIdFont != null) txAbtMessageId.setFont(txAbtMessageIdFont);
        txAbtMessageId.setText("17F8F173");
        panel8.add(txAbtMessageId, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel8.add(spacer7, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Logger", panel9);
        btnClearLog = new JButton();
        btnClearLog.setText("Clear");
        panel9.add(btnClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel9.add(spacer8, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel9.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLog = new JList();
        Font lbLogFont = this.$$$getFont$$$("Courier New", -1, 14, lbLog.getFont());
        if (lbLogFont != null) lbLog.setFont(lbLogFont);
        scrollPane1.setViewportView(lbLog);
        btnSaveLog = new JButton();
        btnSaveLog.setText("Save Log");
        btnSaveLog.setToolTipText("Save the log to the CAN.LOG file");
        panel9.add(btnSaveLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("UDP Tests", panel10);
        final JLabel label28 = new JLabel();
        label28.setText("Number of repetitions:");
        panel10.add(label28, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel10.add(spacer9, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel10.add(spacer10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txUdpRepetitionsNo = new JTextField();
        txUdpRepetitionsNo.setText("100");
        panel10.add(txUdpRepetitionsNo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnRunUdpTests = new JButton();
        btnRunUdpTests.setText("Run Tests !");
        panel10.add(btnRunUdpTests, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txUdpResult = new JTextField();
        panel10.add(txUdpResult, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
