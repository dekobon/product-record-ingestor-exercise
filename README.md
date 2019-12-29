[![Build Status](https://travis-ci.org/dekobon/product-record-ingestor-exercise.svg?branch=master)](https://travis-ci.org/dekobon/product-record-ingestor-exercise) [![](https://images.microbadger.com/badges/version/dekobon/product-record-ingestor.svg)](https://microbadger.com/images/dekobon/product-record-ingestor "Get your own version badge on microbadger.com")
# Product Information Ingestion Library Exercise

## Overview

This project is an implementation of the Swiftly Systems 
[coding exercise](https://github.com/Swiftly-Systems/code-exercise-services)
in Java using the JDK 11.

For information regarding the design of the application please see the 
[design documentation](DESIGN.md).

### Build and Test
In order to build this project you need the following:

* [Java 11](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.x](https://maven.apache.org/)

From command line, go to the project source directory
```
$ mvn clean package verify
```

### Running Benchmarks

This repository uses [git-lfs](https://git-lfs.github.com/) to track test data 
files. You will need to enable git-lfs if you want to access the large test data
file used for benchmarking.

The easiest way to invoke the benchmark is to run it explicitly using 
maven failsafe (integration test plugin) by:

```
mvn -Dit.test=ProductRecordParseBenchmark verify
```

### Running the Application

#### Running the JAR file

To run the application as-is with all embedded dependencies, first build the
application using Maven and then execute the JAR file as follows from within
the project root: 

```
$ java -jar ./target/swiftly-exercise-1.0-SNAPSHOT-jar-with-dependencies.jar <path-or-url-to-data-file>
```

#### Running directly from Maven

To build, test, and execute the application all as a single step, you can run 
the following command in Maven from within the project root:

```
$ mvn clean package exec:java -Dexec.args="<path-or-url-to-data-file>"
```

#### Running from Docker

To run the application from Docker, execute Docker with an additional parameter
specifying the location of the data file:

```
docker run --rm -it product-record-ingestor '<path-or-url-to-data-file>'
```

Note that in this example, we execute with `-it` to give us an active terminal 
in the foreground and with `--rm` to clean up the container after execution has
finished.

If you want to test out the application with an existing remote data file, you
can run the following:

```
docker run --rm -it dekobon/product-record-ingestor 'https://media.githubusercontent.com/media/dekobon/product-record-ingestor-exercise/master/src/integration/resources/large-sample.txt'
```   

## Author

This project is solely the work of Elijah Zupancic.

## License

This project is licensed under the MPLv2. Please see the 
[LICENSE.txt](/LICENSE.txt) file for more details. 
