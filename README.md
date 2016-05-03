# RJavaServer [![Build Status](https://travis-ci.org/jfcorugedo/RJavaServer.svg?branch=master)](https://travis-ci.org/jfcorugedo/RJavaServer) [![Coverage Status](https://coveralls.io/repos/github/jfcorugedo/RJavaServer/badge.svg?branch=master)](https://coveralls.io/github/jfcorugedo/RJavaServer?branch=master)
Java server that can call R functions using JRI and rJava packages

## Requirements

This server needs an installation of R in the local OS, as well as the rJava package installed:

* Install R in your machine (see https://cran.r-project.org/)
* Install rJava using R console: type `install.package("rJava")` inside R console
* Install Rserve using R console: type `install.package("Rserve")` inside R console
 
Once these steps are done, you have to configure some variables inside your local system depending on the approach you're going to use.

### Rserve:

* **R_HOME**: Pointing to your local R installation (Ej: /Library/Frameworks/R.framework/Resources)

### JRI:

* **R_HOME**: Pointing to your local R installation (Ej: /Library/Frameworks/R.framework/Resources)
* **LD_LIBRARY_PATH**: Pointing to R lib directory as well as JRI directory (EJ: /app/vendor/R/lib/R/lib:/app/vendor/R/lib/R/bin)

To sum up, look at the content of the script $R_HOME/library/rJava/jri/run and configure your local system usign the same variable names and values.

Moreover, the server must be started with the JVM parameter -D-Djava.library.path pointing at the JRI installation directory, ie:

    $JAVA_HOME/bin/java -Dserver.port=$PORT -Djava.library.path=/app/vendor/R/lib/R/library/rJava/jri/ -jar target/RJavaServer.jar

If you have trouble finding this directory, try to look for the JRI SO library: 

find / -name "libjri.*"

## Heroku
In order to execute this server in Heroku, a Procfile has been added to the project. It can be deployen to any Heroku applications that has R environment installed.

To install R inside a heroku application, you have to use multipack-buildpack and r-buildpack.

To install this project into a Heroku server, follow theses steps:

1. Create a new application that uses multipack buidpack typing in the command line:

    heroku create --stack cedar-14 --buildpack https://github.com/heroku/heroku-buildpack-multi.git --app RJavaServer
    
2. Create a file called `.buildpacks` inside your project, and type here all the Heroku buildpacks you want to install in your environment (**_this project already contains this file_**)

3. In addition to your application code, you must tell R which packages must be installed. This task is done inside a file called init.r (**_this project already contains a init.r script_**)


## Usage

Right now, the REST API has only very basic operations, but you can extend it to call whatever R function you want. Moreover, you can create your own R script and load it using `engine.parseAndEval("source(\"/yourpath/yourscript.r\"")`. (See an example at UseREngineInFrontOfJRIEngineTest)

To test the server, just try to execute a POST command over the `mean` resource.

Curl example: 

`curl http://localhost:8080/sqrt -X POST -d "5" -H "Content-type: application/json" -i``

It should return:

    HTTP/1.1 201 Created
    Server: Apache-Coyote/1.1
    X-Application-Context: application:local:8080
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Sun, 20 Sep 2015 15:46:45 GMT

    2.23606797749979
    
    
## Datadog

This project has a module that automatically sends metrics to Datadog, an online monitoring system.

To enable this feature, just create an account in Datadog (https://www.datadoghq.com/) and put your API Key in the configuration file (application.yml):

    metrics:
      apiKey: <your API key>
      host: <host of this app (optional)>
      period: <time, in seconds, between events>
      enabled: true
