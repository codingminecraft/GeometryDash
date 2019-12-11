package com.Component;

import com.jade.Component;
import com.util.Constants;
import com.util.Vector2;

public class Rigidbody extends Component {

    public Vector2 velocity;

    public Rigidbody(Vector2 vel) {
        this.velocity = vel;
    }

    @Override
    public void update(double dt) {
        gameObject.transform.position.y += velocity.y * dt;
        gameObject.transform.position.x += velocity.x * dt;

        velocity.y += Constants.GRAVITY * dt;

        if (Math.abs(velocity.y) > Constants.TERMINAL_VELOCITY) {
            velocity.y = Math.signum(velocity.y) * Constants.TERMINAL_VELOCITY;
        }
    }
}
