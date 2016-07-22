# CFB Wallpapers

## Purpose
To automate the creation of minimalistic wallpapers given that there is information available about the brand/color standards for NCAA schools.

## Operations
There are YAML files for each school (`src/main/resources/schools`) and there are SVG templates (`src/main/resources/templates`). This program takes 
each school and replaces colors in the SVG file with colors specified in the YAML file. The output is then saved for the specific school.

## Building
This project has minimal dependencies and can be built with a simple `mvn clean package`.

## Running
The most simple way to run this tool, after building, is `java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar`. This will start a run for all of the schools. If only 
a single school (or multiple schools) are desired you can execute that with the `-s` option like this to generate wallpapers for The Citadel, VMI, and Auburn:
```
java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar -s thecitadel -s vmi -s auburn
```
To find your school(s)
```
java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar --list-schools
```
For more advanced try usage, `java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar --help`.

## How to add a School
Schools are in `src/main/resources/schools` under FCS or FBS and then the conference. The Citadel, for example, is an FCS school in the Southern Conference so 
it is located in `src/main/resources/schools/fcs/socon`. The `id` of the school and the file name for the YAML (`{id}.yml`) should be identical.

Each YAML file looks like this:
```
---
id: 'thecitadel'
name: 'The Citadel'
conference: 'socon'
url: 'http://www.citadel.edu/root/brandtoolbox-colors'
colors:
  - id: 'basic'
    primary: '#4d90cd'
    secondary: '#003263'
    accent: '#ffffff'
  - id: 'alternate'
    primary: '#003263'
    secondary: '#4d90cd'
    accent: '#ffffff'
```

The `id` is unique to the school and is the short-name identifier of the school. The full `name` is used for printing status messages. The `conference` title is to 
organize the schools. The `url` is a helpful property so that other can come and find the same information and so that it can be checked for accuracy.

The colors section is how the SVG is modified. Right now only a few colors (primary, secondary, alt) are supported. Each color scheme is listed separately with an 
`id` that determines where and how it will be named under the school's output folder. Each of the colors is listed in HEX which is the only supported mechanism at 
the current time.

## How to add a Template
If you don't like the current design(s) then another can be added easily by creating an SVG file and saving it to `src/main/resources/templates`. The SVG should 
follow certain conventions:

* The primary color should be the hex color code `#61ed61`.
* The secondary color should be the hex color code `#b1b1b1`.
* The accent color should be the hex color code `#6f6f6f`.

It is important to note that these colors must match exactly and that other colors, if introduced, will not be modified in any way. Templates will be scaled to the 
size of the output. It is recommended that templates be 4096x2160.