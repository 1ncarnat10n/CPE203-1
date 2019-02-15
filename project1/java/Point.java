import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

final class Point {
    private static final int ORE_REACH = 1;
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Optional<Entity> findNearest(WorldModel world,
                                        EntityKind kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.entities) {
            if (entity.kind == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType);
    }

    public boolean isOccupied(WorldModel world) {
        return withinBounds(world) &&
                world.getOccupancyCell(this) != null;
    }

    public boolean withinBounds(WorldModel world) {
        return y >= 0 && y < world.numRows &&
                x >= 0 && x < world.numCols;
    }

    public Optional<Point> findOpenAround(WorldModel world) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(x + dx, y + dy);
                if (newPt.withinBounds(world) &&
                        !newPt.isOccupied(world)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public boolean adjacent(Point p2) {
        return (x == p2.x && Math.abs(y - p2.y) == 1) ||
                (y == p2.y && Math.abs(x - p2.x) == 1);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point &&
                ((Point) other).x == this.x &&
                ((Point) other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    private int distanceSquared(Point p2) {
        int deltaX = x - p2.x;
        int deltaY = y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    private Optional<Entity> nearestEntity(List<Entity> entities) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.position.distanceSquared(this);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(this);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
}
