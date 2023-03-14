package controller.commands;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import model.Pixel;
import model.Image;
import model.ImageCollectionModel;
import model.ImageModel;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * Class to represent the command to load an image from a given file, create an ImageModel, and add
 * it to an ImageCollectionModel.
 */
public class LoadCmd implements ImageProcessingCommand {

  private final String path;
  private final String name;

  /**
   * Constructs a Load object, which requires a pathname to a file and a name for the image/file.
   *
   * @param path the pathname to the file to load from
   * @param name the name to give the image
   */
  public LoadCmd(String path, String name) {
    if (path == null || name == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.path = path;
    this.name = name;
  }

  /**
   * Executes the load command on an ImageCollectionModel. Based on file type, this method calls
   * helper methods in the class to read the file and return an ImageModel. The ImageModel is then
   * added to the ImageCollectionModel using its addImage method.
   *
   * @param model the map of images and their names
   * @throws IllegalArgumentException if the model throws an error at any point or if the file type
   *                                  is not supported
   */
  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    if (path.endsWith(".ppm")) {
      model.addImage(name, this.loadPPM());
    } else {
      model.addImage(name, this.loadImage());
    }
  }

  /**
   * Helper method to read a PPM file and extract an Image object from it. Returns an image to
   * load's execute method, which adds the image to the ImageCollectionModel.
   *
   * @return ImageModel a PPM image represented as an Image object
   * @throws IllegalArgumentException if the file cannot be found
   */
  private ImageModel loadPPM() throws IllegalArgumentException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(path));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found!");
    }

    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    sc.next();
    int cols = sc.nextInt();
    int rows = sc.nextInt();
    int maxValue = sc.nextInt();
    Pixel[][] pixels = new Pixel[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        Pixel pixel = new Pixel(r, g, b, maxValue);
        pixels[i][j] = pixel;
      }
    }
    return new Image(pixels);
  }

  private ImageModel loadImage() throws IllegalArgumentException {
    BufferedImage img;
    try {
      img = ImageIO.read(new File(path));
    } catch (IllegalArgumentException | IOException e) {
      throw new IllegalArgumentException("File reading failed. Please enter a new pathname.");
    }
    if (img == null) {
      throw new IllegalArgumentException("File type not supported.");
    }

    // Convert img to ImageModel
    int rows = img.getHeight();
    int cols = img.getWidth();
    Pixel[][] pixels = new Pixel[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Color c = new Color(img.getRGB(j, i));
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        Pixel pixel = new Pixel(r, g, b, 255);
        pixels[i][j] = pixel;
      }
    }
    return new Image(pixels);
  }
}
