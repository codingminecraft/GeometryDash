package com.dataStructure;

import com.util.Vector2;

public class Transform {
    public Vector2 position;
    public Vector2 scale;
    public float rotation;

    public Transform(Vector2 position) {
        this.position = position;
        this.scale = new Vector2(1.0f, 1.0f);
        this.rotation = 0.0f;
    }

    public Transform copy() {
        Transform transform = new Transform(this.position.copy());
        transform.scale = this.scale.copy();
        transform.rotation = this.rotation;
        return transform;
    }

    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ")";
    }
}
