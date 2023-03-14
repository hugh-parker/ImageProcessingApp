package controller.commands;

import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Class to represent the command to greyscale an image to a given component and add it to
 * the ImageCollectionModel.
 */
public class GreyscaleCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final Greyscale component;

  /**
   * Constructs a Greyscale object, which requires the name of the image to greyscale, the name of
   * the new, greyscaled image, and a certain component by which to greyscale the image.
   *
   * @param newName   the name of the new flipped image
   * @param orig      the name of the image to flip
   * @param component the component used to greyscale the image (check Enum Greyscale)
   */
  public GreyscaleCmd(String orig, String newName, Greyscale component) {
    if (orig == null || newName == null || component == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.newName = newName;
    this.orig = orig;
    this.component = component;
  }

  /**
   * Adds a greyscale image to the ImageCollection model under the new name. Utilizes
   * ImageCollectionModel's micro-methods to retrieve and submit images.
   *
   * @param model the model to execute the command on
   * @throws IllegalArgumentException if at any point the model's methods encounter an error
   */
  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.greyscale(component));
  }
}
