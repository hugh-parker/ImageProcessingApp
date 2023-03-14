package controller;

import java.util.HashMap;
import java.util.Map;

import controller.commands.BrightnessCmd;
import controller.commands.ColorTransformationCmd;
import controller.commands.DownsizeCmd;
import controller.commands.FilterCmd;
import controller.commands.FlipCmd;
import controller.commands.Greyscale;
import controller.commands.GreyscaleCmd;
import controller.commands.ImageProcessingCommand;
import controller.commands.LoadCmd;
import controller.commands.SaveCmd;
import model.ImageCollectionModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;
import view.GUIView;

/**
 * An asynchronous controller to be used for running the GUI for the Image Processor.
 */
public class ImageProcessingGUIController implements Features, ImageProcessingController {

  private final ImageCollectionModel model;
  private boolean imageLoaded;
  private final Map<String, ImageProcessingCommand> commands;
  private final GUIView view;
  private ImageTransformations transformer;
  private String imageName;

  /**
   * A constructor that sets up the GUI controller, before anything has been loaded.
   *
   * @param model the model to be used when running the program.
   */
  public ImageProcessingGUIController(ImageCollectionModel model, GUIView view) {
    this.model = model;
    imageLoaded = false;
    commands = new HashMap<>();
    this.view = view;
  }

  /**
   * Adds this class to the view's features, allowing the view to be used to initiate high-level
   * commands.
   */
  @Override
  public void use() {
    view.addFeatures(this);
    view.renderWelcome("Welcome to the image processor! Upload an image to get started. \n You can"
            + " use the buttons on the left to edit your image, or click 'Reset' to go back to "
            + "your original image. \n The middle panel will display your edits, and the right "
            + "panel displays a histogram of values for your image. \n Click 'Save Image to Files' "
            + "to save your edited image!");
  }

  @Override
  public void loadImage(String path, String name) {
    try {
      model.executeCommand(new LoadCmd(path, name));
      // add a copy under the name "copy" to allow for reset
      model.addImage("copy", model.getImage(name));
      imageLoaded = true;
      imageName = name;
      addCommands();
      view.resetSelections(); // resets view's buttons in case this isn't the first loaded image
      update();
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  @Override
  public void saveImage(String path) {
    try {
      model.executeCommand(new SaveCmd(path, imageName));
      // no call to view required here, as saving an image doesn't remove the image from the view
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  @Override
  public void runCommand(String commandName) {
    try {
      if (imageLoaded) {
        ImageProcessingCommand cmd = commands.getOrDefault(commandName, null);
        if (cmd != null) {
          model.executeCommand(cmd);
          update();
        }
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  @Override
  public void toggleBrightness(int increment) {
    try {
      if (imageLoaded) {
        model.executeCommand(new BrightnessCmd(imageName, imageName, increment));
        update();
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  public void downsize(int width, int height) {
    try {
      if (imageLoaded) {
        model.executeCommand(new DownsizeCmd(imageName, imageName, width, height));
        update();
      }
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  @Override
  public void update() {
    try {
      transformer = new ImageTransformationsImpl(model.getImage(imageName));
      view.displayImage(transformer.toBufferedImg(), transformer.createHistogramData());
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  @Override
  public void reset() {
    try {
      transformer = new ImageTransformationsImpl(model.getImage("copy"));
      view.displayImage(transformer.toBufferedImg(), transformer.createHistogramData());
      // Reset model's collection to 2 copies of the same image by overwriting original w/ the
      // copy
      model.addImage(imageName, model.getImage("copy"));
      view.resetSelections();
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }

  /**
   * Adds the possible commands to the list of known commands.
   */
  private void addCommands() {
    commands.put("Blur", new FilterCmd(imageName, imageName, "Blur"));
    commands.put("Sharpen", new FilterCmd(imageName, imageName, "Sharpen"));
    commands.put("Flip-Vertical", new FlipCmd(imageName, imageName, true));
    commands.put("Flip-Horizontal", new FlipCmd(imageName, imageName, false));
    commands.put("Sepia", new ColorTransformationCmd(imageName, imageName, "Sepia"));
    commands.put("Luma-Component",
            new ColorTransformationCmd(imageName, imageName, "Greyscale"));
    commands.put("Red-Component",
            new GreyscaleCmd(imageName, imageName, Greyscale.RED));
    commands.put("Green-Component",
            new GreyscaleCmd(imageName, imageName, Greyscale.GREEN));
    commands.put("Blue-Component",
            new GreyscaleCmd(imageName, imageName, Greyscale.BLUE));
    commands.put("Value-Component",
            new GreyscaleCmd(imageName, imageName, Greyscale.VALUE));
    commands.put("Intensity-Component",
            new GreyscaleCmd(imageName, imageName, Greyscale.INTENSITY));
  }
}
