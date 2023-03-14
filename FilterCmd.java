package controller.commands;

import java.util.HashMap;
import java.util.Map;
import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Represents the command to filter an image. Requires an enum representing what kind of filter
 * to use. Includes a helper method to create the filter based on the type of filter passed
 * to the command.
 */
public class FilterCmd implements ImageProcessingCommand {

  private final String newName;
  private final String orig;
  private final double[][] filter;
  private final Map<String, double[][]> knownFilters;

  /**
   * Constructor for the command to filter an image.
   *
   * @param orig       the name of the original image to filter
   * @param newName    the name of the new, filtered image
   * @param filterName the name of the filter to apply to the image
   */
  public FilterCmd(String orig, String newName, String filterName) {
    if (orig == null || newName == null || filterName == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.orig = orig;
    this.newName = newName;
    knownFilters = new HashMap<>();
    addFilters();

    filter = knownFilters.getOrDefault(filterName, null);
    if (filter == null) {
      throw new IllegalArgumentException("Filter is null.");
    }
  }

  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageTransformations transformer = new ImageTransformationsImpl(model.getImage(orig));
    model.addImage(newName, transformer.filter(filter));
  }

  /**
   * Adds filters to the command's map of known filters.
   */
  private void addFilters() {
    knownFilters.put("Sharpen", new double[][]{
            {-.125, -.125, -.125, -.125, -.125},
            {-.125, .25, 0.25, .25, -.125},
            {-.125, .25, 1.0, .25, -.125},
            {-.125, .25, .25, .25, -.125},
            {-.125, -.125, -.125, -.125, -.125}});

    knownFilters.put("Blur", new double[][]{
            {.0625, .125, .0625},
            {.125, .25, .125},
            {.0625, .125, .0625}});
  }
}
