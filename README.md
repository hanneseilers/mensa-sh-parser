mensa-sh-parser
===============

Library to parse mensa and menu data from Studentenwerk Schleswig-Holstein (http://www.studentenwerk.sh)

Installation
------------
Include the pre-build .jar file from latest release into your project.

For usage in enviroments (like Android) where using java File objects isn't recommended;
extend the FileWriter class and overwrite it's functions.
FileWriter class is used as an interface between the librarys internal cache management and the file system access.
Set the class of your extended FileWriter class using Cache classes static function.

Usage
-----
Use Mensa classes static functions to get available mensa locations and their menus.
Refer to Test class for an example.

By default caching time is set to 24 hours. To set your own caching time use the static functions of Cache class.
