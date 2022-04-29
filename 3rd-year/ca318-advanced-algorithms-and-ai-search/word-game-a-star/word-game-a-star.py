from WordGameNode import WordGameNode
from string import ascii_lowercase
import sys
from time import time

def bfs(start, goal, valid_words):
    # We will start here, so the list of nodes to do is the start
    todo = [start]
    visited = set()
    num_searches = 0
    while len(todo) > 0:
        next = todo.pop(0)
        num_searches += 1
        if next.name == goal.name:
            return num_searches, next
        else:
            # Keep searching
            visited.add(next) # Remember that we've been here
            children = list(child for child in next.get_children(next, valid_words) if child not in visited)
            # use the length of the path from start to the current word to find the current depth:
            current_depth = len(next.get_path())
            todo += children
            for child in children:
                child.depth = current_depth + 1
                child.score = child.depth + child.heuristic(goal)
                todo.append(child)
        todo.sort(key=lambda x: x.score)
    return num_searches, None # no route to goal    

def all_lowercase(word):
    for letter in word:
        if letter not in ascii_lowercase:
            return False
    return True

def read_dictionary(filename, length):
    valid_words = set()
    with open(filename, 'r') as f:
        for line in f:
            line = line.strip()
            if len(line) == length and all_lowercase(line):
                valid_words.add(line)
    return valid_words

def main(args):
    if len(args) == 3:
        start_word = args[1]
        goal_word = args[2]
    else:
        start_word = input("Enter the start word: ")
        goal_word = input("Enter the goal word: ")
    assert len(start_word) == len(goal_word)

    valid_words = read_dictionary("/etc/dictionaries-common/words", len(start_word))

    if start_word not in valid_words or goal_word not in valid_words:
        print("invalid start or goal word!")
        return

    start = WordGameNode(start_word)
    goal = WordGameNode(goal_word)

    time1 = time()
    num_searches, end = bfs(start, goal, valid_words) # Do the breadth first search
    time2 = time()
    time_taken = time2 - time1
    print("time taken: {:.2f} seconds".format(time_taken))

    print("number of searches:", num_searches)
    if end == None:
        print("There is no path from {0} to {1}".format(start, goal))
    else:
        print("path: ", end.get_path())

if __name__ == "__main__":
   main(sys.argv)