package com.ui;

import com.Component.BoxBounds;
import com.Component.Sprite;
import com.Component.Spritesheet;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.jade.Component;
import com.jade.GameObject;
import com.jade.Window;
import com.util.Constants;
import com.util.Vector2;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContainer extends Component {
    public Sprite containerBg;

    public List<GameObject> menuItems;

    public List<GameObject> tabs;
    public Map<GameObject, List<GameObject>> tabMaps;
    public GameObject currentTab;

    public MainContainer() {
        this.menuItems = new ArrayList<>();
        this.tabs = new ArrayList<>();
        this.tabMaps = new HashMap<>();

        this.containerBg = AssetPool.getSprite("assets/ui/menuContainerBackground.png");
        init();
    }

    public void init() {
        Spritesheet tabSprites = AssetPool.getSpritesheet("assets/ui/tabs.png");

        for (int i=0; i < tabSprites.sprites.size(); i++) {
            Sprite currentTab = tabSprites.sprites.get(i);

            int x = Constants.TAB_OFFSET_X + (currentTab.column * Constants.TAB_WIDTH) +
                    (currentTab.column * Constants.TAB_HORIZONTAL_SPACING);
            int y = Constants.TAB_OFFSET_Y;

            GameObject obj = new GameObject("Tab", new Transform(new Vector2(x, y)), 10);
            obj.setUi(true);
            obj.addComponent(currentTab);

            this.tabs.add(obj);
            this.tabMaps.put(obj, new ArrayList<>());
            Window.getWindow().getCurrentScene().addGameObject(obj);
        }
        this.currentTab = this.tabs.get(3);

        addTabObjects();
    }

    private void addTabObjects() {
        Spritesheet buttonSprites = AssetPool.getSpritesheet("assets/ui/buttonSprites.png");
        Spritesheet groundSprites = AssetPool.getSpritesheet("assets/groundSprites.png");
        Spritesheet spikeSprites = AssetPool.getSpritesheet("assets/spikes.png");
        Spritesheet bigSprites = AssetPool.getSpritesheet("assets/bigSprites.png");
        Spritesheet smallBlocks = AssetPool.getSpritesheet("assets/smallBlocks.png");
        Spritesheet portalSprites = AssetPool.getSpritesheet("assets/portal.png");

        for (int i=0; i < groundSprites.sprites.size(); i++) {
            Sprite currentSprite = groundSprites.sprites.get(i);
            int x = Constants.BUTTON_OFFSET_X + (currentSprite.column * Constants.BUTTON_WIDTH) +
                    (currentSprite.column * Constants.BUTTON_SPACING_HZ);
            int y = Constants.BUTTON_OFFSET_Y + (currentSprite.row * Constants.BUTTON_HEIGHT) +
                    (currentSprite.row * Constants.BUTTON_SPACING_VT);

            // Add first tab container objs
            GameObject obj = new GameObject("Gen", new Transform(new Vector2(x, y)), 10);
            obj.setUi(true);
            obj.setNonserializable();
            obj.addComponent(currentSprite.copy());
            MenuItem menuItem = new MenuItem(x, y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT,
                    buttonSprites.sprites.get(0), buttonSprites.sprites.get(1));
            obj.addComponent(menuItem);
            obj.addComponent(new BoxBounds(Constants.TILE_WIDTH, Constants.TILE_HEIGHT));
            this.tabMaps.get(this.tabs.get(0)).add(obj);

            // Add second tab container objs
            if (i < smallBlocks.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new Vector2(x, y)), 10);
                obj.setUi(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(smallBlocks.sprites.get(i));
                obj.addComponent(menuItem);

                if (i == 0) {
                    obj.addComponent(new BoxBounds(Constants.TILE_WIDTH, 16));
                }
                this.tabMaps.get(tabs.get(1)).add(obj);
            }

            // Add fourth tab container objs
            if (i < spikeSprites.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new Vector2(x, y)), 10);
                obj.setNonserializable();
                obj.setUi(true);
                menuItem = menuItem.copy();
                obj.addComponent(spikeSprites.sprites.get(i));
                obj.addComponent(menuItem);

                // TODO:: Add triangleBounds component here

                this.tabMaps.get(this.tabs.get(3)).add(obj);
            }

            // Add fifth tab container objs
            if (i == 0) {
                obj = new GameObject("Gen", new Transform(new Vector2(x, y)), 10);
                obj.setUi(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(menuItem);
                obj.addComponent(bigSprites.sprites.get(i));

                obj.addComponent(new BoxBounds(Constants.TILE_WIDTH * 2, 56));
                this.tabMaps.get(tabs.get(4)).add(obj);
            }

            // Add sixth tab container objs
            if (i < portalSprites.sprites.size()) {
                obj = new GameObject("Gen", new Transform(new Vector2(x, y)), 10);
                obj.setUi(true);
                obj.setNonserializable();
                menuItem = menuItem.copy();
                obj.addComponent(menuItem);
                obj.addComponent(portalSprites.sprites.get(i));
                obj.addComponent(new BoxBounds(44, 85));

                // TODO:: Create portalComponent here

                this.tabMaps.get(tabs.get(5)).add(obj);
            }
        }
    }

    @Override
    public void start() {
        for (GameObject g : tabs) {
            for (GameObject g2 : tabMaps.get(g)) {
                for (Component c : g2.getAllComponents()) {
                    c.start();
                }
            }
        }
    }

    @Override
    public void update(double dt) {
        for (GameObject g : this.tabMaps.get(currentTab)) {
            g.update(dt);
        }
    }

    @Override
    public Component copy() {
        return null;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(this.containerBg.image, 0, Constants.CONTAINER_OFFSET_Y,
                this.containerBg.width, this.containerBg.height, null);

        for (GameObject g : this.tabMaps.get(currentTab)) {
            g.draw(g2);
        }
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }
}
