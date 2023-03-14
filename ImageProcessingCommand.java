package controller.commands;

import model.ImageCollectionModel;

/**
 * Functional Interface for image processing commands. Provides one method, execute(), which
 * executes the specific command on the model given.
 */
public interface ImageProcessingCommand {

  /**
   * Executes the command on an ImageProcessingModel object.
   * @param model the model to execute the command on
   */
  void execute(ImageCollectionModel model) throws IllegalArgumentException;
}
