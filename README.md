# wagchallenge
by Pablo Gomez

This is my approach for the challenge given to me. I tried to keep it as scalable as possible that is why I have created and API and services to curry an specific task and trying to always keep the MVC and responsiveness in my mind.
I have also used helpers for hashing values, print on the logcat in a proper way and format URLs in a ordered and understandable way.
I have only used two third-party libraries GSON for serializing data and deserializing data and okhttp from Square Inc. for creting HTTP RESTFul request to the stackoverflow API. I could have used Picasso or Glider on top of okhttp for dealing with downloading
and displaying the avatar and they even have caching but I decided to keep the app light do not add more libraries than need it so I wrote my own cache with in memory cache as an LRU cache and simple disk cache.

**Thanks for reading!**
-Pablo Gomez