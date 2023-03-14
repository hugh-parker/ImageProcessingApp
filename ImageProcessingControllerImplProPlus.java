package controller;

import java.util.Scanner;
import java.util.function.Function;
import controller.commands.BrightnessCmd;
import controller.commands.ColorTransformationCmd;
import controller.commands.DownsizeCmd;
import controller.commands.FilterCmd;
import controller.commands.Greyscale;
import controller.commands.GreyscaleCmd;
import controller.commands.ImageProcessingCommand;
import controller.commands.MaskedCmd;
import model.ImageCollectionModel;
import view.ImageProcessingView;

/**
 * Pro-plus controller for the ImageProcessing application. Implements the commands that allow
 * the user to mask image editing (i.e. partial image manipulation).
 */
public class ImageProcessingControllerImplProPlus extends ImageProcessingControllerImplPro {

  /**
   * Default constructor.
   */
  public ImageProcessingControllerImplProPlus() {
    super();
    addDownsizeCommand();
    addMaskedGreyscaleCommands();
    addMaskedFilterCommands();
    addMaskedColorCommands();
    addMaskedBrightnessCommands();
  }

  /**
   * Primary constructor. Takes a model, view, and readable.
   * @param model the ImageCollectionModel
   * @param view the ImageProcessingView
   * @param in the input stream
   * @throws IllegalArgumentException if any arguments are null
   */
  public ImageProcessingControllerImplProPlus(ImageCollectionModel model, ImageProcessingView view,
                                              Readable in) throws IllegalArgumentException {
    super(model, view, in);
    addDownsizeCommand();
    addMaskedGreyscaleCommands();
    addMaskedFilterCommands();
    addMaskedColorCommands();
    addMaskedBrightnessCommands();
  }

  /**
   * Adds the command to downsize an image.
   */
  protected void addDownsizeCommand() {
    knownCommands.put("downsize",
            s -> new DownsizeCmd(s.next(), s.next(), s.nextInt(), s.nextInt()));
  }

  /**
   * Adds the masked commands to partially greyscale an image.
   */
  protected void addMaskedGreyscaleCommands() {
    greyscaleCommandHelper("red-component", Greyscale.RED);
    greyscaleCommandHelper("green-component", Greyscale.GREEN);
    greyscaleCommandHelper("blue-component", Greyscale.BLUE);
    greyscaleCommandHelper("value-component", Greyscale.VALUE);
    greyscaleCommandHelper("intensity-component", Greyscale.INTENSITY);
  }

  /**
   * Helper method to build the masking greyscale commands.
   * @param cmdName the name of the command
   * @param component the greyscale component
   */
  private void greyscaleCommandHelper(String cmdName, Greyscale component) {
    Function<Scanner, ImageProcessingCommand> oldCommand = this.knownCommands.get(cmdName);
    knownCommands.put(cmdName,
            s -> model -> {
              Scanner line = new Scanner(s.nextLine());
              String sourceImg = line.next();
              String maskImg = line.next();
              if (!line.hasNext()) {
                oldCommand.apply(new Scanner(sourceImg + " " + maskImg)).
                        execute(model);
                return;
              }
              String newName = line.next();
              model.executeCommand(new MaskedCmd(new GreyscaleCmd(sourceImg, newName, component),
                      sourceImg, newName, maskImg));
              view.renderMessage("Partial " +
                      component.toString().toLowerCase() + "-component was successful");
            });
  }

  /**
   * Adds the masked commands to partially filter an image.
   */
  protected void addMaskedFilterCommands() {
    filterCommandHelper("blur", "Blur");
    filterCommandHelper("sharpen", "Sharpen");
  }

  /**
   * Helper method to build the masking filter commands.
   * @param cmdName the name of the command
   * @param filterName the name of the filter to use
   */
  private void filterCommandHelper(String cmdName, String filterName) {
    Function<Scanner, ImageProcessingCommand> oldCommand = this.knownCommands.get(cmdName);
    knownCommands.put(cmdName,
            s -> model -> {
              Scanner line = new Scanner(s.nextLine());
              String sourceImg = line.next();
              String maskImg = line.next();
              if (!line.hasNext()) {
                oldCommand.apply(new Scanner(sourceImg + " " + maskImg + " " + filterName)).
                        execute(model);
                return;
              }
              String newName = line.next();
              model.executeCommand(new MaskedCmd(new FilterCmd(sourceImg, newName, filterName),
                      sourceImg, newName, maskImg));
              view.renderMessage("Partial " + filterName + " was successful");
            });
  }

  /**
   * Adds the masked commands to partially color-transform an image.
   */
  protected void addMaskedColorCommands() {
    colorCommandHelper("sepia", "Sepia");
    colorCommandHelper("greyscale", "Greyscale");
  }

  /**
   * Helper method to build the masking color-transformation commands.
   * @param cmdName the name of the command
   * @param matrixName the name of the matrix to use
   */
  private void colorCommandHelper(String cmdName, String matrixName) {
    Function<Scanner, ImageProcessingCommand> oldCommand = this.knownCommands.get(cmdName);
    knownCommands.put(cmdName,
            s -> model -> {
              Scanner line = new Scanner(s.nextLine());
              String sourceImg = line.next();
              String maskImg = line.next();
              if (!line.hasNext()) {
                oldCommand.apply(new Scanner(sourceImg + " " + maskImg + " " + matrixName)).
                        execute(model);
                return;
              }
              String newName = line.next();
              model.executeCommand(
                      new MaskedCmd(new ColorTransformationCmd(sourceImg, newName, matrixName),
                      sourceImg, newName, maskImg));
              view.renderMessage("Partial " + matrixName + " was successful");
            });
  }


  /**
   * Adds the masked commands to partially brighten/darken an image.
   */
  protected void addMaskedBrightnessCommands() {

    Function<Scanner, ImageProcessingCommand> brighten = this.knownCommands.get("brighten");
    knownCommands.put("brighten",
            s -> model -> {
              String sourceImg = s.next();
              String maskImg = s.next();
              if (s.hasNextInt()) {
                int inc = s.nextInt();
                brighten.apply(new Scanner(sourceImg + " " + maskImg + " " + inc)).
                        execute(model);
                return;
              }
              String newImage = s.next();
              model.executeCommand(new MaskedCmd(new BrightnessCmd(sourceImg, newImage, s.nextInt()),
                      sourceImg, newImage, maskImg));
              view.renderMessage("Partial brighten was successful");
            });

    knownCommands.put("darken",
            s -> model -> {
              String sourceImg = s.next();
              String maskImg = s.next();
              if (s.hasNextInt()) {
                int inc = s.nextInt();
                brighten.apply(new Scanner(sourceImg + " " + maskImg + " " + -inc)).
                        execute(model);
                return;
              }
              String newImage = s.next();
              model.executeCommand(new MaskedCmd(new BrightnessCmd(sourceImg, newImage, -s.nextInt()),
                      sourceImg, newImage, maskImg));
              view.renderMessage("Partial brighten was successful");
            });
  }


}
