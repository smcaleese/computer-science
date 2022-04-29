#include <stdio.h>
#include <mpi.h>

// compile: mpicc hello-world.c -o hello-world
// run: mpirun -np 2 ./hello-world

int main(int argc, char *argv[]) {
    int myid, size;
    MPI_Init(&argc, &argv); // initialize the number of processors which was supplied on the command line
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    printf("process %d out of %d says Hello\n", myid, size);
    MPI_Finalize();
    return 0;
}
