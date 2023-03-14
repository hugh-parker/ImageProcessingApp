package model;

import java.util.Objects;

/**
 * Class representing an image. An image includes the format, a 2D array
 * of pixel objects, and the number of rows and columns.
 */
public class Image implements ImageModel {

  private final Pixel[][] pixels;
  private final int rows;
  private final int cols;

  /**
   * Constructor for an image object.
   * @param pixels a 2D array of pixels representing the image
   */
  public Image(Pixel[][] pixels) throws IllegalArgumentException {
    if (pixels == null) {
      throw new IllegalArgumentException("Parameters cannot be null.");
    }
    this.pixels = pixels;
    this.rows = pixels.length;
    this.cols = pixels[0].length;
  }

  /**
   * Returns the number of rows in the image.
   *
   * @return the number of rows
   */
  public int getRows() {
    return this.rows;
  }

  /**
   * Returns the number of columns in the image.
   *
   * @return the number of columns
   */
  public int getCols() {
    return this.cols;
  }

  /**
   * Returns a copy of this image.
   *
   * @return ColorPixel[][] the copy of the array of pixels
   */
  public ImageModel getCopy() {
    Pixel[][] copyImage = new Pixel[this.rows][this.cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        copyImage[i][j] = this.pixels[i][j].copy();
      }
    }
    return new Image(copyImage);
  }

  /**
   * Returns a copy of a pixel in the image.
   * @return Pixel the pixel
   */
  public Pixel getPixel(int row, int col) throws IllegalArgumentException {
    if (row > this.rows || col > this.cols) {
      throw new IllegalArgumentException("Out of range.");
    }
    return this.pixels[row][col].copy();
  }

  /**
   * Sets a pixel in this image at a given row and column to a copy of another pixel.
   * (Copies are used so as to avoid having different pixels refer to the same place in memory).
   *
   * @param row the row of the pixel to set
   * @param col the column of the pixel to set
   * @param p the pixel to set to
   */
  public void setPixel(int row, int col, Pixel p) throws IllegalArgumentException {
    if (row > this.rows || col > this.cols || p == null) {
      throw new IllegalArgumentException("Invalid parameters");
    }
    pixels[row][col] = p.copy();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Image)) {
      return false;
    }

    Image other = (Image) obj;


    if (this.rows != other.rows || this.cols != other.cols) {
      return false;
    }

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (!this.pixels[i][j].equals(other.pixels[i][j])) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = Objects.hash(rows, cols);
    for (Pixel[] p : pixels) {
      for (int i = 0; i < p.length; i++) {
        hash += p[i].hashCode();
      }
    }
    return hash;
  }
}
