import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class OreBlob extends AnimatedEntity {

    private static final String QUAKE_KEY = "quake";

    // TODO: Check resource limit?
    public OreBlob(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, 0, position, actionPeriod, animationPeriod, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        Optional<Entity> blobTarget = getPosition().findNearest(world, "Vein");
        long nextPeriod = getActionPeriod();

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Quake quake = new Quake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += getActionPeriod();
                quake.scheduleActivity(scheduler, world, imageStore);
                quake.scheduleAnimation(scheduler);
            }
        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), nextPeriod);
    }

    private Point nextPositionOreBlob(WorldModel world, Point destPos) {

        Predicate<Point> canPassThrough = (point) -> !point.isOccupied(world);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> p1.adjacent(p2);
        PathingStrategy aStar = new AStarPathingStrategy();
        List<Point> path =  aStar.computePath(getPosition(), destPos, canPassThrough, withinReach,PathingStrategy.CARDINAL_NEIGHBORS);

        return path.get(0);


    }

    private boolean moveToOreBlob(WorldModel world, Entity target, EventScheduler scheduler) {
        if (getPosition().adjacent(target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = this.nextPositionOreBlob(world, target.getPosition());

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

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
