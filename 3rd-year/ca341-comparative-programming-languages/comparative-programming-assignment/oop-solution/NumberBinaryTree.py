# keys are phone numbers
class NumberNode:
    def __init__(self, number, name, address):
        self.number = number
        self.name = name
        self.address = address
        self.left = None
        self.right = None

class NumberBinaryTree:
    def __init__(self):
        self.root = None

    def search(self, number):
        # iterative implementation
        current_node = self.root
        while current_node != None and current_node.number != number:
            if number < current_node.number:
                current_node = current_node.left
            elif number > current_node.number:
                current_node = current_node.right
            else:
                raise Exception("error: duplicate in tree")
        if current_node != None and current_node.number == number:
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

    def add(self, number, name, address):
        self.root = self.r_add(self.root, number, name, address)

    def r_add(self, root, number, name, address):
        if root == None:
            self.root = NumberNode(number, name, address)
            return self.root
        # phone numbers which have smaller sum go left and vice versa
        if self.lessThan(number, root.number):
            root.left = self.r_add(root.left, number, name, address)
        elif self.greaterThan(number, root.number):
            root.right = self.r_add(root.right, number, name, address)
        else:
            raise Exception("error: duplicate in tree")
        return root

    def min_value_node(self, node):
        current = node
        # loop down to find the leftmost leaf
        while(current.left is not None):
            current = current.left
        return current

    def remove(self, number):
        self.root = self.r_remove(self.root, number)

    def r_remove(self, root, number):
        if root == None:
            return root
        if self.lessThan(number, root.number):
            root.left = self.r_remove(root.left, number)
        elif self.greaterThan(number, root.number):
            root.right = self.r_remove(root.right, number)
        else:
            # Node one or less children
            if root.left is None:
                temp = root.right
                root = None
                return temp

            elif root.right is None:
                temp = root.left
                root = None
                return temp
            # If the node has two children, replace it with the smallest
            # node in its right subtree
            temp = self.min_value_node(root.right)
            root.number = temp.number
            root.right = self.r_remove(root.right, temp.number)
        return root

    # how do you decide if a phone number if less than another?
    def lessThan(self, n1, n2):
        n1_sum = sum([int(char) for char in n1])
        n2_sum = sum([int(char) for char in n2])
        return n1_sum < n2_sum

    def greaterThan(self, n1, n2):
        n1_sum = sum([int(char) for char in n1])
        n2_sum = sum([int(char) for char in n2])
        return n1_sum > n2_sum
