package com.google.codeu.data;

import java.util.Objects;

/**
 * Provides the capability to save the data the user submits, and then retrieve that data to display
 * the user's "about me" section on their user page.
 */
public class User {

  private String email;
  private String aboutMe;

  /**
   * If null is passed in through the method then return empty string for each field.
   */
  public User(String email, String aboutMe) {
    this.email = Objects.toString(email, "");
    this.aboutMe = Objects.toString(aboutMe, "");
  }

  public String getEmail() {
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }
}
