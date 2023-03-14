package view;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import controller.Features;

/**
 * A view representing the GUI of our Image Processing program. Uses JFrame to create the user
 * interface.
 */
public class SimpleGUIView extends JFrame implements GUIView {

  private final JLabel imageLabel;
  private JButton fileOpenButton;
  private JButton fileSaveButton;
  private JButton[] editingButtons;

  private JButton downsize;
  private JTextField widthField;
  private JTextField heightField;
  private JRadioButton[] greyscaleButtons;
  private ButtonGroup radioButtonGroup;
  private JSlider brightnessSlider;
  private int previousSliderValue;
  private final ImageHistogram histogram;
  private JButton resetButton;

  /**
   * A default constructor that sets up the frames and information of our GUI.
   */
  public SimpleGUIView() {
    // Set up the frame
    this.setTitle("Image Processor");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setBackground(Color.BLUE);
    this.setSize(1500, 650);
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(0, 0));
    this.add(mainPanel);
    this.imageLabel = new JLabel();
    this.histogram = new ImageHistogram(500, 350);
    JPanel imageInfo = new JPanel();
    imageInfo.add(buildImageViewerPanel());

    mainPanel.add(buildLoadButton(), BorderLayout.NORTH);
    mainPanel.add(buildSaveButton(), BorderLayout.SOUTH);
    mainPanel.add(buildEditingPanel(), BorderLayout.WEST);
    mainPanel.add(imageInfo, BorderLayout.CENTER);
    mainPanel.add(histogram, BorderLayout.EAST);

