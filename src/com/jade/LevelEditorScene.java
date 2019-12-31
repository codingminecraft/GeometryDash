package com.jade;

import com.Component.*;
import com.dataStructure.Transform;
import com.ui.MainContainer;
import com.util.Constants;
import com.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LevelEditorScene extends Scene {
    public GameObject player;
    private GameObject ground;
    private Grid grid;
    private CameraControls cameraControls;
    public GameObject mouseCursor;
    private MainContainer editingButtons = new MainContainer();

    public LevelEditorScene(String name) {
        super.Scene(name);
    }

    @Override
    public void init() {
        grid = new Grid();
        cameraControls = new CameraControls();
        editingButtons.start();

        mouseCursor = new GameObject("Mouse Cursor", new Transform(new Vector2()));
        mouseCursor.addComponent(new SnapToGrid(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));

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

        player.setNonSerializable();
        ground.setNonSerializable();
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
        editingButtons.update(dt);
        mouseCursor.update(dt);

        if (Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_F2)) {
            export("Test");
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(1.0f, 1.0f, 1.0f));
        g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        renderer.render(g2);
        grid.draw(g2);
        editingButtons.draw(g2);

        // Should be drawn last
        mouseCursor.draw(g2);
    }

    private void export(String filename) {
        try {
            FileOutputStream fos = new  FileOutputStream("levels/" + filename + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry(filename + ".json"));

            int i = 0;
            for (GameObject go : gameObjects) {
                String str = go.serialize(0);
                if (str.compareTo("") != 0) {
                    zos.write(str.getBytes());
                    if (i != gameObjects.size() - 1) {
                        zos.write(",\n".getBytes());
                    }
                }
                i++;
            }

            zos.closeEntry();
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
