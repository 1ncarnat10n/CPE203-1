import java.awt.*;

class Rectangle implements Shape {

    private Point topLeft;
    private double width;
    private double height;
    private Color color;

    public Rectangle(double width, double height, Point topLeft, Color color) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double newWidth) {
        width = newWidth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double newHeight) {
        height = newHeight;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public double getArea() {
        return width * height;
    }

    public double getPerimeter() {
        return 2 * width + 2 * height;
    }

    public void translate(Point valuePoint) {
        topLeft.translate(valuePoint.x, valuePoint.y);
    }

}
