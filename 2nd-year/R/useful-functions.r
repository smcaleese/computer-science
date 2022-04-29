grades1 = c(10, 20, 30, 35, 40, 42, 43, 50, 52, 57, 60, 65, 67, 68, 70, 71, 72, 73, 80, 85, 93)
grades2 = c(55, 60, 63, 70, 71, 75, 76, 76, 78, 80, 82, 86, 88, 89, 90, 90, 91, 93, 95, 98, 100)

mean(grades1)
median(grades1)
sd(grades1)
range(grades1)

summary(grades1)
table(grades1)

par(mfrow=c(1,1))

# create a vector of ten ones
rep(1, 10)

# to draw a point at (2, 2)
points(2, 2)

# mean(grades1, na.rm=T) for datasets with missing dataor datasets with missing data

# 60 white balls, 40 black balls, drawing 5 balls from the urn. c(0:5) shows the number of times 1, 2, 3, 4 
# and 5 white balls were drawn from the urn (think of a histogram buckets).
# this can also be done by sampling without replacement and then charting the resulting table
dhyper(c(0:5),60,40,5)

# create random number
runif(1, 0, 10) 

# create your own x-axis
plot(x, y, xaxt="n", type="l")
axis(side = 1, at = x, labels=T)

# add horizontal or vertical line to graph
abline(h = 40)
# or
abline(v = 40)