package model;

import controller.commands.ImageProcessingCommand;

/**
 * Interface for storing a collection of images and allowing commands objects to alter
 * those images and add new ones. Extends ImageCollectionModelState, which provides
 * the method for retrieving images from the model and getting the number of images in the model.
 *
 */
public interface ImageCollectionModel extends ImageCollectionModelState {

  /**
   * Executes an image processing command on this model.
   *
   *
   * @param cmd the command to execute
   */
  void executeCommand(ImageProcessingCommand cmd);

  /**
   * A method to add an image to this model's collection of images.
   *
   *
   * @param imageName the name of the image to add
   * @param img the image
   * @throws IllegalArgumentException if the image is null
   */
  void addImage(String imageName, ImageModel img) throws IllegalArgumentException;
}
