package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

/**
 * A class representing a histogram for an image. A histogram is a graph of the amount of components
 * for each value from 0-255 (unless otherwise specified) in an image. It displays four lines based
 * on the components of the pixel.
 */
public class ImageHistogram extends JPanel {

  private int[] red;
  private int[] green;
  private int[] blue;
  private int[] intensity;
  private boolean hasImageData;
  private int maxFrequency;

  /**
   * A constructor that takes in a width and height from the GUI and creates a histogram.
   * @param histWidth the desired width of the histogram.
   * @param histHeight the desired height of the histogram.
   */
  public ImageHistogram(int histWidth, int histHeight) {
    this.setPreferredSize(new Dimension(histWidth, histHeight));
    hasImageData = false;
    this.maxFrequency = 0;
    this.add(buildGraph(histHeight));
    this.add(buildKey());
  }

  /**
   * Updates the histogram with a new set of values after a transformation has been made.
   * @param histValues A 2d array that holds the amount of pixels for each value, with each row
   *                   representing a different component.
   */
  public void update(int[][] histValues) {
    if (histValues == null) {
      throw new IllegalArgumentException("Histogram given null values array");
    }
    hasImageData = true;
    this.red = histValues[0];
    this.green = histValues[1];
    this.blue = histValues[2];
    this.intensity = histValues[3];
    this.maxFrequency = 0;
    this.setMaxFrequency();
    this.repaint();
  }

  /**
   * Sets the maximum frequency of all of the components to be used for scaling the histogram.
   */
  private void setMaxFrequency() {
    for (int value : red) {
      this.maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : green) {
      this.maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : blue) {
      this.maxFrequency = Integer.max(value, maxFrequency);
    }
    for (int value : intensity) {
      this.maxFrequency = Integer.max(value, maxFrequency);
    }
  }

  /**
   * A class representing the graph of the histogram.
   */
  private class Graph extends JPanel {

    /**
     * Sets the border of the histogram.
     */
    public Graph() {
      this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Draws the lines for each component onto the graph.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (!hasImageData) {
        return;
      }
      drawLine(g, red, Color.RED);
      drawLine(g, green, Color.GREEN);
      drawLine(g, blue, Color.BLUE);
      drawLine(g, intensity, Color.YELLOW);
    }

    /**
     * Draws a line onto the graph based on the maximum frequency.
     * @param g the <code>Graphics</code> object to protect
     * @param component the array of values for the component being drawn
     * @param c the color to use
     */
    private void drawLine(Graphics g, int[] component, Color c) {
      g.setColor(c);
      int x1 = 1;
      int y1 = 0;
      for (int i = 0; i < component.length; i++) {
        int x2 = i + 1;
        int y2 = (component[i] * this.getHeight()) / maxFrequency;
        g.drawLine(x1, this.getHeight() - y1, x2, this.getHeight() - y2);
        x1 = x2;
        y1 = y2;
      }
    }

  }

  /**
   * Builds the key to be displayed under the histogram.
   * @return A panel with labels of information about the histogram.
   */
  private JPanel buildKey() {
    JPanel key = new JPanel();
    key.setLayout(new BoxLayout(key, BoxLayout.Y_AXIS));
    key.add(new JLabel("Histogram Key:"));
    key.add(new JLabel("Red: red component"));
    key.add(new JLabel("Green: green component"));
    key.add(new JLabel("Blue: blue component"));
    key.add(new JLabel("Yellow: intensity component"));
    key.setVisible(true);
    return key;
  }

  /**
   * Builds the graph panel to display the histogram on.
   * @param height the height of the histogram panel, to use to make the graph
   * @return a panel for the graph.
   */
  private JPanel buildGraph(int height) {
    JPanel section = new JPanel();
    section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));

    JPanel top = new JPanel();
    top.add(new JLabel("Image Histogram")).setFont(new Font(Font.SANS_SERIF, Font.BOLD,
            16));

    JPanel upper = new JPanel();
    Graph graph = new Graph();
    graph.setPreferredSize(new Dimension(260, height));
    JLabel yAxis = new JLabel("Frequency");

    upper.add(yAxis);
    upper.add(graph);

    JLabel xAxis = new JLabel("Value");

    section.add(top);
    section.add(upper);
    section.add(xAxis);
    return section;
  }
}



