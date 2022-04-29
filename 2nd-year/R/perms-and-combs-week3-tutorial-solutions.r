# Q1: If passwords can consist of six letters, find the probability that a randomly chosen password
# will not have any repeated letters.

# P(password without any repeating letters) = no. of passwords without repeats / no. of all possible passwords including repeats
prod(26:21)/26^6

# Q2: How many ways can you get a sample of 6 letters with no repeated letters, if the order does
# not matter?

# permutations / r!
prod(26:21)/prod(6:1) 
#or
choose(26,6)

#Q3
x=0
for(i in c(1:10000))
# alternatively, you can use seq(1:10000)
{
  x[i]=sample(c(0, 1), 1, replace=T)
}
table(x)

#actually you don't need to use a for loop.
#you could use the following code
x=sample(c(0,1), 10000, replace=T)
table(x)

#Q4: If a class contains 60 females and 40 males and you choose a random sample of 5 students
#from the class, what is the probability of getting 5 females?

# P(first) = 60/100, P(second) = 59/99 and so on. Multiply all these probabilities to get the answer
prod(60:56)/prod(100:96)

#Q5: Use the sample command to simulate the situation in Question 4. Repeat the sample
#10,000 times. How often do you get 5 females?
y=0
for(i in c(1:10000))
{
  #you could represent females by 1 and males by 0
  s=sample(c(rep(1,60),rep(0,40)),5,replace=F)
  y[i]=sum(s)
  
  #alternatively you could represent females by "f" and males by "m"
  s=sample(c(rep("f",60),rep("m",40)),5,replace=F)
  y[i]=sum(s=="f")
}
table(y)

#you can compare this with the values for the hypergeometric distribution
par(mfrow=c(1, 2))
plot(table(y))
points(c(0:5), dhyper(c(0:5),60,40,5) * 10000)
#plot(dhyper(c(0:5),60,40,5), type="h", ylab="values", main="hypergeometric distribution")
#points(c(0:5), table(y), main="our distribution")