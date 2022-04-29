import matplotlib.pyplot as plt
import numpy as np

iterations = [8, 16, 32, 64, 128, 256, 512, 1024]
costs = [64.732, 53.834, 28.231, 22.757, 21.818, 21.466, 20.991, 20.473]

plt.figure(figsize=(20, 10))
plt.plot(iterations, costs)
plt.xlabel("number of iterations")
plt.ylabel("cost")
plt.show()
