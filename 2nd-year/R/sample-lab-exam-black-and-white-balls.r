balls <- c(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

par(mfrow = c(1, 2))

v1 <- c()
for(i in 1:1000) {
  v1 <- c(v1, sample(balls, size = 5, replace = FALSE))
}

t1 = table(v1)
p1 = plot(t1, xlab = "ball color", ylab = "frequency", main = "Without Replace")


balls <- c(0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

v2 <- c()
for(i in 1:1000) {
  v2 <- c(v2, sample(balls, size = 5, replace = TRUE))
}

t2 = table(v2)
p2 = plot(t2, xlab = "ball color", ylab = "frequency", main = "With Replace")

