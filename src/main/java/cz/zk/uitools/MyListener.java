package cz.zk.uitools;

import cz.zk.frmMain;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

public class MyListener  extends MouseInputAdapter {

    private frmMain parent;

    public MyListener(frmMain parent) {
        this.parent = parent;
    }

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

    private void canvasMoved(MouseEvent e) {
        double coordX = e.getX();
        double coordY = e.getY();
        double panelWidth = parent.panelMain.getWidth();
        double panelHeight = parent.panelMain.getHeight();

        double ABT_WIDTH = Double.parseDouble(parent.txHorizontalResolution.getText());
        double ABT_HEIGHT = Double.parseDouble(parent.txVerticalResolution.getText());

        double finalX = (ABT_WIDTH / panelWidth) * coordX;
        double finalY = (ABT_HEIGHT / panelHeight) * coordY;

        String spom = String.format("X: %d   Y: %d", (int) finalX, (int) finalY);
        parent.lbMousePosition.setText(spom);
    }

    private void mainClick(MouseEvent e) {
        double coordX = e.getX();
        double coordY = e.getY();
        double panelWidth = parent.panelMain.getWidth();
        double panelHeight = parent.panelMain.getHeight();

        double ABT_WIDTH = Double.parseDouble(parent.txHorizontalResolution.getText());
        double ABT_HEIGHT = Double.parseDouble(parent.txVerticalResolution.getText());

        double finalX = (ABT_WIDTH / panelWidth) * coordX;
        double finalY = (ABT_HEIGHT / panelHeight) * coordY;

        /*
        JOptionPane.showMessageDialog(null,
                String.format("Real / ABT: X = %f / %f   Y = %f / %f ",
                                      coordX, finalX, coordY, finalY));
        */
        parent.commonCommTools.SimulateTouch((int) finalX, (int) finalY);
    }

    public void SimulateTouch() {
        int x = Integer.parseInt(parent.txXCoord.getText());
        int y = Integer.parseInt(parent.txYCoord.getText());
        parent.commonCommTools.SimulateTouch(x, y);
    }

}