    setVisible(true);
  }

  @Override
  public void addFeatures(Features f) {

    // when the file open button is clicked, open the user's finder and get
    // the selected file, then pass the pathname to the loadImage method in features.
    fileOpenButton.addActionListener(e -> {
      final JFileChooser chooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
              "jpg", "jpeg", "png", "bmp", "ppm");
      chooser.setFileFilter(filter);
      int retValue = chooser.showOpenDialog(this);
      if (retValue == JFileChooser.APPROVE_OPTION) {
        File file = chooser.getSelectedFile();
        String pathname = file.getAbsolutePath();
        String imageName = file.getName();
        f.loadImage(pathname, imageName);
      }
    });

    // when the save button is clicked, run the same code as the load button, allowing
    // the user to select a file to save to. Then, pass that file name to the saveImage
    // method in features.
    fileSaveButton.addActionListener(e -> {
      final JFileChooser fileChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
              "jpg", "jpeg", "png", "bmp", "ppm");
      fileChooser.setFileFilter(filter);
      int value = fileChooser.showOpenDialog(this);
      if (value == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        String pathname = file.getAbsolutePath();
        //String imageName = getName();
        f.saveImage(pathname);
      }
    });

    // add an action listener for each button command (blur, sharpen, sepia,
    // flip vertical, flip horizontal)
    for (JButton button : editingButtons) {
      button.addActionListener(e -> f.runCommand(e.getActionCommand()));
    }


    // add an action listener for each radio button command (greyscale components)
    for (JRadioButton button : greyscaleButtons) {
      button.addActionListener(e -> f.runCommand(e.getActionCommand()));
    }

    // add a change listener for the brightness slider. Finds the difference between the previous
    // slider value and the new value and changes the image brightness by that amount.
    brightnessSlider.addChangeListener(e -> {
      int updatedValue = brightnessSlider.getValue();
      int increment = updatedValue - previousSliderValue;
      f.toggleBrightness(increment);
      previousSliderValue = updatedValue;
    });

    downsize.addActionListener(e -> {
      int width = Integer.parseInt(widthField.getText());
      int height = Integer.parseInt(heightField.getText());
      f.downsize(width, height);
    });

    // add an action listener for the image reset button. Delegates to the features interface
    resetButton.addActionListener(e -> f.reset());
  }

  @Override
  public void displayImage(BufferedImage img, int[][] histValues) {
    if (img != null) {
      this.imageLabel.setIcon(new ImageIcon(img));
      this.histogram.update(histValues);
    }
  }

  @Override
  public void resetSelections() {
    this.previousSliderValue = 0;
    this.brightnessSlider.setValue(0);
    this.radioButtonGroup.clearSelection();
  }

  @Override
  public void renderMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void renderWelcome(String message) {
    JOptionPane.showMessageDialog(this, message, "Welcome!",
            JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Builds the load button in its own panel.
   * @return the load panel
   */
  private JPanel buildLoadButton() {
    JPanel fileOpenPanel = new JPanel();
    fileOpenPanel.setBackground(Color.PINK);
    fileOpenButton = new JButton("Upload Image");
    fileOpenButton.setActionCommand("Load file");
    fileOpenButton.setSize(new Dimension(20, 5));
    fileOpenPanel.add(fileOpenButton);
    return fileOpenPanel;
  }

  /**
   * Builds the save button in its own panel.
   * @return the save panel
   */
  private JPanel buildSaveButton() {
    JPanel fileSavePanel = new JPanel();
    fileSavePanel.setBackground(Color.PINK);
    fileSaveButton = new JButton("Save Image to Files");
    fileSaveButton.setActionCommand("Save file");
    fileSaveButton.setSize(new Dimension(20, 5));
    fileSavePanel.add(fileSaveButton);
    return fileSavePanel;
  }

  /**
   * Builds the panel that contains the options for editing an image.
   * @return the image editing panel
   */
  private JPanel buildEditingPanel() {
    JPanel editingPanel = new JPanel();
    editingPanel.setLayout(new BoxLayout(editingPanel, BoxLayout.PAGE_AXIS));
    editingPanel.add(new JLabel("Editing Options:")).setFont(new Font(Font.SANS_SERIF,
            Font.BOLD, 16));
    editingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    // Builds editing buttons
    JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
    buttons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    String[] buttonNames = new String[]{"Blur", "Sharpen", "Sepia", "Flip-Vertical",
        "Flip-Horizontal"};
    editingButtons = new JButton[buttonNames.length];

    for (int i = 0; i < buttonNames.length; i++) {
      editingButtons[i] = new JButton(buttonNames[i]);
      editingButtons[i].setSelected(false);
      editingButtons[i].setActionCommand(buttonNames[i]);
      buttons.add(editingButtons[i]);
    }
    editingPanel.add(buttons);

    // Builds greyscale title
    JPanel greyscale = new JPanel();
    greyscale.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
    greyscale.setLayout(new BoxLayout(greyscale, BoxLayout.PAGE_AXIS));
    greyscale.add(new JLabel("Greyscale options:\n")).setFont(new Font(Font.SANS_SERIF,
            Font.PLAIN, 14));

    // Builds greyscale buttons
    String[] greyscaleButtonNames = new String[]{"Red", "Green", "Blue", "Intensity", "Value",
        "Luma"};
    greyscaleButtons = new JRadioButton[greyscaleButtonNames.length];
    radioButtonGroup = new ButtonGroup();

    for (int i = 0; i < greyscaleButtonNames.length; i++) {
      greyscaleButtons[i] = new JRadioButton(greyscaleButtonNames[i]);
      greyscaleButtons[i].setSelected(false);
      greyscaleButtons[i].setActionCommand(greyscaleButtonNames[i] + "-Component");
      radioButtonGroup.add(greyscaleButtons[i]);
      greyscale.add(greyscaleButtons[i]);
    }

    editingPanel.add(greyscale);

    // build downsize input prompt
    JPanel d = new JPanel();
    d.setLayout(new BoxLayout(d, BoxLayout.PAGE_AXIS));
    d.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    JLabel label = new JLabel();
    label.setText("Enter height and width to downsize:");
    d.add(label);

    JPanel entry = new JPanel();
    entry.setLayout(new FlowLayout());

    downsize = new JButton("Downsize");
    heightField = new JTextField(5);
    widthField = new JTextField(5);
    entry.add(widthField);
    entry.add(heightField);
    entry.add(downsize);
    d.add(entry);
    editingPanel.add(d);

    // Build the brightness slider
    JPanel brightnessPanel = new JPanel();
    brightnessPanel.setLayout(new BoxLayout(brightnessPanel, BoxLayout.PAGE_AXIS));
    brightnessPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    brightnessSlider = new JSlider(-255, 255);
    brightnessSlider.setMinorTickSpacing(25);
    brightnessSlider.setSnapToTicks(false);
    brightnessSlider.setPaintTicks(true);
    JLabel sliderLabel = new JLabel("             Brightness");
    brightnessPanel.add(brightnessSlider);
    brightnessPanel.add(sliderLabel);
    previousSliderValue = 0;
    editingPanel.add(brightnessPanel);

    // Build the reset button
    resetButton = new JButton("Reset");
    resetButton.setActionCommand("reset");
    resetButton.setSize(20, 5);
    editingPanel.add(resetButton);

    return editingPanel;
  }

  /**
   * Builds the panel that contains the list of selectable images to view.
   * @return the image viewing panel
   */
  private JPanel buildImageViewerPanel() {

    // top level
    JPanel top = new JPanel();
    top.add(new JLabel("Your Image")).setFont(new Font(Font.SANS_SERIF, Font.BOLD,
            16));
    JPanel imageViewPanel = new JPanel();
    imageViewPanel.setPreferredSize(new Dimension(600, 430));
    imageViewPanel.add(top);

    // image view
    imageLabel.setHorizontalAlignment(JLabel.CENTER);
    JScrollPane imageScroll = new JScrollPane(imageLabel);
    imageScroll.setPreferredSize(new Dimension(600, 390));

    imageViewPanel.add(imageScroll);
    return imageViewPanel;
  }

}

