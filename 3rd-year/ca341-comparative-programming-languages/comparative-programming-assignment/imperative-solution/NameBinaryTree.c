#include <stdlib.h>
#include "string.h"
#include "./NameBinaryTree.h"

char* nameSearch(struct NameNode *root, char *name)
{
    if(root == 0)
    {
        char *answer = malloc(10 * 100 * sizeof(char));
        strcpy(answer, "not ");
        strcat(answer, "found");
        return answer;
    }
    if(strcmp(name, root->name) == 0)
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
    if(strcmp(name, root->name) < 0)
        nameSearch(root->left, name);
    else if(strcmp(name, root->name) > 0)
        nameSearch(root->right, name);
}

void inorderTraversalName(struct NameNode *root)
{
    if(root == 0)
        return;
    inorderTraversalName(root->left);
    if(strcmp(root->name, "ROOT") != 0)
        printf("%s : %s : %s \n", root->name, root->number, root->address);
    inorderTraversalName(root->right);
}

struct NameNode* newNameNode(char *name, char *number, char *address)
{
    //struct NameNode *temp = (struct NameNode *)malloc(sizeof(struct NameNode));
    struct NameNode *temp = malloc(sizeof(struct NameNode));
    temp->name = name;
    temp->number = number;
    temp->address = address;
    temp->left = temp->right = 0;
    return temp;
}

struct NameNode* addNameNode(struct NameNode *root, char *name, char *number, char *address)
{
    if(root == 0)
        return newNameNode(name, number, address);
    if(strcmp(name, root->name) < 0)
        root->left = addNameNode(root->left, name, number, address);
    else if(strcmp(name, root->name) > 0)
        root->right = addNameNode(root->right, name, number, address);
    else
        printf("error: duplicate in tree");
    return root;
}

struct NameNode* minValueNameNode(struct NameNode  *root)
{
    if(root->left == 0)
        return root;
    return minValueNameNode(root->left);
}

struct NameNode* removeNameNode(struct NameNode *root, char *name)
{
    if(root == NULL)
    {
        return root;
    }
    else if(strcmp(name, root->name) < 0)
    {
        root->left = removeNameNode(root->left, name);
    }
    else if(strcmp(name, root->name) > 0)
    {
        root->right = removeNameNode(root->right, name);
    }
    else
    {
        if(root->left == 0)
        {
            struct NameNode *temp = root->right;
            free(root);
            return temp;
        }
        if(root->right == 0)
        {
            struct NameNode *temp = root->left;
            free(root);
            return temp;
        }
        struct NameNode *temp = minValueNameNode(root->right);
        root->name = name;
        root->right = removeNameNode(root->right, temp->name);
    }
    return root;
}
