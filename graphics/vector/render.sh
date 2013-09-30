#!/bin/bash

file_list="$1"

for input_file in $(cat $file_list)
do
inkscape -f "$input_file.svg" -d 11.25 -e "$input_file.png"
done
