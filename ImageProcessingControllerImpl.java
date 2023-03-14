package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import controller.commands.BrightnessCmd;
import controller.commands.FilterCmd;
import controller.commands.FlipCmd;
import controller.commands.Greyscale;
import controller.commands.GreyscaleCmd;
import controller.commands.ImageProcessingCommand;
import controller.commands.LoadCmd;
import controller.commands.MaskedCmd;
import controller.commands.SaveCmd;
import model.ImageCollection;
import model.ImageCollectionModel;
import view.ImageProcessingView;
import view.SimpleImageView;

/**
 * Controller for the ImageProcessingModel. Contains a map of supported commands for
 * loading, editing, and saving images. Using an input stream, the controller runs inputs
 * against the map of commands, calling the functional object to create the command whenever a
 * valid command-input is entered. The command object is then sent to an ImageCollectionModel,
 * which has the capability of executing the command itself.
 * The controller also takes a view to which it sends feedback (welcome messages, error messages,
 * successful command messages, quit messages, etc.)
 */
public class ImageProcessingControllerImpl implements ImageProcessingController {

  protected final Map<String, Function<Scanner, ImageProcessingCommand>> knownCommands;
  private final ImageCollectionModel model;
  protected final ImageProcessingView view;
  private final Readable in;


  /**
   * Default constructor for the controller. Sets the model and view to the most basic known
   * implementations.
   * Adds the necessary commands.
   */
  public ImageProcessingControllerImpl() {
    this.model = new ImageCollection();
    this.view = new SimpleImageView(model);
    this.in = new BufferedReader(new InputStreamReader(System.in));
    knownCommands = new HashMap<>();
    addLoadCommands();
    addSaveCommands();
    addFlipCommands();
    addBrightenCommands();
    addColorTransformCommands();
  }

  /**
   * Constructor for the controller. Builds the map of known commands.
   *
   * @param model the model used to load, edit, and save images
   * @param view  the view used to present images
   * @param in    the stream of input
   * @throws IllegalArgumentException if either the model, view, or readable is null
   */
  public ImageProcessingControllerImpl(ImageCollectionModel model, ImageProcessingView view,
                                       Readable in) throws IllegalArgumentException {
    if (model == null || view == null || in == null) {
      throw new IllegalArgumentException("Controller parameters cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.in = in;
    knownCommands = new HashMap<>();
    addLoadCommands();
    addSaveCommands();
    addFlipCommands();
    addBrightenCommands();
    addColorTransformCommands();
  }

  /**
   * Reads the input stream and sends output messages to the view.
   */
  @Override
  public void use() {
    renderWelcomeMessage();
    Scanner s = new Scanner(in);
    String in;
    ImageProcessingCommand c;

    while (s.hasNext()) {
      in = s.next();
      if (in.equalsIgnoreCase("menu")) {
        printMenu();
        continue;
      }
      if (in.equalsIgnoreCase("quit")) {
        view.renderMessage("Image Processor quit!");
        return;
      }
      in = in.toLowerCase();
      Function<Scanner, ImageProcessingCommand> cmd =
              knownCommands.getOrDefault(in, null);
      if (cmd == null) {
        commandNotFound(in);
      }
      else {
        try {
          c = cmd.apply(s);
          // If the command object is successfully built, move on to execution.
          execute(c);
        } catch (InputMismatchException e) {
          s.next();
          view.renderMessage("An input you entered is not valid. Please re-enter your command.");
        }
      }
    }
  }


  /**
   * Renders the welcoming message to the user. Displays the list of supported commands.w
   */
  protected void renderWelcomeMessage() {
    view.renderMessage("Welcome to the image processing application.\n"
            + "Enter 'menu' to see the list of supported commands.");
  }

  protected void printMenu() {
    view.renderMessage("Supported Commands:\n"
            + "'Quit': Exits the application\n"
            + "'Load': Enter a pathname and a name for the image\n"
            + "'Save': Enter a pathname and the name of the image to save\n"
            + "'Brighten': Enter an image name, a new name, and an increment\n"
            + "'Darken': Enter an image name, a new name, and an increment\n"
            + "'Flip-horizontal': Enter an image name and a new name\n"
            + "'Flip-vertical': Enter an image name and a new name\n"
            + "'X-component': Enter an image name and a new name\n"
            + "Supported components: red, blue, green, value, intensity");
  }


  /**
   * Adds supported "load" commands to the command map.
   */
  private void addLoadCommands() {
    knownCommands.put("load",
            s -> model -> {
              model.executeCommand(new LoadCmd(s.next(), s.next()));
              view.renderMessage("Load was successful");
            });
  }

  /**
   * Adds supported "save" commands to the command map.
   */
  private void addSaveCommands() {
    knownCommands.put("save",
            s -> model -> {
              model.executeCommand(new SaveCmd(s.next(), s.next()));
              view.renderMessage("Save was successful");
            });
  }

  /**
   * Adds a variety of supported "flip" commands to the command map.
   */
  private void addFlipCommands() {
    knownCommands.put("flip-horizontal",
            s -> model -> {
              model.executeCommand(new FlipCmd(s.next(), s.next(), false));
              view.renderMessage("Flip-horizontal was successful");
            });
    knownCommands.put("flip-vertical",
            s -> model -> {
              model.executeCommand(new FlipCmd(s.next(), s.next(), true));
              view.renderMessage("Flip-vertical was successful");
            });
  }

  /**
   * Adds a variety of supported "brighten/darken" commands to the command map.
   */
  private void addBrightenCommands() {
    knownCommands.put("brighten",
            s -> model -> {
              model.executeCommand(new BrightnessCmd(s.next(), s.next(), s.nextInt()));
              view.renderMessage("Brighten was successful");
            });
    knownCommands.put("darken",
            s -> model -> {
              model.executeCommand(new BrightnessCmd(s.next(), s.next(), -s.nextInt()));
              view.renderMessage("Darken was successful");
            });
  }

  /**
   * Adds a variety of supported "component" commands to the command map.
   */
  private void addColorTransformCommands() {
    knownCommands.put("red-component",
            s -> model -> {
              model.executeCommand(new GreyscaleCmd(s.next(), s.next(), Greyscale.RED));
              view.renderMessage("Red-component was successful");
            });
    knownCommands.put("green-component",
            s -> model -> {
              model.executeCommand(new GreyscaleCmd(s.next(), s.next(), Greyscale.GREEN));
              view.renderMessage("Green-component was successful");
            });
    knownCommands.put("blue-component",
            s -> model -> {
              model.executeCommand(new GreyscaleCmd(s.next(), s.next(), Greyscale.BLUE));
              view.renderMessage("Blue-component was successful");
            });
    knownCommands.put("value-component",
            s -> model -> {
              model.executeCommand(new GreyscaleCmd(s.next(), s.next(), Greyscale.VALUE));
              view.renderMessage("Value-component was successful");
            });
    knownCommands.put("intensity-component",
            s -> model -> {
              model.executeCommand(new GreyscaleCmd(s.next(), s.next(), Greyscale.INTENSITY));
              view.renderMessage("Intensity-component was successful");
            });
  }

  /**
   * Renders a command not found message to the view and relists the supported commands.
   */
  private void commandNotFound(String in) {
    view.renderMessage("Command '" + in + "' not found!\nEnter 'menu' to see the list" +
            " of supported commands.");
  }

  /**
   * Executes the command read in use() and catches and renders any exceptions.
   *
   * @param c  the command object
   */
  private void execute(ImageProcessingCommand c) {
    try {
      c.execute(model);
    } catch (IllegalArgumentException e) {
      view.renderMessage(e.getMessage());
    }
  }
}
