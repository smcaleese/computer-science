# virus.r

pos=0
infected=0

for(i in c(1:10000))
{
  # if they have the virus (15% chance)
  if(runif(1) <= 0.15)
  {
    infected[i] = 1
    if(runif(1) <= 0.95) pos[i] = 1 else pos[i] = 0
  }
  else
  {
    infected[i] = 0
    if(runif(1) <= 0.02) pos[i] = 1 else pos[i] = 0
  }
}

# the probability of getting a positive test is: P(pos)
# P(pos) = P(infected and positive) + P(non-infected and positive)
# infected -> positive and non-infected -> positive
# = P(infected)P(positive | infected) + P(non-infected)P(positive | non-infected)
0.15*0.95 + 0.85*0.02
# alternatively,
sum(pos)/10000

# the probability of having the virus given a positive test is: P(infected | pos)
# P(H|E) = P(H)P(E|H) / P(E)
pe <- 0.15*0.95 + 0.85*0.02
(0.15*0.95) / pe
# alternatively, 
# P(H|E) = P(H and E) / P(E)
sum(infected & pos) / sum(pos)

# the probability of having the virus given a negative test is: P(infected | !pos)
sum(infected & !pos) / sum(!pos)