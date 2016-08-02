# CFB Wallpapers

## Purpose
To automate the creation of minimalistic wallpapers given that there is information available about the brand/color standards for NCAA schools. This helps those 
of us with *smaller* schools get in on the action when some cool graphics come out without having to bug people to make one specifically for a school (like mine) 
that has like 500 graduates per year. The idea is to be as inclusive as possible and help everyone find/get cool stuff to show school spirit.

A secondary purpose is to have a list/resource available that has accurate (school approved) colors for every school or at least as many schools as possible.

## Are you Crazy?
I used Java and YAML to do this. So... probably?

## I'm Not Going To Read The Rest
The output of this tool is hosted at http://cfb.ruffalo.org

## FAQ
**Q** Why isn't my school listed?

*A* The goal is to have all schools listed but it just hasn't been done yet. You can add your own if you want!

**Q** I don't like something about the way my school is listed/named/portrayed. How can I fix it?

*A* Please open an issue so we can address it. Most of the data comes from official school sources and wikipedia. That doesn't make it correct.

**Q** The accent color(s) for my school are terrible, why?

*A* I suck at colors. Submit an issue and it can get fixed.

**Q** Why did you do this with (YAML|Java)?

*A* Didn't you read above? I'm probably a little crazy to do something this way that Ruby, Python, or many other languages could do better. Truth is 
I'm more fluent in Java and don't have to think too hard to do it this way.

**Q** How do I add a school?

*A* You can open an issue or create a pull request. See the section called "How to Add A School" for more information on doing it yourself. We will be 
glad to help you. The most important thing to have is official documentation from the school on the colors in use, if it is available.

**Q** How do I add a template?

*A* You need to create an SVG file and a pull request or create an issue with the artifact. See the section "How to Add a Template" for more 
information. If you have a cool graphic idea but aren't familiar with SVG files then creating an issue is a great way to get help.

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
To output a specific conference (In this case SoCon and SEC):
```
java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar -s thecitadel -c socon -c sec
```
To output a specific division (In this case 'FCS' or Division I-AA):
```
java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar -s thecitadel -d fcs
```
To find your school(s)
```
java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar --list
```
For more advanced try usage, `java -jar target/cfb-wallpapers-1.0-SNAPSHOT.jar --help`.

## How to Add a School
Schools are in `src/main/resources/schools` under FCS or FBS and then the conference. The Citadel, for example, is an FCS school in the Southern Conference so 
it is located in `src/main/resources/schools/fcs/socon`. The `id` of the school and the file name for the YAML (`{id}.yml`) should be identical.

Each YAML file looks like this:
```
---
id: 'thecitadel'
name: 'The Citadel'
wiki: 'https://en.wikipedia.org/wiki/The_Citadel,_The_Military_College_of_South_Carolina'
url: 'http://www.citadel.edu/root/brandtoolbox-colors'
tags: ['bulldogs']
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

The `id` is unique to the school and is the short-name identifier of the school. The full `name` is used for printing status messages. The `url` is a helpful 
property so that other can come and find the same information and so that it can be checked for accuracy. The `wiki` property will be used in the web UI 
to generate a link to the school's Wikipedia page.

The tags list is a bit special and it uses single words (no spaces) to further enable the school to be searched for. In this case adding the mascot allows people 
to search just based on that (or other location or identifying words) instead of the name of the school.

The colors section is how the SVG is modified. Right now only a few colors (primary, secondary, alt) are supported. Each color scheme is listed separately with an 
`id` that determines where and how it will be named under the school's output folder. The color should be given in HEX notation (`#RRGGBB`) but RGB notation 
(`rgb(0-255,0-255,0-255)`) and CMYK (`cmyk(0-100,0-100,0-100,0-100)`) are supported as well.

  
Examples:
```
rgb(255,255,255) = #fffff
cmyk(0, 0, 0, 0) = #fffff
cmyk(100, 100, 100, 100) = #000000
```

## How to Add a Template
If you don't like the current design(s) then another can be added easily by creating an SVG file and saving it to `src/main/resources/templates/<target>`. The SVG should 
follow certain conventions:

* The primary color should be the hex color code `#61ed61`.
* The secondary color should be the hex color code `#b1b1b1`.
* The accent color should be the hex color code `#6f6f6f`.

It is important to note that these colors must match exactly and that other colors, if introduced, will not be modified in any way. Templates will be scaled to the 
size of the output. It is recommended that desktop templates be 4096x2160 and mobile templates be 2160x4096.

The two types of template output targets (mobile, desktop) are supported separately of one another.

