package com.jade;

import com.Component.*;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.file.Parser;
import com.util.Constants;
import com.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;

public class LevelScene extends Scene {
    static LevelScene currentScene;

    public GameObject player;
    public BoxBounds playerBounds;

    public LevelScene(String name) {
        super.Scene(name);
    }

    @Override
    public void init() {
        initAssetPool();

        player = new GameObject("Some game object", new Transform(new Vector2(500, 350.0f)));
        Spritesheet layerOne = AssetPool.getSpritesheet("assets/player/layerOne.png");
        Spritesheet layerTwo = AssetPool.getSpritesheet("assets/player/layerTwo.png");
        Spritesheet layerThree = AssetPool.getSpritesheet("assets/player/layerThree.png");
        Player playerComp = new Player(
                layerOne.sprites.get(0),
                layerTwo.sprites.get(0),
                layerThree.sprites.get(0),
                Color.RED,
                Color.GREEN);
        player.addComponent(playerComp);
        player.addComponent(new Rigidbody(new Vector2(395, 0)));
        player.addComponent(new BoxBounds(Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT));
        playerBounds = new BoxBounds(Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        player.addComponent(playerBounds);

        GameObject ground;
        ground = new GameObject("Ground", new Transform(
                new Vector2(0, Constants.GROUND_Y)));
        ground.addComponent(new Ground());

        renderer.submit(player);
        addGameObject(ground);

        importLevel("Test");
    }

    public void initAssetPool() {
        AssetPool.addSpritesheet("assets/player/layerOne.png",
                42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerTwo.png",
                42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/player/layerThree.png",
                42, 42, 2, 13, 13 * 5);
        AssetPool.addSpritesheet("assets/groundSprites.png",
                42, 42, 2, 6, 12);
    }

    @Override
    public void update(double dt) {

        if (player.transform.position.x - camera.position.x > Constants.CAMERA_OFFSET_X) {
            camera.position.x = player.transform.position.x - Constants.CAMERA_OFFSET_X;
        }

        if (player.transform.position.y - camera.position.y > Constants.CAMERA_OFFSET_Y) {
            camera.position.y = player.transform.position.y - Constants.CAMERA_OFFSET_Y;
        }

        if (camera.position.y > Constants.CAMERA_OFFSET_GROUND_Y) {
            camera.position.y = Constants.CAMERA_OFFSET_GROUND_Y;
        }


        player.update(dt);
        player.getComponent(Player.class).onGround = false;
        for (GameObject g : gameObjects) {
            g.update(dt);

            Bounds b = g.getComponent(Bounds.class);
            if (b != null) {
                if (Bounds.checkCollision(playerBounds, b)) {
                    Bounds.resolveCollision(b, player);
                }
            }
        }
    }

    private void importLevel(String filename) {
        Parser.openFile(filename);

        GameObject go = Parser.parseGameObject();
        while (go != null) {
            addGameObject(go);
            go = Parser.parseGameObject();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(1.0f, 1.0f, 1.0f));
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(g2);
    }
}
