import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Blacksmith extends ActivitiedEntity {

    private static final int visibilityRange = 25;

    public Blacksmith(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, actionPeriod, images);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        Point pos = getPosition();  // store current position before removing

        Optional<Entity> wyvernTarget = getPosition().findNearest(world, "Wyvern");

        if (wyvernTarget.isPresent() && wyvernTarget.get().getPosition().distance(pos) < visibilityRange) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            Ninja ninja = new Ninja(pos, imageStore.getImageList("ninja"));

            world.addEntity(ninja);
            ninja.scheduleActivity(scheduler, world, imageStore);
            ninja.scheduleAnimation(scheduler);
        } else {
            scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());
        }
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
