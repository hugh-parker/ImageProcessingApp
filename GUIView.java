package view;

import java.awt.image.BufferedImage;
import controller.Features;

/**
 * Represents the functionality of an ImageProcessing GUI view. Has a method that accepts a Features
 * object, allowing the view to delegate to this object when necessary. Has methods to display an
 * image, reset buttons, and render messages.
 */
public interface GUIView extends ImageProcessingView {

  /**
   * A method that adds the Action Listeners to our GUI for each button in the GUI.
   * @param f the features of the program.
   */
  void addFeatures(Features f);

  /**
   * Displays an image in this view. If the provided image name is not null and not in the list, add
   * it to the list of image names. Sets the image being displayed to the selected image.
   *
   * @param img the BufferedImage to display to the user
   */
  void displayImage(BufferedImage img, int[][] histValues);

  /**
   * Resets the previous brightness slider value to 0 and the current brightness slider to 0.
   * Resets the radio button selections to false.
   */
  void resetSelections();

  /**
   * Renders an error message in a popup box for the user to read.
   * @param message the message to render
   */
  void renderMessage(String message);

  /**
   * Renders a welcome message in a popup box for the user to read.
   * @param message the message to render
   */
  void renderWelcome(String message);
}
