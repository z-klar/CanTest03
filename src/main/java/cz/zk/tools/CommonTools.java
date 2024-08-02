package cz.zk.tools;

import cz.zk.AbtMessage;
import cz.zk.frmMain;

public class CommonTools {
    private frmMain parent;

    public CommonTools(frmMain parent) {
        this.parent = parent;
    }

    private static String [] hexChars = {"0","1","2","3","4","5","6","7",
            "8","9","A","B","C","D","E","F"};


    /**
     *
     * @param literal
     * @return
     */
    public static int convertByte(String literal) {
        String spom = paddString(literal, 2);
        return(16*getHexValue(spom.substring(0,1)) + getHexValue(spom.substring(1)));
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


    public String getMessageBody(String msgName) {

        for (AbtMessage msg : parent.canMessages) {
            if (msg.getName().equals(msgName)) {
                return (msg.getBody());
            }
        }
        return ("");
    }

}
