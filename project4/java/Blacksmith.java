import java.util.List;

import processing.core.PImage;

final class Blacksmith extends Entity {

    public Blacksmith(String id, Point position,
                      List<PImage> images) {
        super(id, position, images);
    }

    public <R> R accept(EntityVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
