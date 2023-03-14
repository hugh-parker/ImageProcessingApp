package model;

/**
 * Interface representing the model for a two-dimensional Image. Provides methods
 * for retrieving information about the image, as well as methods to get and set
 * pixels in the image.
 */
public interface ImageModel {

  /**
   * Get the number of rows of pixels in the image.
   * @return int the number of rows
   */
  int getRows();

  /**
   * Get the number of columns of pixels in the image.
   * @return int the number of columns
   */
  int getCols();

  /**
   * Get a copy of the image.
   * @return ImageModel a copy
   */
  ImageModel getCopy();

  /**
   * Set a pixel at a row and column to another pixel.
   * @param row the row of the pixel to change
   * @param col the column of the pixel to change
   * @param p the pixel to set to
   * @throws IllegalArgumentException if the row or column is out of the bounds of the image,
   *         or if p is null
   */
  void setPixel(int row, int col, Pixel p) throws IllegalArgumentException;

  /**
   * Get a pixel in the image at a given row and column in the image.
   * @param row the row of the pixel
   * @param col the column of the pixel
   * @return PixelModel a pixel in the image
   */
  Pixel getPixel(int row, int col) throws IllegalArgumentException;
}
