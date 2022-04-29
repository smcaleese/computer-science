import numpy as np

# Feed-Forward Network Steps:
# 1. z(2) = X x W(1)
# 2. a(2) = f(z(2))
# 3. z(3) = a(2) x W(2)
# 4. y = f(z(3))

# Input data
X = np.array( ([3,5],[5,1],[10,2]) , dtype = float)

# output vector
y = np.array( ([75], [82], [93]) , dtype = float)

# scale the input and output data
#X = X / np.amax(X,axis=0)
#y = y / 100

# define network parameters
size_input = np.shape(X)[1] # number of columns of X = 2
#print(np.array( ([3,5],[5,1],[10,2])))
size_hidden = 3
size_output = 1

# Create the weights
np.random.seed(107)
W1 = np.random.randn(2, 3)

def sigmoid(z):
    return 1 / (1 + np.exp(-z))

def sol():
    # A dot product of the weights by the inputs provides the values for the next layer (z)
    z2 = np.dot(X, W1)
    #print(X, end="\n\n")
    print(W1, end="\n\n")
    #print(z2, end="\n\n")
    a2 = sigmoid(z2)
    return a2

print(sol())
