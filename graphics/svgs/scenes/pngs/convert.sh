#!/bin/bash

file_list="$1"

for input_file in $(cat $file_list)
do
convert "$input_file.png" "$input_file.gif"
done
