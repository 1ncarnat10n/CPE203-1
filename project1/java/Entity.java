import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Entity {

    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final String QUAKE_ID = "quake";
    private static final String QUAKE_KEY = "quake";
    private static final int ORE_CORRUPT_MAX = 30000;
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final String BLOB_KEY = "blob";

    public EntityKind kind;
    private final String id;
    public Point position;
    private List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public Entity(EntityKind kind, String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod) {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public static Entity createBlacksmith(String id, Point position,
                                          List<PImage> images) {
        return new Entity(EntityKind.BLACKSMITH, id, position, images,
                0, 0, 0, 0);
    }

    public static Entity createMinerFull(String id, int resourceLimit,
                                         Point position, int actionPeriod, int animationPeriod,
                                         List<PImage> images) {
        return new Entity(EntityKind.MINER_FULL, id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Entity createMinerNotFull(String id, int resourceLimit,
                                            Point position, int actionPeriod, int animationPeriod,
                                            List<PImage> images) {
        return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Entity createObstacle(String id, Point position,
                                        List<PImage> images) {
        return new Entity(EntityKind.OBSTACLE, id, position, images,
                0, 0, 0, 0);
    }

    public static Entity createOre(String id, Point position, int actionPeriod,
                                   List<PImage> images) {
        return new Entity(EntityKind.ORE, id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public static Entity createOreBlob(String id, Point position,
                                       int actionPeriod, int animationPeriod, List<PImage> images) {
        return new Entity(EntityKind.ORE_BLOB, id, position, images,
                0, 0, actionPeriod, animationPeriod);
    }

    public static Entity createQuake(Point position, List<PImage> images) {
        return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images,
                0, 0, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Entity createVein(String id, Point position, int actionPeriod,
                                    List<PImage> images) {
        return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
                actionPeriod, 0);
    }


    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore) {
        switch (kind) {
            case MINER_FULL:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this, this.createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0), getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(0), getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        this.createAnimationAction(QUAKE_ANIMATION_REPEAT_COUNT),
                        getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        actionPeriod);
                break;

            default:
        }
    }

    public PImage getCurrentImage() {
        return images.get(imageIndex);
    }

    public int getAnimationPeriod() {
        switch (kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return animationPeriod;
            default:
                throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", kind));
        }
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void executeAnimationAction(EventScheduler scheduler, Action action) {
        nextImage();

        if (action.repeatCount != 1) {
            scheduler.scheduleEvent(this, this.createAnimationAction(Math.max(action.repeatCount - 1, 0)), getAnimationPeriod());
        }
    }

    public void executeActivityAction(EventScheduler scheduler, Action action) {
        switch (kind) {
            case MINER_FULL:
                this.executeMinerFullActivity(action.world, action.imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                this.executeMinerNotFullActivity(action.world, action.imageStore, scheduler);
                break;

            case ORE:
                this.executeOreActivity(action.world, action.imageStore, scheduler);
                break;

            case ORE_BLOB:
                this.executeOreBlobActivity(action.world, action.imageStore, scheduler);
                break;

            case QUAKE:
                this.executeQuakeActivity(action.world, action.imageStore, scheduler);
                break;

            case VEIN:
                this.executeVeinActivity(action.world, action.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", kind));
        }
    }


    private Action createActivityAction(WorldModel world,
                                        ImageStore imageStore) {
        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
    }

    private Action createAnimationAction(int repeatCount) {
        return new Action(ActionKind.ANIMATION, this, null, null, repeatCount);
    }

    private Point nextPositionOreBlob(WorldModel world,
                                      Point destPos) {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE))) {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE))) {
                newPos = position;
            }
        }

        return newPos;
    }

    private Point nextPositionMiner(WorldModel world,
                                    Point destPos) {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz,
                position.y);

        if (horiz == 0 || newPos.isOccupied(world)) {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x,
                    position.y + vert);

            if (vert == 0 || newPos.isOccupied(world)) {
                newPos = position;
            }
        }

        return newPos;
    }

    private boolean moveToOreBlob(WorldModel world,
                                  Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.position)) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        } else {
            Point nextPos = this.nextPositionOreBlob(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToFull(WorldModel world,
                               Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToNotFull(WorldModel world,
                                  Entity target, EventScheduler scheduler) {
        if (position.adjacent(target.position)) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore) {
        Entity miner = createMinerNotFull(id, resourceLimit,
                position, actionPeriod, animationPeriod,
                images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore) {
        if (resourceCount >= resourceLimit) {
            Entity miner = createMinerFull(id, resourceLimit,
                    position, actionPeriod, animationPeriod,
                    images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private void executeVeinActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = position.findOpenAround(world);

        if (openPt.isPresent()) {
            Entity ore = createOre(ORE_ID_PREFIX + id,
                    openPt.get(), ORE_CORRUPT_MIN +
                            Functions.rand.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(WorldModel.ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                actionPeriod);
    }

    private void executeQuakeActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    private void executeOreBlobActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> blobTarget = position.findNearest(world,
                EntityKind.VEIN);
        long nextPeriod = actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().position;

            if (this.moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Entity quake = createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                nextPeriod);
    }

    private void executeOreActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler) {
        Point pos = position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Entity blob = createOreBlob(id + BLOB_ID_SUFFIX,
                pos, actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    private void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> notFullTarget = position.findNearest(world,
                EntityKind.ORE);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    private void executeMinerFullActivity(WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = position.findNearest(world,
                EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

}
