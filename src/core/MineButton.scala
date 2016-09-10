package core

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle

class MineButton {
  var hasMine = false
  var pos = new Vector2()
  var button: TextButton = null
  var state: ButtonState.Value = ButtonState.UNKNOWN
  
  def init(skin: Skin, gameScreen: GameScreen){
    button = new TextButton(s"", skin);
    button.setWidth(30)
    button.setHeight(30)
    button.setPosition(button.getWidth * pos.x + 25, button.getHeight * pos.y + 25)
    val self = this;
    button.addListener(new ClickListener(Buttons.LEFT){
      override def clicked(event: InputEvent, x: Float, y: Float){
        if (button.isDisabled()) return;
        button.setDisabled(true)
        if (hasMine){
          button.setText("x")
          button.setColor(Color.RED)
          gameScreen.gameOver()
        }
        else{
          button.setText("")
          button.setColor(Color.WHITE)
          gameScreen.reveal(self)
        }
      }
    });
    button.addListener(new ClickListener(Buttons.RIGHT){
      override def clicked(event: InputEvent, x: Float, y: Float){
        if (button.isDisabled()) return;
        if (state == ButtonState.UNKNOWN){
          state = ButtonState.MARKED
          button.setText("o")
          button.setColor(Color.GOLDENROD)
          gameScreen.checkGameState()
        }
        else{
          state = ButtonState.UNKNOWN
          button.setText("")
          button.setColor(Color.WHITE)
        }
      }
    });
  }
  override def toString(): String = {
    return s"""-------------------------------------
      position: ${pos.x},${pos.y}
      button: ${button.getText}
      state: ${state}
      hasMine: ${hasMine}"""
  }
}

object ButtonState extends Enumeration{
  val UNKNOWN,CLICKED,MARKED,REVEALED = Value;
}