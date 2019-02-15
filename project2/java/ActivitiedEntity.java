public interface ActivitiedEntity {

    void scheduleActivity(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    void executeActivity(EventScheduler scheduler, Activity activity);
}
