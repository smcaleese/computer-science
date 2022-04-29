from NameBinaryTree import NameBinaryTree
from NameBinaryTree import NameNode
from NumberBinaryTree import NumberBinaryTree
import sys
import re

def printInstructions():
    with open("instructions.txt", "r") as f:
        file_content = f.readlines()
    for line in file_content:
        print(line.rstrip())

def is_number(arg):
    found = re.search(r'^[0][0-9]+$', arg)
    return found != None

def is_name(arg):
    found = re.search(r'^[a-zA-Z\s]+$', arg)
    return found != None

def executeCurrentCommand(command_string, namesTree, numbersTree):
    commandArr = command_string.split(" : ")
    command = commandArr[0]
    if command == "search":
        second_arg = commandArr[1][1:-1]
        if is_name(second_arg):
            name = second_arg
            if not namesTree.search(name):
                print("not found")
            else:
                name, number, address = namesTree.search(name)[1:]
                print("found : '" + name + "' : '" + number + "' : '" + address + "'")
        elif is_number(second_arg):
            number = second_arg
            if not numbersTree.search(number):
                print("not found")
            else:
                name, number, address = numbersTree.search(number)[1:]
                print("found : '" + name + "' : '" + number + "' : '" + address + "'")
        else:
            print("not found")
    elif command == "remove":
        second_arg = commandArr[1][1:-1]
        if is_name(second_arg):
            # get the number associated with that name
            name = second_arg
            if not namesTree.search(name):
                print("not found")
            else:
                number = namesTree.search(name)[2]
                namesTree.remove(name)
                numbersTree.remove(number)
                print("removed : '" + name + "'")
        elif is_number(second_arg):
            # get the name associated with that number
            number = second_arg
            if not numbersTree.search(number):
                print("not found")
            else:
                name = numbersTree.search(number)[1]
                namesTree.remove(name)
                numbersTree.remove(number)
                print("removed : '" + number + "'")
        else:
            print("not found")
    elif command == "add" and len(commandArr) == 4:
        name, number, address = commandArr[1:]
        namesTree.add(name[1:-1], number[1:-1], address[1:-1])
        numbersTree.add(number[1:-1], name[1:-1], address[1:-1])
        print("added : " + name + " : " + number + " : " + address)
    elif command == "list":
        namesTree.inorderTraversal()
    else:
        print("invalid input")

def main():
    # define trees here
    namesTree = NameBinaryTree()
    numbersTree = NumberBinaryTree()
    printInstructions()

    # user input loop
    for line in sys.stdin:
        line = line.strip()
        if line == "q":
            break
        executeCurrentCommand(line, namesTree, numbersTree)

if __name__ == "__main__":
    main()
