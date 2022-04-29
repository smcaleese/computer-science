#ifndef NAMEBINARYTREE_H
#define NAMEBINARYTREE_H

struct NameNode
{
    char *name;
    char *number;
    char *address;
    struct NameNode *left;
    struct NameNode *right;
};

char* nameSearch(struct NameNode *root, char *name);

void inorderTraversalName(struct NameNode *root);

struct NameNode* newNameNode(char *name, char *number, char *address);

struct NameNode* addNameNode(struct NameNode *root, char *name, char *number, char *address);

struct NameNode* minValueNameNode(struct NameNode *root);

struct NameNode* removeNameNode(struct NameNode *root, char *name);

#endif