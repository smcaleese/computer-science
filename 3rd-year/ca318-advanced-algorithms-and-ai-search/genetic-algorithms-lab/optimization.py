from random import randint
from random import random
from random import shuffle

def mutate(gene, space, max_step):
    # choose a spot to mutate
    print(gene)
    spot = randint(0, len(gene) - 1)
    step = randint(0, max_step)
    gene[spot] = gene[spot] + (step if random() < 0.5 else -step)
    if gene[spot] < space[spot].minimum:
        gene[spot] = space[spot].minimum
    if gene[spot] > space[spot].maximum:
        gene[spot] = space[spot].maximum

def choose_parents(elite):
    shuffle(elite)
    mummy = elite[0]
    daddy = elite[1]
    return mummy, daddy

def crossover(mummy, daddy):
    crossover_point = randint(1, len(mummy) - 2)
    gene = mummy[:crossover_point] + daddy[crossover_point:]
    return gene

def genetic_algorithm(space, cost, elite_fraction=0.1, pop_size=50, mutation_rate=0.6, max_step=100, max_iterations=100):
    # Create pop_size random genes + add to population
    population = []
    for pop in range(pop_size):
        gene = [randint(dim.minimum, dim.maximum) for dim in space]
        population.append(gene)

    num_elite = int(pop_size * elite_fraction)

    for iteration in range(max_iterations):
        # Create new population (select the elite first)
        scores = [(cost(gene), gene) for gene in population]
        scores.sort()
        ranked = [gene for score, gene in scores]
        elite = ranked[:num_elite]  # cream of the crop

        population = elite[:]  # start off with the elite

        while len(population) < pop_size:  # Add children of the elite
            # Choose parents
            mummy, daddy = choose_parents(elite)
            # make child
            child = crossover(mummy, daddy)
            population.append(child)

        # Mutate the population
        for gene in population:
            if random() < mutation_rate:
                mutate(gene, space, max_step)

    # Take the gene with best fitness
    return min(population, key=lambda x: cost(x))
