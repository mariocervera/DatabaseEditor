# Database Editor

This repository contains a set of Eclipse plug-ins that implement a graphical database editor. This
editor allows the user to define (as models) database schemas (i.e., the structure of databases in
terms of tables, views, columns, data types, primary keys, foreign keys, etc.) and also to automatically
generate (DDL) code for PostgreSQL. To develop this editor, I used Java and also model-driven Eclipse
technologies such as the Eclipse Modeling Framework (EMF), the Eclipse Graphical Modeling Framework (GMF),
and a model-to-text transformation language: Xpand.

The following video provides an introduction to the editor:

https://www.youtube.com/watch?v=kq8pBGw7kBc

The editor is distributed as part of the MOSKitt tool (https://www.prodevelop.es/en/products/MOSKitt). A Windows version of MOSKitt can be downloaded from:

https://www.dropbox.com/s/2l9c73qst7718yh/moskitt-1.3.10.zip?dl=0

The database editor was developed using version 3.5 of the Eclipse Modeling Tools. If you want to try the database editor outside of MOSKitt, you must perform the following steps:

1. Download the Eclipse Modeling Tools from http://www.eclipse.org/downloads/packages/release/Galileo/SR2
2. Import the plug-ins of this repository (both in the root folder and the DEPENDENCIES folder) into the Eclipse workspace.
3. Install the following dependencies into Eclipse: EMF Search 0.7.0, EMF SDO 2.4.3, and datatools 1.7.1.
4. Run a second instance of Eclipse (Run as -> Eclipse Application).
5. In this second instance, create a file of type "MOSKitt Sqlmodel Diagram".
