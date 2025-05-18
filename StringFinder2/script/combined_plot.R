#!/usr/bin/env Rscript

# สคริปต์ R สำหรับพล็อตกราฟ evaluate รวมทุกรอบ
library(ggplot2)

# ฟังก์ชันสำหรับพล็อตกราฟรวมและบันทึกเป็นไฟล์ PDF
plot_combined_evaluation <- function() {
  # ค้นหาไฟล์ CSV ทั้งหมด
  result_files <- list.files(path = "../result", pattern = "evaluate_[0-9]+\\.csv", full.names = TRUE)
  
  if (length(result_files) == 0) {
    cat("No evaluation files found in ../result directory.\n")
    quit(status = 1)
  }
  
  # อ่านข้อมูลจากทุกไฟล์และรวมเข้าด้วยกัน
  all_data <- data.frame()
  
  for (file in result_files) {
    # ดึงหมายเลขรอบจากชื่อไฟล์
    run_num <- as.integer(gsub(".*evaluate_([0-9]+)\\.csv", "\\1", file))
    
    # อ่านข้อมูล
    data <- read.csv(file)
    data$run <- paste("Run", run_num)
    
    # รวมข้อมูล
    all_data <- rbind(all_data, data)
  }
  
  # สร้างกราฟ
  p <- ggplot(all_data, aes(x = pass_no, y = evaluate, color = run)) +
    geom_line() +
    geom_point(size = 1) +
    labs(
      title = "Combined Evaluation Results",
      x = "Pass Number",
      y = "Evaluation Score (Lower is Better)",
      color = "Run"
    ) +
    theme_minimal() +
    theme(
      plot.title = element_text(hjust = 0.5, size = 14, face = "bold"),
      axis.title = element_text(size = 12),
      axis.text = element_text(size = 10),
      legend.position = "bottom"
    )
  
  # บันทึกกราฟเป็นไฟล์ PDF
  output_file <- "../result/combined_plot.pdf"
  ggsave(output_file, plot = p, width = 10, height = 8)
  
  cat(sprintf("Created combined plot file: %s\n", output_file))
  
  # สร้างกราฟเปรียบเทียบประสิทธิภาพ
  # ใช้ค่าต่ำสุดในแต่ละรอบเป็นตัวชี้วัด
  best_scores <- aggregate(evaluate ~ run, data = all_data, FUN = min)
  
  p2 <- ggplot(best_scores, aes(x = run, y = evaluate, fill = run)) +
    geom_bar(stat = "identity") +
    labs(
      title = "Best Score Comparison",
      x = "Run",
      y = "Best Score (Lower is Better)"
    ) +
    theme_minimal() +
    theme(
      plot.title = element_text(hjust = 0.5, size = 14, face = "bold"),
      axis.title = element_text(size = 12),
      axis.text = element_text(size = 10),
      legend.position = "none"
    )
  
  # บันทึกกราฟเป็นไฟล์ PDF
  output_file2 <- "../result/best_scores.pdf"
  ggsave(output_file2, plot = p2, width = 8, height = 6)
  
  cat(sprintf("Created best scores plot file: %s\n", output_file2))
}

# เรียกใช้ฟังก์ชัน
plot_combined_evaluation()

cat("Combined plotting complete.\n") 