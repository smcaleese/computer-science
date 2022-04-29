import torch
import numpy as np

c = np.ones(5)
c = 1
c = torch.from_numpy(c)
np.add(c, 1, out=c)
print(c)
print(c)
