package com.Component;

import com.jade.*;
import com.util.Constants;

import java.awt.Graphics2D;
import java.awt.Color;


public class Ground extends Component {

    @Override
    public void update(double dt) {
        if (!Window.getWindow().isInEditor) {
            LevelScene scene = (LevelScene) Window.getWindow().getCurrentScene();
            GameObject player = scene.player;

            if (player.transform.position.y + player.getComponent(BoxBounds.class).height >
                    gameObject.transform.position.y) {
                player.transform.position.y = gameObject.transform.position.y -
                        player.getComponent(BoxBounds.class).height;
            }
            gameObject.transform.position.x = scene.camera.position.x - 10;
        } else {
            gameObject.transform.position.x = Window.getWindow().getCurrentScene().camera.position.x - 10;
        }

    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawRect((int)gameObject.transform.position.x - 10, (int)gameObject.transform.position.y,
                Constants.SCREEN_WIDTH + 20, Constants.SCREEN_HEIGHT);
    }

    @Override
    public Component copy() {
        return null;
    }
}
