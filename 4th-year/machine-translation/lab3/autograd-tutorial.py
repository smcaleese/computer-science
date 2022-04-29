import torch, torchvision
model = torchvision.models.resnet18(pretrained=True)

data = torch.rand(1, 3, 64, 64)
labels = torch.rand(1, 1000)
# print(data)
# print(labels)

prediction = model(data) # forward pass
# print(prediction)

# subtract vectors and find the sum of all the values
# in the resulting vector
loss = (prediction - labels).sum()
loss.backward() # backward pass, compute the gradient

# SGD with a learning rate of 0.01 and a momentum of 0.9
optim = torch.optim.SGD(model.parameters(), lr=1e-2, momentum=0.9)
optim.step()
