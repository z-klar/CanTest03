package cz.zk;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import cz.zk.comm_tools.CanlancCommTools;
import cz.zk.comm_tools.CommunicationTools;
import cz.zk.comm_tools.CommonCommTools;
import cz.zk.comm_tools.PeakHsCommTools;
import cz.zk.init.InitData;
import cz.zk.init.InitGui;
import cz.zk.tools.CommonTools;
import cz.zk.tools.Logging;
import cz.zk.uitools.MyListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;


public class frmMain {
    public JPanel panel1;
    public JTabbedPane tabbedPane1;
    public JTextField txCanlancIpAddress;
    public JTextField txCanlancPort;
    public JComboBox cbMessageType1;
    public JButton btnSendMessage1;
    public JTextField txAbtMessageId;
    public JButton btnClearLog;
    public JList lbLog;
    public JComboBox cbMessageType10;
    public JTextField txDelay;
    public JComboBox cbMessageType11;
    public JButton btnStartSequence;
    public JTextField txXCoord;
    public JTextField txYCoord;
    public JButton btnSimulateTouch;
    public JButton btnHome;
    public JButton btnMenu;
    public JButton btnPower;
    public JPanel panelMain;
    public JButton btnSaveLog;
    public JLabel lbMousePosition;
    public JTextField txUdpRepetitionsNo;
    public JButton btnRunUdpTests;
    public JTextField txUdpResult;
    public JTextField txMflButtonsMsgId;
    public JCheckBox chkMflCanFD;
    public JComboBox cbMflMsgType;
    public JButton btnSendMflMessage;
    public JComboBox cbMflMessage1;
    public JTextField txMflMessageDelay;
    public JComboBox cbMflMessage2;
    public JButton btnMflSendSequence;
    public JTextField txMflRawMsgValue;
    public JButton btnMflSendRawMsg;
    public JCheckBox chkMflBrs;
    public JCheckBox chkMflExt;
    public JTextField txMflRawMsgValue1;
    public JTextField txMflRawMsgValue2;
    public JTextField txMflMessageDelay2;
    public JButton btnMflSendRawSequence;
    public JTextField txIcas4X;
    public JTextField txIcas4Y;
    public JButton btnIcas4Send;
    public JCheckBox chkIcas4Fd;
    public JCheckBox chkIcas4Ext;
    public JCheckBox chkIcas4Bsr;
    public JCheckBox chkIcas4SendUpdate;
    public JTextArea notesIPAddressCanLANcTextArea;
    public JTextField txIcas4Delay;
    public JCheckBox chkIcas4SendReleaseMessage;
    public JTextField txAudiX;
    public JTextField txAudiY;
    public JCheckBox chkAudiFD;
    public JCheckBox chkAudiBRS;
    public JCheckBox chkAudiEXT;
    public JButton btnAudiSend;
    public JTextField txAudiAbtMessageId;
    public JTextField txAudiDelay;
    public JCheckBox chkAudiSendRelease;
    public JTextField txAudiFreeFormMsg;
    public JButton btnAudiSendFreeFormMsg;
    public JComboBox cbCanCoverterType;
    public JTextField txPeakHsIpAddress;
    public JTextField txPeakHsPort;
    public JTextField txHorizontalResolution;
    public JTextField txVerticalResolution;

    public final ArrayList<AbtMessage> canMessages = new ArrayList<>();
    public final ArrayList<AbtMessage> mflMessages = new ArrayList<>();
    public final ArrayList<AbtMessage> icas4Messages = new ArrayList<>();
    public DefaultListModel<String> dlmLog = new DefaultListModel<>();

    public final Icas4 icas4;
    public final InitData initData = new InitData(this);
    public final InitGui initGui = new InitGui(this);
    public final MyListener myListener = new MyListener(this);
    public final Logging logging = new Logging(this);
    public final CanlancCommTools canlancCommTools = new CanlancCommTools(this);
    public final CommonCommTools commonCommTools = new CommonCommTools(this);
    public final CommonTools commonTools = new CommonTools(this);
    public final PeakHsCommTools peakHsCommTools = new PeakHsCommTools(this);
    public final CommunicationTools communicationTools = new CommunicationTools(this);

