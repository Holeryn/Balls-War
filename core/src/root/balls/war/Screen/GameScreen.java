package root.balls.war.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import root.balls.war.BallsWar;
import root.balls.war.Utils.GdxUtils;
import root.balls.war.grandezze.GameConfig;
import root.balls.war.paths.AssetPaths;

public class GameScreen implements Screen {
    // == attributes ==
    ShapeRenderer renderer;
    AssetManager assetManager;
    Viewport viewport;
    OrthographicCamera orthographicCamera;
    SpriteBatch batch;
    GlyphLayout layout;

    private BitmapFont scoreFont;

    float PlayerX;

    float RectX;
    float RectY;

    float WIDTH;
    float HEIGHT;

    int gamestate;
    int velocityObstacles;
    int Points = 0;

    boolean newCircle;

    // == classes ==
    BallsWar ballsWar;
    GdxUtils gdxUtils;

    // == entity ==
    Circle PlayerTrigger;
    Rectangle ObstacleTrigger;

    Texture playerTexture;
    Texture Beckground;

    public GameScreen(BallsWar ballsWar) {
        this.ballsWar = ballsWar;
        assetManager = ballsWar.getAssetManager();
    }

    // == public methods ==
    @Override
    public void show() {
        init();
    }

    @Override
    public void render(float delta) {
        gdxUtils.ClearScreen();

        //draw Player , Obstacles and Hud
        drawTextureAndHud();

        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

       // System.out.println("ooooooooo" + HEIGHT);

        if (gamestate == 0) {
            //use inputs for players and obstacles
            useInput();

            /*
             * Check Collision <Player> - <Circle>
             */
            if (Intersector.overlaps(PlayerTrigger, ObstacleTrigger)) {
               // System.out.println("////////check collision");
                gamestate = 1;
            }

            if (PlayerX >= WIDTH - GameConfig.RADIUS) {
                PlayerX = WIDTH - GameConfig.RADIUS;
            } else if (PlayerX <= 0.0f) {
                PlayerX = GameConfig.MARGIN_LEFT + GameConfig.RADIUS;
            }

            if (RectY < 0) {
                RectY = HEIGHT;
                RectX = MathUtils.random(GameConfig.MARGIN_LEFT, WIDTH - GameConfig.SQUARE_WIDTH);
                velocityObstacles += 1;
                Points += 2;
            }

            System.out.println(PlayerX);

            RectY -= velocityObstacles;
        } else if (gamestate == 1) {
            if(Gdx.input.isTouched()){
                velocityObstacles = GameConfig.VelocityQ;
                RectY = GameConfig.ObstacleYstart;
                PlayerX = WIDTH / 2f - GameConfig.RADIUS / 2f;
                PlayerX = WIDTH / 2f - GameConfig.SQUARE_WIDTH / 2f;
                gamestate = 0;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        System.out.println("pause()");
    }

    @Override
    public void resume() {
        System.out.println("resume()");
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    // == init ==
    public void init() {
        ballsWar = new BallsWar();
        gdxUtils = new GdxUtils();

        renderer = new ShapeRenderer();

        orthographicCamera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), orthographicCamera);

        PlayerX = WIDTH / 2f - GameConfig.RADIUS / 2f;
        RectY = GameConfig.ObstacleYstart;

        newCircle = true;
        velocityObstacles = GameConfig.VelocityQ;

        batch = new SpriteBatch();
        layout = new GlyphLayout();

        assetManager.load(AssetPaths.SCORE_FONT, BitmapFont.class);
        assetManager.load(AssetPaths.TEXTURE, Texture.class);
        assetManager.load(AssetPaths.BECKGROUND, Texture.class);
        assetManager.finishLoading();

        scoreFont = assetManager.get(AssetPaths.SCORE_FONT, BitmapFont.class);
        playerTexture = assetManager.get(AssetPaths.TEXTURE, Texture.class);
        Beckground = assetManager.get(AssetPaths.BECKGROUND, Texture.class);

        ObstacleTrigger = new Rectangle(RectX, RectY, GameConfig.SQUARE_WIDTH, GameConfig.SQUARE_WIDTH);
        PlayerTrigger = new Circle(PlayerX, 3, GameConfig.RADIUS);
    }

    // == private methods ==
    private void draw() {
        renderer.setProjectionMatrix(orthographicCamera.combined);

            renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(new Color(1, 1, 1, 0.0f));
        renderer.circle(PlayerX, 100, GameConfig.RADIUS, 30);
        PlayerTrigger = new Circle(PlayerX, 3, GameConfig.RADIUS);
        PlayerTrigger.setX(PlayerX);

        renderer.setColor(Color.RED);
        renderer.rect(RectX, RectY, GameConfig.SQUARE_WIDTH, GameConfig.SQUARE_WIDTH);
        ObstacleTrigger.setX(RectX);
        ObstacleTrigger.setY(RectY);

        newCircle = false;

        renderer.end();
    }

    private void useInput() {
        if (Gdx.input.isTouched()) {
            PlayerX = Gdx.input.getX();
        }
    }

    private void drawTextureAndHud() {
        batch.setProjectionMatrix(orthographicCamera.combined);
        batch.begin();

        batch.draw(Beckground, 0, 0, WIDTH, HEIGHT);

        batch.draw(playerTexture,
                PlayerX - (ObstacleTrigger.width - 55) / 2f, 100 - (ObstacleTrigger.height - 55) / 2f,
                ObstacleTrigger.width - 55, ObstacleTrigger.height - 55);

        viewport.apply();

        String Score = "SCORE: " + Points;
        scoreFont.getData().setScale(GameConfig.FONT_SCALE);
        layout.setText(scoreFont, Score);
        scoreFont.draw(batch, layout,
                WIDTH, HEIGHT);
        System.out.println("layout " + layout);

        draw();

        batch.end();
    }
}
