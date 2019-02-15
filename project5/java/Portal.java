import java.util.List;
import java.util.Random;

import processing.core.PImage;

final class Portal extends AnimatedEntity {

    private static int MAX_CREEP_TILES = 15;
    private boolean portalFormed = false;
    private int portalFormingStep = 0;

    public Portal(Point position, List<PImage> images) {
        super("portal", 0, position, 5000, 50, images, -1);
    }

    public void executeActivity(EventScheduler scheduler, Activity activity) {
        WorldModel world = activity.world;
        ImageStore imageStore = activity.imageStore;

        if (!portalFormed) {
            Background background = new Background("portal_steps", imageStore.getImageList("portal_steps"));
            switch (portalFormingStep) {
                case 0:  world.setBackground(new Point(getPosition().x-1, getPosition().y-1), background); break;
                case 1: world.setBackground(new Point(getPosition().x, getPosition().y-1), background); break;
                case 2: world.setBackground(new Point(getPosition().x+1, getPosition().y-1), background); break;
                case 3: world.setBackground(new Point(getPosition().x+1, getPosition().y), background); break;
                case 4: world.setBackground(new Point(getPosition().x+1, getPosition().y+1), background); break;
                case 5: world.setBackground(new Point(getPosition().x, getPosition().y+1), background); break;
                case 6: world.setBackground(new Point(getPosition().x-1, getPosition().y+1), background); break;
                case 7: world.setBackground(new Point(getPosition().x-1, getPosition().y), background); break;
                case 8: portalFormed = true;
            }
            portalFormingStep++;
        } else {
            Random random = new Random();
            if (random.nextInt(100) <= 33) {
                EnemyHealer enemyHealer = new EnemyHealer(new Point(getPosition().x + random.nextInt(3)-1, getPosition().y + random.nextInt(3)-1),
                        imageStore.getImageList("enemy_healer"));
                try {
                    world.tryAddEntity(enemyHealer);
                } catch (IllegalArgumentException e) {
                    return;
                }
                enemyHealer.scheduleActivity(scheduler, world, imageStore);
                enemyHealer.scheduleAnimation(scheduler);
                scheduler.scheduleEvent(enemyHealer, new Activity(enemyHealer, world, imageStore), enemyHealer.getActionPeriod());
            }
            if (random.nextInt(100) <= 50) {
                Wyvern wyvern = new Wyvern(new Point(getPosition().x + random.nextInt(3)-1, getPosition().y + random.nextInt(3)-1),
                        imageStore.getImageList("wyvern"));
                try {
                    world.tryAddEntity(wyvern);
                } catch (IllegalArgumentException e) {
                    return;
                }
                wyvern.scheduleActivity(scheduler, world, imageStore);
                wyvern.scheduleAnimation(scheduler);
                scheduler.scheduleEvent(wyvern, new Activity(wyvern, world, imageStore), wyvern.getActionPeriod());
            }

        }

        scheduler.scheduleEvent(this, createActivityAction(world, imageStore), getActionPeriod());

    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
