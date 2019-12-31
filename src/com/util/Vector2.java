package com.util;

import com.file.Serialize;

public class Vector2 extends Serialize {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2 copy() {
        return new Vector2(this.x, this.y);
    }

    @Override
    public String serialize(int tabSize) {
        StringBuilder builder = new StringBuilder();

        builder.append(addFloatProperty("x", x, tabSize, true, true));
        builder.append(addFloatProperty("y", y, tabSize, true, false));

        return builder.toString();
    }
}
