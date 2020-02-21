package com.ui;

import com.Component.LevelEditorControls;
import com.Component.Sprite;
import com.jade.Component;
import com.jade.GameObject;
import com.jade.LevelEditorScene;
import com.jade.Window;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MenuItem extends Component {
    private int x, y, width, height;
    private Sprite buttonSprite, hoverSprite, myImage;
    public boolean isSelected;

    private int bufferX, bufferY;

    private MainContainer parentContainer;

    public MenuItem(int x, int y, int width, int height, Sprite buttonSprite, Sprite hoverSprite,
                    MainContainer parent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonSprite = buttonSprite;
        this.hoverSprite = hoverSprite;
        this.isSelected = false;
        this.parentContainer = parent;
    }

    @Override
    public void start() {
        myImage = gameObject.getComponent(Sprite.class);

        this.bufferX = (int)((this.width / 2.0) - (myImage.width / 2.0));
        this.bufferY = (int)((this.height / 2.0) - (myImage.height / 2.0));
    }

    @Override
    public void update(double dt) {
        if (Window.getWindow().mouseListener.mousePressed &&
                Window.getWindow().mouseListener.mouseButton == MouseEvent.BUTTON1) {
            if (!isSelected &&
                Window.getWindow().mouseListener.x > this.x && Window.getWindow().mouseListener.x <= this.x + this.width &&
                Window.getWindow().mouseListener.y > this.y && Window.getWindow().mouseListener.y <= this.y + this.height) {
                    // Clicked inside the button
                GameObject obj = gameObject.copy();
                obj.removeComponent(MenuItem.class);
                LevelEditorScene scene = (LevelEditorScene)Window.getWindow().getCurrentScene();

                LevelEditorControls levelEditorControls = scene.mouseCursor.getComponent(LevelEditorControls.class);
                obj.addComponent(levelEditorControls);
                scene.mouseCursor = obj;
                isSelected = true;
                this.parentContainer.setHotButton(gameObject);
            }
        }

        if (Window.keyListener().isKeyPressed(KeyEvent.VK_ESCAPE)) {
            isSelected = false;
        }
    }

    @Override
    public MenuItem copy() {
        return new MenuItem(this.x, this.y, this.width, this.height,
                (Sprite)this.buttonSprite.copy(), (Sprite)this.hoverSprite.copy(),
                parentContainer);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.buttonSprite.image, this.x, this.y, this.width, this.height, null);
        g2.drawImage(myImage.image, this.x + bufferX, this.y + bufferY,
                myImage.width, myImage.height, null);
        if (isSelected) {
            g2.drawImage(hoverSprite.image, this.x, this.y, this.width, this.height, null);
        }
    }

    @Override

    public String serialize(int tabSize) {
        return "";
    }
}