    public frmMain() {
        $$$setupUI$$$();

        initData.InitCanMessages();
        initGui.InitGuiForm();

        btnSendMessage1.addActionListener(e -> commonCommTools.SendMessage1());
        btnClearLog.addActionListener(e -> dlmLog.clear());
        btnStartSequence.addActionListener(e -> commonCommTools.LaunchSequence());
        btnSimulateTouch.addActionListener(e -> myListener.SimulateTouch());
        btnHome.addActionListener(e -> commonCommTools.SendHomeButton());
        btnMenu.addActionListener(e -> commonCommTools.SendMenuButton());
        btnPower.addActionListener(e -> commonCommTools.SendPOwerButton());

        panelMain.addMouseListener(myListener);
        panelMain.addMouseMotionListener(myListener);

        btnRunUdpTests.addActionListener(e -> RunUdpTests());

        btnSendMflMessage.addActionListener(e -> commonCommTools.SendSingleMflMsg());
        btnMflSendRawMsg.addActionListener(e -> commonCommTools.SendMflRawMsg());
        btnMflSendSequence.addActionListener(e -> commonCommTools.SendMflSequence());
        btnMflSendRawSequence.addActionListener(e -> commonCommTools.SendRawSequence());

        btnSaveLog.addActionListener(e -> logging.SaveLog());

        icas4 = new Icas4(icas4Messages, dlmLog);
        btnIcas4Send.addActionListener(e -> commonCommTools.SimulateIcas4Touch());
        btnAudiSend.addActionListener(e -> commonCommTools.SimulateAudiTouch());
        btnAudiSendFreeFormMsg.addActionListener(e -> commonCommTools.SendAudiFreeFormMsg());
    }

