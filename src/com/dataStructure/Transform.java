package com.dataStructure;

import com.util.Vector2;

public class Transform {
    public Vector2 position;
    public Vector2 scale;
    public Vector2 rotation;

    public Transform(Vector2 position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Position (" + position.x + ", " + position.y + ")";
    }
}
