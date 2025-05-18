#!/bin/bash

# สคริปต์สำหรับรันโปรแกรมและพล็อตกราฟทั้งหมด

# สร้างโฟลเดอร์ที่จำเป็น
mkdir -p ../bin ../result

echo "Compiling Java program..."
javac -d ../bin ../src/StringFinder.java

# รันโปรแกรม 4 รอบ
for i in {1..4}
do
    echo "Running StringFinder - Run $i"
    java -cp ../bin StringFinder
    sleep 1
done

echo "Running R script to generate plots..."
if [ -f "./plot_results.R" ]; then
    Rscript ./plot_results.R
else
    echo "Warning: plot_results.R not found in current directory"
    if [ -f "../script/plot_results.R" ]; then
        Rscript ../script/plot_results.R
    fi
fi

echo "All tasks completed. Results are in the 'result' directory."
echo "- CSV files: evaluate_XX.csv"
echo "- Plot files: plot_XX.pdf"
echo "- Timing information: time.txt" 