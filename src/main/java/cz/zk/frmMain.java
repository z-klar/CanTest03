package cz.zk;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

    private ArrayList<AbtMessage> canMessages = new ArrayList<>();
    DefaultListModel<String> dlmLog = new DefaultListModel<>();

    public frmMain() {
        $$$setupUI$$$();
        canMessages.add(new AbtMessage("HOME_PRESSED", "04306601AAAAAAAA"));
        canMessages.add(new AbtMessage("HOME_RELEASED", "04306600AAAAAAAA"));
        canMessages.add(new AbtMessage("MENU_PRESSED", "04301A0100AAAAAA"));
        canMessages.add(new AbtMessage("MENU_RELEASED", "04301A0010AAAAAA"));
        canMessages.add(new AbtMessage("POWER_PRESSED", "04303801AAAAAAAA"));
        canMessages.add(new AbtMessage("POWER_RELEASED", "04303800AAAAAAAA"));

        /*
        canMessages.add(new AbtMessage("HOME_PRESSED", "AAAAAAAA01663004"));
        canMessages.add(new AbtMessage("HOME_RELEASED", "AAAAAAAA00663004"));
        canMessages.add(new AbtMessage("MENU_PRESSED", "AAAAAA00011A3004"));
        canMessages.add(new AbtMessage("MENU_RELEASED", "AAAAAA10001A3004"));
        canMessages.add(new AbtMessage("POWER_PRESSED", "AAAAAAAA01383004"));
        canMessages.add(new AbtMessage("POWER_RELEASED", "AAAAAAAA00383004"));
         */

        canMessages.add(new AbtMessage("PRE_TOUCH_PRESS", "11"));
        canMessages.add(new AbtMessage("PRE_TOUCH_RELEASE", "10"));
        canMessages.add(new AbtMessage("POST_TOUCH_PRESS", "20"));
        canMessages.add(new AbtMessage("POST_TOUCH_RELEASE", "FF"));
        canMessages.add(new AbtMessage("TOUCH_PREFIX_GLOBAL", "07A000"));
        canMessages.add(new AbtMessage("X_COORDS_PREFIX", "0001"));  // orig
        /*
        canMessages.add(new AbtMessage("TOUCH_PREFIX_GLOBAL", "00A007"));
        canMessages.add(new AbtMessage("X_COORDS_PREFIX", "0001"));
        */

        lbLog.setModel(dlmLog);

        cbMessageType1.removeAllItems();
        cbMessageType10.removeAllItems();
        cbMessageType11.removeAllItems();
        for (AbtMessage msg : canMessages) {
            cbMessageType1.addItem(msg.getName());
            cbMessageType10.addItem(msg.getName());
            cbMessageType11.addItem(msg.getName());
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
        dlmLog.addElement(stitle);
        String spom = "";
        for (int i = 0; i < data.length; i++) {
            String ss = String.format("%02X:", data[i]);
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


    /************************************************************
     *
     ************************************************************/
    public static void main(String[] args) {
        JFrame frame = new JFrame("CAN Test 03");
        frame.setContentPane(new frmMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1200, 800);
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
        tabbedPane1.addTab("Tests", panel2);
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
        label7.setText("Version: 1.2.0");
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
        panel3.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setForeground(new Color(-6513508));
        tabbedPane1.addTab("Configuration", panel3);
        final JLabel label8 = new JLabel();
        label8.setText("CanLANc IP Address:");
        panel3.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel3.add(spacer4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txCanlancIpAddress = new JTextField();
        Font txCanlancIpAddressFont = this.$$$getFont$$$(null, -1, 14, txCanlancIpAddress.getFont());
        if (txCanlancIpAddressFont != null) txCanlancIpAddress.setFont(txCanlancIpAddressFont);
        txCanlancIpAddress.setText("192.168.4.10");
        panel3.add(txCanlancIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("CanLANc PORT:");
        panel3.add(label9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCanlancPort = new JTextField();
        Font txCanlancPortFont = this.$$$getFont$$$(null, -1, 14, txCanlancPort.getFont());
        if (txCanlancPortFont != null) txCanlancPort.setFont(txCanlancPortFont);
        txCanlancPort.setText("7777");
        panel3.add(txCanlancPort, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("ABT Message ID:");
        panel3.add(label10, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAbtMessageId = new JTextField();
        Font txAbtMessageIdFont = this.$$$getFont$$$(null, -1, 14, txAbtMessageId.getFont());
        if (txAbtMessageIdFont != null) txAbtMessageId.setFont(txAbtMessageIdFont);
        txAbtMessageId.setText("17F8F173");
        panel3.add(txAbtMessageId, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Logger", panel4);
        btnClearLog = new JButton();
        btnClearLog.setText("Clear");
        panel4.add(btnClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel4.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLog = new JList();
        Font lbLogFont = this.$$$getFont$$$("Courier New", -1, 14, lbLog.getFont());
        if (lbLogFont != null) lbLog.setFont(lbLogFont);
        scrollPane1.setViewportView(lbLog);
        btnSaveLog = new JButton();
        btnSaveLog.setText("Save Log");
        btnSaveLog.setToolTipText("Save the log to the CAN.LOG file");
        panel4.add(btnSaveLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("UDP Tests", panel5);
        final JLabel label11 = new JLabel();
        label11.setText("Number of repetitions:");
        panel5.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel5.add(spacer6, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel5.add(spacer7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txUdpRepetitionsNo = new JTextField();
        txUdpRepetitionsNo.setText("100");
        panel5.add(txUdpRepetitionsNo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnRunUdpTests = new JButton();
        btnRunUdpTests.setText("Run Tests !");
        panel5.add(btnRunUdpTests, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txUdpResult = new JTextField();
        panel5.add(txUdpResult, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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