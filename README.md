# Parser

Parser for "aboutyou.de". Can parse carts from search by the keyword.

### Usage: ###
Run in console with an argument "java -jar yourProgram.jar shirt+only" and parsing start immediately.
Or run program without arguments "java -jar yourProgram.jar" and parser ask you to enter keyword for search.

After finishing result saves to offers.xml.
You can see log info in parser.log.

Parser tested on search request with __8__ search pages, __750__ product pages and __3773__ product variations(color&size).
Request: "_shirt+only_". URL: https://www.aboutyou.de/suche?category=20201&term=shirt+only.

### Some important log lines: ###
```12:45:47 |  INFO | ggalantsev.Main | All search urls (750) collected in 9726 ms.
12:52:24 |  INFO | ggalantsev.Main | Parsing finished in 406630 ms.
12:52:24 |  INFO | ggalantsev.Main | Amount of triggered HTTP request: 758.
12:52:24 |  INFO | ggalantsev.Main | Amount of extracted products: 3773.
12:52:24 |  INFO | ggalantsev.Main | Memory Footprint: 29698 kilobytes.
```

Executable jar, log and xml example you can find [here](https://drive.google.com/drive/folders/0BwH0ZuFp6vh4aVQteDg1Z2JLd1U).
