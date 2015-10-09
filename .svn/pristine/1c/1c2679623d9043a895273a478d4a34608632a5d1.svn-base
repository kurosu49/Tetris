/*
 * TCSS305 - Spring 2013
 * Project Tetris
 * 
 */

package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Block;
import model.Board;

/**
 * The statics display for the Tetris game.
 * 
 * @author Crystal Miraflor (mirafcry@uw.edu)
 * @version 1.00
 *
 */
@SuppressWarnings("serial")
public class TetrisStats extends JPanel implements Observer {
  
  /**
   * The width.
   */
  private static final int WIDTH = 200;
  
  /**
   * The height.
   */
  private static final int HEIGHT = 200;
  
  /**
   * The number lines until the next level.
   */
  private static final int NEXT_LEVEL = 10;
  
  /**
   * Score multiplier.
   */
  private static final int MULTIPLIER = 40;
  
  /**
   * List used for comparison against the 
   * list of frozen blocks.
   */
  private static final List<Block[]> LIST = new ArrayList<Block[]>();
  
  /**
   * Starting level.
   */
  private static final int START = 1;
  
  /**
   * The tetris board being used.
   */
  private final Board my_board;
  
  /**
   * The JLabel for the score display.
   */
  private JLabel my_score;
  
  /**
   * The JLabel for the level display.
   */
  private JLabel my_level;
  
  /**
   * The JLabel for the total lines 
   * cleared display.
   */
  private JLabel my_lines_cleared;
  
  /**
   * The current score.
   */
  private int my_score_num;
  
  /**
   * The current level.
   */
  private int my_level_num;
  
  /**
   * The current number of lines
   * cleared.
   */
  private int my_lines_num;
  
  /**
   * Number of frozen lines.
   */
  private int my_frozen_lines;
  
  /**
   * The constructor for the statistics panel
   * of tetris.
   * 
   * @param the_board The board.
   */
  public TetrisStats(final Board the_board) {
    super();
    my_board = the_board;
    my_frozen_lines = my_board.getFrozenBlocks().size();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    my_score_num = 0;
    my_level_num = START;
    my_lines_num = 0;

    addStatstoPanel();
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    
  }
  
  /**
   * Adds all labels onto the panel.
   */
  private void addStatstoPanel() {
    my_score = new JLabel();
    my_level = new JLabel();
    my_lines_cleared = new JLabel();
    renewPanel();
    this.add(my_score);
    this.add(my_level);
    this.add(my_lines_cleared);
    
  }
  
  /**
   * Renews the panels to the current
   * statistics.
   */
  private void renewPanel() {
    my_score.setText("Score: " + my_score_num);
    my_level.setText("Level: " + my_level_num);
    my_lines_cleared.setText("Total Lines Cleared: " + my_lines_num); 
  }
  
  /**
   * Resets the panel.
   */
  public void resetPanel() {
    my_score_num = 0;
    my_level_num = START;
    my_lines_num = 0;
    renewPanel();
  }
  
  /**
   * Obtains the current level of 
   * the tetris game.
   * 
   * @return the level.
   */
  public int getLevel() {
    return my_level_num;
  }

  /**
   * Updates the statistics panel for Tetris.
   * 
   * @param the_arg The argument.
   * @param the_object  The object.
   */
  @Override
  public void update(final Observable the_arg, final Object the_object) {
    final List<Block[]> compare = my_board.getFrozenBlocks();
    if (my_frozen_lines > my_board.getFrozenBlocks().size()
        && !compare.equals(LIST)) {
      my_lines_num += my_frozen_lines - my_board.getFrozenBlocks().size();
    }
    if (my_lines_num >= NEXT_LEVEL) {
      my_level_num = (my_lines_num / NEXT_LEVEL) + START;
    }
    my_score_num = my_lines_num * MULTIPLIER * my_level_num;
    my_frozen_lines = my_board.getFrozenBlocks().size();
    renewPanel();
  }

}
