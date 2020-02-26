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

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

public class LevelEditorControls extends Component {
    private float debounceTime = 0.2f;
    private float debounceLeft = 0.0f;

    private float debounceKey = 0.2f;
    private float debounceKeyLeft = 0.0f;

    private boolean shiftKeyPressed = false;

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
        debounceKeyLeft -= dt;

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

        if (Window.keyListener().isKeyPressed(KeyEvent.VK_SHIFT)) {
            shiftKeyPressed = true;
        } else {
            shiftKeyPressed = false;
        }

        if (debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_LEFT)) {
            moveObjects(Direction.LEFT, shiftKeyPressed ? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if (debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_RIGHT)) {
            moveObjects(Direction.RIGHT, shiftKeyPressed ? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if (debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_UP)) {
            moveObjects(Direction.UP, shiftKeyPressed ? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        } else if (debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_DOWN)) {
            moveObjects(Direction.DOWN, shiftKeyPressed ? 0.1f : 1.0f);
            debounceKeyLeft = debounceKey;
        }

        if (debounceKeyLeft <= 0 && Window.keyListener().isKeyPressed(KeyEvent.VK_CONTROL)) {
            if (Window.keyListener().isKeyPressed(KeyEvent.VK_D)) {
                duplicate();
                debounceKeyLeft = debounceKey;
            }
        }

    }

    public void moveObjects(Direction direction, float scale) {
        Vector2 distance = new Vector2(0.0f, 0.0f);
        switch (direction) {
            case UP:
                distance.y = -Constants.TILE_HEIGHT * scale;
                break;
            case DOWN:
                distance.y = Constants.TILE_HEIGHT * scale;
                break;
            case LEFT:
                distance.x = -Constants.TILE_WIDTH * scale;
                break;
            case RIGHT:
                distance.x = Constants.TILE_WIDTH * scale;
                break;
            default:
                System.out.println("Error: Direction has no enum '" + direction + "'");
                System.exit(-1);
                break;
        }

        for (GameObject go : selectedObjects) {
            go.transform.position.x += distance.x;
            go.transform.position.y += distance.y;
            float gridX = (float)(Math.floor(go.transform.position.x / Constants.TILE_WIDTH) + 1) * Constants.TILE_WIDTH;
            float gridY = (float)(Math.floor(go.transform.position.y / Constants.TILE_WIDTH) * Constants.TILE_WIDTH);

            if (go.transform.position.x < gridX + 1 && go.transform.position.x > gridX - 1) {
                go.transform.position.x = gridX;
            }

            if (go.transform.position.y < gridY + 1 && go.transform.position.y > gridY - 1) {
                go.transform.position.y = gridY;
            }
        }
    }

    public void duplicate() {
        for (GameObject go : selectedObjects) {
            Window.getScene().addGameObject(go.copy());
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
