# MyStart

## Why build something like this?

I don't know. I have a lot of bookmarks and there is no (proper) tooling for it. I use Google Bookmarks a lot
but there is no nice add-on for both Chrome and Firefox. I use Firefox mostly as browser and I also use the
bookmarks in the browser a lot. But then you suddenly think: I know I found something that I bookmarked. But
where did I bookmark it in?

# Features of MyStart

 * Quick search

    Searches in title, description, label and url.

 * Advanced search

    Specify a single label, part of description, part of title, part of url.

 * Bookmarklet

    I copied the one from Google that works with MyStart.

 * Statistics

    Added some statistics on source, visits and creation date. There is also a "Last visited" and "Last created" page.

 * Import

    MyStart can import from multiple sources.

 * Deduplication help

   When adding a new URL or editing an exiting one the page will show you a list of similar bookmarks that you can delete or edit.

 * User accounts

   Regular stuff: create account, change password, forgot password, set a nice picture.

# Importing bookmarks

This application can import from the following sources:

 * FireFox (upload your places.sqlite from your profile directory)
 * GitHub (enter a link: https://api.github.com/users/ivolimmen/starred) (can only import 60 pages per hours due to rate-limiting)
 * Google Bookmarks XML (export the XML: https://www.google.com/bookmarks/?output=xml and upload it)
 * Google Bookmarks RSS (export the RSS: https://www.google.com/bookmarks/?output=rss&num=500&start=0 and upload it)
 * Google Chrome/Chromium (upload your Bookmarks file from your profile directory)
 * Netscape (It is an XML file if your still got one)
 * XBEL (It's a XML standard for bookmarks)

# Building

Do a checkout of this project. You need to following to build:

* Minimal JDK 11 and up
* Maven 3.* and up

Then execute the following command:

    mvn clean install

# Database

Create a new database (postgres) by executing:

    createdb mystart    

(On start the application will create the tables)

# Configuration

In the directory `config` there are two configuration files, one for the application and one for the logging.
create copies of the files:

```sh
$ cp config/application-local.properties.example config/application-local.properties
$ cp config/logback-local.xml.example config/logback-local.xml
```
And customize accordingly. When you deploy it you should also have a `production` version.

# Starting the application

## For development tests

    mvn -pl :server exec:java

## For release

Extract the generated server-[VERSION].tar.gz. This will create a directory called mystart.
Go into the `bin` directory and execute the `mystart` script to start it.

# Credits

* For emoji handling: https://github.com/kcthota/emoji4j
* For icons: https://fontawesome.com/
* For look and feel: https://github.com/ColorlibHQ/AdminLTE