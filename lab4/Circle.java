import java.awt.*;

class Circle implements Shape {

    private Point center;
    private double radius;
    private Color color;

    public Circle(double radius, Point center,  Color color) {
        this.center = center;
        this.radius = radius;
        this.color  = color;
    }

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double newRadius) {
        radius = newRadius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public void translate(Point valuePoint) {
        center.translate(valuePoint.x, valuePoint.y);
    }

}
