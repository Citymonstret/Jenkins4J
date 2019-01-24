# Jenkins4J
Simple and effective non-blocking Jenkins API wrapper written in Java

#### What can it do?
Jenkins4J supports the following actions:
- Get all publicly available jobs from a Jenkins instance
- Parse job data for builds and metadata
- Parse job data for artifacts and metadata

Jenkins4J does not *currently* support protected jobs, but if there is a demand for such 
functionality it can be added.

#### How does it work?
Jenkins4J uses the Jenkins [remote access JSON API](https://wiki.jenkins.io/display/JENKINS/Remote+access+API)
(in the future, XML support *may* be added). [Retrofit](https://github.com/square/retrofit) is used
as the HTTP client, [Gson](https://github.com/google/gson) is used for JSON deserialization and 
general [Guava](https://github.com/google/guava) utilities are used throughout Jenkins4J.

#### How to use it?
In order to get started, create a new `Jenkins` instance using a `JenkinsBuilder`, like this:
```java
final Jenkins jenkins = Jenkins.newBuilder().withPath("https://your.jenkins.path/").build();
```

Using this instance, you can fetch the master node, available jobs, specific job information and/or
specific build information. 

There are two types of objects that you will encounter when working with Jenkins4J:
- description
- info

A description is a simple member of an info class, and it will often just contain a name or a number.
You can use a description to get a (`CompletableFuture of`) an information class. Information classes
contain references to child nodes, and a bunch of meta data. This is to mirror how the Jenkins API works.

You can use description classes to retrieve their corresponding information classes, by using `getParent()`.
All information classes can also be retrieved using the Jenkins instance.

Retrieval of objects will always be non-blocking, and return `CompletableFuture`'s. 

##### Examples
Examples can be found in the [examples](https://github.com/Sauilitired/Jenkins4J/tree/master/examples/) directory.\
JavaDoc can be found in the [docs](https://github.com/Sauilitired/Jenkins4J/tree/master/docs/) directory.

##### Maven
Jenkins4J is using the JitPack maven repository. 

![JitPack Badge](https://jitpack.io/v/Sauilitired/Jenkins4J.svg)

To use it, add the following repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
and the following dependency:
```xml
<dependency>
    <groupId>com.github.Sauilitired</groupId>
    <artifactId>Jenkins4J</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Contributions &amp; Contact
Contributions are very welcome! The project uses the 
[Google Java](https://google.github.io/styleguide/javaguide.html) code style. The project is licensed 
under the MIT license.

If you have any further questions or feedback, then feel free to join our [Discord](https://discord.gg/ngZCzbU).\
If the project helped you, then you are free to give me some coffee money via [PayPal](https://www.paypal.me/Sauilitired)
:coffee:
