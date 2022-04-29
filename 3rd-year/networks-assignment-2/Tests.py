import unittest
from Router import Router
from Graph import Graph
from copy import deepcopy

graph = Graph()
graph.add_router("a", "b", 7)
graph.add_router("a", "c", 9)
graph.add_router("a", "f", 14)
graph.add_router("b", "c", 10)
graph.add_router("b", "d", 15)
graph.add_router("c", "d", 11)
graph.add_router("c", "f", 2)
graph.add_router("d", "e", 6)
graph.add_router("e", "f", 9)


class Tests(unittest.TestCase):

    def test_graph_creation(self):
        global graph
        keys = ["a", "b", "c", "d", "e", "f"]
        expected_output_dict = {"a": {"b": 7, "c": 9, "f": 14},
            "b": {"a": 7, "c": 10, "d": 15}, "c": {"a": 9, "b": 10, "d": 11, "f": 2},
            "d": {"b": 15, "c": 11, "e": 6}, "e": {"d": 6, "f": 9},
            "f": {"a": 14, "c": 2, "e": 9}}
        for k in keys:
            self.assertEqual(graph.adj_list[k], expected_output_dict[k])

    def test_find_shortest_path(self):
        router = Router("a", graph)
        end_node, cost, path_dict = router.find_shortest_path("a", "f", graph.adj_list)
        path_list = router.find_path(path_dict, "f")
        self.assertEqual((end_node, cost, path_list), ("f", 11, "a->c->f"))

    def test_routing_table(self):
        router = Router("a", graph)
        routing_table_data = router.get_routing_table()[1]
        print("data1:", routing_table_data)
        self.assertEqual({'from': ['a', 'a', 'a', 'a', 'a'], 'to': ['b', 'c', 'd', 'e', 'f'],
            'cost': [7, 9, 20, 20, 11], 'path': ['a->b', 'a->c', 'a->c->d', 'a->c->f->e', 'a->c->f']},
            routing_table_data)

    def test_remove_node(self):
        router = Router("a", deepcopy(graph))
        router.remove_router("c")
        routing_table_data_2 = router.get_routing_table()[1]
        print("data2:", routing_table_data_2)
        self.assertEqual({'from': ['a', 'a', 'a', 'a'], 'to': ['b', 'd', 'e', 'f'],
            'cost': [7, 22, 23, 14], 'path': ['a->b', 'a->b->d', 'a->f->e', 'a->f']},
            routing_table_data_2)

    def test_multiple_routers(self):
        router = Router("a", graph)
        router_two = Router("b", graph)
        routing_table_data = router.get_routing_table()[1]
        routing_table_data_two = router_two.get_routing_table()[1]
        # test routing_table_data
        self.assertEqual({'from': ['a', 'a', 'a', 'a', 'a'], 'to': ['b', 'c', 'd', 'e', 'f'],
            'cost': [7, 9, 20, 20, 11], 'path': ['a->b', 'a->c', 'a->c->d', 'a->c->f->e', 'a->c->f']},
            routing_table_data)
        # test test routing_table_data_two
        self.assertEqual({'from': ['b', 'b', 'b', 'b', 'b'], 'to': ['a', 'c', 'd', 'e', 'f'],
            'cost': [7, 10, 15, 21, 12], 'path': ['b->a', 'b->c', 'b->d', 'b->d->e', 'b->c->f']},
            routing_table_data_two)

if __name__ == "__main__":
    unittest.main()