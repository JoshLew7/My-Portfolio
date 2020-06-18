// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}


/**
 * Fetches a random quote from the server and adds it to the DOM.
 */
function getRandomQuote() {
  console.log('Fetching a random quote.');

  // The fetch() function returns a Promise because the request is asynchronous.
  const responsePromise = fetch('/random-quote');

  // When the request is complete, pass the response into handleResponse().
  responsePromise.then(handleResponse);
}

/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
function handleResponse(response) {
  console.log('Handling the response.');

  // response.text() returns a Promise, because the response is a stream of
  // content and not a simple variable.
  const textPromise = response.text();

  // When the response is converted to text, pass the result into the
  // addQuoteToDom() function.
  textPromise.then(addQuoteToDom);
}

/** Adds a random quote to the DOM. */
function addQuoteToDom(quote) {
  console.log('Adding quote to dom: ' + quote);

  const quoteContainer = document.getElementById('quote-container');
  quoteContainer.innerText = quote;
}

function addComment() {
  const username = document.getElementById("comment-username").value;
  const text = document.getElementById("comment-input").value;
  fetch(`/data?username=${username}&text=${text}`, { method: "POST" }).then(
      (res) => {
        if (res.ok) {
          rerenderCommentsWithCurrentLimit();
          document.getElementById("comment-username").value = "";
          document.getElementById("comment-input").value = "";
        } else {
          alert("Error: Enter a valid comment.");
        }
      }
  );
}

async function deleteComments() {
  await fetch("/delete-data", { method: "POST" });
  displayServletContent(0);
}
