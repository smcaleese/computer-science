#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>

void printInstructions()
{
    FILE *filePointer = fopen("instructions.txt", "r");
    char data[1000];
    if(filePointer != NULL)
    {
        while(fgets(data, 1000, filePointer) != NULL)
        {
            printf("%s", data);
        }
        printf("\n");
        fclose(filePointer);
    }
    else
    {
        printf("failed to open file");
    }
}

char* stripQuotesAndSpaces(char *arr)
{
    char *newArr = malloc(50 * sizeof(char));
    while(*arr != '\0')
    {
        if(*(arr - 1) == '\"')
            break;
        arr++;
    }
    int i = 0;
    while(*arr != '\"' && *arr != '\0')
    {
        newArr[i] = *arr;
        i++;
        arr++;
    }
    newArr[i] = '\0';
    return newArr;
}

int isName(char *name)
{
    int isTrue = 1;
    while(*name != '\0')
    {
        if(!(isalpha(*name)))
        {
            isTrue = 0;
        }
        name++;
    }
    return isTrue;
}

int isNumber(char *number)
{
    int isTrue = 1;
    while(*number != '\0')
    {
        if(!(isdigit(*number)))
        {
            isTrue = 0;
        }
        number++;
    }
    return isTrue;
}