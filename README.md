Build Status
====================
[![Build Status](https://travis-ci.org/izmailoff/MongoDB-Schema-Analyzer.png?branch=master)](https://travis-ci.org/izmailoff/MongoDB-Schema-Analyzer)

Abstract
====================

This tool allows you to print all unique JSON ASTs from
a MongoDB collection.

Project Structure
====================

It contains:
* SBT project files
* Scala source code
* Scala unit tests source code
* No binaries or jar files except for SBT launcher

Dependencies
====================

Requires mongod server process running on:

    localhost:27017

Tests can be run without MongoDB server.
All Scala dependencies (JARs) will be downloaded by SBT.

MongoDB Installation Instructions
====================
Ubuntu
-------------
run in shell:

    sudo apt-get install mongodb
    vim /etc/mongodb.conf  # uncomment port
    service mongodb restart

Redhat/Fedora
-------------
run in shell:

    sudo yum install mongodb-server mongodb
    service mongod start

How To Run Applications
====================

SBT
-------------
Go to the project root directory.

Download SBT runner by running:

    ./get_sbt.sh

Now type `./sbt`. After this you can issue commands in SBT prompt:

    ; clean; compile; test; run

Alternatively you can type in shell:

    ./sbt update clean compile test run

The easiest way to run apps is to type `run` in SBT and select app number from the list.

JAR
-------------
Generate a jar that contains all dependencies with SBT command:

    oneJar

Afterwards run it like a regular jar (default main class will be set):

    java -jar target/scala-2.12/mongodbschema_2.12-0.0.3-one-jar.jar <collection name>

Connection properties can be set in default.props. To change connection settings at runtime
 add `default.props` file to your current directory similar to one found in `src/main/resources/props`.

Tests and Examples
====================
Let's insert a few documents into a test collection using mongo shell:

    > 
    > db.docs.insert({name: "Alex", age: 123})
    > db.docs.insert({name: "Alex", age: 123})
    > db.docs.insert({name: "Alex", age: 123})
    > db.docs.insert({name: "Bob"})
    > db.docs.insert({age: 123, name: "Alex"})
    > db.docs.insert({tags: ["one", "two", "three"]})
    > db.docs.insert({tags: ["one", "two", "three"]})
    > db.docs.insert({tags: ["one", "two"]})
    > db.docs.insert({tags: ["one", "two", "something else"]})
    > db.docs.insert({tags: [12, 23434, 344]})
    > db.docs.insert({tags: [12, 23434, 344]})
    > db.docs.insert({tags: [12, "23434", 344]})
    > db.docs.insert({tags: [12, "23434", 344]})
    > db.docs.insert({tags: [12, 23434, "344"]})

Find returns these docs which we just inserted:

    > db.docs.find()
    { "_id" : ObjectId("516eaf267173e5159f553038"), "name" : "Alex", "age" : 123 }
    { "_id" : ObjectId("516eaf277173e5159f553039"), "name" : "Alex", "age" : 123 }
    { "_id" : ObjectId("516eaf277173e5159f55303a"), "name" : "Alex", "age" : 123 }
    { "_id" : ObjectId("516eaf2d7173e5159f55303b"), "name" : "Bob" }
    { "_id" : ObjectId("516eaf317173e5159f55303c"), "age" : 123, "name" : "Alex" }
    { "_id" : ObjectId("516eaf4e7173e5159f55303d"), "tags" : [ "one", "two", "three" ] }
    { "_id" : ObjectId("516eaf4f7173e5159f55303e"), "tags" : [ "one", "two", "three" ] }
    { "_id" : ObjectId("516eaf537173e5159f55303f"), "tags" : [ "one", "two" ] }
    { "_id" : ObjectId("516eaf607173e5159f553040"), "tags" : [ "one", "two", "something else" ] }
    { "_id" : ObjectId("516eb8fc7173e5159f553041"), "tags" : [ 12, 23434, 344 ] }
    { "_id" : ObjectId("516eb8fe7173e5159f553042"), "tags" : [ 12, 23434, 344 ] }
    { "_id" : ObjectId("516eba217173e5159f553043"), "tags" : [ 12, "23434", 344 ] }
    { "_id" : ObjectId("516eba267173e5159f553044"), "tags" : [ 12, "23434", 344 ] }
    { "_id" : ObjectId("516eba2f7173e5159f553045"), "tags" : [ 12, 23434, "344" ] }

Now if we run the tool we get the following output:

    {
      "_id":{
        "$oid":"String"
      },
      "name":"String"
    }
    {
      "_id":{
        "$oid":"String"
      },
      "tags":["Double","String"]
    }
    {
      "_id":{
        "$oid":"String"
      },
      "name":"String",
      "age":"Double"
    }
    {
      "_id":{
        "$oid":"String"
      },
      "tags":["String"]
    }
    {
      "_id":{
        "$oid":"String"
      },
      "tags":["Double"]
    }

These are all unique ASTs/document schemas that the tool found.

Another example of running this tool on `fs.chunks` (GridFS) - binary data:

    {
      "_id":{
        "$oid":"String"
      },
      "files_id":{
        "$oid":"String"
      },
      "n":"Integer",
      "data":"<Binary Data>"
    }
