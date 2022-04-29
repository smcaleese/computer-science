attach(sleep)

# Question 1
# par(mfrow=c(2,1))
# hist(extra[1:10], breaks=c(-4:6), xlab="extra", main="Drug 1")
# hist(extra[11:20], breaks=c(-4:6), xlab="extra", main="Drug 2")

# plot(extra[1:10], extra[11:20], xlim=c(-5,10), ylim=c(-5,10))
# abline(2, 1)

# Question 2
box <- c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1)

# a)
sample(box, 5, replace=T)
sample(box, 5, replace=F)

# b)
xWithRep <- c()
xWithoutRep <- c()

for(i in seq(1:10000))
{
  xWithRep[i] <- sum(sample(box, 5, replace=T))
  xWithoutRep[i] <- sum(sample(box, 5, replace=F))
}

par(mfrow=c(2,1))
table(xWithRep)
plot(table(xWithRep), type="l")
points(c(0:5), dbinom(c(0:5), 5, 0.25) * 10000, col="red")

table(xWithoutRep)
plot(table(xWithoutRep), type="l")
points(c(0:5), dhyper(c(0:5), 5, 15, 5) * 10000, col="red")