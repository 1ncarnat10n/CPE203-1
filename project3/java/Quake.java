import java.util.List;

import processing.core.PImage;

final class Quake extends AnimatedEntity {

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ACTION_PERIOD = 1100;

    // TODO: Check resource limit?
    public Quake(Point position, List<PImage> images) {
        super(QUAKE_ID, 0, position, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD, images);
    }

    public void executeAnimation(EventScheduler scheduler, Animation animation) {
        nextImage();

        if (animation.repeatCount != 1) {
            scheduler.scheduleEvent(this, this.createAnimationAction(Math.max(animation.repeatCount - 1, 0)), getAnimationPeriod());
        }
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;

        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
