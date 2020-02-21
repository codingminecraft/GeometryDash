package com.jade;

import com.util.Vector2;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    String name;
    public Camera camera;
    List<GameObject> gameObjects;
    Renderer renderer;

    public void Scene(String name) {
        this.name = name;
        this.camera = new Camera(new Vector2());
        this.gameObjects = new ArrayList<>();
        this.renderer = new Renderer(this.camera);
    }

    public void init() {

    }

    public List<GameObject> getAllGameObjects() {
        return gameObjects;
    }

    public void addGameObject(GameObject g) {
        gameObjects.add(g);
        renderer.submit(g);
        for (Component c : g.getAllComponents()) {
            c.start();
        }
    }

    public abstract void update(double dt);
    public abstract void draw(Graphics2D g2);
}
