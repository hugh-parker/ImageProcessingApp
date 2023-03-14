package controller.commands;

import java.util.HashMap;
import java.util.Map;
import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Represents the command to color-transform an image. Has a map of known matrices
 * that can be applied to ImageModels.
 */
public class ColorTransformationCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final Map<String, double[][]> knownMatrices;
  private final double[][] matrix;

  /**
   * Constructor for the ColorTransformation command. Takes a name of an image, a new name for the
   * image, and a name of a matrix to apply to the image.
   * @param orig the name of the original image
   * @param newName the name of the new transformed image
   * @param matrixName the name of the matrix to apply
   */
  public ColorTransformationCmd(String orig, String newName, String matrixName) {
    this.orig = orig;
    this.newName = newName;
    knownMatrices = new HashMap<>();
    addMatrices();
    matrix = knownMatrices.getOrDefault(matrixName, null);
    if (matrix == null) {
      throw new IllegalArgumentException("Matrix not found.");
    }
  }


  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.transformColor(matrix));
  }

  /**
   * Adds the supported matrices to the class' map of known matrices.
   */
  private void addMatrices() {
    knownMatrices.put("Greyscale", new double[][]{
            {.2126, .7152, .0722},
            {.2126, .7152, .0722},
            {.2126, .7152, .0722}});
    knownMatrices.put("Sepia", new double[][]{
            {.393, .769, .189},
            {.349, .686, .168},
            {.272, .534, .131}});
  }
}
