import matplotlib.pyplot as plt
import numpy as np

# for every epoch from 0 to 10, the training time and accuracy outputted by the program (data is from screenshot in document)
training_times = np.array([0, 0.25, 0.48, 0.73, 0.98, 1.24, 1.62, 1.75, 1.98, 2.24, 2.43])
accuracies = np.array([0.0583, 0.9338, 0.9476, 0.9574, 0.9637, 0.9629, 0.9695, 0.9618, 0.9654, 0.9732, 0.9699])

epochs = np.array([x for x in range(0, 110, 10)])

plt.plot(epochs / 10, accuracies * 100, 'r')
plt.xlabel('epochs')
plt.ylabel('accuracy %')
plt.title('Relationship Between Training Epochs and Accuracy')
plt.show()

plt.plot(epochs / 10, training_times, 'b')
plt.xlabel('epochs')
plt.ylabel('training time (minutes)')
plt.title('Relationship Between Training Epochs and Training Time')
plt.show()
