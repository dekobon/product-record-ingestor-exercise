# Product Information Ingestion Library Design

## Application Design Overview

The product record parser works by incrementally reading lines from a character 
stream reader as a Java stream instance is iterated. Each iteration returns a
single ProductRecord object that maps to the data of a single line of the record
data (file).

Business logic for each record is accessible as methods on the ProductRecord
class.   

```
  +------------+    Memory Efficient Streaming Parser
  |Input Source|
  +-----+------+
        |
        |Progressively
        |read line by
        |line from source
        |
        |           Input source
        |           is only read
        |           when another
        |           record is
+-------v--------+  streamed       +----------------+
|Streaming Parser+----------------->Stream Iteration|
+----------------+                 |   By Client    |
                                   +----------------+
```

## First Principles

In this project, certain aspects of extensibility have been deliberately 
over-emphasized in order to demonstrate competence with extensibility
principles. This is a bit of a deviation from how I normally design 
applications - which is a balance between extensibility, ease of change, and not
over-doing things. Experience has taught me that what needs to be extended in 
an app in the future is unpredictable, so it is best not over-design nor to
under-design for scale or change. With that disclaimer in mind, you will find
the following design principles emphasized.

### Using IntelliJ annotations

Since this is an internal application that is not intended as a library, I 
brought in the IntelliJ annotations library to provide better clarity for the
API contracts in the code base.

### Dependency injection

Guice was used for dependency injection. I was on the fence about actually using
a DI framework for such a simple application because everything could have been
wired fine using inversion of control. However, DI allowed for the explicit
configuration of global settings that could be reconfigurable at runtime in a
clean manner. This was the approach taken for defining the tax rate. See
[ProductRecordingIngestorModule](src/main/java/com/useswiftly/ingestion/product/app/ProductRecordIngestorModule.java) for examples.

### Liberal use of interfaces

Extracting interfaces is so easy in modern IDEs that it often isn't necessary
to provide interfaces for most of your classes as it once was. That said, since
this project is intending to emphasize extensibility and SOLID principles, the
most classes were given an interface even if they only had a single 
implementation and didn't need to be mocked.     

### Using numeric data types that will not need to be changed in the future

For financial and business applications, that aren't pushing the performance
limits of systems, I prefer to use the BigXXX numeric types in Java because
I will be assured that we will never need to do a type migration and those
types are compatible with many mathematical and financial libraries. This is
why I chose to use BigInteger for `ProductID`, `RegularForX`, and 
`PromotionalForX`. Realistically, all of those types are fine as 32-bit signed
integers, but I wanted to emphasize forward thinking regarding extensibility.

### Using purpose designed monetary types

For pricing information, experience has taught me that there are numerous 
problems with using decimal or floating point types to represent currencies.
Whenever possible, I opt to use an actually currency type that represents 
different currencies and their various quirks. This is why I opted to use
JSR 354 for prices. By using it, it allows for extensibility when adding 
support for other currencies.

### Using configurable character sets

The character set of the product record data file is configurable, which allows
us to support other languages/character sets other than US-ASCII in the future.

### Using `BufferedReader` to read the data file

My initial approach for reading the data file was going to use an `InputStream`
or `Readable`. However, I deemed that overkill because it would require a lot
more complexity when doing a memory efficient, thread-safe, idiomatic Java
implementation. 

That said, it did introduce a serious trade-off in extensibility because the
current approach wouldn't work if we were to have to load a data-file that 
wasn't new-line delimited and/or used fixed byte boundaries for fields (not
characters). This is due to the abstraction of a `BufferedReader` using 
characters and not bytes. This works fine with US-ASCII where 1 character is
the same as a single byte. However, with multibyte encodings, this wouldn't
hold true. 
     
### Providing a memory efficient parser implementation

Experience has taught me that just throwing collections of objects in memory and
passing them around is not the way to build scalable software. It is far 
preferable to pass iterators or streams of objects/data such that the data can
be processed incrementally. With this principle in mind, the
`ProductRecordFileParser.parse()` method returns a Java 8 `Stream` such that
each record is returned incrementally as it is read.

