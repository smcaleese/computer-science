class NameNode:
    def __init__(self, name, number, address):
        self.name = name
        self.number = number
        self.address = address
        self.left = None
        self.right = None

class NameBinaryTree:
    def __init__(self):
        self.root = None

    def search(self, name):
        # iterative implementation
        current_node = self.root
        while current_node != None and current_node.name != name:
            if name < current_node.name:
                current_node = current_node.left
            elif name > current_node.name:
                current_node = current_node.right
            else:
                raise Exception("error: duplicate in tree")
        if current_node != None and current_node.name == name:
            return [True, current_node.name, current_node.number, current_node.address]
        else:
            return False

    def inorderTraversal(self):
        self.r_inorderTraversal(self.root)

    def r_inorderTraversal(self, node):
        if node == None:
            return
        self.r_inorderTraversal(node.left)
        print(node.name + " : " + node.number + " : " + node.address)
        self.r_inorderTraversal(node.right)

    def add(self, name, number, address):
        self.root = self.r_add(self.root, name, number, address)

    def r_add(self, root, name, number, address):
        if root == None:
            self.root = NameNode(name, number, address)
            return self.root
        if name < root.name:
            root.left = self.r_add(root.left, name, number, address)
        elif name > root.name:
            root.right = self.r_add(root.right, name, number, address)
        else:
            raise Exception("error: duplicate in tree")
        return root

    def min_value_node(self, node):
        current = node
        # loop down to find the leftmost leaf
        while(current.left is not None):
            current = current.left
        return current

    def remove(self, name):
        self.root = self.r_remove(self.root, name)

    def r_remove(self, root, name):
        if root == None:
            return root
        if name < root.name:
            root.left = self.r_remove(root.left, name)
        elif name > root.name:
            root.right = self.r_remove(root.right, name)
        else:
            # Node with only one child or no child
            if root.left is None:
                temp = root.right
                root = None
                return temp

            if root.right is None:
                temp = root.left
                root = None
                return temp

            # If the node has two children, replace it with the smallest
            # node in its right subtree
            temp = self.min_value_node(root.right)
            root.name = temp.name
            root.right = self.r_remove(root.right, temp.name)
        return root
