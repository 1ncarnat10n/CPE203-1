import java.util.List;

import processing.core.PImage;

final class Quake implements Entity, ActivitiedEntity, AnimatedEntity {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ACTION_PERIOD = 1100;

    private final String id = QUAKE_ID;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;

    public Quake(Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = QUAKE_ACTION_PERIOD;
        this.animationPeriod = QUAKE_ANIMATION_PERIOD;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public void scheduleActivity(EventScheduler scheduler,
                                 WorldModel world, ImageStore imageStore) {
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                        getAnimationPeriod());

    }

    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void executeAnimation(EventScheduler scheduler, Animation animation) {
        nextImage();

        if (animation.repeatCount != 1) {
            scheduler.scheduleEvent(this, this.createAnimationAction(Math.max(animation.repeatCount - 1, 0)), getAnimationPeriod());
        }
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
                executeQuakeActivity(activity.world, activity.imageStore, scheduler);

    }

    private Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    private Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    private void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
