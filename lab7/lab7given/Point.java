public class Point
{
   public final int x;
   public final int y;

   private double g;
   private double h;
   private Point cameFrom;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public Point getCameFrom() { return cameFrom;}

   public void setCameFrom(Point cameFrom) {this.cameFrom = cameFrom;}

   public int distanceSquared(Point p2) {
      int deltaX = x - p2.x;
      int deltaY = y - p2.y;
      return deltaX * deltaX + deltaY * deltaY;
   }

   public double getH() {return h;}

   public void setH(double h) {this.h = h;}

   public double getG() {return g;}

   public void setG(double g) {this.g = g;}

   public double getHeuristic() {return g + h;}
}
