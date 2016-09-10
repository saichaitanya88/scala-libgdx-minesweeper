package core

import com.badlogic.gdx.Screen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.tools.particleeditor.Chart.Point
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GameScreen(val game: MyGdxGame) extends Screen {
  var mineBoard = Array[Array[MineButton]]()
  val gridSize = 20
  var stage: Stage = null
  var font: BitmapFont = null
  var skin: Skin = null
  var buttonAtlas: TextureAtlas = null
  var gameState = ""
  var spriteBatch: SpriteBatch = null
  
  def dispose(): Unit = {}
  def hide(): Unit = {}
  def pause(): Unit = {}
  def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    stage.draw()
    spriteBatch.begin()
    font.draw(spriteBatch, gameState, 200, Gdx.graphics.getHeight - 50)
    spriteBatch.end()
    if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
      Gdx.app.exit()
    }
    if(Gdx.input.isKeyJustPressed(Input.Keys.F5)){
      initBoard()
    }
  }
  def resize(width: Int,height: Int): Unit = {}
  def resume(): Unit = {}
  def show(): Unit = {
    spriteBatch = new SpriteBatch()
    stage = new Stage();
    Gdx.input.setInputProcessor(stage)
    font = new BitmapFont()
    skin = new Skin(Gdx.files.internal("assets/ui/uiskin.json"))
    buttonAtlas = new TextureAtlas(Gdx.files.internal("assets/ui/uiskin.atlas"))
    skin.addRegions(buttonAtlas)
    initBoard()
  }
  def initBoard(){
    gameState = ""
    mineBoard = Array.fill[MineButton](gridSize, gridSize)(new MineButton)
    List.range(0, gridSize).foreach(x => {
      List.range(0, gridSize).foreach(y => {
        mineBoard(x)(y).pos = new Vector2(x,y)
        mineBoard(x)(y).hasMine = false
        mineBoard(x)(y).state = ButtonState.UNKNOWN
        mineBoard(x)(y).init(skin, this);
        stage.addActor(mineBoard(x)(y).button);
      });
    });
    List.range(0, game.mineCount).foreach(r => {
      val p = getEmptyLocation()
      mineBoard(p.x.toInt)(p.y.toInt).hasMine = true
      mineBoard(p.x.toInt)(p.y.toInt).button.setText("")
    });
  }
  def getEmptyLocation(): Vector2 = {
    var x = scala.util.Random.nextInt(gridSize)
    var y = scala.util.Random.nextInt(gridSize)
    while (mineBoard(x)(y).hasMine){
      x = scala.util.Random.nextInt(gridSize)
      y = scala.util.Random.nextInt(gridSize)
    }
    new Vector2(x,y)
  }
  def reveal(btn: MineButton){
    var mineButtons = mineBoard.flatten
    val buttons = getNeighbors(btn, mineButtons);
    val neighbouringMineCount = buttons.count(p => p.hasMine)
    if (neighbouringMineCount > 0){
      btn.button.setText(neighbouringMineCount.toString)
    }
    else{
      btn.state = ButtonState.REVEALED
      buttons.filter(p => !p.hasMine).foreach(nb => {
        val btnNeighbours = getNeighbors(nb, mineButtons).filter { p => p.state == ButtonState.UNKNOWN }
        val count = btnNeighbours.count(b => b.hasMine)
        nb.state = ButtonState.REVEALED
        nb.button.setDisabled(true)
        if(count == 0){
          nb.button.setText("")
        }
        else {
          nb.button.setText(count.toString)
        }
        btnNeighbours.filter(p => !p.hasMine).filter(p => !p.button.isDisabled())
        .foreach(b => {
          if (getNeighbors(b, mineButtons).count {p => p.hasMine} == 0)
            reveal(b)
        })
      })
    }
  }
  def gameOver(){
    gameState = "GAME OVER. Press F5 to restart."
    revealMines()
  }
  def revealMines(){
    mineBoard.flatten.filter(m => m.hasMine).foreach(m => {
      m.button.setText("M")
    })
  }
  def checkGameState(){
    // if all mines are marked and no safe mines are marked - player wins.
    // disable all buttons
    var mineButtons = mineBoard.flatten
    val mines = mineButtons.filter(p => p.hasMine)
    val markedButtons = mineButtons.filter(p => p.state == ButtonState.MARKED)
    if(markedButtons.forall { m => mines.contains(m) } && mines.forall { m => markedButtons.contains(m) }){
      gameState = "YOU WIN. Press F5 to restart."
      disableBoard
    }
  }
  def disableBoard(){
    mineBoard.flatten.foreach(m => m.button.setDisabled(true))
  }
  def getNeighbors(btn: MineButton, mineButtons: Array[MineButton]): List[MineButton] = {
    val neighbourVectors = List[Vector2](new Vector2(-1,0),new Vector2(-1,1),new Vector2(0,1),new Vector2(1,1),new Vector2(1,0),new Vector2(1,-1),new Vector2(0,-1),new Vector2(-1,-1))
    val neighborOpts = neighbourVectors.map(v => {
      mineButtons.find(b => b.pos.x.toInt == (btn.pos.x + v.x).toInt && b.pos.y.toInt == (btn.pos.y + v.y).toInt) 
    }).filter(o => o.isDefined)
    neighborOpts.map(b => b.get)
  }
}