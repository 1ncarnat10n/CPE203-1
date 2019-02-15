import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActivitiedEntity {

    private final int resourceLimit;
    private final int animationPeriod;
    private final int animationRepeatCount;

    public AnimatedEntity(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images, int animationRepeatCount) {
        super(id, position, actionPeriod, images);
        this.resourceLimit = resourceLimit;
        this.animationPeriod = animationPeriod;
        this.animationRepeatCount = animationRepeatCount;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public int getResourceLimit() {return resourceLimit;}

    public void executeAnimation(EventScheduler scheduler, Animation animation) {
        nextImage();

        if (animation.repeatCount > 0) {
            scheduler.scheduleEvent(this, createAnimationAction(animation.repeatCount - 1), getAnimationPeriod());
        }
        if (animation.repeatCount == -1) {
            scheduler.scheduleEvent(this, createAnimationAction(-1), getAnimationPeriod());
        }
    }

    public void scheduleAnimation(EventScheduler scheduler) {
        scheduler.scheduleEvent(this, createAnimationAction(animationRepeatCount), animationPeriod);
    }

    public void nextImage() {
        setImageIndex((getImageIndex() + 1) % getImages().size());
    }

    protected Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
}
