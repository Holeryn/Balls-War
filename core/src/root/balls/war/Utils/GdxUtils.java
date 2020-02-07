package root.balls.war.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class GdxUtils {
    /**
     * pulire lo schermo
     * con i prametri rgb
     */
    public void ClearScreen(){
        Gdx.gl.glClearColor( 0, 0, 1, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
    }
}
