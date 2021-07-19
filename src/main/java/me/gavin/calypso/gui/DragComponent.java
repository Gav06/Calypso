package me.gavin.calypso.gui;

public abstract class DragComponent extends Component {

    protected boolean dragging;

    private int dragX, dragY;

    public DragComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected void updateDragPos(int mouseX, int mouseY) {
        if (dragging) {
            x = (mouseX - dragX);
            y = (mouseY - dragY);
        }
    }

    public void startDragLogic(int mouseX, int mouseY) {
        dragging = true;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    public void stopDragLogic(int mouseX, int mouseY) {
        dragging = false;
    }
}
