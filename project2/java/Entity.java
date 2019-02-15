import processing.core.PImage;

interface Entity {

    Point getPosition();
    void setPosition(Point newPosition);
    PImage getCurrentImage();
}
