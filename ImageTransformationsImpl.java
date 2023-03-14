package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import controller.commands.Greyscale;

/**
 * Implements the methods outlined by the ImageTransformation interface. Provides implementation
 * for filtering, flipping, brightening/darkening, and transforming the color of ImageModels and
 * returning the new images.
 */
public class ImageTransformationsImpl implements ImageTransformations {

  private final ImageModel img;

  /**
   * Constructor for the ImageTransformationsImpl. Takes an ImageModel and sets this
   * class' field to the image.
   *
   * @param image the image to be transformed
   */
  public ImageTransformationsImpl(ImageModel image) {
    this.img = image;
  }

  @Override
  public ImageModel filter(double[][] filter) {
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        double r = 0;
        double g = 0;
        double b = 0;
        int filterSize = filter.length;
        int filterSize2 = filter[0].length;
        Pixel p = img.getPixel(i, j);
        for (int x = 0; x < filterSize; x++) {
          for (int y = 0; y < filterSize2; y++) {
            try {
              r += img.getPixel(i - (filterSize / 2 - x),
                      j - (filterSize2 / 2 - y)).getRed() * filter[x][y];
              g += img.getPixel(i - (filterSize / 2 - x),
                      j - (filterSize2 / 2 - y)).getGreen() * filter[x][y];
              b += img.getPixel(i - (filterSize / 2 - x),
                      j - (filterSize2 / 2 - y)).getBlue() * filter[x][y];
            } catch (IndexOutOfBoundsException | IllegalArgumentException ignored) {
              // We're expecting these exceptions, when encountered do nothing to the variables.
            }
          }
        }
        img.setPixel(i, j, new Pixel(p.constrain((int) r), p.constrain((int) g),
                p.constrain((int) b), 255));
      }
    }
    return img;
  }

  @Override
  public ImageModel flip(boolean vertical) {
    int rows = img.getRows();
    int cols = img.getCols();
    if (vertical) {
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols / 2; j++) {
          Pixel temp = img.getPixel(i, j);
          img.setPixel(i, j, img.getPixel(i, cols - 1 - j));
          img.setPixel(i, cols - 1 - j, temp);
        }
      }
    } else {
      for (int i = 0; i < rows / 2; i++) {
        for (int j = 0; j < cols; j++) {
          Pixel temp = img.getPixel(i, j);
          img.setPixel(i, j, img.getPixel(rows - 1 - i, j));
          img.setPixel(rows - 1 - i, j, temp);
        }
      }
    }
    return img;
  }

  @Override
  public ImageModel brighten(int increment) {
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        Pixel p = img.getPixel(i, j);
        p.add(increment);
        img.setPixel(i, j, p);
      }
    }
    return img;
  }

  @Override
  public BufferedImage toBufferedImg() {
    BufferedImage image =
            new BufferedImage(img.getCols(), img.getRows(), BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        Color c = new Color(
                img.getPixel(i, j).getRed(),
                img.getPixel(i, j).getGreen(),
                img.getPixel(i, j).getBlue());
        image.setRGB(j, i, c.getRGB());
      }
    }
    return image;
  }

  @Override
  public ImageModel greyscale(Greyscale component) {
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        Pixel p = img.getPixel(i, j);
        img.setPixel(i, j, greyscaleHelper(p, component));
      }
    }
    return img;
  }

  @Override
  public ImageModel transformColor(double[][] matrix) {
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        Pixel p = img.getPixel(i, j);
        double[] values = {p.getRed(), p.getGreen(), p.getBlue()};
        double[] result = new double[3];
        for (int x = 0; x < 3; x++) {
          for (int y = 0; y < 3; y++) {
            result[x] += values[y] * matrix[x][y];
          }
        }
        img.setPixel(i, j, new Pixel(p.constrain((int) result[0]), p.constrain((int) result[1]),
                p.constrain((int) result[2]), 255));
      }
    }
    return img;
  }


  /**
   * Helper method to determine what value to set an RGB pixel to given a greyscale component.
   *
   * @param p the pixel to examine
   * @return a value to assign to the RGB values of a pixel.
   */
  private Pixel greyscaleHelper(Pixel p, Greyscale component) {
    int r = p.getRed();
    int g = p.getGreen();
    int b = p.getBlue();
    switch (component) {
      case RED:
        p.setRGB(r);
        break;
      case GREEN:
        p.setRGB(g);
        break;
      case BLUE:
        p.setRGB(b);
        break;
      case INTENSITY:
        p.setRGB(p.getIntensity());
        break;
      case VALUE:
        p.setRGB(Math.max(Math.max(r, g), b));
        break;
      default:
        throw new IllegalArgumentException("Invalid component type.");
    }
    return p;
  }

  @Override
  public ImageModel downsize(int newWidth, int newHeight) {
    if (newWidth > img.getCols() || newHeight > img.getRows()) {
      throw new IllegalArgumentException("Width and height must be less than original " +
              "width and height to downsize.");
    }
    Pixel[][] newPixels = new Pixel[newHeight][newWidth];
    Image downsized = new Image(newPixels);

    for (int i = 0; i < downsized.getRows(); i++) {
      for (int j = 0; j < downsized.getCols(); j++) {
        double oldX = j * img.getCols() / newWidth;
        double oldY = i * img.getRows() / newHeight;
        int r = 0;
        int g = 0;
        int b = 0;
        int maxValue = 0;

        int top = (int) Math.floor(oldY);
        int bottom = (int) Math.ceil(oldY);
        int left = (int) Math.floor(oldX);
        int right = (int) Math.ceil(oldX);

        Pixel topleft = img.getPixel(top, left);
        Pixel topright = img.getPixel(top, right);
        Pixel bottomleft = img.getPixel(bottom, left);
        Pixel bottomright = img.getPixel(bottom, right);
        Pixel[] pixelset = new Pixel[]{topleft, topright, bottomleft, bottomright};

        for (Pixel p : pixelset) {
          try {
            r += p.getRed();
            g += p.getGreen();
            b += p.getBlue();
            maxValue = p.getMaxValue();
          } catch (IndexOutOfBoundsException ignored) {
          }
        }
        downsized.setPixel(i, j, new Pixel(r / 4, g / 4, b / 4, maxValue));
      }
    }
    return downsized;
  }

  @Override
  public int[][] createHistogramData() {
    int[][] values = new int[4][256]; // values @ 0 = red, 1 = green, 2 = blue, 3 = intensity
    for (int i = 0; i < img.getRows(); i++) {
      for (int j = 0; j < img.getCols(); j++) {
        Pixel p = img.getPixel(i, j);
        values[0][p.getRed()]++;
        values[1][p.getGreen()]++;
        values[2][p.getBlue()]++;
        values[3][p.getIntensity()]++;
      }
    }
    return values;
  }
}
