final class Action {
    private final ActionKind kind;
    private final Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(ActionKind kind, Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount) {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        switch (kind) {
            case ACTIVITY:
                entity.executeActivityAction(scheduler, this);
                break;

            case ANIMATION:
                entity.executeAnimationAction(scheduler, this);
                break;
        }
    }

}
