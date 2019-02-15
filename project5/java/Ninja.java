import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class Ninja extends AnimatedEntity {

    public Ninja(Point position, List<PImage> images) {
        super("ninja", 0, position, 1800, 1000, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;
        List<Entity> nearbyEntities = new ArrayList<>();

        Optional<Entity> _wyvern = getPosition().findNearest(world, "Wyvern");
        Optional<Entity> _enemyHealer = getPosition().findNearest(world, "EnemyHealer");

        if (_wyvern.isPresent()) {
            nearbyEntities.add(_wyvern.get());
        } else if (_enemyHealer.isPresent()) {
            nearbyEntities.add(_enemyHealer.get());
        }

        if (nearbyEntities.size() > 0) {
            int distance = 1000000;
            Entity closestTarget = nearbyEntities.get(0);
            for (Entity entity: nearbyEntities) {
                if (entity.getPosition().distanceSquared(getPosition()) < distance) {
                    closestTarget = entity;
                }
            }
            moveToAttack(world, closestTarget, scheduler);

            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
        else {
            Point pos = getPosition();

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            Blacksmith smith = new Blacksmith("blacksmith",
                    pos, 25, imageStore.getImageList("blacksmith"));
            world.tryAddEntity(smith);
            smith.scheduleActivity(scheduler, world, imageStore);
        }
    }

    private boolean moveToAttack(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            if (target.getClass().equals(Wyvern.class)) {
                Wyvern enemy = (Wyvern)target;
                enemy.setHealth(enemy.getHealth() - 25);
                if (enemy.getHealth() <= 0) {
                    world.removeEntity(target);
                    scheduler.unscheduleAllEvents(target);
                }
            } else if (target.getClass().equals(EnemyHealer.class)) {
                world.removeEntity(target);
                scheduler.unscheduleAllEvents(target);
            }
        } else {
            Point nextPos = this.nextPositionNinja(world, target.getPosition());

            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
        }
        return false;
    }

    private Point nextPositionNinja(WorldModel world, Point destPos) {

        Predicate<Point> canPassThrough = (point) ->
                !point.isOccupied(world) &&
                        !world.getBackgroundCell(point).getId().equals("portal_steps");
        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);
        PathingStrategy aStar = new AStarPathingStrategy();
        List<Point> path =  aStar.computePath(getPosition(), destPos, canPassThrough, withinReach,PathingStrategy.CARDINAL_NEIGHBORS);

        return path.get(0);
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
