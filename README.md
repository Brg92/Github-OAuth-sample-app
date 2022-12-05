# Github OAuth sample app
###### Overview
The app allow the user to sign in his github account by perform the login process on the web view showed to the entry point. 
Once logged, he sees a transition screen with a main logo animation who let him land to the Home screen. To the Home he sees his repositories listed in the cards. 
The user taps on the card to display the details of the repository, once there he might star the repository.
The app has a cache implemented by a local database to store the repositories, so allows the offline navigation. 

###### Specs
* Kotlin
* MVVM
* Coroutines for async task, Live Data, Flow
* Hilt2
* Retrofit REST apis 
* Room database 
* Andoridx jetpack libraries
* Material3 components  
