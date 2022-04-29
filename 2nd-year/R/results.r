resultsFile <- read.table(file="results.txt", sep=" ", quote="", comment.char="")

grades <- c()
for(i in seq(nrow(resultsFile)))
{
  total <- resultsFile$V2[i] + resultsFile$V3[i] + resultsFile$V4[i]
  grades[i] <- total
  print(grades[i])
}

hist(grades, breaks=10, main="CA269 CA Grades", xlab="%", ylab="number of students")
#abline(v = 87)
