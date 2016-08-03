#!/bin/bash

# use this to run pngquant (if installed) on wallpaper directory after completing the build
# -- this tool could be integrated into Java but... why --

find wallpaper/ -name '*.png' -print0 | xargs -0 -P8 -L1 pngquant --ext .png --force 4
