forecast_days=60

# todo
# why is minf negative?

ninf=1        # running total number of infections
nimm=0        # running total number of immune patients        
pop=100000
sick_people=12

# every infected person is infected for 12 days before they recover. 
# when they recover, ninf will decrease by 1 and nimm will increase by 1

everyone_infected = FALSE
index=2
for(i in c(2:forecast_days))
{
  # 1. add new infections to vector of sick people (append 12's)
  
  if(everyone_infected == FALSE)
  {
  minf = ninf[i - 1] * (pop - nimm[i - 1]) / 250000    # infected * healthy
  new_inf = rpois(1, minf)
  new_inf = round(new_inf, digits = 0)
  
  #print(new_inf)
  ninf[i] = ninf[i - 1] + new_inf
  if(ninf[i - 1] + new_inf >= pop) 
  {
    new_inf = 0
    ninf[i] = pop
    everyone_infected = TRUE
  }
  
  # add new infections to vector
  if(new_inf >= 1)
  {
    end = (index + new_inf) - 1
    for(j in c(index:end))
    {
      sick_people[j] = 12
    }
    index = end + 1 
  }
  }

  # 2. check for zeros
  new_imm = 0
  for(k in c(1:length(sick_people)))
  {
    if(sick_people[k] == 0)
    {
      new_imm = new_imm + 1         # increase number of immune people 
      sick_people[k] = -1           # don't count again
      ninf[i] = ninf[i - 1] - 1     # decrease number of infected people
    }
  }
  nimm[i] = nimm[i - 1] + new_imm
  
  # 3. subtract from every case
  for(m in c(1:length(sick_people)))
  {
    if(sick_people[m] >= 1)
    {
      sick_people[m] = sick_people[m] - 1
    }
  }
}

print(ninf)
print(length(ninf))
print(length(sick_people))
print(nimm)

par(mfrow=c(1,1))
plot(c(1:forecast_days), ninf, type="b", main="Total Cases Over 1000 Days", xlab="day", ylab="cases")

