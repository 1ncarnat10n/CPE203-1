public interface AnimatedEntity {

    void executeAnimation(EventScheduler scheduler, Animation animation);
    int getAnimationPeriod();
    void nextImage();
}
