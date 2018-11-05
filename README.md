# MyStart

## Why build something like this? 

I don't know. I have a lot of bookmarks and there is no (proper) tooling for it.

# Importing bookmarks

This application can import from the following sources:

 * FireFox (upload your places.sqlite from your profile directory)
 * GitHub (enter a link: https://api.github.com/users/ivolimmen/starred)
 * Google Bookmarks XML (export the XML: https://www.google.com/bookmarks/?output=xml and upload it)
 * Google Bookmarks RSS (export the RSS: https://www.google.com/bookmarks/?output=rss&num=500&start=0 and upload it)
 * Google Chrome/Chromium (upload your Bookmarks file from your profile directory)
 * Netscape (It is an XML file if your still got one)
 * XBEL (It's a XML standard for bookmarks)

# Building

Do a checkout of this project. You need to following to build:

* Minimal JDK 8*
* Maven 3.5.*

(*) I made it in JDK11 but needed it to work on JDK8 so it would work under a PINE64 (ARM). It builds on JDK's 8 
through 11 but it is only tested on 8 & 11.

Then execute the following command:

    mvn clean install

# Database

Create a new database (postgres) by executing:

    createdb mystart    

(On start the application will create the tables)

# Starting the application

After a full build execute the following to start the server:

    ./server/target/appassembler/bin/mystart

# Thanks

Thanks to the following project this project could be build:

 * [AdminLTE](https://adminlte.io/), for the nice look and feel.
 * [Awesomplete](https://leaverou.github.io/awesomplete/), for the nice and small autocomplete function.