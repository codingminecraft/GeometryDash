package com.Component;

import com.dataStructure.AssetPool;
import com.jade.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sprite extends Component {

    public BufferedImage image;
    public String pictureFile;
    public int width, height;

    public Sprite(String pictureFile) {
        this.pictureFile = pictureFile;

        try {
            File file = new File(pictureFile);

            if (AssetPool.hasSprite(pictureFile)) {
                throw new Exception("Sprite: Asset already exists: " + pictureFile);
            }

            this.image = ImageIO.read(file);
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public Sprite(BufferedImage image) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(image, (int)gameObject.transform.position.x,
                (int)gameObject.transform.position.y, width, height, null);
    }

    @Override
    public Component copy() {
        return new Sprite(this.image);
    }
}
