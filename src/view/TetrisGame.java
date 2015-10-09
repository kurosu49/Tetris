/*
 * TCSS305 - Spring 2013
 * Project Tetris
 * 
 */

package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;
import model.AbstractPiece;
import model.Block;
//import model.Block;
import model.Board;

/**
 * The game display of Tetris.
 * 
 * @author Crystal Miraflor (mirafcy@uw.edu)
 * @version 1.00
 *
 */
@SuppressWarnings("serial")
public class TetrisGame extends JPanel implements Observer {
  
  /**
   * The width of the dimension.
   */
  private static final int WIDTH = 400;
  
  /**
   * The height of the dimension.
   */
  private static final int HEIGHT = 800;
  
  /**
   * The dimensions of this game panel.
   */
  private static final Dimension DIMENSION = new Dimension(WIDTH, HEIGHT);
  
  /**
   * Number of extra rows above the board.
   */
  private static final int EXTRA_ROWS = 0;
  
  /**
   * The speed of the tetris game.
   */
  private static final int SPEED = 1000;
  
  /**
   * Square size based on pixels.
   */
  private static final int SQUARE_SIZE = 40;
  
  /**
   * The font.
   */
  private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, SQUARE_SIZE);
  
  /**
   * The multiplier constant applied to the speed.
   */
  private static final double MULTIPLIER = .8;
  
  /**
   * The subtraction applied to the level and
   * speed calculation.
   */
  private static final double SUBTRACTION = .9;
  
  /**
   * Number of blocks for each piece.
   */
  private static final int BLOCKS = 4;
  
  /**
   * A random generator.
   */
  private static final Random GENERATOR = new Random();
 
  /**
   * KeyListener for the tetris game.
   */
  private final MainKeys my_keylistener;
  
  /**
   * The key listener that pauses the
   * game.
   */
  private final PauseKey my_pausekey;
  
  /**
   * The Tetris board for the Tetris game panel.
   */
  private final Board my_board;
  
  /**
   * The statistics panel of tetris.
   */
  private final TetrisStats my_stats;
  
  /**
   * The timer for tetris.
   */
  private final Timer my_timer;
  
//  /**
//   * The current level of the game.
//   */
//  private int my_level;
  
  /**
   * The flag indicating whether or not the
   * tetris game is over or not.
   */
  private boolean my_game_over_flag;
  
  /**
   * The flag indicating whether or not 
   * tetris game is paused or not.
   */
  private boolean my_pause_flag;
  
  /**
   * The boolean indicating whether or
   * not the game is set on the normal
   * mode or not.
   */
  private boolean my_normal_mode;
  
  /**
   * Constructor that takes specified heights and widths
   * for the Tetris game panel.
   * 
   * @param the_board The board.
   * @param the_stats The statistics.
   * @param the_listener The listener.
   * 
   */
  public TetrisGame(final Board the_board, final TetrisStats the_stats,
                    final ActionListener the_listener) {
    super();
    my_board = the_board;
    my_stats = the_stats;
//    my_level = 1;
    my_normal_mode = true;
    my_timer = new Timer(speedCalculation(), the_listener);
    my_pausekey = new PauseKey();
    my_keylistener = new MainKeys();
    this.setPreferredSize(DIMENSION);
    setKeyListeners();
  }
  
  /**
   * Sets all the needed key listeners for the 
   * game board.
   */
  private void setKeyListeners() {
    this.addKeyListener(my_keylistener);
    this.addKeyListener(my_pausekey);
    this.setFocusable(true);
    this.setFocusTraversalKeysEnabled(false);
  }
