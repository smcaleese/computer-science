# Flip a coin ten times and record the result. Repeat this process 10,000 times and 
# plot the distribution.

x=0
for(i in c(1:10000))
{
  x[i] = sum(sample(c(0,1), 10, replace=T))
}

table(x)
plot(table(x), type="h", xlab="heads", ylab="N")