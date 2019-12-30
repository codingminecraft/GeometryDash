package com.jade;

import com.dataStructure.Transform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private List<Component> components;
    private String name;
    public Transform transform;

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(c);
                return;
            }
        }
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public GameObject copy() {
        GameObject newGameObject = new GameObject("Generated", transform.copy());
        for (Component c : components) {
            Component copy = c.copy();
            if (copy != null) {
                newGameObject.addComponent(copy);
            }
        }
        return newGameObject;
    }


    public void update(double dt) {
        for (Component c : components) {
            c.update(dt);
        }
    }

    public void draw(Graphics2D g2) {
        for (Component c : components) {
            c.draw(g2);
        }
    }
}
