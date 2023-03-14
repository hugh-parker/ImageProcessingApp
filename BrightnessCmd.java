package controller.commands;

import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Class to represent the command to brighten an image by a certain increment and add it to
 * the ImageCollectionModel.
 */
public class BrightnessCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final int increment;

  /**
   * Constructs a Brighten object, which requires a name of the original image to brighten,
   * a name for the brightened image, and an amount by which to brighten the original.
   *
   *
   * @param newName the name of the new brightened image
   * @param orig the name of the image to brighten
   * @param increment the amount by which to brighten the image
   */
  public BrightnessCmd(String orig, String newName, int increment) {
    if (orig == null || newName == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.newName = newName;
    this.orig = orig;
    this.increment = increment;
  }

  /**
   * Brightens/darkens an Image in an ImageCollection and adds it
   * under its new name using ImageCollectionModel's addImage().
   *
   *
   * @param model the ImageCollectionModel to execute the command on
   * @throws IllegalArgumentException if the model runs into an error at any point
   */
  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.brighten(increment));
  }
}
