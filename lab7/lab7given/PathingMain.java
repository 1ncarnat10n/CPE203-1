import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.*;

public class PathingMain extends PApplet {
    private List<PImage> imgs;
    private int current_image;
    private long next_time;
    private PImage background;
    private PImage obstacle;
    private PImage goal;
    private List<Point> path;

    private static final int TILE_SIZE = 32;

    private static final int ANIMATION_TIME = 100;

    private GridValues[][] grid;
    private static final int ROWS = 15;
    private static final int COLS = 20;

    private static enum GridValues {BACKGROUND, OBSTACLE, GOAL, SEARCHED}

    private Point wPos;
    private Point pathCurrentPos;

    private boolean drawPath = false;



    public void settings() {
        size(640, 480);
    }

    /* runs once to set up world */
    public void setup() {

        path = new LinkedList<>();
        wPos = new Point(2, 2);
        pathCurrentPos = wPos;
        imgs = new ArrayList<>();
        imgs.add(loadImage("images/wyvern1.bmp"));
        imgs.add(loadImage("images/wyvern2.bmp"));
        imgs.add(loadImage("images/wyvern3.bmp"));

        background = loadImage("images/grass.bmp");
        obstacle = loadImage("images/vein.bmp");
        goal = loadImage("images/water.bmp");

        grid = new GridValues[ROWS][COLS];
        initialize_grid();

        current_image = 0;
        next_time = System.currentTimeMillis() + ANIMATION_TIME;
        noLoop();
        draw();
    }

    /* set up a 2D grid to represent the world */
    private void initialize_grid() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = GridValues.BACKGROUND;
            }
        }

        //set up some obstacles
        for (int row = 2; row < 8; row++) {
            grid[row][row + 5] = GridValues.OBSTACLE;
        }

        for (int row = 8; row < 12; row++) {
            grid[row][19 - row] = GridValues.OBSTACLE;
        }

        for (int col = 1; col < 8; col++) {
            grid[11][col] = GridValues.OBSTACLE;
        }

        grid[13][14] = GridValues.GOAL;
    }

    private void next_image() {
        current_image = (current_image + 1) % imgs.size();
    }

    /* runs over and over */
    public void draw() {
        // A simplified action scheduling handler
        long time = System.currentTimeMillis();
        if (time >= next_time) {
            next_image();
            next_time = time + ANIMATION_TIME;
        }

        draw_grid();
        draw_path();

        image(imgs.get(current_image), wPos.x * TILE_SIZE, wPos.y * TILE_SIZE);
    }

    private void draw_grid() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                draw_tile(row, col);
            }
        }
    }

    private void draw_path() {
        if (drawPath) {
            for (Point p : path) {
                fill(128, 0, 0);
                rect(p.x * TILE_SIZE + TILE_SIZE * 3 / 8,
                        p.y * TILE_SIZE + TILE_SIZE * 3 / 8,
                        TILE_SIZE / 4, TILE_SIZE / 4);
            }
        }
    }

    private void draw_tile(int row, int col) {
        switch (grid[row][col]) {
            case BACKGROUND:
                image(background, col * TILE_SIZE, row * TILE_SIZE);
                break;
            case OBSTACLE:
                image(obstacle, col * TILE_SIZE, row * TILE_SIZE);
                break;
            case SEARCHED:
                fill(0, 128);
                rect(col * TILE_SIZE + TILE_SIZE / 4,
                        row * TILE_SIZE + TILE_SIZE / 4,
                        TILE_SIZE / 2, TILE_SIZE / 2);
                break;
            case GOAL:
                image(goal, col * TILE_SIZE, row * TILE_SIZE);
                break;
        }
    }

    public static void main(String args[]) {
        PApplet.main("PathingMain");
    }

    public void keyPressed() {
        if (key == ' ') {
            path.clear();


//            // While loop DFS
//            while (!isGoal(pathCurrentPos)) {
//                grid[pathCurrentPos.y][pathCurrentPos.x] = GridValues.SEARCHED;
//                dfsPath(pathCurrentPos);
//            }
//
//            // Recursive DFS
//            dfsPathRec(pathCurrentPos);

            AStarPathingStrategy aStar = new AStarPathingStrategy();
            path = aStar.computePath(pathCurrentPos, new Point(14, 13), p -> withinBounds(p) && grid[p.y][p.x] != GridValues.OBSTACLE, (p1, p2) -> neighbors(p1, p2), PathingStrategy.CARDINAL_NEIGHBORS);

            System.out.println("Done!");

        } else if (key == 'p') {
            drawPath ^= true;
            redraw();
        }
    }

    // DFS implementation with while loop
    private boolean dfsPath(Point pos) {

        // Populate Fringe for current position
        // Fringe Choices are in the following order: Right, Up, Left, Down
        Point[] fringe = new Point[4];
        fringe[0] = new Point(pos.x + 1, pos.y);
        fringe[2] = new Point(pos.x, pos.y + 1);
        fringe[1] = new Point(pos.x - 1, pos.y);
        fringe[3] = new Point(pos.x, pos.y - 1);

        for (Point choice: fringe) {
            if (validGridCell(choice)) {
                path.add(0, pos);
                pathCurrentPos = choice;
                return true;
            }
        }
        pathCurrentPos = path.get(0);   // If all choices on fringe fail, go back to previous point on path
        path.remove(0);
        return false;
    }

    // DFS implementation with recursion
    private boolean dfsPathRec(Point pos) {
        path.add(0, pos);
        if (isGoal(pos)) {
            return true;
        } else {
            grid[pos.y][pos.x] = GridValues.SEARCHED;

            Point[] fringe = new Point[4];
            fringe[0] = new Point(pos.x + 1, pos.y);
            fringe[2] = new Point(pos.x, pos.y + 1);
            fringe[1] = new Point(pos.x - 1, pos.y);
            fringe[3] = new Point(pos.x, pos.y - 1);

            for (Point choice: fringe) {
                if (validGridCell(choice)) {
                    if (dfsPathRec(choice)) {
                        return true;
                    }
                }
            }
            path.remove(0);
            return false;
        }
    }

    private boolean withinBounds(Point p) {
        return p.y >= 0 && p.y < grid.length &&
                p.x >= 0 && p.x < grid[0].length;
    }

    private boolean checkCellNotOccupied(Point p) {
        return  grid[p.y][p.x] != GridValues.OBSTACLE;
    }

    private boolean checkCellNotSearched(Point p) {
        return grid[p.y][p.x] != GridValues.SEARCHED;
    }

    private boolean isGoal(Point p) {
        return grid[p.y][p.x] == GridValues.GOAL;
    }

    private boolean validGridCell(Point pos) {
        if (withinBounds(pos) && checkCellNotOccupied(pos) && checkCellNotSearched(pos)) { return true; }
        else { return false; }
    }


    private static int getX(Point p) {return p.x;}

    private static int getY(Point p) {return p.y;}

    private static boolean neighbors(Point p1, Point p2)
    {
        return getX(p1)+1 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1)-1 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1)+1 == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1)-1 == getY(p2);
    }
}
