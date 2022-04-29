#include <stdio.h>
#include <omp.h>

void main()
{
    omp_set_num_threads(2);

    #pragma omp parallel
    {
        #pragma omp for
        for(int i = 1; i <= 10; i++) {
            int threadId = omp_get_thread_num();
            printf("i: %d, threadId: %d\n", i, threadId);
        }
    }
}