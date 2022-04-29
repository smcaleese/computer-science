from PIL import Image, ImageDraw
import sys
import math
import random
import optimization
from functools import cmp_to_key
import time


class DimensionRange():
    def __init__(self, mini, maxi):
        self.minimum = mini
        self.maximum = maxi

    def __str__(self):
        return "({}, {})".format(self.minimum, self.maximum)

    def __repr__(self):
        return str(self)


class Point():
    def __init__(self, x_val, y_val):
        self.x = x_val
        self.y = y_val

    def distance(self, other):
        return math.sqrt((self.x - other.x)**2 + (self.y - other.y)**2)

    def __str__(self):
        return "({}, {})".format(self.x, self.y)

    def __repr__(self):
        return str(self)


nodes = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']

links = [('B', 'G'),
         ('E', 'F'),
         ('H', 'E'),
         ('D', 'B'),
         ('H', 'G'),
         ('A', 'E'),
         ('C', 'F'),
         ('G', 'B'),
         ('G', 'B'),
         ('F', 'A'),
         ('C', 'B'),
         ('H', 'F')]


def get_connections():
    connections = {}
    for node in nodes:
        connections[node] = []

        for edge in links:
            if node in edge:
                if node == edge[0]:
                    other = edge[1]
                else:
                    other = edge[0]
                if not other in connections[node]:  # avoid duplicates
                    connections[node].append(other)

    return connections


def get_angle(a, b, c):
    if b == a or b == c:
        return -0.1
    else:
        atana = math.atan2(a.y - b.y, a.x - b.x)
        atanc = math.atan2(c.y - b.y, c.x - b.x)
        ang = atanc - atana
        if ang < 0:
            ang += 2 * math.pi

        return ang

# Compare angle of two points wrt centre
# (https://stackoverflow.com/questions/6989100/sort-points-in-clockwise-order)
# def compare_angle(Point a, Point b):


def compare_angle(a, b, centre):
    if a.x - centre.x >= 0 and b.x - centre.x < 0:
        return -1
    if a.x - centre.x < 0 and b.x - centre.x >= 0:
        return 1
    if a.x - centre.x == 0 and b.x - centre.x == 0:
        if a.y - centre.y >= 0 or b.y - centre.y >= 0:
            if a.y > b.y:
                return -1
            elif a.y < b.y:
                return 1
            else:
                return 0

    det = (a.x - centre.x) * (b.y - centre.y) - \
        (b.x - centre.x) * (a.y - centre.y)
    if det < 0:
        return -1
    if det > 0:
        return 1

    return 0

# Have a cost for uneven angles


