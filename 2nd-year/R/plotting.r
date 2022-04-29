par(mfrow=c(1, 3))

numBedrooms <- c(1, 2, 3, 4, 5)
prices <- c(100000, 250000, 550000, 750000, 1500000)
dataTable = data.frame(numBedrooms, prices)

# use dataTable$numBedrooms[5] to get the fifth element of the first column.
# read.table("data.txt", header=T) to read in text files

# Histogram takes a vector as input and finds thet frequency of each value in the vector
grades1 = c(10, 20, 30, 35, 40, 42, 43, 50, 52, 57, 60, 65, 67, 68, 70, 71, 72, 73, 80, 85, 93)
grades2 = c(55, 60, 63, 70, 71, 75, 76, 76, 78, 80, 82, 86, 88, 89, 90, 90, 91, 93, 95, 98, 100)
hist(grades1, breaks = 5)

# Box Plot
#par(mfrow=c(1, 2))
boxplot(grades1, grades2, main="Boxplot", xlab="grades1, grades2")

# Line Plot
plot(dataTable$numBedrooms, dataTable$prices, main="Relationship Between No. Bedrooms and Price", 
     xlab="Number of Bedrooms", ylab="$", type="b")

# Plotting
# types: p, l, b, h (point, line, both or histogram)
# set x and y-axis: xlim=c(1,20), ylim=c(1,5)