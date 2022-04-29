import torch
import numpy as np

data = [
    [1, 2],
    [3, 4]
]

# 1. create a tensor directly from data
x_data = torch.tensor(data)
print(x_data)

tensor1 = torch.tensor([
    [1, 1],
    [1, 1]
])
tensor1 = torch.tensor([
    [2, 2],
    [2, 2]
])

# 2. create a tensor from numpy
np_array = np.array(data)
x_np = torch.from_numpy(np_array)

x_ones = torch.ones_like(x_data) # retains the properties of x_data
print(f"Ones Tensor: \n {x_ones} \n")

x_rand = torch.rand_like(x_data, dtype=torch.float) # overrides the datatype of x_data
print(f"Random Tensor: \n {x_rand} \n")

tensor = torch.rand(3, 4)
print(f"tensor: {tensor}")

if torch.cuda.is_available():
      tensor = tensor.to('cuda')

print(f"Device tensor is stored on: {tensor.device}\n")

tensor = torch.ones(4, 4)
tensor[:,1] = 0
print(f"tensor: {tensor}")

t1 = torch.cat([tensor, tensor, tensor], dim=1)
print(t1)

# This computes the element-wise product
print(f"tensor.mul(tensor) \n {tensor.mul(tensor)} \n")
# Alternative syntax:
print(f"tensor * tensor \n {tensor * tensor}")

print(f"tensor.matmul(tensor.T) \n {tensor.matmul(tensor.T)} \n")
# Alternative syntax:
print(f"tensor @ tensor.T \n {tensor @ tensor.T}")

print(f'tensor: {tensor}')
print(f'transpose: {tensor.T}')

# in-place add:
tensor.add_(5)
print(f'new tensor: {tensor}')

# numpy array to tensor
n = np.ones(5)
t = torch.from_numpy(n)
print(f'n: {n}')
print(f't: {t}')
