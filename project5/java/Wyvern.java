import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class Wyvern extends AnimatedEntity {

    private int health = 100;

    public Wyvern(Point position, List<PImage> images) {
        super("wyvern", 0, position, 500, 1000, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;
        List<Entity> nearbyEntities = new ArrayList<>();

        Optional<Entity> _blacksmithTarget = getPosition().findNearest(world, "Blacksmith");
        Optional<Entity> _ninjaTarget = getPosition().findNearest(world, "Ninja");
        Optional<Entity> _minerFullTarget = getPosition().findNearest(world, "MinerFull");
        Optional<Entity> _minerNotFullTarget = getPosition().findNearest(world, "MinerNotFull");

        if (_minerFullTarget.isPresent()) {
            nearbyEntities.add(_minerFullTarget.get());
        } else if (_minerNotFullTarget.isPresent()) {
            nearbyEntities.add(_minerNotFullTarget.get());
        } else if (_blacksmithTarget.isPresent()) {
            nearbyEntities.add(_blacksmithTarget.get());
        } else if (_ninjaTarget.isPresent()) {
            nearbyEntities.add(_ninjaTarget.get());
        }

        if (nearbyEntities.size() > 0) {
            int distance = 1000000;
            Entity closestTarget = nearbyEntities.get(0);
            for (Entity entity: nearbyEntities) {
                if (entity.getPosition().distanceSquared(getPosition()) < distance) {
                    closestTarget = entity;
                }
            }
            if (moveToAttack(world, closestTarget, scheduler)) {

            }

        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }


    }

    private boolean moveToAttack(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            if (!target.getClass().equals(Ninja.class)) {
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        } else {
            Point nextPos = this.nextPositionWyvern(world, target.getPosition());

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

    private Point nextPositionWyvern(WorldModel world, Point destPos) {

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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
