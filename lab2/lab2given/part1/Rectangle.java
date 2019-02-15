public class Rectangle {
    private final Point topL;
    private final Point botR;

    public Rectangle(Point topL, Point botR) {
        this.topL = topL;
        this.botR = botR;
    }

    public Point getTopLeft() {
        return topL;
    }

    public Point getBottomRight() {
        return botR;
    }
}
