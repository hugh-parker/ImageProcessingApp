package controller.commands;

import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

public class DownsizeCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final int newWidth;
  private final int newHeight;

  public DownsizeCmd(String orig, String newName, int newWidth, int newHeight) {
    if (orig == null || newName == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }

    //HANDLE GREATER THAN IMAGE IN EXECUTE
    if (newWidth < 1 || newHeight < 1) {
      throw new IllegalArgumentException("Dimensions must be greater than 0.");
    }
    this.orig = orig;
    this.newName = newName;
    this.newWidth = newWidth;
    this.newHeight = newHeight;
  }

  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.downsize(newWidth, newHeight));
  }
}
