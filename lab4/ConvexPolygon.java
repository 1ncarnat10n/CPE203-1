import java.awt.*;

class ConvexPolygon implements Shape {

    private Point[] pointArray;
    private Color color;

    public ConvexPolygon(Point[] pointArray, Color color) {
        this.pointArray = pointArray;
        this.color = color;
    }

    public Point getVertex(int index) {
        return pointArray[index];
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public double getArea() {
        double area = 0;
        int j = pointArray.length - 1;    // The previous vertex index
        for (int i=0; i < pointArray.length; i++) {
            area += (pointArray[j].x* pointArray[i].y);
            j = i;                        // Update j to be the 'prev' point of the next vertex
        }

        return Math.abs(area / 2);

    }

    public double getPerimeter() {
        double perimeter = 0;
        int j = pointArray.length - 1;    // The previous vertex index
        for (int i=0; i < pointArray.length; i++) {
            perimeter += pointArray[j].distance(pointArray[i]);
            j = i;                        // Update j to be the 'prev' point of the next vertex
        }
        return perimeter;
    }

    public void translate(Point valuePoint) {
        for (Point point: pointArray) {
            point.translate(valuePoint.x, valuePoint.y);
        }
    }
}
