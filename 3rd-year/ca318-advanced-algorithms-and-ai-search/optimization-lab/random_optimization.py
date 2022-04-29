import math
from random import randint

def cost(sol):
    error = 81 - sol[0] ** 2
    return error ** 2  # Square the error ensures it is always positive.

def random_optimize(space, solution, steps):
    # Get a random solution
    random_sol = [randint(space[i].minimum, space[i].maximum) for i in range(len(space))]
    best_so_far = random_sol
    steps_taken = 1

    for j in range(1, steps):
        if random_sol[0] == solution:
            return best_so_far, steps_taken
        # Create a random solution
        steps_taken += 1
        random_sol = [randint(space[i].minimum, space[i].maximum) for i in range(len(space))]

        # is this better than our best so far
        if cost(random_sol) < cost(best_so_far):
            best_so_far = random_sol

    return best_so_far, steps_taken
