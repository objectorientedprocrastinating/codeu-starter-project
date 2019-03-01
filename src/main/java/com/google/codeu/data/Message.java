/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import java.util.UUID;

/** A single message posted by a user. */
public class Message {

  // add recipient variable to expand the class
  private String recipient;
  private UUID id;
  private String user;
  private String text;
  private long timestamp;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content to {@code
   * recipient}. Generates a random ID and uses the current system time for the creation time.
   */
  public Message(String user, String text, String recipient) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient);
  }

  /**
   * Constructs a new message.
   *
   * @param id A non-null uuid to uniquely identify the constructed message.
   * @param user Sender of the message
   * @param text Content of the message
   * @param timestamp Time of sending out the message
   * @param recipient Recipient of the message
   */
  public Message(UUID id, String user, String text, long timestamp, String recipient) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
  }

  /** @return current non-null UUID */
  public UUID getId() {
    return id;
  }

  /** @return the sender of the user */
  public String getUser() {
    return user;
  }

  /** @return the content of the message */
  public String getText() {
    return text;
  }

  /** @return time of message is sent */
  public long getTimestamp() {
    return timestamp;
  }

  /** @return recipient of the message */
  public String getRecipient() {
    return recipient;
  }
}
