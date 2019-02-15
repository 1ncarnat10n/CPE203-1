import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> closedSet = new ArrayList<>();
        Queue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(Point::getHeuristic));

//        start.setH(start.distanceSquared(end));
//        start.setG(start.distanceSquared(end));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();

            if (withinReach.test(current, end)) {
                closedSet.add(current);
                closedSet.remove(start);
                return closedSet;
            }

            List<Point> validNeighbors = potentialNeighbors.apply(current)
                    .filter(neighbor -> canPassThrough.test(neighbor))
                    .collect(Collectors.toList());

            for (Point neighbor: validNeighbors) {
                neighbor.setG(current.getG() + neighbor.distanceSquared(current));
                neighbor.setH(neighbor.distanceSquared(end));
                neighbor.setCameFrom(current);

                if (current.equals(start)) {
                    openSet.add(neighbor);
                }
                if (neighbor.getHeuristic() < current.getHeuristic()) {
                    openSet.add(neighbor);
                }
            }
            closedSet.add(current);
        }
        return closedSet;
    }
}
