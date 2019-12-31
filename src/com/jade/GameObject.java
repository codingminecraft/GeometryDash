package com.jade;

import com.dataStructure.Transform;
import com.file.Serialize;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameObject extends Serialize {
    private List<Component> components;
    private String name;
    public Transform transform;
    private boolean serialize = true;

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

    public void setNonSerializable() {
        this.serialize = false;
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

    @Override
    public String serialize(int tabSize) {
        if (!serialize) return "";

        StringBuilder builder = new StringBuilder();
        // GameObject
        builder.append(beginObjectProperty("GameObject", tabSize));

        // Transform
        builder.append(transform.serialize(tabSize + 1)).append(addEnding(true, true));

        // Name
        if (components.size() > 0) {
            builder.append(addStringProperty("Name", name, tabSize + 1, true, true));
            builder.append(beginObjectProperty("Components", tabSize + 1));
        } else {
            builder.append(addStringProperty("Name", name, tabSize + 1, true, false));
        }

        // Components
        int i = 0;
        for (Component c : components) {
            String str = c.serialize(tabSize + 2);
            if (str.compareTo("") != 0) {
                builder.append(str);
                if (i != components.size() - 1) {
                    builder.append(addEnding(true, true));
                } else {
                    builder.append(addEnding(true, false));
                }
            }

            i++;
        }

        if (components.size() > 0) {
            builder.append(closeObjectProperty(tabSize + 1));
        }

        builder.append(addEnding(true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }
}