This approach has the added advantage of supporting the parallelization in the
future if the per-record parsing computation is a bottleneck. A similar approach
is used throughout the project [Presto](https://prestodb.io/) where data read
is streamed into a buffer and multiple parsers simultaneously handle different
lines. In this model you see multiple CPUs light up nicely. I provided some
level of proof for the claim of a parallel processing model to be more efficient
within the benchmarking results provided in [ProductRecordParseBenchmark](src/integration/java/com/useswiftly/ingestion/product/app/ProductRecordParseBenchmark.java)
class. According to that benchmark, we see a marked improvement when using the 
Java `.parallel()` stream operator:

```
Benchmark                                    Mode  Cnt  Score   Error  Units
ProductRecordParseBenchmark.parallelStream  thrpt    5  14.806 ± 0.749  ops/s
ProductRecordParseBenchmark.singleThreaded  thrpt    5   4.253 ± 0.179  ops/s
```

### Decoupling business logic from domain entity logic

The typical mantra in domain driven design is to place business logic directly
in domain entities. This works well in most cases, except when you want to
reconfigure your business logic dynamically or your business logic needs to draw
upon external resources that have no business being injected into your domain
entity.

With this in mind, the business logic associated with the `ProductRecord` entity
is encapsulated into a closure (Java `Function`) and injected into the entity
such that it can be configured in a decoupled manner. Please note, this is a
deliberate emphasis to show off this principle and probably not actually needed
for this size of project.

### Decoupling domain entity metadata and parsing from entities

Each field on `ProductRecord` has an implementation of the `Field` interface
that contains the definition of where the field is located a given line within
the data file. Additionally, the implementations of `Field` contain a method to
convert from a String to the data type associated with the column and assign
that converted value to a given `ProductRecord` instance
(the `convertAndAssignValueToRecord()` method).

I had some internal debate regarding whether or not the metadata methods and the
the `convertAndAssignValueToRecord()` method were in violation of the single
responsibility principle. However, putting that logic within the field seemed to
be the cleanest representation without inflating the object graph.  

### Exception handling

Runtime and checked exceptions should be explicitly caught and chained to 
exceptions with messages that contain the runtime values of key variables so
that engineers debugging error reports can understand what state caused the
problem.

In the current implementation, any problem parsing the product records data
file will result in the entire process stopping. Ideally, we would wrap the
`ProductRecord` object in another object that would contain error state and
allow the consumer to aggregate all of the parsing errors encountered so that
a single error on a single record wouldn't cause the entire process to abort.

### Nullity

In the ProductRecord class many of the fields are Nullable because I wanted to
allow for the domain entity to be easily testable in parts without having to
populate all values. However, this does come with the risk of having to handle
more potential null values downstream in the application. This is an aspect of
the design, that I would give more thought to in a production application. 

### Further Improvements

* Add more javadocs and package-info docs.
* Slim down the Docker image by using the new Java modules system to have a
  lightweight JVM in conjunction with using an Alpine base image.
* Adding improved per-line error handling so that a single bad line does
  cause the application to exit 
* Adding a logging system like logback that supports SLF4J
* Adding a proper CLI help system
* Improving performance for per-line substring parsing - currently
  there are multiple reads of the same byte buffer per line
* Allow for configurable character buffer sizes for `BufferedReader` in
  order to better tune performance
* Move formatting of display prices outside of `ProductRecordImpl` into a 
  localization aware resource. This would allow for flexible redefinition of
  how the display price is formatted.
* Encapsulate `*displayPrice` and `calculate*CalculatorPrice`  methods as lambdas
  and allow them to be injected into `ProductRecordImpl` if there is a driving
  need to separate that logic from the domain entity.
* Tune the `ProductRecord` implementation to match the object relational mapping
  used for persistence. This implementation favors a domain entity model and
  by such a choice doesn't optimize for some persistence models. For more a
  better discussion on the trade-offs see [here](https://www.mehdi-khalili.com/orm-anti-patterns-part-4-persistence-domain-model). 
