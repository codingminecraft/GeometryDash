package com.Component;

import com.jade.Component;
import com.jade.GameObject;
import com.jade.LevelEditorScene;
import com.jade.Window;
import com.util.Constants;
import com.util.Vector2;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class LevelEditorControls extends Component {
    private float debounceTime = 0.2f;
    private float debounceLeft = 0.0f;

    private List<GameObject> selectedObjects;

    int gridWidth, gridHeight;
    private float worldX, worldY;
    private boolean isEditing = false;

    public LevelEditorControls(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.selectedObjects = new ArrayList<>();
    }

    public void updateSpritePosition() {
        this.worldX = (float)Math.floor((Window.getWindow().mouseListener.x + Window.getWindow().getCurrentScene().camera.position.x + Window.getWindow().mouseListener.dx) / gridWidth);
        this.worldY= (float)Math.floor((Window.getWindow().mouseListener.y + Window.getWindow().getCurrentScene().camera.position.y + Window.getWindow().mouseListener.dy) / gridWidth);
        this.gameObject.transform.position.x = worldX * gridWidth - Window.getWindow().getCurrentScene().camera.position.x;
        this.gameObject.transform.position.y = worldY * gridWidth - Window.getWindow().getCurrentScene().camera.position.y;
    }

    public void copyGameObjectToScene() {
        GameObject object = gameObject.copy();
        object.transform.position = new Vector2(this.worldX * gridWidth, this.worldY * gridHeight);
        Window.getWindow().getCurrentScene().addGameObject(object);
    }

    public void addGameObjectToSelected(Vector2 mousePos) {
        mousePos.x += Window.getScene().camera.position.x;
        mousePos.y += Window.getScene().camera.position.y;
        for (GameObject go : Window.getScene().getAllGameObjects()) {
            Bounds bounds = go.getComponent(Bounds.class);
            if (bounds != null && bounds.raycast(mousePos)) {
                selectedObjects.add(go);
                bounds.isSelected = true;
                break;
            }
        }
    }

    public void clearSelectedObjectsAndAdd(Vector2 mousePos) {
        clearSelected();
        addGameObjectToSelected(mousePos);
    }

    public void escapeKeyPressed() {
        GameObject newGameObj = new GameObject("Mouse Cursor",
                this.gameObject.transform.copy(), this.gameObject.zIndex);
        newGameObj.addComponent(this);
        LevelEditorScene scene = (LevelEditorScene)Window.getScene();
        scene.mouseCursor = newGameObj;
        isEditing = false;
    }

    public void clearSelected() {
        for (GameObject go : selectedObjects) {
            go.getComponent(Bounds.class).isSelected = false;
        }
        selectedObjects.clear();
    }

    @Override
    public void update(double dt) {
        debounceLeft -= dt;

        if (!isEditing && this.gameObject.getComponent(Sprite.class) != null) {
            this.isEditing = true;
        }

        if (isEditing) {
            if (selectedObjects.size() != 0) {
                clearSelected();
            }
            updateSpritePosition();
        }


        if (Window.getWindow().mouseListener.y < Constants.TAB_OFFSET_Y &&
            Window.getWindow().mouseListener.mousePressed &&
            Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1 &&
            debounceLeft < 0) {
            // Mouse has been clicked
            debounceLeft = debounceTime;

            if (isEditing) {
                copyGameObjectToScene();
            } else if (Window.keyListener().isKeyPressed(KeyEvent.VK_SHIFT)){
                addGameObjectToSelected(new Vector2(Window.mouseListener().x, Window.mouseListener().y));
            } else {
                clearSelectedObjectsAndAdd(new Vector2(Window.mouseListener().x, Window.mouseListener().y));
            }
        }

        if (Window.keyListener().isKeyPressed(KeyEvent.VK_ESCAPE)) {
            escapeKeyPressed();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        Sprite sprite = gameObject.getComponent(Sprite.class);
        if (sprite != null) {
            float alpha = 0.5f;
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
            g2.drawImage(sprite.image, (int)gameObject.transform.position.x, (int)gameObject.transform.position.y,
                    (int)sprite.width, (int)sprite.height, null);
            alpha = 1.0f;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(ac);
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
