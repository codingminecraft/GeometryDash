package com.Component;

import com.file.Parser;
import com.jade.Component;
import com.util.Vector2;

public class TriangleBounds extends Bounds {
    private float base, height, halfWidth, halfHeight;
    private float enclosingRadius;

    private float x1, x2, x3, y1, y2, y3;

    public TriangleBounds(float base, float height) {
        this.type = BoundsType.Triangle;
        this.base = base;
        this.height = height;
        this.halfWidth = this.base / 2.0f;
        this.halfHeight = this.height / 2.0f;
        this.enclosingRadius = Math.max(this.halfHeight, this.halfWidth);
    }

    @Override
    public void start() {
        calculateTransform();
    }

    public static boolean checkCollision(BoxBounds b1, TriangleBounds t2) {
        if (t2.broadPhase(b1)) {
            System.out.println("Broad phase collision!!!");
            return t2.narrowPhase(b1);
        }

        return false;
    }

    private boolean broadPhase(BoxBounds b1) {
        float bRadius = b1.enclosingRadius;
        float tRadius = this.enclosingRadius;

        float centerX = this.x2;
        float centerY = this.y2 + halfHeight;

        float plrCenterX = b1.gameObject.transform.position.x + b1.halfWidth;
        float plrCenterY = b1.gameObject.transform.position.y + b1.halfHeight;

        Vector2 distance = new Vector2(plrCenterX - centerX, plrCenterY - centerY);
        float magSquared = (distance.x * distance.x) + (distance.y * distance.y);
        float radiiSquared = (bRadius + tRadius) * (bRadius + tRadius);

        return magSquared <= radiiSquared;
    }

    private boolean narrowPhase(BoxBounds b1) {
        return true;
    }

    private void calculateTransform() {
        this.x1 = gameObject.transform.position.x;
        this.y1 = gameObject.transform.position.y + height;
        this.x2 = gameObject.transform.position.x + halfWidth;
        this.y2 = gameObject.transform.position.y;
        this.x3 = gameObject.transform.position.x + base;
        this.y3 = gameObject.transform.position.y + height;
    }

    @Override
    public float getWidth() {
        return this.base;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public Component copy() {
        return new TriangleBounds(this.base, this.height);
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(beginObjectProperty("TriangleBounds", tabSize));
        builder.append(addFloatProperty("Base", this.base, tabSize + 1, true, true));
        builder.append(addFloatProperty("Height", this.height, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static TriangleBounds deserialize() {
        float base = Parser.consumeFloatProperty("Base");
        Parser.consume(',');
        float height = Parser.consumeFloatProperty("Height");
        Parser.consumeEndObjectProperty();

        return new TriangleBounds(base, height);
    }
}
