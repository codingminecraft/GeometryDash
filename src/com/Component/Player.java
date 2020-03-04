package com.Component;

import com.dataStructure.AssetPool;
import com.jade.Component;
import com.jade.Window;
import com.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;


public class Player extends Component {

    Sprite layerOne, layerTwo, layerThree, spaceship;
    public int width, height;
    public boolean onGround = true;
    public PlayerState state;

    public Player(Sprite layerOne, Sprite layerTwo, Sprite layerThree,
                  Color colorOne, Color colorTwo) {
        this.spaceship = AssetPool.getSprite("assets/player/spaceship.png");

        this.width = Constants.PLAYER_WIDTH;
        this.height = Constants.PLAYER_HEIGHT;
        this.layerOne = layerOne;
        this.layerTwo = layerTwo;
        this.layerThree = layerThree;
        this.state = PlayerState.NORMAL;

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
            if (state == PlayerState.NORMAL) {
                addJumpForce();
            }
            this.onGround = false;
        }

        if (PlayerState.FLYING == this.state && Window.getWindow().keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            addFlyForce();
            this.onGround = false;
        }

        if (this.state != PlayerState.FLYING && !onGround) {
            gameObject.transform.rotation += 10.0f * dt;
        } else if (this.state != PlayerState.FLYING) {
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

    private void addFlyForce() {
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

        if (state == PlayerState.NORMAL) {
            g2.drawImage(layerOne.image, transform, null);
            g2.drawImage(layerTwo.image, transform, null);
            g2.drawImage(layerThree.image, transform, null);
        } else {
            transform.setToIdentity();
            transform.translate(gameObject.transform.position.x,
                    gameObject.transform.position.y);
            transform.rotate(gameObject.transform.rotation,
                    width * gameObject.transform.scale.x / 4.0,
                    height * gameObject.transform.scale.y / 4.0);
            transform.scale(gameObject.transform.scale.x / 2, gameObject.transform.scale.y / 2);
            transform.translate(15, 15);
            g2.drawImage(layerOne.image, transform, null);
            g2.drawImage(layerTwo.image, transform, null);
            g2.drawImage(layerThree.image, transform, null);


            transform.setToIdentity();
            transform.translate(gameObject.transform.position.x, gameObject.transform.position.y);
            transform.rotate(gameObject.transform.rotation,
                    width * gameObject.transform.scale.x / 2.0,
                    height * gameObject.transform.scale.y / 2.0);
            transform.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);
            g2.drawImage(spaceship.image, transform, null);
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
