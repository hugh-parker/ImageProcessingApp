package view;

import java.io.IOException;
import model.ImageCollectionModelState;

/**
 * This class represents a simple view for the ImageProcessing application.
 * Provides methods to render an image and render a message.
 */
public class SimpleImageView implements ImageProcessingView {

  private final ImageCollectionModelState model;
  private final Appendable dest;

  /**
   * Constructor for the view. Takes an ImageCollectionModelState object
   * and throws an error if the model is null. Sets the appendable to the command line
   * output stream.
   * @param model the model from which images can be accessed
   * @throws IllegalArgumentException if the model is null
   */
  public SimpleImageView(ImageCollectionModelState model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null.");
    }
    this.model = model;
    this.dest = System.out;
  }

  /**
   * Alternate constructor for the view. Instead of setting the appendable to System.out, this
   * constructor takes a specific appendable and sends output there.
   * @param model the ImageCollectionModel from which images can be accessed
   * @param dest the appendable to append output to
   */
  public SimpleImageView(ImageCollectionModelState model, Appendable dest) {
    if (model == null || dest == null) {
      throw new IllegalArgumentException("View parameters cannot be null.");
    }
    this.model = model;
    this.dest = dest;
  }

  /**
   * Renders a specific message to the output stream.
   * @param message the message to render
   * @throws IllegalArgumentException if output writing fails
   */
  @Override
  public void renderMessage(String message) throws IllegalArgumentException {
    if (message == null) {
      throw new IllegalArgumentException("Input is null.");
    }
    try {
      dest.append(message + "\n");
    }
    catch (IOException e) {
      throw new IllegalArgumentException("Output appending failed.");
    }
  }
}
