# six phone calls per minute
# 6/60 -> 1/10

x <- c()
for(i in seq(1:10000)) 
{
  x[i] = sum(sample(c(1,0), 60, replace=T, prob=c(1,9)))
}

table(x)

par(mfrow=c(1,1))
plot(table(x))
points(c(0:20), dpois(c(0:20), 6) * 10000)