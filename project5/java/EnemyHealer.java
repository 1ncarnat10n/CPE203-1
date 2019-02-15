import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class EnemyHealer extends AnimatedEntity {

    public EnemyHealer(Point position, List<PImage> images) {
        super("enemy_healer", 0, position, 1800, 1000, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        Optional<Entity> wyvern = getPosition().findNearest(world, "Wyvern");

        if (wyvern.isPresent() && moveToWyvern(world, wyvern.get(), scheduler)) {
        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }

    private boolean moveToWyvern(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            Wyvern enemy = (Wyvern)target;
            enemy.setHealth(enemy.getHealth() + 10);
            return true;
        } else {
            Point nextPos = this.nextPositionEnemyHealer(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private Point nextPositionEnemyHealer(WorldModel world, Point destPos) {

        Predicate<Point> canPassThrough = (point) ->
                !point.isOccupied(world);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);
        PathingStrategy aStar = new AStarPathingStrategy();
        List<Point> path =  aStar.computePath(getPosition(), destPos, canPassThrough, withinReach,PathingStrategy.CARDINAL_NEIGHBORS);

        return path.get(0);
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
