public class Point {
    private double x;
    private double y;

    public Point(double _x, double _y) {
        x = _x;
        y = _y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return Math.hypot(0-x, 0-y);
    }

    public double getAngle() {
        return Math.atan2(y, x);
    }

    public Point rotate90() {
        return new Point(-y, x);
    }
}