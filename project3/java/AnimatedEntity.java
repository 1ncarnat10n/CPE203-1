import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActivitiedEntity {

    private final int resourceLimit;
    private final int animationPeriod;

    public AnimatedEntity(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod, List<PImage> images) {
        super(id, position, actionPeriod, images);
        this.resourceLimit = resourceLimit;
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return animationPeriod;
    }

    public int getResourceLimit() {return resourceLimit;}

    public abstract void executeAnimation(EventScheduler scheduler, Animation animation);

    public void nextImage() {
        setImageIndex((getImageIndex() + 1) * getImages().size());
    }

    protected Animation createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }
}
