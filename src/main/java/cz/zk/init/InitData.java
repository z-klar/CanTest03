package cz.zk.init;

import cz.zk.AbtMessage;
import cz.zk.frmMain;

public class InitData {

    private frmMain parent;

    public InitData(frmMain parent) {
        this.parent = parent;
    }

    public void InitCanMessages() {
        parent.canMessages.add(new AbtMessage("HOME_PRESSED", "04306601AAAAAAAA"));
        parent.canMessages.add(new AbtMessage("HOME_RELEASED", "04306600AAAAAAAA"));
        parent.canMessages.add(new AbtMessage("MENU_PRESSED", "04301A0100AAAAAA"));
        parent.canMessages.add(new AbtMessage("MENU_RELEASED", "04301A0010AAAAAA"));
        parent.canMessages.add(new AbtMessage("POWER_PRESSED", "04303801AAAAAAAA"));
        parent.canMessages.add(new AbtMessage("POWER_RELEASED", "04303800AAAAAAAA"));

        parent.canMessages.add(new AbtMessage("PRE_TOUCH_PRESS", "11"));
        parent.canMessages.add(new AbtMessage("PRE_TOUCH_RELEASE", "10"));
        parent.canMessages.add(new AbtMessage("POST_TOUCH_PRESS", "20"));
        parent.canMessages.add(new AbtMessage("POST_TOUCH_RELEASE", "FF"));
        parent.canMessages.add(new AbtMessage("TOUCH_PREFIX_GLOBAL", "07A000"));
        parent.canMessages.add(new AbtMessage("X_COORDS_PREFIX", "0001"));  // orig

        parent.mflMessages.add(new AbtMessage("END_COMMAND", "000000A3"));
        parent.mflMessages.add(new AbtMessage("KEY_RELEASED_NO_KEY", "000001A3"));
        parent.mflMessages.add(new AbtMessage("CONTEXT_MENU", "010001A3"));
        parent.mflMessages.add(new AbtMessage("MENU_UP_NEXT_SCREEN", "020001A3"));
        parent.mflMessages.add(new AbtMessage("MENU_DOWN_NEXT_SCREEN", "030001A3"));
        parent.mflMessages.add(new AbtMessage("UP", "040001A3"));
        parent.mflMessages.add(new AbtMessage("DOWN", "050001A3"));
        parent.mflMessages.add(new AbtMessage("UP_THUMBWHEEL", "060001A3"));
        parent.mflMessages.add(new AbtMessage("DOWN_THUMBWHEEL", "06000FA3"));
        parent.mflMessages.add(new AbtMessage("OK_THUMBWHEEL_BUTTON", "070001A3"));
        parent.mflMessages.add(new AbtMessage("CANCEL_ESCAPE", "080001A3"));
        parent.mflMessages.add(new AbtMessage("MAIN_MENU", "090001A3"));
        parent.mflMessages.add(new AbtMessage("SIDE_MENU_LEFT", "0A0001A3"));
        parent.mflMessages.add(new AbtMessage("SIDE_MENU_RIGHT", "0B0001A3"));
        parent.mflMessages.add(new AbtMessage("FAS_MENU", "0C0001A3"));
        parent.mflMessages.add(new AbtMessage("LEFT_RIGHT_THUMBWHEEL", "0D0001A3"));
        parent.mflMessages.add(new AbtMessage("VOLUME_UP", "100001A3"));
        parent.mflMessages.add(new AbtMessage("VOLUME_DOWN", "110001A3"));
        parent.mflMessages.add(new AbtMessage("VOLUME_UP_THUMBWHEEL", "120001A3"));
        parent.mflMessages.add(new AbtMessage("VOLUME_DOWN_THUMBWHEEL", "12000FA3"));
        parent.mflMessages.add(new AbtMessage("VOLUME_THUMBWHEEL_BUTTON", "130001A3"));
        parent.mflMessages.add(new AbtMessage("AUDIO_SOURCE", "140001A3"));
        parent.mflMessages.add(new AbtMessage("ARROW_A_UP_RIGHT", "150001A3"));
        parent.mflMessages.add(new AbtMessage("ARROW_A_DOWN_LEFT", "160001A3"));
        parent.mflMessages.add(new AbtMessage("ARROW_B_UP_RIGHT", "170001A3"));
        parent.mflMessages.add(new AbtMessage("ARROW_B_DOWN_LEFT", "180001A3"));
        parent.mflMessages.add(new AbtMessage("PTT_PUSHTOTALK", "190001A3"));
        parent.mflMessages.add(new AbtMessage("PTT_CANCEL", "1A0001A3"));
        parent.mflMessages.add(new AbtMessage("ROUT_INFO", "1B0001A3"));
        parent.mflMessages.add(new AbtMessage("HOOK", "1C0001A3"));
        parent.mflMessages.add(new AbtMessage("HANG_UP", "1D0001A3"));
        parent.mflMessages.add(new AbtMessage("OFF_HOOK", "1E0001A3"));
        parent.mflMessages.add(new AbtMessage("LIGHT_ON_OFF", "1F0001A3"));
        parent.mflMessages.add(new AbtMessage("MUTE", "200001A3"));
        parent.mflMessages.add(new AbtMessage("JOKER1", "210001A3"));

        parent.icas4Messages.add(new AbtMessage("PRE_TOUCH_PRESS", "11"));
        parent.icas4Messages.add(new AbtMessage("PRE_TOUCH_RELEASE", "10"));
        parent.icas4Messages.add(new AbtMessage("PRE_TOUCH_UPDATE", "11"));
        parent.icas4Messages.add(new AbtMessage("POST_TOUCH_PRESS", "FD"));
        parent.icas4Messages.add(new AbtMessage("POST_TOUCH_RELEASE", "FE"));
        parent.icas4Messages.add(new AbtMessage("POST_TOUCH_UPDATE", "FF"));
        parent.icas4Messages.add(new AbtMessage("MSG_PREFIX", "07A000"));

    }
}
