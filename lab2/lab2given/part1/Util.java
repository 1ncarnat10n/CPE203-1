import java.util.List;

public class Util {
    public static double perimeter(Circle circle) {
        return 2 * Math.PI * Math.pow(circle.getRadius(), 2);
    }

    public static double perimeter(Rectangle rectangle) {
        double tlX = rectangle.getTopLeft().getX();
        double tlY = rectangle.getTopLeft().getY();
        double brX = rectangle.getBottomRight().getX();
        double brY = rectangle.getBottomRight().getY();
        return 2 * (Math.abs(brX - tlX) + Math.abs(brY - tlY));
    }

    public static double perimeter(Polygon polygon) {
        List<Point> points = polygon.getPoints();
        double perimeter = 0;
        boolean first = true;
        for (int i=0; i < points.size()-1; i++) {
            perimeter += points.get(i).distance(points.get(i+1));

            // On the first element we will add the distance from the last point to this point to get the full loop of walking around the polygon.
            if (first) {
                perimeter += points.get(points.size()-1).distance(points.get(i));
                first = false;
            }
        }
        return perimeter;
    }
}
