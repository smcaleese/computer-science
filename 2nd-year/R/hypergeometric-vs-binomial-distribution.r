#15 white balls and 5 black balls
balls <- c(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1)


x <- c()   # with replacement, creates a binomial distribution
y <- c()   # without replacement, creates a hypergenomic distribution

for(i in seq(1:10000))
{
  x[i] = sum(sample(balls, 5, replace=T))
  y[i] = sum(sample(balls, 5, replace=F))
}

par(mfrow=c(1,2))
table(x)
plot(table(x), ylim=c(0,5000))
points(c(0:5), dbinom(c(0:5), 5, 0.25) * 10000, type="b")

table(y)
plot(table(y), ylim=c(0,5000))
points(c(0:5), dhyper(c(0:5), 5, 15, 5) * 10000, type="b")



# Explanation
# the dhypher graph (without replacement) is "thinner" than the binomial distribution. Getting zero black 
# balls (1's) is less likely without replacement because as more zeros are removed, the probability of 
# getting a one increases. With dypher, the probability of getting all ones is also lower because each 
# one is removed every time.