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

