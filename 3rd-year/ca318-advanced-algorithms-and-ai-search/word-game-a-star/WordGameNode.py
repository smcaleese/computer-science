import os
from string import ascii_lowercase

class WordGameNode():
    def __init__(self, name, parent = None):
        # Ensure lowercase letters (no digits or special chars)
        for letter in name:
            assert letter in ascii_lowercase
        self.name = name
        self.parent = parent
        self.score = None

    def __str__(self):
        return "{}".format(self.name)

    def get_one_char_mutations(self, word):
        alphabet_list = list(ascii_lowercase)
        mutations = []
        for i in range(len(word)):
            for c in alphabet_list:
                if c != word[i]:
                    new_mutation = word[:i] + c + word[i + 1:]
                    mutations.append(new_mutation)
        return mutations

    def heuristic(self, goal):
        # find the distance between self and goal
        # the score should be high if the word stored by self is not close to the goal
        # if self.name is similar to goal, the score should be low
        # find the number of characters in common, then subtract from length
        score = 0
        for i in range(len(self.name)):
            if self.name[i] == goal.name[i]:
                score += 1
        cost = len(self.name) - score
        return cost

    def get_children(self, parent, valid_words):
        return [WordGameNode(name, parent) for name in self.get_one_char_mutations(str(self)) if name in valid_words]

    def get_path(self):
        path = []
        current_node = self
        while current_node != None:
            path.append(current_node)
            current_node = current_node.parent
        visible_path = ["{}".format(node.name) for node in path]
        return visible_path
