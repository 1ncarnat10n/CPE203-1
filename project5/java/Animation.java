class Animation implements Action {

    public AnimatedEntity entity;
    public int repeatCount;

    public Animation(AnimatedEntity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void execute(EventScheduler scheduler) {
        entity.executeAnimation(scheduler, this);
    }
}
