package controller.commands;

import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Class to represent the command to flip an image either horizontally or vertically and add it to
 * the ImageCollectionModel.
 */
public class FlipCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final boolean vertical;

  /**
   * Constructs a Flip object.
   * @param newName the name of the new flipped image
   * @param orig the name of the image to flip
   * @param vertical true if flipping vertically, false if horizontally
   */
  public FlipCmd(String orig, String newName, boolean vertical) {
    if (orig == null || newName == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.newName = newName;
    this.orig = orig;
    this.vertical = vertical;
  }

  /**
   * Method to execute the command to flip an Image in an ImageCollection and add it
   * under its new name.
   *
   *
   * @param model the model to execute the command on
   * @throws IllegalArgumentException if the model throws an exception at any point in execution.
   */
  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.flip(vertical));
  }
}