def get_uneven_angle_cost(sol):
    global nodes
    points = dict([(nodes[i], Point(sol[i*2], sol[i*2+1]))
                   for i in range(0, len(nodes))])
    connections = get_connections()

    total_cost = 0

    # Sort by degree (i.e. number of connected nodes)
    nodes = sorted(nodes, key=lambda node: len(
        connections[node]), reverse=True)

    # Only do the highest degree nodes
    for node in nodes[:len(nodes)//2]:
        degree = len(connections[node])
        even_angle = math.pi * 2 / degree
        if degree < 3:
            break

        # look at all neighbours arranged in a circle
        sorted_neighbours = sorted(connections[node], reverse=True, key=cmp_to_key(
            lambda a, b: compare_angle(points[a], points[b], points[node])))

        for i in range(len(sorted_neighbours)):
            prev = sorted_neighbours[i]
            next = sorted_neighbours[(i+1) % len(sorted_neighbours)]
            angle = get_angle(points[prev], points[node], points[next])
            #print("\t", prev, node, next, round(angle * 180 / math.pi))
            total_cost += (angle - even_angle) ** 2

    return total_cost


#
#  Charge for nodes being near each other
#
def get_near_node_cost(sol):
    TOO_CLOSE = 100  # number of pixels regarded as too close
    points = dict([(nodes[i], Point(sol[i*2], sol[i*2+1]))
                   for i in range(0, len(nodes))])
    total = 0
    for i in range(len(nodes)):
        for j in range(i+1, len(nodes)):
            # Get the locations of thse nodes
            p1 = points[nodes[i]]
            p2 = points[nodes[j]]
            # Get the distance bewteen the two points
            distance = p1.distance(p2)

            if distance < TOO_CLOSE:
                # Penalty depends on how close.
                total += (TOO_CLOSE / (distance+.0000001))**2

    return total

#
#  Charge for nodes being near each other
#


def get_different_line_length_cost(sol):
    points = dict([(nodes[i], Point(sol[i*2], sol[i*2+1]))
                   for i in range(0, len(nodes))])
    total = 0
    count = 0
    for i in range(len(nodes)):
        for j in range(i+1, len(nodes)):
            # Get the locations of thse nodes
            p1 = points[nodes[i]]
            p2 = points[nodes[j]]
            # Get the distance bewteen the two points
            distance = p1.distance(p2)

            total += distance
            count += 1

    average = total / count

    error = 0
    for i in range(len(nodes)):
        for j in range(i+1, len(nodes)):
            # Get the locations of thse nodes
            p1 = points[nodes[i]]

            p2 = points[nodes[j]]
            # Get the distance bewteen the two points
            distance = p1.distance(p2)
            error += (distance - average) ** 2

    return error

#
#  Charge for crossed lines
#


def number_crossed_lines(sol):
    # Convert the number list into a dictionary of node:(x,y)
    points = dict([(nodes[i], Point(sol[i*2], sol[i*2+1]))
                   for i in range(0, len(nodes))])
    total = 0

    # Loop through every pair of links (any could be crossed)
    for i in range(len(links)):
        for j in range(i+1, len(links)):

            # Get the points
            start1 = points[links[i][0]]
            end1 = points[links[i][1]]
            start2 = points[links[j][0]]
            end2 = points[links[j][1]]

            den = (end2.y - start2.y) * (end1.x - start1.x) - \
                (end2.x - start2.x) * (end1.y - start1.y)

            # den==0 if the lines are parallel
            if den != 0:
                # Otherwise ua and ub are the fraction of the
                # line where they cross
                ua = ((end2.x - start2.x) * (start1.y - start2.y) -
                      (end2.y-start2.y)*(start1.x-start2.x))/den
                ub = ((end1.x - start1.x) * (start1.y - start2.y) -
                      (end1.y-start1.y)*(start1.x-start2.x))/den

                # If the fraction is between 0 and 1 for both lines
                # then they cross each other
                if ua > 0 and ua < 1 and ub > 0 and ub < 1:
                    total += 1

    return total


def cost(sol):
    return get_uneven_angle_cost(sol) + get_near_node_cost(sol) + 1000 * number_crossed_lines(sol) + get_different_line_length_cost(sol) * 0.0001


def draw_network(sol, max_x=400, max_y=400):
    # Create the image
    img = Image.new('RGB', (max_x, max_y), (255, 255, 255))
    draw = ImageDraw.Draw(img)

    # Create the position dict
    pos = dict([(nodes[i], (sol[i*2], sol[i*2+1]))
                for i in range(0, len(nodes))])

    for (a, b) in links:
        draw.line((pos[a], pos[b]), fill=(255, 0, 0))

    for n, p in pos.items():
        draw.text(p, n, (0, 0, 0))

    img.show()


def do_ga():

    max_x = 500
    max_y = 500

    # Each dimension in the space has a max and min range.
    # Max and min for the x and y of each node => two values for each node.
    space = [DimensionRange(10, max_x - 10), DimensionRange(10, max_y - 10)] * len(nodes)
    print(len(nodes), len(space))

    steps = [32, 64, 128, 256]
    for num_steps in steps:
        #random.seed(173)
        random.seed(0)
        time1 = time.time()
        sol = optimization.genetic_algorithm(space, cost, elite_fraction = 0.2, pop_size = 50, mutation_rate = 0.6, max_step = 100, max_iterations = num_steps)
        time2 = time.time()
        time_taken = time2 - time1
        print("{} : {:6.3f} : {}".format(num_steps, cost(sol), time_taken))
    print()

    #draw_network(sol, max_x, max_y)

# time complexity: O(n x m)
# where n is the number of iterations and m is the population size

def main(args):
    do_ga()


main(sys.argv)
