from collections import defaultdict
import networkx as nx
import matplotlib.pyplot as plt

# each node in the graph is a router
# each edge is a link between routers
# edges are weighted with a cost
class Graph:

    def __init__(self):
        self.adj_list = {}

    def add_router(self, router_1, router_2, cost):
        if router_1 not in self.adj_list:
            self.adj_list[router_1] = {router_2: cost}
        else:
            self.adj_list[router_1][router_2] = cost

        if router_2 not in self.adj_list:
            self.adj_list[router_2] = {router_1: cost}
        else:
            self.adj_list[router_2][router_1] = cost

    def draw(self):
        G = nx.Graph()
        # add edges
        for (k1, sub_dict) in self.adj_list.items():
            for (k2, v) in sub_dict.items():
                G.add_edge(k1, k2, weight=v)

        # draw nodes
        pos = nx.spring_layout(G)
        nx.draw_networkx_nodes(G, pos, node_size=700, node_color="#0026ff") # blue
        nx.draw_networkx_labels(G, pos, font_size=20, font_color="#ffffff") # white

        # draw edges
        edges = [(a, b) for (a, b, c) in G.edges(data=True)]
        nx.draw_networkx_edges(G, pos, edgelist=edges, width=5)

        # draw edge weights
        labels = nx.get_edge_attributes(G, "weight")
        nx.draw_networkx_edge_labels(G, pos, font_size=20, edge_labels=labels)

        plt.axis("off")
        plt.show()

    def __str__(self):
        print("printing graph:")
        for (k, v) in self.adj_list.items():
            print(k, v)
        return ""