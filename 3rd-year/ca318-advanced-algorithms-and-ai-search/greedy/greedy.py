import sys
from Puzzle8Node import Puzzle8Node
from Permutation import Permutation

def greedy(start, goal, debug = False):
   # We will start here, so the list of nodes to do is the start
   start.score = start.heuristic(goal)
   done = False
   num_searches = 0
   next = start
   while not done:
      # Find the next position
      num_searches += 1

      if debug:
         print(num_searches)
         print(next)

      if next == goal:
         done = True
      else:
         # Keep searching.
         # What searching? This is a greedy search - there is no searching - just try the best child.
         children = next.get_children()
         # Get the score for each of these children
         for child in children:
            child.score = child.heuristic(goal)

            #print("\t\t{}".format(child))

         # Best child ...
         best = min(children, key = lambda x : x.score)
         #print("\t choose {}".format(best))
         if best.score < next.score:
            next = best # we improved, start from here.
         else:
            print(next.get_position())
            done = True # Our move made no progress ... hang our head in shame and give up.

   return num_searches, next

def main(args):
   '''
   p = Permutation()
   permutations = p.permute("12345678 ")
   goal = Puzzle8Node("247863 51")
   solutions = []
   i = 0
   for s in permutations:
      num_searches, next = greedy(Puzzle8Node(s), goal)
      if next == goal:
         solutions.append(s)
      if i == 10:
         break
      i += 1
   print(solutions)
   return
   '''

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
   num_searches, next = greedy(start, goal)
   #print("Searches: {}: path = {}".format(num_searches, next.get_path()) )
   print("searches:", num_searches)
   if next == goal:
      print("We dun it!")
   else:
      print(":[")

if __name__ == "__main__":
   main(sys.argv)

# start: " 12345678"
# goal: "247863 51"