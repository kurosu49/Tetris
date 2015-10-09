/*
 * TCSS305 - Spring 2013
 * Project Tetris
 * 
 */

package view;


/**
 * Class that runs tetris.
 * 
 * @author Crystal Miraflor
 * @version 1.00
 *
 */
public final class TetrisMain {

  /**
   * Private constructor to override the default
   * constructor given.
   */
  private TetrisMain() {
    //does nothing.
  }
  
  /**
   * Main method.
   * 
   * @param the_args The command line arguments.
   */
  public static void main(final String[] the_args) {
    final TetrisGUI gui = new TetrisGUI();
    gui.start();

  }

}
