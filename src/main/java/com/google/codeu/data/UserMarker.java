package com.google.codeu.data;

public class UserMarker {

  private double lat;
  private double lng;
  private String content;
  private String user;

  /**
   * Create a usermarker.
   *
   * @param lat lattitude of the location.
   * @param lng longitude of the location.
   * @param content content to show in the info box.
   */
  public UserMarker(double lat, double lng, String content, String user) {
    this.lat = lat;
    this.lng = lng;
    this.content = content;
    this.user = user;
  }

  /**
   * Returns the lattitude.
   *
   * @return lat
   */
  public double getLat() {
    return lat;
  }

  /**
   * Returns the longitude.
   *
   * @return lng
   */
  public double getLng() {
    return lng;
  }

  /**
   * Returns the content.
   *
   * @return content
   */
  public String getContent() {
    return content;
  }

  public String getUser() {
    return user;
  }
}
