import numpy as np

class NeuralNetwork(object):
    np.random.seed(107)
    def __init__(self, size_input, size_hidden, size_output):
        self.W1 = np.random.randn(size_input, size_hidden)
        self.W2 = np.random.randn(size_hidden, size_output)

np.random.seed(107)
NN = NeuralNetwork(2, 3, 1)

def sol():
    return np.array(NN.W1)

print(sol())