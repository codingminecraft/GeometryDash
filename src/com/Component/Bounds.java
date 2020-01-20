package com.Component;

import com.jade.Component;

enum BoundsType {
    Box,
    Triangle
}

public abstract class Bounds extends Component {
    public BoundsType type;

    abstract public float getWidth();
    abstract public float getHeight();

    public static boolean checkCollision(Bounds b1, Bounds b2) {
        if (b1.type == b2.type && b1.type == BoundsType.Box) {
            return BoxBounds.checkCollision((BoxBounds)b1, (BoxBounds)b2);
        }

        return false;
    }
}
