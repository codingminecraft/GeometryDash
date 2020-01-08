package com.util;

import com.file.Parser;
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

        builder.append(beginObjectProperty("Vector2", tabSize));
        builder.append(addFloatProperty("x", x, tabSize + 1, true, true));
        builder.append(addFloatProperty("y", y, tabSize + 1, true, false));
        builder.append(closeObjectProperty(tabSize));

        return builder.toString();
    }

    public static Vector2 deserialize() {
        Parser.consumeBeginObjectProperty("Vector2");
        float x = Parser.consumeFloatProperty("x");
        Parser.consume(',');
        float y = Parser.consumeFloatProperty("y");
        Parser.consumeEndObjectProperty();
        return new Vector2(x, y);
    }
}
