import java.awt.*;

public interface Shape {
    Color getColor();
    double getArea();
    double getPerimeter();
    void setColor(Color newColor);
    void translate(Point valuePoint);
}
