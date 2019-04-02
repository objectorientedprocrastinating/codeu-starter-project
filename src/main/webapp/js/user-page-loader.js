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
 /* global google */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');
let editMarker;
let map;

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfLoggedIn() {
  fetch('/login-status')
    .then((response) => {
      return response.json();
    })
    .then((loginStatus) => {
      if (loginStatus.isLoggedIn &&
        loginStatus.username == parameterUsername) {
        const messageForm = document.getElementById('message-form');
        fetchImageUploadUrlAndShowForm()
        messageForm.action = '/messages?recipient=' + parameterUsername;
        fetchImageUploadUrlAndShowForm();
        messageForm.classList.remove('hidden');
      }
    });
  document.getElementById('about-me-form').classList.remove('hidden');
}
function fetchImageUploadUrlAndShowForm() {
  fetch('/image-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      const messageForm = document.getElementById('message-form');
      messageForm.action = imageUploadUrl;
      messageForm.classList.remove('hidden');
    });
}
/** Fetches messages and add them to the page. */
function fetchMessages() {
  console.log(parameterUsername)
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
    .then((response) => {
      return response.json();
    })
    .then((messages) => {
      const messagesContainer = document.getElementById('message-container');
      if (messages.length == 0) {
        messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
      } else {
        messagesContainer.innerHTML = '';
      }
      messages.forEach((message) => {
        const messageDiv = buildMessageDiv(message);
        messagesContainer.appendChild(messageDiv);
      });
    });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
    message.user + ' - ' + new Date(message.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

function fetchInterests(){
  const url = '/interest?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
   }).then((interest)=> {
    var interestContainer = document.getElementById('interest-container');
    interestContainer.innerHTML = interest;
   });
}


function fetchAboutMe() {
  const url = '/about?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.text();
  }).then((aboutMe) => {
    const aboutMeContainer = document.getElementById('about-me-container');
    if (aboutMe == '') {
      aboutMe = 'This user has not entered any information yet.';
    }
    aboutMeContainer.innerHTML = aboutMe;
  });
}

function createMap(){

  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 38.5949, lng: -94.8923},
    zoom: 4
  });

  map.addListener('click', (event) => {
    createMarkerForEdit(event.latLng.lat(), event.latLng.lng());
  });

  fetchMarkers();
}

function fetchMarkers(){
  const url = '/user-markers?user=' + parameterUsername;
  fetch(url).then((response) => {
    return response.json();
  }).then((markers) => {
    markers.forEach((marker) => {
     createMarkerForDisplay(marker.lat, marker.lng, marker.content)
    });
  });
}

function createMarkerForDisplay(lat, lng, content){

  const marker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map
  });

  var infoWindow = new google.maps.InfoWindow({
    content: content
  });
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}

function postMarker(lat, lng, content, user){
  const params = new URLSearchParams();
  params.append('lat', lat);
  params.append('lng', lng);
  params.append('content', content);
  params.append('user', user)

  const url = '/user-markers?user=' + user;
  fetch(url, {
    method: 'POST',
    body: params
  });
}

function createMarkerForEdit(lat, lng){

  if(editMarker){
   editMarker.setMap(null);
  }

  editMarker = new google.maps.Marker({
    position: {lat: lat, lng: lng},
    map: map
  });

  const infoWindow = new google.maps.InfoWindow({
    content: buildInfoWindowInput(lat, lng)
  });

  google.maps.event.addListener(infoWindow, 'closeclick', () => {
    editMarker.setMap(null);
  });

  infoWindow.open(map, editMarker);
}

function buildInfoWindowInput(lat, lng){
  const textBox = document.createElement('textarea');
  const button = document.createElement('button');
  button.appendChild(document.createTextNode('Submit'));

  button.onclick = () => {
    postMarker(lat, lng, textBox.value, parameterUsername);
    createMarkerForDisplay(lat, lng, textBox.value);
    editMarker.setMap(null);
  };

  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);

  return containerDiv;
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showMessageFormIfLoggedIn();
  fetchMessages();
  fetchAboutMe();
  createMap();
  fetchInterests();
}

