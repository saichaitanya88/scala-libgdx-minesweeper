package core

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup

class MainScreen(val game: MyGdxGame) extends Screen{
  
  var stage: Stage = null
  var playButton: TextButton = null
  var easyRadio: CheckBox = null
  var mediumRadio: CheckBox = null
  var hardRadio: CheckBox = null
  var font: BitmapFont = null
  var skin: Skin = null
  var buttonAtlas: TextureAtlas = null
  val buttonGroup = new ButtonGroup[CheckBox]()
  
  def dispose(): Unit = {
    
  }
  def hide(): Unit = {
    
  }
  def pause(): Unit = {
    
  }
  def render(delta: Float): Unit = {
    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
      Gdx.app.exit()
    }
    clear()
    stage.draw()
  }
  def clear(){
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
  }
  def resize(width: Int, height: Int): Unit = {
    
  }
  def resume(): Unit = {
    
  }
  def show(): Unit = {
    stage = new Stage();
    Gdx.input.setInputProcessor(stage)
    font = new BitmapFont()
    skin = new Skin(Gdx.files.internal("assets/ui/uiskin.json"))
    buttonAtlas = new TextureAtlas(Gdx.files.internal("assets/ui/uiskin.atlas"))
    skin.addRegions(buttonAtlas)
    
    playButton = new TextButton("Play", skin)
    playButton.setPosition(Gdx.graphics.getWidth/2 - playButton.getWidth/2, Gdx.graphics.getHeight/2)
    playButton.addListener(new ClickListener(){
      override def clicked(event: InputEvent, x: Float, y: Float){
        val easy = easyRadio.getText().toString();
        val med = mediumRadio.getText.toString
        val hard = hardRadio.getText.toString
        val selectedBtn = buttonGroup.getChecked().getText().toString()
        val mineCount = if (selectedBtn == easy) 8 else if (selectedBtn == med) 12 else 16
        game.mineCount = mineCount
        game.setScreen(new GameScreen(game))
      }
    })
    stage.addActor(playButton)
    
    easyRadio = new CheckBox("Easy", skin)
    easyRadio.setPosition(Gdx.graphics.getWidth/2 - playButton.getWidth/2, Gdx.graphics.getHeight/2 + 100)
    
    mediumRadio = new CheckBox("Medium", skin)
    mediumRadio.setPosition(Gdx.graphics.getWidth/2 - playButton.getWidth/2, Gdx.graphics.getHeight/2 + 80)
    
    hardRadio = new CheckBox("Hard", skin)
    hardRadio.setPosition(Gdx.graphics.getWidth/2 - playButton.getWidth/2, Gdx.graphics.getHeight/2 + 60)
    
    stage.addActor(easyRadio)
    stage.addActor(mediumRadio)
    stage.addActor(hardRadio)
    buttonGroup.add(easyRadio, mediumRadio, hardRadio)
    buttonGroup.setChecked("Hard")
  }
}