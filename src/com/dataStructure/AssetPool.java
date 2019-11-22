package com.dataStructure;

import com.Component.Sprite;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    static Map<String, Sprite> sprites = new HashMap<>();

    public static boolean hasSprite(String pictureFile) {
        return AssetPool.sprites.containsKey(pictureFile);
    }

    public static Sprite getSprite(String pictureFile) {
        File file = new File(pictureFile);
        if (AssetPool.hasSprite(pictureFile)) {
            return AssetPool.sprites.get(file.getAbsolutePath().toString());
        } else {
            Sprite sprite = new Sprite(pictureFile);
            AssetPool.addSprite(pictureFile, sprite);
            return AssetPool.sprites.get(file.getAbsolutePath());
        }
    }

    /**
     *
     * @param pictureFile The absolute path to the picture
     * @param sprite
     */
    public static void addSprite(String pictureFile, Sprite sprite) {
        File file = new File(pictureFile);
        if (!AssetPool.hasSprite(file.getAbsolutePath())) {
            AssetPool.sprites.put(file.getAbsolutePath(), sprite);
        } else {
            System.out.println("Asset pool already has asset: " + file.getAbsolutePath());
            System.exit(-1);
        }
    }

}
