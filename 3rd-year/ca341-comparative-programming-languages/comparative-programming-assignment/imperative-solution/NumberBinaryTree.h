#ifndef NUMBERBINARYTREE_H
#define NUMBERBINARYTREE_H

struct NumberNode
{
    char *name;
    char *number;
    char *address;
    struct NumberNode *left;
    struct NumberNode *right;
};

char* numberSearch(struct NumberNode *root, char *number);

struct NumberNode* newNumberNode(char *number, char *name, char *address);

struct NumberNode* addNumberNode(struct NumberNode *root, char *number, char *name, char *address);

struct NumberNode* minValueNumberNode(struct NumberNode *root);

struct NumberNode* removeNumberNode(struct NumberNode *root, char *number);

#endif