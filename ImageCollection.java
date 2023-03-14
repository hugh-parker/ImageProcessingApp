package model;

import java.util.HashMap;
import java.util.Map;

import controller.commands.ImageProcessingCommand;

/**
 * This class stores a map of images and their names and provides functionality for adding images,
 * retrieving images, and executing commands on images.
 */
public class ImageCollection implements ImageCollectionModel {

  private final Map<String, ImageModel> images;

  /**
   * Constructs the hashmap of images and sets image count to 0.
   */
  public ImageCollection() {
    images = new HashMap<>();
  }

  @Override
  public void executeCommand(ImageProcessingCommand cmd) throws IllegalArgumentException {
    cmd.execute(this);
  }

  @Override
  public ImageModel getImage(String name) throws IllegalArgumentException {
    if (!this.images.containsKey(name)) {
      throw new IllegalArgumentException(
              name + " image not found. Please load an image or check that " +
              "the image name is correct.");
    }
    return this.images.get(name).getCopy();
  }

  @Override
  public void addImage(String name, ImageModel img) throws IllegalArgumentException {
    if (img == null) {
      throw new IllegalArgumentException("Cannot add a null image.");
    }
    this.images.put(name, img);
  }

  @Override
  public int getNumImages() {
    return images.size();
  }

}