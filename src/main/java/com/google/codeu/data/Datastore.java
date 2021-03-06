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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    if (message.getImageUrl() != null) {
      messageEntity.setProperty("imageUrl", message.getImageUrl());
    }
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());

    // passing the recipient from the client to the sever
    messageEntity.setProperty("recipient", message.getRecipient());
    datastore.put(messageEntity);
  }

  /** returns a List of the interest a user has entered. */
  public List<Interest> getInterests(String person) {
    List<Interest> likes = new ArrayList<>();
    Query query =
        new Query("Interest")
            .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, person));
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      try {
        String user = (String) entity.getProperty("email");
        String info = (String) entity.getProperty("interest");
        Interest like = new Interest(user, info);
        likes.add(like);
      } catch (Exception e) {
        System.err.println("Error reading interest.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return likes;
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   *         never posted a message. List is sorted by time descending. Messages
   *         are public and will display on the recipient's user page
   */
  public List<Message> getMessages(String recipient) {
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message")
        .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
        .addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");

        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        String imageUrl = (String) entity.getProperty("imageUrl");
        Message message = new Message(id, user, text, timestamp, recipient, imageUrl);

        // Message message = new Message(id, user, text, timestamp, recipient);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   *         never posted a message. List is sorted by time descending.
   */
  public List<Message> getAllMessages() {
    List<Message> messages = new ArrayList<>();

    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        String recipient = (String) entity.getProperty("recipient");

        String imageUrl = (String) entity.getProperty("imageUrl");
        Message message = new Message(id, user, text, timestamp, recipient, imageUrl);

        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }

    return messages;
  }

  /** Stores the user interest in the database. */
  public void storeInterest(Interest newInterest) {
    Entity userEntity = new Entity("Interest", newInterest.getEmail());
    userEntity.setProperty("email", newInterest.getEmail());
    userEntity.setProperty("interest", newInterest.getInfo());
    datastore.put(userEntity);
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /** Returns the User owned by the email address, or null if no matching User was found. */
  public User getUser(String email) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    User user = new User(email, aboutMe);

    return user;
  }

  /**
   * Get all the markers from the user.
   *
   * @return a list of markers
   */
  public List<UserMarker> getMarkers(String user) {
    List<UserMarker> markers = new ArrayList<>();

    Query query =
        new Query("UserMarker")
            .setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user));
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        double lat = (double) entity.getProperty("lat");
        double lng = (double) entity.getProperty("lng");
        String content = (String) entity.getProperty("content");

        UserMarker marker = new UserMarker(lat, lng, content, user);
        markers.add(marker);
      } catch (Exception e) {
        System.err.println("Error reading marker.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return markers;
  }

  /** Store the markers in the datastore. */
  public void storeMarker(UserMarker marker) {
    Entity markerEntity = new Entity("UserMarker");
    markerEntity.setProperty("lat", marker.getLat());
    markerEntity.setProperty("lng", marker.getLng());
    markerEntity.setProperty("content", marker.getContent());
    markerEntity.setProperty("user", marker.getUser());
    datastore.put(markerEntity);
  }
}
