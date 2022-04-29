#include <stdio.h>
#include <stdlib.h>
#include "./NameBinaryTree.c"
#include "./NumberBinaryTree.c"
#include "./helperFunctions.c"
#include <ctype.h>

void executeInstructions(char *line, struct NameNode *nameRoot, struct NumberNode *numberRoot)
{
    char b[100];
    strcpy(b, line); // convert string to array of chars
    char commands[5][50];
    char *p = strtok(b, ":");
    int j = 0;
    while(p != NULL)
    {
        strcpy(commands[j], p);
        p = strtok(NULL, ":");
        j++;
    }
    char *command = stripQuotesAndSpaces(commands[0]);
    char *secondArg = stripQuotesAndSpaces(commands[1]);

    if(strcmp(command, "add") == 0)
    {
        // "add" : "Bob" : "0831234521" : "24 Oak Drive, Whitehall, Co. Dublin"
        char *name = stripQuotesAndSpaces(commands[1]);
        char *number = stripQuotesAndSpaces(commands[2]);
        char *address = stripQuotesAndSpaces(commands[3]);
        if(strlen(name) > 0 && strlen(number) > 0 && strlen(address) > 0)
        {
            if(isName(name) && isNumber(number))
            {
                printf("adding\n");
                nameRoot = addNameNode(nameRoot, name, number, address);
                numberRoot = addNumberNode(numberRoot, name, number, address);
            }
            else
            {
                printf("invalid arguments: only chars for name and only numbers for number \n");
            }
        }
        else
        {
            printf("invalid input: too few arguments for the add command \n");
        }

    }
    else if(strcmp(command, "search") == 0)
    {
        if(isName(secondArg))
        {
            char *name = secondArg;
            char *output = nameSearch(nameRoot, name);
            printf("%s \n", output);
        }
        else if(isNumber(secondArg))
        {
            char *number = secondArg;
            char *output = numberSearch(numberRoot, number);
            printf("%s \n", output);
        }
    }
    else if(strcmp(command, "remove") == 0)
    {
        if(isName(secondArg))
        {
            if(nameRoot == 0 || numberRoot == 0)
            {
                printf("can't remove from empty tree");
            }
            else
            {
                char *name = secondArg;
                char *output = nameSearch(nameRoot, name);
                char *number = strtok(output, " : ");
                for(int i = 0; i < 2; i++)
                {
                    number = strtok(NULL, " : ");
                }
                nameRoot = removeNameNode(nameRoot, name);
                numberRoot = removeNumberNode(numberRoot, number);
            }
        }
        else if(isNumber(secondArg))
        {
            char *number = secondArg;
            char *output = numberSearch(numberRoot, number);
            char *name = strtok(output, " : ");
            for(int i = 0; i < 1; i++)
            {
                name = strtok(NULL, " : ");
            }
            nameRoot = removeNameNode(nameRoot, name);
            numberRoot = removeNumberNode(numberRoot, number);
        }
    }
    else if(strcmp(command, "list") == 0)
    {
        inorderTraversalName(nameRoot);
    }
    else
    {
        printf("invalid input \n");
    }
}

void main()
{
    // define name and number binary trees here
    // these root nodes won't be printed
    struct NameNode *nameRoot = addNameNode(nameRoot, "ROOT", "0123456789", "DO NOT REMOVE");
    struct NumberNode *numberRoot = addNumberNode(numberRoot, "ROOT", "0123456789", "DO NOT REMOVE");

    int x = 1;
    printf("size: %ld \n", sizeof(x));

    // user input loop
    printInstructions();
    int i = 0;
    char line[100];
    while(1)
    {
        fgets(line, sizeof(line), stdin);
        if(line[0] == 'q')
            break;
        executeInstructions(line, nameRoot, numberRoot);
    }
}