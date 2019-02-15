import java.awt.*;

class Triangle implements Shape {

    private Point vertexA;
    private Point vertexB;
    private Point vertexC;
    private Color color;

    public Triangle(Point A, Point B, Point C, Color color) {
        this.vertexA = A;
        this.vertexB = B;
        this.vertexC = C;
        this.color = color;
    }

    public Point getVertexA() {
        return vertexA;
    }

    public Point getVertexB() {
        return vertexB;
    }

    public Point getVertexC() {
        return vertexC;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color newColor) {
        color = newColor;
    }

    public double getArea() {
        return Math.abs( ( vertexA.x * (vertexB.y - vertexC.y) +
                           vertexB.x * (vertexC.y - vertexA.y) +
                           vertexC.x * (vertexA.y - vertexB.y) )
                           / 2
        );
    }

    public double getPerimeter() {
        return vertexA.distance(vertexB) +
               vertexB.distance(vertexC) +
               vertexC.distance(vertexA);
    }

    public void translate(Point valuePoint) {
        vertexA.translate(valuePoint.x, valuePoint.y);
        vertexB.translate(valuePoint.x, valuePoint.y);
        vertexC.translate(valuePoint.x, valuePoint.y);
    }
}
