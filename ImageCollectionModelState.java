package model;

/**
 * Represents the state of an ImageCollectionModel. Provides methods to retrieve
 * an image as well as the number of images in the model.
 */
public interface ImageCollectionModelState {

  /**
   * Method to get a specific image from the model (i.e. brighten-koala).
   *
   *
   * @param imageName the name of the variant of the image (provided by user)
   * @return ImageModel the image
   * @throws IllegalArgumentException if the selected image is not currently being stored in the
   *         model
   */
  ImageModel getImage(String imageName) throws IllegalArgumentException;

  /**
   * Returns the number of images stored by the model at the current time.
   * @return int the number of images.
   */
  int getNumImages();

}
