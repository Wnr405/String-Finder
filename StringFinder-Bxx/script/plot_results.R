#!/usr/bin/env Rscript

# สคริปต์ R สำหรับพล็อตกราฟ evaluate ในแต่ละรอบ
library(ggplot2)

# ฟังก์ชันสำหรับพล็อตกราฟและบันทึกเป็นไฟล์ PDF
plot_evaluation <- function(file_num) {
  # สร้างชื่อไฟล์
  input_file <- sprintf("../result/evaluate_%02d.csv", file_num)
  output_file <- sprintf("../result/plot_%02d.pdf", file_num)
  
  # อ่านข้อมูลจากไฟล์ CSV
  data <- read.csv(input_file)
  
  # สร้างกราฟ
  p <- ggplot(data, aes(x = pass_no, y = evaluate)) +
    geom_line(color = "blue") +
    geom_point(color = "red", size = 1) +
    labs(
      title = sprintf("Evaluation Results - Run %02d", file_num),
      x = "Pass Number",
      y = "Evaluation Score (Lower is Better)"
    ) +
    theme_minimal() +
    theme(
      plot.title = element_text(hjust = 0.5, size = 14, face = "bold"),
      axis.title = element_text(size = 12),
      axis.text = element_text(size = 10)
    )
  
  # บันทึกกราฟเป็นไฟล์ PDF
  ggsave(output_file, plot = p, width = 8, height = 6)
  
  cat(sprintf("Created plot file: %s\n", output_file))
}

# ตรวจสอบอาร์กิวเมนต์ที่ส่งเข้ามา
args <- commandArgs(trailingOnly = TRUE)

if (length(args) == 0) {
  # ถ้าไม่มีอาร์กิวเมนต์ ให้พล็อตทุกไฟล์ที่มีอยู่
  result_files <- list.files(path = "../result", pattern = "evaluate_[0-9]+\\.csv")
  
  if (length(result_files) == 0) {
    cat("No evaluation files found in ../result directory.\n")
    quit(status = 1)
  }
  
  # พล็อตแต่ละไฟล์
  for (file in result_files) {
    # ดึงหมายเลขรอบจากชื่อไฟล์
    run_num <- as.integer(gsub("evaluate_([0-9]+)\\.csv", "\\1", file))
    plot_evaluation(run_num)
  }
} else {
  # ถ้ามีอาร์กิวเมนต์ ให้พล็อตเฉพาะไฟล์ที่ระบุ
  run_num <- as.integer(args[1])
  plot_evaluation(run_num)
}

cat("Plotting complete.\n") 