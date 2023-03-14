package controller.commands;

import model.ImageCollectionModel;
import model.ImageModel;
import model.Pixel;

/**
 * Command class which represents the ability to partially manipulate images.
 */
public class MaskedCmd implements ImageProcessingCommand {

  private final ImageProcessingCommand cmd;
  private final String original;
  private final String newName;
  private final String maskImage;

  /**
   * Constructor for a masked command object. Takes an ImageProcessingCommand, as well
   * as the name of the image to partially edit and the name for the new image.
   * @param cmd the command to execute (partially)
   * @param original the original image
   * @param newName the new, partially edited, image
   */
  public MaskedCmd(ImageProcessingCommand cmd, String original, String newName, String maskImage) {
    this.cmd = cmd;
    this.original = original;
    this.newName = newName;
    this.maskImage = maskImage;
  }

  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {

    // Retrieve the original image
    ImageModel originalImage = model.getImage(original);

    // Execute the command which fully edits the image
    cmd.execute(model);

    // Retrieve the fully edited image
    ImageModel editedImage = model.getImage(newName);

    // Create the "mask-image" from the original
    ImageModel mask = model.getImage(maskImage);

    if (mask.getCols() != editedImage.getCols() || mask.getRows() != editedImage.getRows()) {
      throw new IllegalArgumentException("Image must be the same size as its mask counterpart.");
    }

    for (int i = 0; i < mask.getRows(); i++) {
      for (int j = 0; j < mask.getCols(); j++) {
        // if the pixel in the mask image is NOT black
        if (!mask.getPixel(i,j).equals(new Pixel(0,0,0, 255))) {
          // reset the pixel in the edited image to the pixel in the original
          editedImage.setPixel(i, j, originalImage.getPixel(i, j));
        }
      }
    }
    // overwrite the fully edited image with the partially edited version
    model.addImage(newName, editedImage);
  }
}
