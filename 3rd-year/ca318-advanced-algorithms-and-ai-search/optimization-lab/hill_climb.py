from random import randint

def hill_climb(space, cost):
    # Create a random start solution
    current_sol = [randint(space[i].minimum, space[i].maximum) for i in range(len(space))]
    # print("cs:", current_sol)
    # Main loop
    updates = 0
    while True:
        # Create list of neighbouring solutions
        neighbours = []

        print("cs:", current_sol)
        for i in range(len(space)):  # for each dimension
            # One away in each direction
            if current_sol[i] > space[i].minimum:
                # keep all dimensions the same except for dimension i which should be changed by +1 and -1
                neighbours.append(current_sol[0:i] + [current_sol[i] - 1] + current_sol[i + 1:])
                #print(current_sol[0:i] + [current_sol[i] - 1] + current_sol[i + 1:])
            if current_sol[i] < space[i].maximum:
                neighbours.append(current_sol[0:i] + [current_sol[i] + 1] + current_sol[i + 1:])
                #print(current_sol[0:i] + [current_sol[i] + 1] + current_sol[i + 1:])
            # two dimensions, two variations each (+1 and -1), four neighbours
            # print()

        # See what the best solution amongst the neighbours is
        #print(neighbours)
        #print(current_sol)
        best_neighbour = neighbours[0]
        for neighbour in neighbours[1:]:
            if cost(neighbour) < cost(best_neighbour):
                best_neighbour = neighbour

        if cost(best_neighbour) < cost(current_sol):
            updates += 1
            current_sol = best_neighbour
        else:
            # If there's no improvement, then we've reached the top
            break

    return current_sol, updates
