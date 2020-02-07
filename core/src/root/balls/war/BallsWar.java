package root.balls.war;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import root.balls.war.Screen.GameScreen;
import root.balls.war.paths.AssetPaths;

public class BallsWar extends Game {

    private AssetManager assetManager;
    private SpriteBatch batch;

    @Override
    public void create() {
        assetManager = new AssetManager();
        batch = new SpriteBatch();

        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        batch.dispose();
    }

    // getters
    public AssetManager getAssetManager() {
        return assetManager;
    }

}
