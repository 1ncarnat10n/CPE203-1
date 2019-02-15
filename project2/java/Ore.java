import java.util.List;

import processing.core.PImage;

final class Ore implements Entity, ActivitiedEntity {

    private static final int BLOB_ANIMATION_MAX = 150;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final String BLOB_KEY = "blob";

    private final String id;
    private Point position;
    private final int actionPeriod;
    private final List<PImage> images;

    public Ore(String id, Point position, int actionPeriod, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.actionPeriod = actionPeriod;
        this.images = images;
    }


    public PImage getCurrentImage() {
        return images.get(0);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public void scheduleActivity(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);

    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        this.executeOreActivity(activity.world, activity.imageStore, scheduler);
    }

    private Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    private void executeOreActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        OreBlob blob = new OreBlob(id + BLOB_ID_SUFFIX,
                pos, actionPeriod / BLOB_PERIOD_SCALE, BLOB_ANIMATION_MIN + Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),  imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActivity(scheduler, world, imageStore);
    }

}
