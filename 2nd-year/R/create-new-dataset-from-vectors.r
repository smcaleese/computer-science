par(mfrow = c(1, 1))

numBedrooms <- c(1, 2, 3, 4, 5)
prices <- c(100000, 250000, 550000, 750000, 1500000)

dataTable = data.frame(numBedrooms, prices)

plot(dataTable$numBedrooms, dataTable$prices, main="Relationship Between No. Bedrooms and Price", 
     xlab="Number of Bedrooms", ylab="$", type="b")