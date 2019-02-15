import java.util.List;

public class Polygon {
    private final List<Point> points;

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public double perimeter() {
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
