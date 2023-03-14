package controller.commands;

/**
 * Enum representing the options for greyscaling an image.
 * VALUE = Maximum RGB value
 * INTENSITY = Average RGB value
 * LUMA = ((red * .2126) + (green * .7152) + (blue * .0722)) / 3
 * RED = R value
 * GREEN = G value
 * BLUE = B value
 */
public enum Greyscale {
  VALUE, INTENSITY, BLUE, RED, GREEN
}
