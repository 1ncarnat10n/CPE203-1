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
    
    public double perimeter() {
        double tlX = topL.getX();
        double tlY = topL.getY();
        double brX = botR.getX();
        double brY = botR.getY();
        return 2 * (Math.abs(brX - tlX) + Math.abs(brY - tlY));
    }

}
