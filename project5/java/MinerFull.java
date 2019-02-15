import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class MinerFull extends AnimatedEntity {

    public MinerFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, resourceLimit, position, actionPeriod, animationPeriod, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        Optional<Entity> fullTarget = getPosition().findNearest(world, "Blacksmith");

        if (fullTarget.isPresent() && moveToFull(world, fullTarget.get(), scheduler)) {
            transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }

    private Point nextPositionMiner(WorldModel world, Point destPos) {

        Predicate<Point> canPassThrough = (point) ->
                !point.isOccupied(world) &&
                !world.getBackgroundCell(point).getId().equals("portal_steps");
        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);
        PathingStrategy aStar = new AStarPathingStrategy();
        List<Point> path =  aStar.computePath(getPosition(), destPos, canPassThrough, withinReach,PathingStrategy.CARDINAL_NEIGHBORS);

        return path.get(0);
    }

    private boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

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

    private void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        MinerNotFull miner = new MinerNotFull(getId(), getResourceLimit(), getPosition(), getActionPeriod(), getAnimationPeriod(), getImages());

        miner.setImageIndex(getImageIndex());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActivity(scheduler, world, imageStore);
        miner.scheduleAnimation(scheduler);
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
