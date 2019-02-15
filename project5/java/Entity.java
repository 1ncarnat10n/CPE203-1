import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private final String id;
    private Point position;
    protected List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        imageIndex = 0;
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
        return images.get(imageIndex);
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected void setImageIndex(int val) {
        imageIndex = val;
    }

    public abstract <R> R accept(EntityVisitor<R> visitor);
}
