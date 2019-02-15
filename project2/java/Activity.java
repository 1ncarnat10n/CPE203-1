class Activity implements Action {

    public ActivitiedEntity entity;
    public WorldModel world;
    public ImageStore imageStore;

    public Activity(ActivitiedEntity entity, WorldModel world, ImageStore imageStore) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void execute(EventScheduler scheduler) {
        entity.executeActivity(scheduler, this);
    }
}
