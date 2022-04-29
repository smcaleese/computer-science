import numpy as np

class NeuralNetwork():
    def __init__(self, size_input, size_hidden, size_output):
        self.W1 = np.random.randn(size_input, size_hidden)
        self.W2 = np.random.randn(size_hidden, size_output)

    def __str__(self):
        return "W1:\n {} \nW2:\n {}".format(self.W1, self.W2)

def sol():
    arr = np.array([[0.49399194, -0.65397777, -1.47497761], [0.34038658, -0.94363936, -0.43104416]])
    print("arr:")
    return arr

def main():
    np.random.seed(107)
    NN = NeuralNetwork(2, 3, 1)
    print(NN)
    print(sol())

if __name__ == "__main__":
    main()
