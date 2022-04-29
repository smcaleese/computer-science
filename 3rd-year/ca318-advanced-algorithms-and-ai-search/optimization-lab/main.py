import sys
import math
import random
from hill_climb import hill_climb
from simulated_annealing import simulated_annealing

class DimensionRange():
    def __init__(self, mini, maxi):
        self.minimum = mini
        self.maximum = maxi

    def __str__(self):
        return "({}, {})".format(self.minimum, self.maximum)

    def __repr__(self):
        return str(self)

def cost(sol):
    x = sol[0]
    y = sol[1]

    error1 = 20 - x - y  # This error will be zero when x+y = 20
    error2 = 362 - x**2 - y**2 # this error will be zero when x**2+y**2 = 362
    total_error = error1 ** 2 + error2 ** 2
    #print(x, y, total_error)
    return total_error

def main(args):
    #random.seed(113)

    space = [DimensionRange(0, 20), DimensionRange(0, 20)]
    print(space)

    sol = simulated_annealing(space, cost)
    print("solution:", sol)

    #sol = simulated_annealing(space, cost, initial_T = 10.0, final_T = 1.0, cooling_rate = 0.9, max_step = 1)
    #print("Solution is {}, the cost is {}".format(sol, cost(sol)))

main(sys.argv)