    /**
     *  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *  !!!!!!   Hardcoded SendMessage() from CanCommTools !!!!!!
     *  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    private void RunUdpTests() {

        LocalDateTime start, end;

        String ipAddr = txCanlancIpAddress.getText();
        int port = Integer.parseInt(txCanlancPort.getText());
        byte[] buffer = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int opak = Integer.parseInt(txUdpRepetitionsNo.getText());
        start = LocalDateTime.now();
        for (int i = 0; i < opak; i++) {
            canlancCommTools.SendMessage(ipAddr, port, buffer);
        }
        end = LocalDateTime.now();
        long milliseconds = ChronoUnit.MILLIS.between(start, end);
        txUdpResult.setText(String.format("Duration = %d ms", milliseconds));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("CAN Test 03");
        frame.setContentPane(new frmMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1400, 800);
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
        panel2.setLayout(new GridLayoutManager(7, 8, new Insets(0, 0, 0, 0), -1, -1));
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
        panel2.add(lbMousePosition, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, -1, 14, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("HOR Resolution:");
        panel2.add(label7, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txHorizontalResolution = new JTextField();
        Font txHorizontalResolutionFont = this.$$$getFont$$$(null, -1, 14, txHorizontalResolution.getFont());
        if (txHorizontalResolutionFont != null) txHorizontalResolution.setFont(txHorizontalResolutionFont);
        txHorizontalResolution.setHorizontalAlignment(0);
        txHorizontalResolution.setText("1740");
        panel2.add(txHorizontalResolution, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, -1, 14, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("VERT Resolution:");
        panel2.add(label8, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txVerticalResolution = new JTextField();
        Font txVerticalResolutionFont = this.$$$getFont$$$(null, -1, 14, txVerticalResolution.getFont());
        if (txVerticalResolutionFont != null) txVerticalResolution.setFont(txVerticalResolutionFont);
        txVerticalResolution.setHorizontalAlignment(0);
        txVerticalResolution.setText("720");
        panel2.add(txVerticalResolution, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("MFL Buttons", panel3);
        final JLabel label9 = new JLabel();
        label9.setText("MFL Buttons Message ID:");
        panel3.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txMflButtonsMsgId = new JTextField();
        Font txMflButtonsMsgIdFont = this.$$$getFont$$$(null, -1, 14, txMflButtonsMsgId.getFont());
        if (txMflButtonsMsgIdFont != null) txMflButtonsMsgId.setFont(txMflButtonsMsgIdFont);
        txMflButtonsMsgId.setText("5BF");
        panel3.add(txMflButtonsMsgId, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 30), null, 0, false));
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, -1, 14, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Message Type:");
        panel3.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMsgType = new JComboBox();
        panel3.add(cbMflMsgType, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 38), null, 0, false));
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, -1, 14, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Message Type 1:");
        panel3.add(label11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMessage1 = new JComboBox();
        panel3.add(cbMflMessage1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 38), null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$(null, -1, 14, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Delay [ms]:");
        panel3.add(label12, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflMessageDelay = new JTextField();
        Font txMflMessageDelayFont = this.$$$getFont$$$(null, -1, 14, txMflMessageDelay.getFont());
        if (txMflMessageDelayFont != null) txMflMessageDelay.setFont(txMflMessageDelayFont);
        txMflMessageDelay.setText("100");
        panel3.add(txMflMessageDelay, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label13 = new JLabel();
        Font label13Font = this.$$$getFont$$$(null, -1, 14, label13.getFont());
        if (label13Font != null) label13.setFont(label13Font);
        label13.setText("Message Type 2:");
        panel3.add(label13, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbMflMessage2 = new JComboBox();
        panel3.add(cbMflMessage2, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(98, 38), null, 0, false));
        btnMflSendSequence = new JButton();
        btnMflSendSequence.setText("Send Message");
        panel3.add(btnMflSendSequence, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkMflCanFD = new JCheckBox();
        chkMflCanFD.setText("CAN FD");
        panel3.add(chkMflCanFD, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        Font label14Font = this.$$$getFont$$$(null, -1, 14, label14.getFont());
        if (label14Font != null) label14.setFont(label14Font);
        label14.setText("Raw Message Value:");
        panel3.add(label14, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        final JLabel label15 = new JLabel();
        Font label15Font = this.$$$getFont$$$(null, -1, 14, label15.getFont());
        if (label15Font != null) label15.setFont(label15Font);
        label15.setText("Raw Message Value:");
        panel3.add(label15, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflRawMsgValue1 = new JTextField();
        Font txMflRawMsgValue1Font = this.$$$getFont$$$(null, -1, 14, txMflRawMsgValue1.getFont());
        if (txMflRawMsgValue1Font != null) txMflRawMsgValue1.setFont(txMflRawMsgValue1Font);
        txMflRawMsgValue1.setText("00112233");
        panel3.add(txMflRawMsgValue1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(124, 30), null, 0, false));
        final JLabel label16 = new JLabel();
        Font label16Font = this.$$$getFont$$$(null, -1, 14, label16.getFont());
        if (label16Font != null) label16.setFont(label16Font);
        label16.setText("Delay [ms]:");
        panel3.add(label16, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txMflMessageDelay2 = new JTextField();
        Font txMflMessageDelay2Font = this.$$$getFont$$$(null, -1, 14, txMflMessageDelay2.getFont());
        if (txMflMessageDelay2Font != null) txMflMessageDelay2.setFont(txMflMessageDelay2Font);
        txMflMessageDelay2.setText("100");
        panel3.add(txMflMessageDelay2, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(169, 30), null, 0, false));
        final JLabel label17 = new JLabel();
        Font label17Font = this.$$$getFont$$$(null, -1, 14, label17.getFont());
        if (label17Font != null) label17.setFont(label17Font);
        label17.setText("Raw Message Value:");
        panel3.add(label17, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        final JLabel label18 = new JLabel();
        label18.setText("X Coordinate (WIDTH):");
        panel4.add(label18, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txIcas4X = new JTextField();
        txIcas4X.setText("0");
        panel4.add(txIcas4X, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label19 = new JLabel();
        label19.setText("Y Coordinate (HEIGHT):");
        panel4.add(label19, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        final JLabel label20 = new JLabel();
        label20.setText("Delay between messages [ms]");
        panel4.add(label20, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        panel6.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Audi HCP ABT", panel6);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(2, 2, 2, 2), -1, -1));
        panel6.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JTextArea textArea1 = new JTextArea();
        Font textArea1Font = this.$$$getFont$$$("Courier New", -1, 12, textArea1.getFont());
        if (textArea1Font != null) textArea1.setFont(textArea1Font);
        textArea1.setText("Notes:\n- IP address & CanLANc port is shared from Configuration Tab\n");
        panel7.add(textArea1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(6, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel6.add(panel8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label21 = new JLabel();
        label21.setText("X Coordinate (WIDTH):");
        panel8.add(label21, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiX = new JTextField();
        txAudiX.setText("0");
        panel8.add(txAudiX, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label22 = new JLabel();
        label22.setText("Y Coordinate (WIDTH):");
        panel8.add(label22, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiY = new JTextField();
        txAudiY.setText("0");
        panel8.add(txAudiY, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnAudiSend = new JButton();
        btnAudiSend.setText("Send Message");
        panel8.add(btnAudiSend, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkAudiFD = new JCheckBox();
        chkAudiFD.setSelected(true);
        chkAudiFD.setText("FD");
        chkAudiFD.setToolTipText("Set the Flexible Date Rate bit");
        panel8.add(chkAudiFD, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkAudiBRS = new JCheckBox();
        chkAudiBRS.setText("BRS");
        chkAudiBRS.setToolTipText("Set the Bit Rate Switch bit");
        panel8.add(chkAudiBRS, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chkAudiEXT = new JCheckBox();
        chkAudiEXT.setText("EXT");
        chkAudiEXT.setToolTipText("Set the Error State Indicator bit");
        panel8.add(chkAudiEXT, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label23 = new JLabel();
        label23.setText("Delay between messages [ms]");
        panel8.add(label23, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiDelay = new JTextField();
        txAudiDelay.setText("20");
        panel8.add(txAudiDelay, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        chkAudiSendRelease = new JCheckBox();
        chkAudiSendRelease.setSelected(true);
        chkAudiSendRelease.setText("Send RELEASE Message");
        chkAudiSendRelease.setToolTipText("Send the UPDATE message at the end (for MQB37W and ICAS3 only)");
        panel8.add(chkAudiSendRelease, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label24 = new JLabel();
        label24.setText("FreeForm Message");
        panel8.add(label24, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiFreeFormMsg = new JTextField();
        txAudiFreeFormMsg.setText("");
        txAudiFreeFormMsg.setToolTipText("Provide sequence of Hexadecimal numbers separated by a space");
        panel8.add(txAudiFreeFormMsg, new GridConstraints(4, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnAudiSendFreeFormMsg = new JButton();
        btnAudiSendFreeFormMsg.setText("Send Message");
        panel8.add(btnAudiSendFreeFormMsg, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label25 = new JLabel();
        label25.setText("ABT Message ID (Decimal):");
        panel8.add(label25, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAudiAbtMessageId = new JTextField();
        Font txAudiAbtMessageIdFont = this.$$$getFont$$$(null, -1, 14, txAudiAbtMessageId.getFont());
        if (txAudiAbtMessageIdFont != null) txAudiAbtMessageId.setFont(txAudiAbtMessageIdFont);
        txAudiAbtMessageId.setText("483402347");
        txAudiAbtMessageId.setToolTipText("DECIMAL value of the Message ID !!!!");
        panel8.add(txAudiAbtMessageId, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel6.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(3, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel9.setForeground(new Color(-6513508));
        tabbedPane1.addTab("Configuration", panel9);
        final Spacer spacer6 = new Spacer();
        panel9.add(spacer6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label26 = new JLabel();
        label26.setText("CAN Converter Type:");
        panel9.add(label26, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cbCanCoverterType = new JComboBox();
        panel9.add(cbCanCoverterType, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel9.add(panel10, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel10.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   CAN Lanc Parameters   ", TitledBorder.CENTER, TitledBorder.TOP, this.$$$getFont$$$("Arial", Font.BOLD, 12, panel10.getFont()), new Color(-14930971)));
        final JLabel label27 = new JLabel();
        label27.setText("CanLANc IP Address:");
        panel10.add(label27, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCanlancIpAddress = new JTextField();
        Font txCanlancIpAddressFont = this.$$$getFont$$$(null, -1, 14, txCanlancIpAddress.getFont());
        if (txCanlancIpAddressFont != null) txCanlancIpAddress.setFont(txCanlancIpAddressFont);
        txCanlancIpAddress.setText("192.168.4.10");
        panel10.add(txCanlancIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label28 = new JLabel();
        label28.setText("CanLANc PORT:");
        panel10.add(label28, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txCanlancPort = new JTextField();
        Font txCanlancPortFont = this.$$$getFont$$$(null, -1, 14, txCanlancPort.getFont());
        if (txCanlancPortFont != null) txCanlancPort.setFont(txCanlancPortFont);
        txCanlancPort.setText("7777");
        panel10.add(txCanlancPort, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label29 = new JLabel();
        label29.setText("ABT Message ID:");
        panel10.add(label29, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txAbtMessageId = new JTextField();
        Font txAbtMessageIdFont = this.$$$getFont$$$(null, -1, 14, txAbtMessageId.getFont());
        if (txAbtMessageIdFont != null) txAbtMessageId.setFont(txAbtMessageIdFont);
        txAbtMessageId.setText("17F8F173");
        panel10.add(txAbtMessageId, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel9.add(panel11, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Peak HS Parameter   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$("Arial", Font.BOLD, 12, panel11.getFont()), new Color(-14930971)));
        final JLabel label30 = new JLabel();
        label30.setText("CanLANc IP Address:");
        panel11.add(label30, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label31 = new JLabel();
        label31.setText("CanLANc PORT:");
        panel11.add(label31, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txPeakHsIpAddress = new JTextField();
        Font txPeakHsIpAddressFont = this.$$$getFont$$$(null, -1, 14, txPeakHsIpAddress.getFont());
        if (txPeakHsIpAddressFont != null) txPeakHsIpAddress.setFont(txPeakHsIpAddressFont);
        txPeakHsIpAddress.setText("192.168.4.10");
        panel11.add(txPeakHsIpAddress, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        txPeakHsPort = new JTextField();
        Font txPeakHsPortFont = this.$$$getFont$$$(null, -1, 14, txPeakHsPort.getFont());
        if (txPeakHsPortFont != null) txPeakHsPort.setFont(txPeakHsPortFont);
        txPeakHsPort.setText("7777");
        panel11.add(txPeakHsPort, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel11.add(spacer7, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(3, 2, new Insets(5, 5, 5, 5), -1, -1));
        panel9.add(panel12, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "   Version Info   ", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$("Arial", Font.BOLD, 12, panel12.getFont()), new Color(-14930971)));
        final JLabel label32 = new JLabel();
        label32.setText("Version:");
        panel12.add(label32, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel12.add(spacer8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JTextField textField1 = new JTextField();
        textField1.setEditable(false);
        Font textField1Font = this.$$$getFont$$$(null, -1, 14, textField1.getFont());
        if (textField1Font != null) textField1.setFont(textField1Font);
        textField1.setText("1.1.0.0");
        panel12.add(textField1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label33 = new JLabel();
        label33.setText("Build Date:");
        panel12.add(label33, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JTextField textField2 = new JTextField();
        textField2.setEditable(false);
        Font textField2Font = this.$$$getFont$$$(null, -1, 14, textField2.getFont());
        if (textField2Font != null) textField2.setFont(textField2Font);
        textField2.setText("2024-08-02");
        panel12.add(textField2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer9 = new Spacer();
        panel9.add(spacer9, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Logger", panel13);
        btnClearLog = new JButton();
        btnClearLog.setText("Clear");
        panel13.add(btnClearLog, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer10 = new Spacer();
        panel13.add(spacer10, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel13.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbLog = new JList();
        Font lbLogFont = this.$$$getFont$$$("Courier New", -1, 14, lbLog.getFont());
        if (lbLogFont != null) lbLog.setFont(lbLogFont);
        scrollPane1.setViewportView(lbLog);
        btnSaveLog = new JButton();
        btnSaveLog.setText("Save Log");
        btnSaveLog.setToolTipText("Save the log to the CAN.LOG file");
        panel13.add(btnSaveLog, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("UDP Tests", panel14);
        final JLabel label34 = new JLabel();
        label34.setText("Number of repetitions:");
        panel14.add(label34, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer11 = new Spacer();
        panel14.add(spacer11, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer12 = new Spacer();
        panel14.add(spacer12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        txUdpRepetitionsNo = new JTextField();
        txUdpRepetitionsNo.setText("100");
        panel14.add(txUdpRepetitionsNo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnRunUdpTests = new JButton();
        btnRunUdpTests.setText("Run Tests !");
        panel14.add(btnRunUdpTests, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txUdpResult = new JTextField();
        panel14.add(txUdpResult, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
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
