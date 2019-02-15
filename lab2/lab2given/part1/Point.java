public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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

    public double distance(Point other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}