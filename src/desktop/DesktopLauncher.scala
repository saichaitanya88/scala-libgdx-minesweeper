package desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import core.MyGdxGame

object DesktopLauncher {
  def main(args: Array[String]){
    val config = new LwjglApplicationConfiguration();
    config.height = 20*30 + 150;
    config.width = 20*30 + 50;
    new LwjglApplication(new MyGdxGame(), config);
  }
}