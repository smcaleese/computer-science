class Node:
    def __init__(self, item = -1, l=None, m=None, r=None):
        self.item = item
        self.left = l
        self.right = r;
        self.mid = m

    def is_terminal(self):
        return self.left == None and self.mid == None and self.right == None

    def evaluation(self):
        return self.item

    def num_children(self):
        total = 0
        if self.left:
            total += 1
        if self.right:
            total += 1
        if self.mid:
            total += 1
        return total

class Tree:
    """ An implementation of a Binary Search Tree """
    def __init__(self):
        self.root = None

    def add(self, item):
        if not self.root:
            self.root = Node(item)
        # BFS for a missing place
        queue = [self.root]
        while len(queue) > 0:
            current_node = queue.pop(0)
            # try and add the item as a child to the current_node
            if current_node.num_children() < 3:
                if not current_node.left:
                    current_node.left = Node(item)
                elif not current_node.mid:
                    current_node.mid = Node(item)
                elif not current_node.right:
                    current_node.right = Node(item)
                return
            else:
                # if you can't, then add all three children to the queue
                for child in [current_node.left, current_node.mid, current_node.right]:
                    queue.append(child)

def get_children(node):
    return [node.left, node.mid, node.right]

def r_print_tree(node):
    if node == None:
        return
    if node.is_terminal():
        print(node.item)
        return
    print(node.item)
    r_print_tree(node.left)
    r_print_tree(node.mid)
    r_print_tree(node.right)

def find_tree_depth(root):
    return r_find_tree_depth(root)

def r_find_tree_depth(node):
    if node is None:
        return 0
    if node.is_terminal():
        return 1
    return 1 + max(find_tree_depth(node.left), find_tree_depth(node.mid), find_tree_depth(node.right))

def minimax(tree):
    tree_depth = find_tree_depth(tree.root)
    print("tree depth:", tree_depth)
    return r_minimax(tree.root, tree_depth, True)

def r_minimax(node, depth, maximizingPlayer):
    # return the value of the node when a leaf is reached
    if depth == 1 or node.is_terminal():
        return node.evaluation()
    if maximizingPlayer:
        value = float('-inf')
        for child in get_children(node):
            if child:
                value = max(value, r_minimax(child, depth - 1, False))
        return value
    else:
        value = float('inf')
        for child in get_children(node):
            if child:
                value = min(value, r_minimax(child, depth - 1, True))
        return value

def main():
    # create tree
    tree = Tree()
    numbers_to_add = [-1, -1, 25, 27, 3, -1, 17, 33, 32, -1, 26, 20, 31]
    for num in numbers_to_add:
        tree.add(num)
    #r_print_tree(tree.root)
    print("max: ", minimax(tree))

if __name__ == "__main__":
    main()