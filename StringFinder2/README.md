# String Finder

โปรแกรมค้นหาสตริงที่กำหนดโดยใช้อัลกอริทึมพันธุกรรม (Genetic Algorithm)

## วิธีการทำงาน

โปรแกรมนี้ใช้อัลกอริทึมพันธุกรรมในการค้นหาสตริงเป้าหมายที่ถูกกำหนดไว้ในคลาส Secret โดยมีขั้นตอนดังนี้:

1. สร้างประชากรเริ่มต้นแบบสุ่ม
2. ประเมินแต่ละสตริงในประชากรโดยใช้เมธอด evaluate() ของคลาส Secret
3. คัดเลือกสตริงที่ดีที่สุดไว้ (Elitism)
4. สร้างประชากรรุ่นใหม่โดยใช้การคัดเลือกแบบทัวร์นาเมนต์ (Tournament Selection)
5. ใช้การผสมพันธุ์ (Crossover) และการกลายพันธุ์ (Mutation) เพื่อสร้างสตริงใหม่
6. ทำซ้ำจนกว่าจะพบสตริงที่ถูกต้อง (evaluate() คืนค่า 0)

## วิธีการใช้งาน

### การคอมไพล์และรัน

```bash
# คอมไพล์
javac -d bin src/StringFinder.java

# รัน
cd bin
java StringFinder
cd ..
```

### การรันบน SLURM

```bash
# รันโปรแกรม 4 รอบ
sbatch script/run_string_finder.sh
```

### การสร้างกราฟ

```bash
# สร้างกราฟแยกแต่ละรอบ
Rscript script/plot_results.R

# สร้างกราฟรวมทุกรอบ
Rscript script/combined_plot.R
```

## ผลลัพธ์

- ไฟล์ CSV บันทึกผลการประเมิน: `./result/evaluate_XX.csv`
- ไฟล์กราฟ: `./result/plot_XX.pdf`
- ไฟล์กราฟรวม: `./result/combined_plot.pdf`
- ไฟล์เปรียบเทียบคะแนนที่ดีที่สุด: `./result/best_scores.pdf`
- ไฟล์บันทึกเวลา: `./result/time.txt`

## การปรับแต่งพารามิเตอร์

สามารถปรับแต่งพารามิเตอร์ต่อไปนี้ในคลาส StringFinder:

- `POPULATION_SIZE`: ขนาดของประชากร
- `MUTATION_RATE`: อัตราการกลายพันธุ์
- `TOURNAMENT_SIZE`: ขนาดของทัวร์นาเมนต์ในการคัดเลือก
- `LOG_FREQUENCY`: ความถี่ในการบันทึกผลการประเมิน 