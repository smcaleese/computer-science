# shark population rapidly increases at first as the intial shark death rate is 5/100 and the birth rate
# is 1/100 (the eat rate). The rapid increase in the population of sharks causes the population of fish
# to crash which then causes the eat rate, shark birth rate and shark population to crash. This 
# cycle continues resulting in a shark peak just after the fish peak.
# fish population increases because of a low shark population -> shark population increases by feeding
# on the fish population -> fish population crashes along with shark population

nfish=20
nshar=5
for(i in c(2:3000))
{
  
  # multiply because if you double nfish or nsharks, the eat_rate will double
  # initially 0.1
  eat_rate = nfish[i-1] * nshar[i-1] / 1000 
  fish_eaten = rpois(1, eat_rate)
  
  # initially 0.1
  fish_b_rate = nfish[i-1] / 100
  nfish[i] = nfish[i-1] + rpois(1, fish_b_rate) - fish_eaten
  if(nfish[i] <= 0) nfish[i] = 5
  
  # initially 0.1
  shark_d_rate = nshar[i-1] / 100
  nshar[i] = nshar[i-1] - rpois(1, shark_d_rate) + fish_eaten
  if(nshar[i] <= 0) nshar[i] = 5
  
}

plot(nfish,ylim=c(0,50), type="l", xlab="months", ylab="fish")
points(c(1:3000),nshar,col='red', type="l")