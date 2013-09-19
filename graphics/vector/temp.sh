#!/bin/bash

file_list="$1"

for input_file in $(cat $file_list)
do
inkscape -f "$input_file" -d 22.5 -e "$input_file.png"
done
