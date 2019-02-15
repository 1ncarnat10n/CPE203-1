public class Bigger {
    public static double whichIsBigger(Circle circle, Rectangle rectangle, Polygon polygon) {
        return Math.max(Math.max(Util.perimeter(circle), Util.perimeter(rectangle)), Util.perimeter(polygon));
    }
}