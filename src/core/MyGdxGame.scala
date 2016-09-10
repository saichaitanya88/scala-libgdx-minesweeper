package core

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.Game

class MyGdxGame extends Game{
  var mineCount: Int = 0;
	override def create () { 
	  this.setScreen(new MainScreen(this))
	}

	override def render () {
	  super.render()
	}
	
	override def dispose () {
		super.dispose()
	}
}