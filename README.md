# RJavaServer
Java server that can call R functions using JRI and rJava packages

## Requirements

This server needs an installation of R in the local OS, as well as the rJava package installed:

* Install R in your machine (see https://cran.r-project.org/)
* Install rJava using R console: type `install.package("rJava")` inside R console
 
Once these steps are done, you have to configure some variables inside your local system:

* **R_HOME**: Pointing to your local R installation (Ej: /Library/Frameworks/R.framework/Resources)
* **LD_LIBRARY_PATH**: Pointing to R lib directory as well as JRI directory (EJ: /app/vendor/R/lib/R/lib:/app/vendor/R/lib/R/bin)

To sum up, look at the content of the script $R_HOME/library/rJava/jri/run and configure your local system usign the same variable names and values.

More over, the server must be started with the JVM parameter -D-Djava.library.path pointing at the JRI installation directory (Ej: -Djava.library.path=/app/vendor/R/lib/R/library/rJava/jri/).

If you have trouble finding this directory, try to look for the JRI SO library: 

find / -name "libjri.*"

## Usage

Right now, the REST API has only very basic operations, but you can extend it to call whatever R function you want. Moreover, you can create your own R script and load it using `engine.parseAndEval("source(\"/yourpath/yourscript.r\"")`. (See an example at UseREngineInFrontOfJRIEngineTest)

To test the server, just try to execute a POST command over the `mean` resource.

Curl example: 

`curl http://localhost:8080/mean -X POST -d "[1,2,3,4,5,6,7,8,9.345]" -H "Content-type: application/json" -i``

It should return:

    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    X-Application-Context: application:8080
    Content-Type: application/json;charset=UTF-8
    Transfer-Encoding: chunked
    Date: Tue, 08 Sep 2015 15:50:20 GMT
    
    5.038333333333333
    
    
