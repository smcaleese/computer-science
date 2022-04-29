from Graph import Graph
from Router import Router


def main():
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
    print(graph)

    router = Router("a", graph)
    router_two = Router("b", graph)

    # part 1
    router.get_path("f")

    # part 2
    router.print_routing_table()
    router_two.print_routing_table()

    # part 3
    print("remove 'c' from graph and reprint router a:", end="\n\n")
    router.remove_router("c")

    # part 4
    print("print out new routing table for router b:", end="\n\n")
    router_two.print_routing_table()

    # part 5
    graph.draw()

if __name__ == "__main__":
    main()