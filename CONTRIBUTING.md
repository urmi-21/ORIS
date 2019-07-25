# Contributing

To contribute to ORIS, please fork this repository, make changes and then send a pull request.
Before contributing to this project please discuss the suggested changes via issues or email.


# Getting Started
Here, information is provided for contributers who would like to contribute to ORIS.

## Dependencies
jmathplot (https://github.com/yannrichet/jmathplot)

## Cloning ORIS
Use the command 
```
git clone https://github.com/urmi-21/ORIS.git
```

## Accessing the code
Netbeans IDE is suggested to open the ORIS project. ORIS is divided into two projects: _bioapp_ and _genelib_. 
_bioapp_ contains classes for the GUI components of ORIS. _genelib_ contains the computatinal methods which are accessed through the GUI. 

## Building
After opening _bioapp_ and _genelib_ projects first build the _genelib_ project. Then to build the _bioapp_ project, add _genelib.jar_ as a dependency. _jmathplot.jar_ is required for both projects.

