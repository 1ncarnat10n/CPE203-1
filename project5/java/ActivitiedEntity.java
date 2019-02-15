import processing.core.PImage;

import java.util.List;

public abstract class ActivitiedEntity extends Entity {

    private final int actionPeriod;

    public ActivitiedEntity(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {return actionPeriod;}

    public void scheduleActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), actionPeriod);
    }

    public abstract void executeActivity(EventScheduler scheduler, Activity activity);

    protected Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }
}