//  
  /**
   * Starts the timer and the game of tetris.
   * @return 
   */
  public void startGame() {
    my_timer.setInitialDelay(SPEED / 2);
    my_timer.start();
  }
  
  /**
   * Draws the board for the Tetris game.
   * 
   * @param the_graphic The graphic.
   */
  public void paintComponent(final Graphics the_graphic) {
    super.paintComponent(the_graphic);
    final Graphics2D graphic = (Graphics2D) the_graphic;
    graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
    if (my_game_over_flag) {
      drawGameOver(graphic);
    
    } else {
    
      for (int i = my_board.getHeight() + EXTRA_ROWS - 1; i > my_board.getHeight() - 1; i--) {
        drawRow(i, graphic);
      }
      for (int i = my_board.getHeight() - 1; i >= 0; i--) {
        drawRow(i, graphic);
      }
    }

  }
  
  /**
   * Draws out the sent in row of the Tetris
   * game board.
   * 
   * @param the_row The row.
   * @param the_graphic The graphic.
   */
  private void drawRow(final int the_row, final Graphics2D the_graphic) {
    final Graphics2D graphic = the_graphic;
    final List<Block[]> frozen = my_board.getFrozenBlocks();
    if (frozen.size() - 1 < the_row) {
      for (int col = 0; col < my_board.getWidth(); col++) {
        for (int block = 0; block < BLOCKS; block++) {
          graphic.setColor(Color.BLACK);
          graphic.fillRect(col * SQUARE_SIZE, 
                               getHeight() - (the_row * SQUARE_SIZE) - SQUARE_SIZE, 
                               SQUARE_SIZE, SQUARE_SIZE);
          drawCurrentPiece(graphic);
        }
      }
    } else {
      final Block[] row_blocks = frozen.get(the_row);
      
      for (int i = 0; i < my_board.getWidth(); i++) {
        for (int block = 0; block < BLOCKS; block++) {

          if (row_blocks[i] == Block.EMPTY) {
            graphic.setColor(Color.BLACK);
            graphic.fillRect(i * SQUARE_SIZE,  
                                 getHeight() - (the_row * SQUARE_SIZE) - SQUARE_SIZE, 
                                 SQUARE_SIZE, SQUARE_SIZE);
          } else {
            Color color = row_blocks[i].getColor();
            if (isNormalMode()) {
              graphic.setColor(color);
            } else {
              color = discoMode(color);
              graphic.setColor(color);
            }
            graphic.fillRect(i * SQUARE_SIZE,  
                                 getHeight() - (the_row * SQUARE_SIZE) - SQUARE_SIZE, 
                                 SQUARE_SIZE, SQUARE_SIZE);
            if (color != Color.BLACK) {
              graphic.setColor(color.darker());
              graphic.setStroke(new BasicStroke(2));
              graphic.drawRect(i * SQUARE_SIZE, 
                               getHeight() - (the_row * SQUARE_SIZE) - SQUARE_SIZE, 
                               SQUARE_SIZE, SQUARE_SIZE);
            }
          }
          drawCurrentPiece(graphic);
        }
      }
    }
    
  }


  /**
   * Draws the current piece on the board.
   * 
   * @param the_graphic The graphic.
   */
  private void drawCurrentPiece(final Graphics2D the_graphic) {
    final Object current_block = (AbstractPiece) my_board.getCurrentPiece();
    final int[][] current_block_coordinates = 
        ((AbstractPiece) current_block).getBoardCoordinates();
    for (int point = 0; point < BLOCKS; point++) {
      for (int row = my_board.getHeight() - 1; row >= 0; row--) {
        final int y = getHeight() - (row * SQUARE_SIZE) - SQUARE_SIZE;   
        for (int col = my_board.getWidth() - 1; col >= 0; col--) {
          final int x = col * SQUARE_SIZE;
          if (col == current_block_coordinates[point][0] 
             && row == current_block_coordinates[point][1]) {
            Color color = ((AbstractPiece) current_block).getBlock().getColor();
            if (isNormalMode()) {
              the_graphic.setColor(color); 
            } else {
              color = discoMode(color);
              the_graphic.setColor(color);
            }
            the_graphic.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            if (color != Color.BLACK) {
              the_graphic.setColor(color.darker());
              the_graphic.setStroke(new BasicStroke(2));
              the_graphic.drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE);
            }
          }
        }
      }
    }
  }
  
  /**
   * Changes the color for
   * vanish mode.
   * 
   * @param the_color The color.
   * @return a color.
   */
  private Color discoMode(final Color the_color) {
    final int random = GENERATOR.nextInt(2);
    Color color;
    if (random == 0) {
      color = Color.BLACK;
    } else {
      color = the_color;
    }
    return color;
    
  }

  /**
   * Draws the game over screen.
   * 
   * @param the_graphic The graphic.
   */
  private void drawGameOver(final Graphics2D the_graphic) {
    the_graphic.setColor(Color.RED);
    the_graphic.fillRect(0, 0, getWidth(), getHeight());
//    final Font font = new Font(Font.MONOSPACED, Font.BOLD, SQUARE_SIZE);
    the_graphic.setFont(FONT);
    the_graphic.setColor(Color.BLACK);
    the_graphic.drawString("...GAME OVER...", SQUARE_SIZE / 2, getHeight() / 2);
  }
  
  /**
   * Changes the state of the board
   * when it is paused or not.
   */
  public void changePause() {
    if (my_pause_flag) {
      my_pause_flag = false;
      my_timer.start();
      this.addKeyListener(my_keylistener);
    } else {
      my_pause_flag = true;
      my_timer.stop();
      this.removeKeyListener(my_keylistener);
    }
  }
  
  /**
   * This method ends the current game
   * but still leaves the current board displayed.
   */
  public void endGame() {
    my_timer.stop();
    this.removeKeyListener(my_keylistener);
    this.removeKeyListener(my_pausekey);
  }
  
  /**
   * This method essentially resets this
   * game panel. 
   */
  public void resetGame() {
    my_game_over_flag = false;
    my_normal_mode = true;
    my_timer.setDelay(SPEED);
    if (my_pause_flag) {
      changePause();
    } else {
      my_pause_flag = false;
    }
    if (this.getKeyListeners().length == 0) {
      this.addKeyListener(my_keylistener);
      this.addKeyListener(my_pausekey);
    }
    this.setFocusable(true);
    repaint();
  }

  /**
   * Updates the game panel.
   * 
   * @param the_arg The argument.
   * @param the_object The object.
   */
  @Override
  public void update(final Observable the_arg, final Object the_object) {
    if (my_board.gameIsOver()) {
      my_game_over_flag = true;
    }
    my_timer.setDelay(speedCalculation());
    repaint();
  }
  
  /**
   * Sets the normal mode according to the
   * parameter. Normal mode is on if true
   * and false if off.
   * 
   * @param the_value The value.
   */
  public void setNormalMode(final boolean the_value) {
    my_normal_mode = the_value;
  }
  
  /**
   * Returns the my_normal_mode value.
   * 
   * @return true if normal mode is on
   * and false if normal mode is off. 
   */
  public boolean isNormalMode() {
    return my_normal_mode;
  }
  
  
  /**
   * Calculates the speed/delay time
   * for the timer on the tetris board.
   * 
   * @return the speed.
   */
  private int speedCalculation() {
    int speed = 0;
    if (my_stats.getLevel() == 1) {
      speed = SPEED;
    } else {
      speed = (int) (SPEED * (MULTIPLIER / (my_stats.getLevel() - SUBTRACTION)));
    }
    return speed;
  }
  
  /**
   * The KeyListener to pause the game.
   * 
   * @author Crystal
   *
   */
  class PauseKey extends KeyAdapter {
    
    /**
     * The method for when a certain key is
     * pressed so that it may pause the game.
     * 
     * @param the_event The event.
     */
    public void keyPressed(final KeyEvent the_event) {
      final int key = the_event.getKeyCode();
      if (key == KeyEvent.VK_P) {
        changePause();
      }
    }
  }

  /**
   * Class for Key actions for the tetris game.
   * 
   * @author Crystal Miraflor
   * @version 1.00
   *
   */
  class MainKeys extends KeyAdapter {
    //class for keys
    
    /**
     * The method for the main controls of
     * the game tetris, such as how to move,
     * rotate and instant drop a piece.
     * 
     * @param the_event The event.
     */
    public void keyPressed(final KeyEvent the_event) {
      final int key = the_event.getKeyCode();
      switch (key) {
        case KeyEvent.VK_DOWN:
          my_board.moveDown();
          break;
        case KeyEvent.VK_LEFT:
          my_board.moveLeft();
          break;
        case KeyEvent.VK_RIGHT:
          my_board.moveRight();
          break;
        case KeyEvent.VK_UP:
          my_board.rotate();
          break;
        case KeyEvent.VK_SPACE:
          my_board.hardDrop();
          break;

        default:
      }
      
    }
  }

}
