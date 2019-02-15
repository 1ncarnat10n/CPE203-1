public class Bigger {
    public static double whichIsBigger(Circle circle, Rectangle rectangle, Polygon polygon) {
        return Math.max(Math.max(circle.perimeter(), rectangle.perimeter()), polygon.perimeter());
    }
}