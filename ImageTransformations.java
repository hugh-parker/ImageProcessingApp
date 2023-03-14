package model;

import java.awt.image.BufferedImage;

import controller.commands.Greyscale;

/**
 * Represents a collection of methods that edit an ImageModel and return the new ImageModel.
 * Supports filtering, flipping, brightening/darkening, and color transformations of ImageModels.
 */
public interface ImageTransformations {

  /**
   * Filters the RGB channels of an image based on a 2D array of doubles.
   *
   * @param filter the filter to apply
   * @return the filtered image
   */
  ImageModel filter(double[][] filter);


  /**
   * Flips an image either horizontally or vertically. Utilizes ImageModel's micro-methods
   * to restructure the locations of pixels in an Image.
   *
   * @param vertical true if flipping vertically, false if horizontally
   * @return the flipped image
   */
  ImageModel flip(boolean vertical);


  /**
   * Transforms the coloring of an image based on individual pixel values. Whereas a filter
   * relies on the information of surrounding pixels to change a single pixel, a color
   * transformation relies only on the information of the pixel itself in order to change it.
   *
   * @param component the component to set the image's pixels to
   * @return the new color-transformed image
   */
  ImageModel greyscale(Greyscale component);

  /**
   * Changes the brightness of an entire image by an increment. Utilizes ImageModel
   * and Pixel's micro-methods.
   *
   * @param increment the amount by which to brighten or darken the image
   * @return the brightened/darkened image
   */
  ImageModel brighten(int increment);

  /**
   * Downsizes an image based on new dimensions.
   * @param newWidth the new width of the image.
   * @param newHeight the new height of the image.
   * @return the newly sized image.
   */
  ImageModel downsize(int newWidth, int newHeight);

  /**
   * Converts this class' image model to a buffered image.
   * @return a BufferedImage object
   */
  BufferedImage toBufferedImg();

  /**
   * Transforms the color of an image by applying a matrix to its pixels.
   * @param matrix the matrix to apply.
   * @return the filtered image.
   */
  ImageModel transformColor(double[][] matrix);

  /**
   * Returns the histogram data for an image.
   * @return a 2d array of ints representing the image's histogram values
   */
  int[][] createHistogramData();
}
