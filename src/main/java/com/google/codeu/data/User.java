package com.google.codeu.data;

//Provides the capability to save the data the user submits, 
//and then retrieve that data to display the user's "about me" 
//section on their user page.

public class User {

    private String email;
    private String aboutMe;

    //If null is passed in through the method then return empty string for each field. 
    public User(String email, String aboutMe) {
      if(email == null || aboutMe == null){
        this.email = "";
        this.aboutMe = "";
      }  
      else{
        this.email = email;
        this.aboutMe = aboutMe;
      }
    }

    public String getEmail() {
        return email;
    }

    public String getAboutMe() {
        return aboutMe;
    }
}