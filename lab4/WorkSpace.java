import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class WorkSpace {

    private ArrayList<Shape> shapes = new ArrayList<Shape>();

    public WorkSpace() {}

    public void add(Shape shape) {
        shapes.add(shape);
    }

    public Shape get(int index) {
        return shapes.get(index);
    }

    public int size() {
        return shapes.size();
    }

    public ArrayList<Circle> getCircles() {
        ArrayList<Circle> circles = new ArrayList<Circle>();
        for (Shape shape: shapes) {
            if (shape instanceof Circle) {
                circles.add((Circle)shape);
            }
        }
        return circles;
    }

    public ArrayList<Rectangle> getRectangles() {
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Shape shape: shapes) {
            if (shape instanceof Rectangle) {
                rectangles.add((Rectangle) shape);
            }
        }
        return rectangles;
    }

    public ArrayList<Triangle> getTriangles() {
        ArrayList<Triangle> triangles = new ArrayList<Triangle>();
        for (Shape shape: shapes) {
            if (shape instanceof Triangle) {
                triangles.add((Triangle) shape);
            }
        }
        return triangles;
    }

    public ArrayList<ConvexPolygon> getConvexPolygons() {
        ArrayList<ConvexPolygon> convexPolygons = new ArrayList<ConvexPolygon>();
        for (Shape shape: shapes) {
            if (shape instanceof ConvexPolygon) {
                convexPolygons.add((ConvexPolygon) shape);
            }
        }
        return convexPolygons;
    }

    public ArrayList<Shape> getShapesByColor(Color color) {
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        for (Shape shape: shapes) {
            if (shape.getColor() == color) {
                shapes.add(shape);
            }
        }
        return shapes;
    }

    public double getAreaOfAllShapes() {
        double sumArea = 0;
        for (Shape shape: shapes) {
            sumArea += shape.getArea();
        }
        return sumArea;
    }

    public double getPerimeterOfAllShapes() {
        double sumPerimeter = 0;
        for (Shape shape: shapes) {
            sumPerimeter += shape.getPerimeter();
        }
        return sumPerimeter;
    }

}
