package com.ui;

import com.Component.SnapToGrid;
import com.Component.Sprite;
import com.jade.Component;
import com.jade.GameObject;
import com.jade.LevelEditorScene;
import com.jade.Window;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuItem extends Component {
    int x, y, width, height;
    Sprite buttonSprite, hoverSprite, myImage;
    public boolean isSelected;

    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
    }

    @Override
    public void start() {
        myImage = gameObject.getComponent(Sprite.class);
    }

    @Override
    public void update(double dt) {
        if (!isSelected &&
            Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width &&
            Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height) {
            if (Window.getWindow().mouseListener.mousePressed &&
                    Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1) {
                // Clicked inside the button
                GameObject obj = gameObject.copy();
                obj.removeComponent(MenuItem.class);
                LevelEditorScene scene = (LevelEditorScene)Window.getWindow().getCurrentScene();

                SnapToGrid snapToGrid = scene.mouseCursor.getComponent(SnapToGrid.class);
                obj.addComponent(snapToGrid);
                scene.mouseCursor = obj;
                isSelected = true;
            }
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.buttonSprite.image, this.x, this.y, this.width, this.height, null);
        g2.drawImage(myImage.image, this.x, this.y, myImage.width, myImage.height, null);
        if (isSelected) {
            g2.drawImage(hoverSprite.image, this.x, this.y, this.width, this.height, null);
        }
    }
}
