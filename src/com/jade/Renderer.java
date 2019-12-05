package com.jade;

import com.dataStructure.Transform;
import com.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    List<GameObject> gameObjects;
    Camera camera;

    public Renderer(Camera camera) {
        this.camera = camera;
        this.gameObjects = new ArrayList<>();
    }

    public void submit(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void render(Graphics2D g2) {
        for (GameObject g : gameObjects) {
            Transform oldTransform = new Transform(g.transform.position);
            oldTransform.rotation = g.transform.rotation;
            oldTransform.scale = new Vector2(g.transform.scale.x, g.transform.scale.y);

            g.transform.position = new Vector2(g.transform.position.x - camera.position.x,
                    g.transform.position.y - camera.position.y);
            g.draw(g2);
            g.transform = oldTransform;
        }
    }
}
