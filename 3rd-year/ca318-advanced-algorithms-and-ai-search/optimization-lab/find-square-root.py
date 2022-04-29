import sys
import random
import math
from random import randint
from random_optimization import random_optimize, cost

class DimensionRange():
    def __init__(self, mini, maxi):
        self.minimum = mini
        self.maximum = maxi

    def __str__(self):
        return "({}, {})".format(self.minimum, self.maximum)

    def __repr__(self):
        return str(self)


def main(args):
    if len(args) >= 2:
        num_steps = int(sys.argv[-1])
    else:
        num_steps = 1000

    random.seed(113)

    # Wanna get the square root of 81. Easy problem but used here
    #   to test the random_optimse algorithm.
    # First, assume that the solution is between 1 and 81.
    space = [DimensionRange(1, 81)]  # Just one dimension in the range 1 to 81.
    print(space)
    solution = 9

    sol, steps_taken = random_optimize(space, solution, num_steps)
    print("Solution is {}, the cost is {}".format(sol, cost(sol)))
    print(f"Steps taken: {steps_taken}")

main(sys.argv)
