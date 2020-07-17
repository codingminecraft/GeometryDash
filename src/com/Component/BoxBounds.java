package com.Component;

import com.file.Parser;
import com.jade.Component;
import com.jade.GameObject;
import com.util.Constants;
import com.util.Vector2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class BoxBounds extends Bounds {
    public float width, height;
    public float halfWidth, halfHeight;
    public Vector2 center = new Vector2();
    public boolean isTrigger;
    public float xBuffer = 0.0f;
    public float yBuffer = 0.0f;

    public float enclosingRadius;

    public BoxBounds(float width, float height) {
        init(width, height, false);
    }

    public BoxBounds(float width, float height, boolean isTrigger) {
        init(width, height, isTrigger);
    }

    public void init(float width, float height, boolean isTrigger) {
        this.width = width;
        this.height = height;
        this.halfWidth = this.width / 2.0f;
        this.halfHeight = this.height / 2.0f;
        this.enclosingRadius = (float)Math.sqrt((this.halfWidth * this.halfWidth) +
                (this.halfHeight * this.halfHeight));
        this.type = BoundsType.Box;
        this.isTrigger = isTrigger;
    }

    @Override
    public void start() {
        this.calculateCenter();
    }

    public void calculateCenter() {
        this.center.x = this.gameObject.transform.position.x + this.halfWidth + this.xBuffer;
        this.center.y = this.gameObject.transform.position.y + this.halfHeight + this.yBuffer;
    }

    @Override
    public void update(double dt){

    }

    public static boolean checkCollision(BoxBounds b1, BoxBounds b2) {
        b1.calculateCenter();
        b2.calculateCenter();

        float dx = b2.center.x - b1.center.x;
        float dy = b2.center.y - b1.center.y;

        float combinedHalfWidths = b1.halfWidth + b2.halfWidth;
        float combinedHalfHeights = b1.halfHeight + b2.halfHeight;

        if (Math.abs(dx) <= combinedHalfWidths) {
            return Math.abs(dy) <= combinedHalfHeights;
        }

        return false;
    }

    public void resolveCollision(GameObject player) {
        if (isTrigger) return;

        BoxBounds playerBounds = player.getComponent(BoxBounds.class);
        playerBounds.calculateCenter();
        this.calculateCenter();

        float dx = this.center.x - playerBounds.center.x;
        float dy = this.center.y - playerBounds.center.y;

        float combinedHalfWidths = playerBounds.halfWidth + this.halfWidth;
        float combinedHalfHeights = playerBounds.halfHeight + this.halfHeight;

        float overlapX = combinedHalfWidths - Math.abs(dx);
        float overlapY = combinedHalfHeights - Math.abs(dy);

        if (overlapX >= overlapY) {
            if (dy > 0) {
                // Collision on the top of the player
                player.transform.position.y = gameObject.transform.position.y - playerBounds.getHeight() + yBuffer;
                player.getComponent(Rigidbody.class).velocity.y = 0;
                player.getComponent(Player.class).onGround = true;
            } else {
                // Collision on the bottom of the player
                player.getComponent(Player.class).die();
            }
        } else {
            // Collision on the left or right of the player
            if (dx < 0 && dy <= 0.3) {
                player.transform.position.y = gameObject.transform.position.y - playerBounds.getHeight() + yBuffer;
                player.getComponent(Rigidbody.class).velocity.y = 0;
                player.getComponent(Player.class).onGround = true;
            } else {
                player.getComponent(Player.class).die();
            }
        }
    }

    @Override
    public Component copy() {
        BoxBounds bounds = new BoxBounds(width, height, isTrigger);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("BoxBounds", tabSize));
        builder.append(addFloatProperty("Width", this.width, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, true));
        builder.append(addFloatProperty("xBuffer", this.xBuffer, tabSize + 1, true, true));
        builder.append(addFloatProperty("yBuffer", this.yBuffer, tabSize + 1, true, true));
        builder.append(addBooleanProperty("isTrigger", this.isTrigger, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static BoxBounds deserialize() {
        float width = Parser.consumeFloatProperty("Width");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consume(',');
        float xBuffer = Parser.consumeFloatProperty("xBuffer");
        Parser.consume(',');
        float yBuffer = Parser.consumeFloatProperty("yBuffer");
        Parser.consume(',');
        boolean isTrigger = Parser.consumeBooleanProperty("isTrigger");
        Parser.consumeEndObjectProperty();

        BoxBounds bounds =  new BoxBounds(width, height, isTrigger);
        bounds.xBuffer = xBuffer;
        bounds.yBuffer = yBuffer;
        return bounds;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public boolean raycast(Vector2 position) {
        return position.x > this.gameObject.transform.position.x + xBuffer &&
                position.x < this.gameObject.transform.position.x + this.width + xBuffer &&
                position.y > this.gameObject.transform.position.y + yBuffer &&
                position.y < this.gameObject.transform.position.y + this.height + yBuffer;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isSelected) {
            g2.setColor(Color.GREEN);
            g2.setStroke(Constants.THICK_LINE);
            g2.draw(new Rectangle2D.Float(
                    this.gameObject.transform.position.x + xBuffer,
                    this.gameObject.transform.position.y + yBuffer,
                    this.width,
                    this.height));
            g2.setStroke(Constants.LINE);
        }
    }
}
