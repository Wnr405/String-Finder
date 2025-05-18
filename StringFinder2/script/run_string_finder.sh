#!/bin/bash
#SBATCH --job-name=string_finder
#SBATCH --output=string_finder_%j.out
#SBATCH --error=string_finder_%j.err
#SBATCH --ntasks=4
#SBATCH --time=00:05:00

# สร้างโฟลเดอร์ result ถ้ายังไม่มี
mkdir -p ../result

# คอมไพล์โปรแกรม
javac -d ../bin ../src/StringFinder.java

# รันโปรแกรม 4 รอบ
for i in {1..4}
do
    echo "Running task $i"
    java -cp ../bin StringFinder
    sleep 1
done

echo "All tasks completed" 