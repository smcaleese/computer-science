import sys
from Puzzle8Node import Puzzle8Node
from collections import deque

def bfs(start, goal):
       # We will start here, so the list of nodes to do is the start
   todo = [start]
   visited = []
   num_searches = 0
   while len(todo) > 0:
      next = todo.pop(0) # Get (and remove) first element in the list (using the list as a queue)
      num_searches += 1

      if next == goal:
         return num_searches, next
      else:
         # Keep searching.
         visited.append(next) # Remember that we've been here
         children = list(child for child in next.get_children() if child not in visited and child not in todo)
         todo += children
   return num_searches, None # no route to goal

def main(args):

   if len(args) == 3:
      start_string = args[1]
      goal_string = args[2]
   else:
      start_string = "2 7863451"
      goal_string = "2 6487351"
   assert len(start_string) == len(goal_string)

   print("Performing a greedy search from {} to {}".format(start_string, goal_string))

   start = Puzzle8Node(start_string)
   goal = Puzzle8Node(goal_string)
   num_searches, next = bfs(start, goal)
   print("Searches: {}: path = {}".format(num_searches, len(next.get_path())) )
   if next == goal:
      print("We dun it!")
   else:
      print(":[")

if __name__ == "__main__":
   main(sys.argv)

# start: " 12345678"
# goal: "247863 51"