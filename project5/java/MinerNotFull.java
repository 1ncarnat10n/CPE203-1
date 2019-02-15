import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class MinerNotFull extends AnimatedEntity {

    private int resourceCount;

    public MinerNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, resourceLimit, position, actionPeriod, animationPeriod, images, -1);

        this.resourceCount = 0;
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        Optional<Entity> notFullTarget = getPosition().findNearest(world, "Ore");

        if (!notFullTarget.isPresent() || !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !transformNotFull(world, scheduler, imageStore)) {
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

    private boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = nextPositionMiner(world, target.getPosition());

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

    private boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= getResourceLimit()) {
            MinerFull miner = new MinerFull(getId(), getResourceLimit(),
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            miner.setImageIndex(getImageIndex());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActivity(scheduler, world, imageStore);
            miner.scheduleAnimation(scheduler);

            return true;
        }

        return false;
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
