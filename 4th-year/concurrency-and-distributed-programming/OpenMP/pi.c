#include <stdio.h>
#include <omp.h>
static long num_steps = 1000000; double step;
#define NUM_THREADS 4

void main () {
    int i, nthreads; double pi, sum[NUM_THREADS]; /* Promote scalar to an array dimensioned by number of threads to avoid race condition.*/
    step = 1.0 / (double) num_steps;
    omp_set_num_threads(NUM_THREADS);

    #pragma omp parallel
    {
        int i, id, nthrds;
        double x;
        id = omp_get_thread_num();
        printf("id: %d\n", id);
        nthrds = omp_get_num_threads();
        printf("nthrds: %d\n", nthrds);
        if (id == 0) nthreads = nthrds; /* Only one thread should copy the number
        of threads to the global value to make sure multiple threads writing to the same
        address don’t conflict. */
        for (i = id, sum[id] = 0.0; i < num_steps; i = i + nthrds) {
            /*This is a common trick in SPMD programs to create a cyclic distribution of loop iterations */
            x = (i + 0.5) * step;
            sum[id] += 4.0 / (1.0 + x*x);
        }
    }
    for(i = 0, pi = 0.0; i < nthreads; i++) {
        pi += sum[i] * step;
    }
    printf("pi: %f\n", pi);
}