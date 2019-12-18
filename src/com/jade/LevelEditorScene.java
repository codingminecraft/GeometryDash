package com.jade;

import com.Component.*;
import com.dataStructure.Transform;
import com.util.Constants;
import com.util.Vector2;

import java.awt.*;

public class LevelEditorScene extends Scene {
    public GameObject player;
    GameObject ground;
    Grid grid;
    CameraControls cameraControls;
    GameObject mouseCursor;

    public LevelEditorScene(String name) {
        super.Scene(name);
    }

    @Override
    public void init() {
        grid = new Grid();
        cameraControls = new CameraControls();

        Spritesheet objects = new Spritesheet("assets/spritesheet.png", 42, 42, 2, 6, 12);
        Sprite mouseSprite = objects.sprites.get(0);
        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Vector2()));
        mouseCursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
        mouseCursor.addComponent(mouseSprite);

        player = new GameObject("Some game object", new Transform(new Vector2(500, 350.0f)));
        Spritesheet layerOne = new Spritesheet("assets/player/layerOne.png",
                42, 42, 2, 13, 13 * 5);
        Spritesheet layerTwo = new Spritesheet("assets/player/layerTwo.png",
                42, 42, 2, 13, 13 * 5);
        Spritesheet layerThree = new Spritesheet("assets/player/layerThree.png",
                42, 42, 2, 13, 13 * 5);
        Player playerComp = new Player(
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN);
        player.addComponent(playerComp);

        ground = new GameObject("Ground", new Transform(
                new Vector2(0, Constants.GROUND_Y)));
        ground.addComponent(new Ground());

        addGameObject(player);
        addGameObject(ground);
    }

    @Override
    public void update(double dt) {

        if (camera.position.y > Constants.CAMERA_OFFSET_GROUND_Y) {
            camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y;
        }

        for (GameObject g : gameObjects) {
            g.update(dt);
        }

        cameraControls.update(dt);
        grid.update(dt);
        mouseCursor.update(dt);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(1.0f, 1.0f, 1.0f));
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(g2);
        grid.draw(g2);
        mouseCursor.draw(g2);
    }
}
