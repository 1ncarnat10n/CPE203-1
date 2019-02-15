import java.util.List;

import processing.core.PImage;

final class Blacksmith implements Entity {

    private final String id;
    private Point position;
    private List<PImage> images;

    public Blacksmith(String id, Point position,
                      List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
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
}
