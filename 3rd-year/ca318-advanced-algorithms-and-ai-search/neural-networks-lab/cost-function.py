import numpy as np

def cost(y, y_hat):
    error = 0
    for i in range(len(y)):
        error += 0.5*((y[i][0] - y_hat[i][0])**2)
    return error

def sol():
    y = np.array([[1], [2]])
    y_hat = np.array([[2], [2]])
    return cost(y, y_hat)

print(sol())
