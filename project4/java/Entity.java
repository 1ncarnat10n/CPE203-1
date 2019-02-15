import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private final String id;
    private Point position;
    private List<PImage> images;

    public Entity(String id, Point position, List<PImage> images) {

        this.id = id;
        this.position = position;
        this.images = images;
    }

    public String getId() {return id;}

    public List<PImage> getImages() {
        return images;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        position = newPosition;
    }

    public PImage getCurrentImage() {
        return images.get(0);
    }

    public abstract <R> R accept(EntityVisitor<R> visitor);
}
