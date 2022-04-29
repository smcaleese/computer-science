import numpy as np

class NeuralNetwork(object):
    def __init__(self, size_input, size_hidden, size_output):
        self.W1 = np.random.randn(size_input, size_hidden)
        self.W2 = np.random.randn(size_hidden, size_output)

    def forward(self, X):
        self.z2 = np.dot(X, self.W1)
        self.a2 = self.sigmoid(self.z2)
        self.z3 = np.dot(self.a2, self.W2)
        y_hat = self.sigmoid(self.z3)
        return y_hat

    def sigmoid(self, z):
        return 1 / (1 + np.exp(-z))

    def sigmoid_prime(self, z):
        return np.exp(-z) / (1 + np.exp(-z))**2

    def cost(self, X, y):
        y_hat = self.forward(X)
        J = 0.5 * sum((y - y_hat) ** 2)
        return J

    def cost_prime(self, X, y):
        y_hat = self.forward(X)
        delta3 = np.multiply(-(y - y_hat), self.sigmoid_prime(self.z3))
        # .T transpose of the matrix
        dJdW2 = np.dot(self.a2.T, delta3)

        delta2 = np.dot(delta3, self.W2.T) * self.sigmoid_prime(self.z2)
        dJdW1 = np.dot(X.T, delta2)
        return dJdW1, dJdW2


# Input data
X = np.array(([3, 5], [5, 1], [10, 2]), dtype=float)
# output vector
y = np.array(([75], [82], [93]), dtype=float)
# scale the input and output data
X = X / np.amax(X, axis=0)
y = y / 100


def main():
    np.random.seed(107)
    NN = NeuralNetwork(2, 3, 1)
    dJdW1, dJdW2 = NN.cost_prime(X, y)
    new_W1 = NN.W1 - dJdW1
    print(new_W1)

if __name__ == '__main__':
    main()
