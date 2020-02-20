package com.Component;

import com.jade.Component;
import com.jade.Window;
import com.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;


public class Player extends Component {

    Sprite layerOne, layerTwo, layerThree;
    public int width, height;
    public boolean onGround = true;

    public Player(Sprite layerOne, Sprite layerTwo, Sprite layerThree,
                  Color colorOne, Color colorTwo) {
        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.layerOne = layerOne;
        this.layerTwo = layerTwo;
        this.layerThree = layerThree;

        int threshold = 200;
        for (int y=0; y < layerOne.image.getWidth(); y++) {
            for (int x=0; x < layerOne.image.getHeight(); x++) {
                Color color = new Color(layerOne.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {
                    layerOne.image.setRGB(x, y, colorOne.getRGB());
                }
            }
        }

        for (int y=0; y < layerTwo.image.getWidth(); y++) {
            for (int x=0; x < layerTwo.image.getHeight(); x++) {
                Color color = new Color(layerTwo.image.getRGB(x, y));
                if (color.getRed() > threshold && color.getGreen() > threshold && color.getBlue() > threshold) {
                    layerTwo.image.setRGB(x, y, colorTwo.getRGB());
                }
            }
        }
    }

    @Override
    public void update(double dt) {
        if (onGround && Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            addJumpForce();
            this.onGround = false;
        }

        if (!onGround) {
            gameObject.transform.rotation += 10.0f * dt;
        } else {
            gameObject.transform.rotation = (int)gameObject.transform.rotation % 360;
            if (gameObject.transform.rotation > 180 && gameObject.transform.rotation < 360) {
                gameObject.transform.rotation = 0;
            } else if (gameObject.transform.rotation > 0 && gameObject.transform.rotation < 180) {
                gameObject.transform.rotation = 0;
            }
        }
    }

    private void addJumpForce() {
        gameObject.getComponent(Rigidbody.class).velocity.y = Constants.JUMP_FORCE;
    }

    public void die() {
        gameObject.transform.position.x = 500;
        gameObject.transform.position.y = 350;
        gameObject.getComponent(Rigidbody.class).velocity.y = 0;
        gameObject.transform.rotation = 0;
        Window.getWindow().getCurrentScene().camera.position.x = 0;
    }

    @Override
    public void draw(Graphics2D g2) {
        AffineTransform transform = new AffineTransform();
        transform.setToIdentity();
        transform.translate(gameObject.transform.position.x, gameObject.transform.position.y);
        transform.rotate(gameObject.transform.rotation,
                width * gameObject.transform.scale.x / 2.0,
                height * gameObject.transform.scale.y / 2.0);
        transform.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);
        g2.drawImage(layerOne.image, transform, null);
        g2.drawImage(layerTwo.image, transform, null);
        g2.drawImage(layerThree.image, transform, null);
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
