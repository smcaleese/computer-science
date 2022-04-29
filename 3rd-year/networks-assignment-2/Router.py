import pandas

class Router:

    def __init__(self, node, graph):
        self.node = node
        self.graph = graph.adj_list

    def get_routing_table(self):
        # get the path from self.node to every other node
        routing_table_data = {"from": [], "to": [], "cost": [], "path": []}
        nodes = sorted([n for n in self.graph.keys() if n != self.node])
        for n in nodes:
            cost, path_dict = self.find_shortest_path(self.node, n, self.graph)[1:]
            path_list = self.find_path(path_dict, n)

            routing_table_data["from"].append(self.node)
            routing_table_data["to"].append(n)
            routing_table_data["cost"].append(cost)
            routing_table_data["path"].append(path_list)
        routing_table = pandas.DataFrame.from_dict(routing_table_data)
        return routing_table, routing_table_data

    def print_routing_table(self):
        routing_table = self.get_routing_table()[0]
        print("routing table for router", self.node)
        print(routing_table, end="\n\n")

    def remove_router(self, router):
        # remove a router that self.node is currently pointing to. Then print a new routing table.
        # remove the node from the graph
        del self.graph[router]
        # remove all connections of other nodes to the deleted node
        for (k, sub_dict) in self.graph.items():
            if router in sub_dict:
                del sub_dict[router]
        self.print_routing_table()

    def get_path(self, node):
        # find the shortest path between self.node and node
        end_node, cost, path_dict = self.find_shortest_path(self.node, node, self.graph)
        if len(path_dict) > 0:
            path_list = self.find_path(path_dict, end_node)
        else:
            path_list = []
        print(f"Start: {self.node} \nEnd: {end_node} \nPath: {path_list} \nCost: {cost}\n")
        return ""

    def find_path(self, path_dict, end_node):
        # find the shortest path with breadth-first search
        path = []
        curr_node = end_node
        while curr_node != None:
            path.append(curr_node)
            curr_node = path_dict[curr_node]
        path.reverse()
        return "->".join(path)

    def find_shortest_path(self, start_node, end_node, graph):
        # use Dijkstra's algorithm to find the shortest path
        distances = {vertex: float('inf') for vertex in graph}
        distances[start_node] = 0
        visited = set()
        # Build up path. Key is child and value is parent.
        path_dict = {start_node: None}

        queue = [start_node]
        while len(queue) > 0:
            # relax the edges
            current_node = queue.pop(0)
            visited.add(current_node)
            if current_node == end_node:
                return current_node, distances[current_node], path_dict

            for vertex, weight in graph[current_node].items():
                if vertex not in visited:
                    queue.append(vertex)
                    new_distance = distances[current_node] + weight
                    if new_distance < distances[vertex]:
                        distances[vertex] = new_distance
                        path_dict[vertex] = current_node

        return "not found", -1, {}
