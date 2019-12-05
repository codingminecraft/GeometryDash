package com.jade;

import java.awt.*;

public abstract class Scene {
    String name;

    public void Scene(String name) {
        this.name = name;
        init();
    }

    public void init() {

    }

    public abstract void update(double dt);
    public abstract void draw(Graphics2D g2);
}
