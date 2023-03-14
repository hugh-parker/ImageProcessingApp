package controller;

import controller.commands.ColorTransformationCmd;
import controller.commands.FilterCmd;
import controller.commands.FlipCmd;
import model.ImageCollectionModel;
import view.ImageProcessingView;

/**
 * An enhanced version of the ImageProcessingControllerImpl. Inherits all the functionality of
 * ImageProcessingControllerImpl, but adds new supported commands.
 */
public class ImageProcessingControllerImplPro extends ImageProcessingControllerImpl {

  /**
   * Constructor for the pro controller. Adds the transformation and filter commands.
   */
  public ImageProcessingControllerImplPro() {
    super();
    addTransformCommands();
    addFilterCommands();
  }

  /**
   * Constructor for the controller. Builds the map of known commands.
   *
   * @param model the model used to load, edit, and save images
   * @param view  the view used to present images
   * @param in    the stream of input
   * @throws IllegalArgumentException if either the model, view, or readable is null
   */
  public ImageProcessingControllerImplPro(ImageCollectionModel model, ImageProcessingView view,
                                          Readable in) throws IllegalArgumentException {
    super(model, view, in);
    addTransformCommands();
    addFilterCommands();
  }

  private void addTransformCommands() {
    knownCommands.put("sepia",
            s -> model -> {
              model.executeCommand(
                      new ColorTransformationCmd(s.next(), s.next(), "Sepia"));
              view.renderMessage("Sepia was successful");
            });
    knownCommands.put("greyscale",
            s -> model -> {
              model.executeCommand(
                      new ColorTransformationCmd(s.next(), s.next(), "Greyscale"));
              view.renderMessage("Greyscale was successful");
            });
  }

  private void addFilterCommands() {
    knownCommands.put("blur",
            s -> model -> {
              model.executeCommand(
                      new FilterCmd(s.next(), s.next(), "Blur"));
              view.renderMessage("Blur was successful");
            });
    knownCommands.put("sharpen",
            s -> model -> {
              model.executeCommand(
                      new FilterCmd(s.next(), s.next(), "Sharpen"));
              view.renderMessage("Sharpen was successful");
            });
  }

  /**
   * Renders the welcoming message to the user. Displays the list of supported commands.
   */
  @Override
  protected void printMenu() {
    super.printMenu();
    this.view.renderMessage("'Blur: Enter an image name and a new name\n" +
            "'Sharpen': Enter an image name and a new name\n" +
            "'Greyscale': Enter an image name and a new name\n" +
            "'Sepia': Enter an image name and a new name");
  }
}
