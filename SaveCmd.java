package controller.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import model.ImageCollectionModel;
import model.ImageModel;
import model.ImageTransformations;
import model.ImageTransformationsImpl;

/**
 * Class to represent the command to save an image to a given file.
 */
public class SaveCmd implements ImageProcessingCommand {

  private final String path;
  private final String name;

  /**
   * Constructs a Save object, which requires the pathname to file to save to, and the name of the
   * image to save.
   *
   * @param path the pathname to the file to save to
   * @param name the name of the image to save
   */
  public SaveCmd(String path, String name) {
    if (path == null || name == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.path = path;
    this.name = name;
    new File(path);
  }

  /**
   * Writes an image to a file based on the pathname. Utilizes ImageCollectionModel's method to
   * retrieve an image.
   *
   * @throws IllegalArgumentException if the output file cannot be located
   */
  @Override
  public void execute(ImageCollectionModel model) throws IllegalArgumentException {
    ImageModel img = model.getImage(name);
    if (path.endsWith(".ppm")) {
      saveToPPM(img);
    } else {
      saveImage(img);
    }
  }

  /**
   * Saves an ImageModel object to a PPM file. Writes it to the file in the specific PPM format.
   * Utilizes ImageModel and Pixel's micro-methods to output the entire image in the correct format.
   * Prints each value of each pixel on a new line.
   *
   * @param img the image to save
   * @throws IllegalArgumentException if the file name cannot be found
   */
  private void saveToPPM(ImageModel img) throws IllegalArgumentException {
    FileOutputStream file;
    PrintWriter writer;
    try {
      file = new FileOutputStream(path);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File creation failed.");
    }
    writer = new PrintWriter(file);

    // Print the header of the PPM file
    writer.println("P3");
    writer.println("# Image created by program.");
    writer.println(img.getCols() + " " + img.getRows());
    writer.println(img.getPixel(0, 0).getMaxValue());

    // Print the pixels, one pixel per line.
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        writer.println(img.getPixel(i, j).getRed());
        writer.println(img.getPixel(i, j).getGreen());
        writer.println(img.getPixel(i, j).getBlue());
      }
    }
    writer.close();
  }

  /**
   * Saves an ImageModel object to a file using the BufferedImage class.
   *
   * @param img the image to save
   * @throws IllegalArgumentException if the file writing fails.
   * @throws IllegalArgumentException if a parameter is null.
   */
  private void saveImage(ImageModel img) throws IllegalArgumentException {
    File f = new File(path);
    ImageTransformations transformer = new ImageTransformationsImpl(img);
    BufferedImage image = transformer.toBufferedImg();
    try {
      ImageIO.write(image, getFileExtension(f), f);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("File writing failed.");
    }
    catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid file name.");
    }

  }

  /**
   * Helper method for retrieving the extension of a file name.
   *
   * @param file the file to examine
   * @return String the extension of the file (i.e. jpg, png, etc.)
   */
  private String getFileExtension(File file) {
    String fileName = file.getName();
    int extensionStart = fileName.lastIndexOf(".");
    if (extensionStart == -1) {
      throw new IllegalArgumentException("Pathname has no file extension.");
    }
    return fileName.substring(extensionStart + 1); // "image.png" returns "png"
  }
}
