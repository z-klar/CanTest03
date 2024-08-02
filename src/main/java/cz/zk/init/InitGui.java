package cz.zk.init;

import cz.zk.AbtMessage;
import cz.zk.frmMain;

public class InitGui {
    private frmMain parent;

    public InitGui(frmMain parent) {
        this.parent = parent;
    }
    public int InitGuiForm() {

        parent.lbLog.setModel(parent.dlmLog);

        parent.cbMessageType1.removeAllItems();
        parent.cbMessageType10.removeAllItems();
        parent.cbMessageType11.removeAllItems();
        for (AbtMessage msg : parent.canMessages) {
            parent.cbMessageType1.addItem(msg.getName());
            parent.cbMessageType10.addItem(msg.getName());
            parent.cbMessageType11.addItem(msg.getName());
        }

        parent.cbMflMsgType.removeAllItems();
        parent.cbMflMessage1.removeAllItems();
        parent.cbMflMessage2.removeAllItems();
        for (AbtMessage msg : parent.mflMessages) {
            parent.cbMflMsgType.addItem(msg.getName());
            parent.cbMflMessage1.addItem(msg.getName());
            parent.cbMflMessage2.addItem(msg.getName());
        }

        parent.cbCanCoverterType.removeAllItems();
        parent.cbCanCoverterType.addItem("CANlanc V1 (old)");
        parent.cbCanCoverterType.addItem("PEAK Can Gateway HS");
        parent.cbCanCoverterType.setSelectedIndex(0);
        return 0;
    }
}
