#include <stdio.h>
#include <stdlib.h>
#include "string.h"
#include "./NumberBinaryTree.h"

int sumCompare(char *a, char *b)
{
    int aSum = atoi(a);
    int bSum = atoi(b);
    if(aSum < bSum)
    {
        return -1;
    } else if(aSum > bSum)
        return 1;
    else
    {
        return 0;
    }
}

char* numberSearch(struct NumberNode *root, char *number)
{
    if(root == 0)
    {
        char *answer = malloc(10 * 100 * sizeof(char));
        strcpy(answer, "not ");
        strcat(answer, "found");
        return answer;
    }
    if(sumCompare(number, root->number) == 0)
    {
        char *answer = malloc(10 * 100 * sizeof(char));
        strcpy(answer, "found : ");
        strcat(answer, root->name);
        strcat(answer, " : ");
        strcat(answer, root->number);
        strcat(answer, " : ");
        strcat(answer, root->address);
        return answer;
    }
    else if(sumCompare(number, root->number) < 0)
    {
        numberSearch(root->left, number);
    }
    else if(sumCompare(number, root->number) > 0)
    {
        numberSearch(root->right, number);
    }
}

struct NumberNode* newNumberNode(char *name, char *number, char *address)
{
    //struct NumberNode *temp = (struct NumberNode *)malloc(sizeof(struct NumberNode));
    struct NumberNode *temp = malloc(sizeof(struct NumberNode));
    temp->number = number;
    temp->name = name;
    temp->address = address;
    temp->left = temp->right = 0;
    return temp;
}

struct NumberNode* addNumberNode(struct NumberNode *root, char *name, char *number, char *address)
{
    if(root == 0)
        return newNumberNode(name, number, address);
    if(sumCompare(number, root->number) < 0)
        root->left = addNumberNode(root->left, name, number, address);
    else if(sumCompare(number, root->number) > 0)
        root->right = addNumberNode(root->right, name, number, address);
    else
        printf("error: duplicate in tree");
    return root;
}

struct NumberNode* minValueNumberNode(struct NumberNode *root)
{
    if(root->left == 0)
        return root;
    return minValueNumberNode(root->left);
}

struct NumberNode* removeNumberNode(struct NumberNode *root, char *number)
{
    if(root == 0)
        return root;
    if(sumCompare(number, root->number) < 0)
    {
        root->left = removeNumberNode(root->left, number);
    }
    else if(sumCompare(number, root->number) > 0)
    {
        root->right = removeNumberNode(root->right, number);
    }
    else
    {
        if(root->left == 0)
        {
            struct NumberNode *temp = root->right;
            free(root);
            return temp;
        }
        if(root->right == 0)
        {
            struct NumberNode *temp = root->left;
            free(root);
            return temp;
        }
        struct NumberNode *temp = minValueNumberNode(root->right);
        root->number = number;
        root->right = removeNumberNode(root->right, temp->number);
    }
    return root;
}