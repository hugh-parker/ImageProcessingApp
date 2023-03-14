package view;

import java.io.IOException;

/**
 * Represents the functionality of a view for image processing. A view should be able
 * to render images and messages.
 */
public interface ImageProcessingView {

  /**
   * Renders a message.
   * @param message the message to render
   * @throws IOException if view fails to render message
   */
  void renderMessage(String message) throws IllegalArgumentException;

}
