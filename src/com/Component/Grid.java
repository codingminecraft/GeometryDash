package com.Component;

import com.jade.Camera;
import com.jade.Component;
import com.jade.Window;
import com.util.Constants;

import java.awt.*;
import java.awt.geom.Line2D;

public class Grid extends Component {

    Camera camera;
    public int gridWidth, gridHeight;
    private int numYLines = 31;
    private int numXLines = 20;

    public Grid() {
        this.camera = Window.getWindow().getCurrentScene().camera;
        this.gridHeight = Constants.TILE_HEIGHT;
        this.gridWidth = Constants.TILE_WIDTH;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(0.2f, 0.2f, 0.2f, 0.5f));

        float bottom = Math.min(Constants.GROUND_Y - camera.position.y, Constants.SCREEN_HEIGHT);
        float startX = (float)Math.floor(camera.position.x / gridWidth) * gridWidth - camera.position.x;
        float startY = (float)Math.floor(camera.position.y / gridHeight) * gridHeight - camera.position.y;

        for (int column = 0; column <= numYLines; column++) {
            g2.draw(new Line2D.Float(startX, 0, startX, bottom));
            startX += gridWidth;
        }

        for (int row = 0; row <= numXLines; row++) {
            if (camera.position.y + startY < Constants.GROUND_Y) {
                g2.draw(new Line2D.Float(0, startY, Constants.SCREEN_WIDTH, startY));
                startY += gridHeight;
            }
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
