package controller;

/**
 * Outlines the higher-level features that the GUI-based ImageProcessing app offers.
 */
public interface Features {

  /**
   * Loads an image from a given pathname.
   * @param path the pathname to the image file.
   * @param name the name of the image.
   */
  void loadImage(String path, String name);

  /**
   * Saves an image to a given pathname.
   * @param path the pathname to the image file.
   */
  void saveImage(String path);

  /**
   * Runs a command on an image given the name of the command.
   * @param commandName the name of the command
   */
  void runCommand(String commandName);

  /**
   * Toggles the brightness of an image.
   * @param increment the amount by which to brighten an image
   */
  void toggleBrightness(int increment);

  /**
   * Downsizes an image
   * @param width the width to downsize to
   * @param height the height to downsize to
   */
  void downsize(int width, int height);

  /**
   * Updates the image to the most current version of the image.
   */
  void update();

  /**
   * Resets the image to the original, unedited version.
   */
  void reset();

}

