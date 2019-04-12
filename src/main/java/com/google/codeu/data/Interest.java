package com.google.codeu.data;

import java.util.Objects;

/**
 * Provides the capability to save the data the user submits, and then retrieve that data to display
 * the user's "about me" section on their user page.
 */
public class Interest {

  private String email;
  private String info;

  /** If null is passed in through the method then return empty string for each field. */
  public Interest(String email, String info) {
    this.email = Objects.toString(email, "");
    this.info = Objects.toString(info, "");
  }

  public String getEmail() {
    return email;
  }

  public String getInfo() {
    return info;
  }
}
