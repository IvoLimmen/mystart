# MyStart

## Why build something like this? 

I don't know. I have a lot of bookmarks and there is no tooling for it.

## But why in JSP's????

This project started like three years ago? Made a nice back end in Ratpack. Nice! Humm might be nicer in Vert.x? Rewrite!
Let's make a front end in JavaFX, very cool. Humm in JDK 9 there no longer is a JavaFX when you run Linux; damn. Let's build
a front end in Angular. Everyone is doing it. It takes a long time to build it and lose interest. Humm I have more experience
in Wicket. Let's rewrite. Still takes long, lose interest. 

Screw it, I will make a JSP front end in a weekend. Check. After 3 years finally got a product that actually works.

Yes looks like crap but that I will remedy in the future.

# Storage

 Storage in PostgreSQL (table creation script is included)

# Importing bookmarks

This application can import from the following sources:

 * FireFox (upload your places.sqlite from your profile directory)
 * GitHub (enter a link: https://api.github.com/users/ivolimmen/starred)
 * Google Bookmarks (export the XML: https://www.google.com/bookmarks/?output=xml and upload it)
 * Google Chrome/Chromium (upload your Bookmarks file from your profile directory)
 * Netscape (It is an XML file if your still got one)
 * XBEL (It's a XML standard for bookmarks)

# What for features will come in the future?
 
 * Properties user/password for database.
 * Settings in general
 * Import of Delicious bookmarks (?).
 * Exports (?)
 * Configure browser.
 * Searching on fields using 'sounds like'.
 * Import Microsoft Shortcuts (favorites).
 * Configuration file for HTTP/HTTPS and other settings.
 * Proper front end?
