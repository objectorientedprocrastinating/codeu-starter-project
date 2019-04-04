/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Message {

  private String recipient;
  private UUID id;
  private String user;
  private String text;
  private long timestamp;
  private String imageUrl;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text}
   * content to {@code
   * recipient}. Generates a random ID and uses the current system time for the
   * creation time.
   */
  public Message(String user, String text, String recipient, String imageUrl) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient, imageUrl);
  }

  /**
   * Constructs a new message.
   *
   * @param id        A non-null uuid to uniquely identify the constructed
   *                  message.
   * @param user      Sender of the message
   * @param text      Content of the message
   * @param timestamp Time of sending out the message
   * @param recipient Recipient of the message
   * @param imageUrl  Url of any image in the message
   */
  public Message(UUID id, String user, String text, long timestamp, String recipient, String imageUrl) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
    this.imageUrl = imageUrl;
  }

  /**
   * Returns current non-null UUID.
   * 
   * @return non-null UUID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the sender of the message.
   * 
   * @return user
   */
  public String getUser() {
    return user;
  }

  /**
   * Returns the content of the message.
   * 
   * @return message
   */
  public String getText() {
    return text;
  }

  /**
   * Returns the time of the message is sent.
   * 
   * @return time stamp of the message
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Returns the recipient as a string.
   * 
   * @return recipient
   */
  public String getRecipient() {
    return recipient;
  }

  public void setImageUrl(String imageUrl) {
    System.out.println("imageurl");
    System.out.println(imageUrl);
    this.imageUrl = imageUrl;

  }

  public Object getImageUrl() {
    return imageUrl;
  }
}
