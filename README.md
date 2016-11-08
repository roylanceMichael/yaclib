# yaclib

***


## What is this?

This is **Y**et **A**nother **C**ross **L**anguage **I**nterface **B**uilder.


***

## What? 


Here's the problem with N-Tier architectures: let's say you have this model that you want to serialize to a database, serve up in a Java back-end to a JavaScript UI. Normally, you have to have redundant models in each language which can be a maintenance nightmare (make a change to your ORM, make sure you make the change in JavaScript too, etc). Furthermore, if you change the rest contract definition, you have to update that in multiple places as well.

Yaclib is very similar to Google's GRPC. So to better understand it, understand the problem that GRPC is solving (https://github.com/grpc/grpc). 

Why not just use Google's GRPC? I did, but it has a few limitations: 


Its servers don't naturally serve up web pages (or, it's more work to configure it to run on port 80 and serve up many of the assets like html pages, css, etc). I want a server to have a UI viewable through the browser.


It doesn't support TypeScript naturally. I personally feel that any web development should be done with TypeScript, as there is nothing to lose and so much to gain with this approach. Yaclib supports building TypeScript interfaces from the get-go.

I like Java's servlet 3.0 architecture, especially coupled with Jersey. 

So Yaclib is like GRPC, except the server is running Java and it supports TypeScript protobufs.

***

## Okay, how does Yaclib solve that problem?

Yaclib solves that problem by keeping the model and rest communication definition in one place. So you make a change to the definition, then run the Yaclib process to update projects that use it as a dependency.

***

## What is the prerequisite knowledge to use Yaclib?


You must also understand how project structures, code compilation, packaging, and dependency resolution work with the following tools:

Java development with gradle (https://gradle.org/).

Java development with maven (https://maven.apache.org/).

JavaScript and TypeScript development with npm (https://www.npmjs.com/) (http://gulpjs.com/) (https://www.typescriptlang.org/) as well as Angular (https://angularjs.org/). Yaclib is currently using Angular 1.

(optional) CSharp development with the new dotnet core and nuget publishing (https://www.microsoft.com/net/core and https://www.nuget.org/).

(optional) Python development with pip and wheel (https://pypi.python.org/pypi/pip).

***

## How does it work?


First, Yaclib uses protocol buffers for two roles: model and controller (rest) definitions. In order to use Yaclib, you must understand how protocol buffers work (https://developers.google.com/protocol-buffers/).

The protobuf models created are serialized and used throughout any Yaclib application. 

Protocol messages that are in the controller protobuf file and have a suffix of controller have the following convention around them:

Rest services are built (currently in Java Jersey) as well as strongly typed interfaces. Yaclib will auto generate both for the server solution, and the connecting implementations for the Java, TypeScript, C#, and Python solutions.

## ... Okay, tell me more

There are several key projects for Yaclib to function. First is the api project. This is where the models and controller definitions (protobuf) live. 

Next are the client projects. Currently Yaclib supports Java (capi), TypeScript (javascript), C# (csharp), and Python (python). The Yaclib process will delete and auto generate each of those projects. For Java, it'll package the jar and deploy it to either Bintray or Artifactory (system credentials required). For C#, it'll publish to nuget if a nuget key is provided. For TypeScript, it'll deploy it to either NPM or Artifactory. Finally, Python currently just builds a wheel. 

This process also generates the Java rest server (sapi), currently using Jersey. The Java rest server is a maven project (embedded tomcat) that uses the models and controller definitions generated from the api. The sapi project also implements an Angular UI with the TypeScript connections generated earlier (that are published and consumed from either npmjs.org or the defined artifactory server).

The end result is a working framework to make changes safely between your Java back-end, C#, Python, TypeScript, and Java clients. 

The rest server uses post requests for everything, with each model being serialized/deserialized to a base64 string.

***

## How do I get started?

First, grab the UI application from the releases. Currently this only runs on Mac. On the first page, it will display if you have all the prerequisite programs installed.

![alt tag](https://raw.githubusercontent.com/roylanceMichael/yaclib/master/settings.png)

Second, fill out the information in the second tab (group name, repository, etc). Then click create project.

![alt tag](https://raw.githubusercontent.com/roylanceMichael/yaclib/master/start_project.png)

Next, open up the project with IntelliJ. 

Next, update the information in the gradle.properties files. This is currently filled out with default values. 

Next, make sure that you have either bintray or artifactory credentials to use. Set this in your environment variables (ARTIFACTORY_USER/ARTIFACTORY_PASSWORD or BINTRAY_USER/BINTRAY_KEY). 

Next, make sure you have access to the following programs in your path: gradle, maven (mvn), npm, tsc, pip, python, dotnet, nuget, tar. Yaclib uses all of these. The gradle plugin will check if they exist or not and not run if they don't exist (dotnet and nuget are optional, though).

Next, make changes to your model. Create messages. I suggest a standard request and response model for the controller as well.

Next, make changes to the controller protobuf. Define what a controller end-point should look like. 

Next, use the generate_proto.sh file to generate your model.

Next, build the solution.

Next, publish the solution to bintray or artifactory.

Next, run the "runYaclib" task in gradle. 

Next, update the sapi project definition. The controller you defined will have an auto-generated rest end-point as well as a service. The service will be an interface that will need an implementation. Create and implement that. As the controller changes, this implementation will need to be updated too.

## Anything else?
The following might be useful to reference. I currently use these (as well as many other private ones) for many applications I'm building:

https://github.com/roylanceMichael/yaorm

https://github.com/roylanceMichael/yadel

https://github.com/roylanceMichael/yaas

Usually with N-Tier applications there are more than just a web server (back-end and UI). There are mobile apps, desktop apps, workers, etc that will access this. The "yaclib.gradle" has ways to update those projects which consume the models as dependencies, build them, then publish them as well. For now, look at the following project yaclib.gradle files for more information:

This is a standard yaclib process. It just builds and publishes the capi, javascript, csharp, and python projects. It also builds and packages the sapi application (witht he Angular interface)
https://github.com/roylanceMichael/yaorm/blob/master/api/yaclib.gradle

This builds the cli, plugin, and ui projects, and publishes them as well each time yaclib is run.
https://github.com/roylanceMichael/yadel/blob/master/api/yaclib.gradle

This builds the common, redis, and yaorm projects, and publishes them as well (to npmjs.org and bintray).
https://github.com/roylanceMichael/yaas/blob/master/api/yaclib.gradle




This is a work in progress.

