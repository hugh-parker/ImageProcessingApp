package model;

import java.util.Objects;

/**
 * Class representing a Pixel in a 2D image. Contains methods to
 * retrieve information about the pixel and change its values.
 */
public class Pixel {

  private int r;
  private int g;
  private int b;
  private int maxValue;

  /**
   * Constructor for a Pixel object.
   * @param r the red value of the pixel
   * @param g the blue value of the pixel
   * @param b the green value of the pixel
   * @throws IllegalArgumentException if a pixel value is negative
   * @throws IllegalArgumentException if a pixel value is greater than the maximum value.
   */
  public Pixel(int r, int g, int b, int maxValue) {
    if (r < 0 || g < 0 || b < 0 || maxValue < 0) {
      throw new IllegalArgumentException("Pixels cannot have negative values.");
    }
    if (r > maxValue || g > maxValue || b > maxValue) {
      throw new IllegalArgumentException("Pixel values cannot be greater than the maximum " +
              "value.");
    }
    this.r = r;
    this.g = g;
    this.b = b;
    this.maxValue = maxValue;
  }

  /**
   * Gets the red value of the pixel.
   * @return int the red value.
   */
  public int getRed() {
    return this.r;
  }

  /**
   * Gets the green value of the pixel.
   * @return int the green value.
   */
  public int getGreen() {
    return this.g;
  }

  /**
   * Gets the blue value of the pixel.
   * @return int the blue value.
   */
  public int getBlue() {
    return this.b;
  }

  /**
   * Gets the maximum allowed value of the pixel (i.e. for a 24 bit RGB pixel, it's 255).
   * @return int the max value
   */
  public int getMaxValue() {
    return this.maxValue;
  }

  /**
   * Gets the intensity value of the pixel (average of r, g, b).
   * @return int the intensity value.
   */
  public int getIntensity() {
    return (this.r + this.g + this.b) / 3 ;
  }

  /**
   * Adds an increment to the pixel's values.
   * @param increment the amount by which to change the values
   */
  public void add(int increment) {
    this.r = constrain(this.r + increment);
    this.g =  constrain(this.g + increment);
    this.b = constrain(this.b + increment);
  }

  /**
   * Sets each of the pixel's RGB values to a certain value.
   * If the value is above 255 or below 0, the pixels are set to 255 and 0, respectively.
   * @param value the value to set RGB to
   */
  public void setRGB(int value) {
    this.r = constrain(value);
    this.b = constrain(value);
    this.g = constrain(value);
  }

  /**
   * Takes a value and constrains it within this pixel's allowed range.
   * (i.e. if 300 is passed and the pixel has an 8-bit range from 0-255, 255 will be returned)
   * @param value the int value to constrain
   * @return the constrained value
   */
  public int constrain(int value) {
    return Math.max(0, Math.min(value, maxValue));
  }

  /**
   * Sets the pixel's red value to a certain value.
   * If the value is above 255 or below 0, the pixels are set to 255 and 0, respectively.
   * @param value the value to set RGB to
   */
  public void setR(int value) {
    this.r = constrain(value);
  }

  /**
   * Sets the pixel's green value to a certain value.
   * If the value is above 255 or below 0, the pixels are set to 255 and 0, respectively.
   * @param value the value to set RGB to
   */
  public void setG(int value) {
    this.g = constrain(value);
  }

  /**
   * Sets the pixel's blue value to a certain value.
   * If the value is above 255 or below 0, the pixels are set to 255 and 0, respectively.
   * @param value the value to set RGB to
   */
  public void setB(int value) {
    this.b = constrain(value);
  }

  /**
   * Return a copy of the pixel.
   * @return AbstractPixel a copy
   */
  public Pixel copy() {
    return new Pixel(this.r, this.g, this.b, this.maxValue);
  }

  @Override
  public String toString() {
    return this.r + " " + this.g + " " + this.b;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Pixel)) {
      return false;
    }

    Pixel other = (Pixel) obj;

    if (this.maxValue != other.maxValue) {
      return false;
    }

    return this.r == other.r && this.g == other.g && this.b == other.b;
  }

  @Override
  public int hashCode() {
    return Objects.hash(r, g, b, maxValue);
  }

}
