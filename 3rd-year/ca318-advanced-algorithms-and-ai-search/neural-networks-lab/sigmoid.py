import numpy as np

def sigmoid(z):
    return 1 / (1 + np.exp(-z))

def sol():
    input_matrix = np.array(np.array([[0.97, 0.23, 0.3],[0.46, 0.12, 0.05],[0.08, 0.61, 0.35],[0.23, 0.57, 0.5]]))
    print(input_matrix)
    print()
    print(sigmoid(input_matrix))

sol()