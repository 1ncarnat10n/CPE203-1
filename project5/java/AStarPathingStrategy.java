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

        start.setG(0);
        start.setH(start.distanceSquared(end));
        openSet.add(start);

        while (!openSet.isEmpty()) {

            if (openSet.size() > 100) {
                break;
            }

            Point current = openSet.remove();

            if (withinReach.test(current, end)) {
                List<Point> result = new ArrayList<>();
                Point retracePath = current;
                while (retracePath != start) {
                    result.add(retracePath);
                    retracePath = retracePath.getCameFrom();
                }
                Collections.reverse(result);
                return result;
            }


            List<Point> validNeighbors = potentialNeighbors.apply(current)
                    .filter(neighbor -> canPassThrough.test(neighbor))
                    .filter(neighbor -> !closedSet.contains(neighbor))
                    .collect(Collectors.toList());

            for (Point neighbor: validNeighbors) {

                double neighborNewG = current.getG() + neighbor.distanceSquared(current);
                double neighborNewH = neighbor.distanceSquared(end);
                double neighborNewHeuristic = neighborNewG + neighborNewH;

                if (neighborNewHeuristic < neighbor.getHeuristic()) {
                    neighbor.setG(neighborNewG);
                    neighbor.setH(neighborNewH);
                    neighbor.setCameFrom(current);
                }


                if (!openSet.contains(neighbor)) {
                    neighbor.setCameFrom(current);
                    openSet.add(neighbor);
                }

            }
            closedSet.add(current);
        }
        List<Point> noPathFound = new ArrayList<>();
        noPathFound.add(start);
        return noPathFound;
    }
}
