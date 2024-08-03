package cz.zk.comm_tools;

import cz.zk.frmMain;

public class CommonCommTools {
    private frmMain parent;

    public CommonCommTools(frmMain parent) {
        this.parent = parent;
    }

    public void SendHomeButton() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.LaunchSequence("HOME_PRESSED", "HOME_RELEASED");
                break;
            case 1:

                break;
        }
    }

    //------------------------------------------------------------
    public void SendMenuButton() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.LaunchSequence("MENU_PRESSED", "MENU_RELEASED");
                break;
            case 1:

                break;
        }
    }

    //------------------------------------------------------------
    public void SendPOwerButton() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.LaunchSequence("POWER_PRESSED", "POWER_RELEASED");
                break;
            case 1:

                break;
        }
    }

    public void LaunchSequence() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.LaunchSequence();
                break;
            case 1:    // PEAK HS

                break;
        }
    }

    public void SimulateIcas4Touch() {
        int x = Integer.parseInt(parent.txIcas4X.getText());
        int y = Integer.parseInt(parent.txIcas4Y.getText());
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SimulateIcas4Touch(x, y);
                break;
            case 1:

                break;
        }
    }

    //------------------------------------------------------------
    public void SimulateAudiTouch() {
        int x = Integer.parseInt(parent.txAudiX.getText());
        int y = Integer.parseInt(parent.txAudiY.getText());
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SimulateAudiTouch(x, y);
                break;
            case 1:

                break;
        }
    }
    public void SendAudiFreeFormMsg() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendAudiFreeFormMsg();
                break;
            case 1:

                break;
        }
    }

    public void SendMflSequence() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch(index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendMflSequence();
                break;
            case 1:

                break;
        }
    }

    public void SendSingleMflMsg() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch (index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendSingleMflMsg();
                break;
            case 1:

                break;
        }
    }

    public void SendMflRawMsg() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch (index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendMflRawMsg();
                break;
            case 1:

                break;
        }
    }

    public void SendRawSequence() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch (index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendRawSequence();
                break;
            case 1:

                break;
        }
    }

    public void SendMessage1() {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch (index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SendRawSequence();
                break;
            case 1:

                break;
        }
    }

    public void SimulateTouch(int x, int y) {
        int index = parent.cbCanCoverterType.getSelectedIndex();
        switch (index) {
            case 0:    // CANlanc
                parent.canlancCommTools.SimulateTouch(x, y);
                break;
            case 1:
                parent.peakHsCommTools.SimulateTouch(x, y);
                break;
        }

    }


}
