#include <stdio.h>
#include <mpi.h>

int main(int argc, char *argv[]) {
    int myid, size, otherid, myvalue, othervalue, tag = 1;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &myid);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size != 2) {
        printf("Use exactly two processes\n");
    }

    if (myid == 0) {
        otherid = 1; myvalue = 14;
    } else {
        otherid = 0; myvalue = 42;
    }

    printf("Process %d sending value %d to process %d\n", myid, myvalue, otherid);
    MPI_Send(&myvalue, 1, MPI_INT, otherid, tag, MPI_COMM_WORLD);
    MPI_Recv(&othervalue, 1, MPI_INT, otherid, MPI_ANY_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

    printf("Process %d received value %d from process %d\n", myid, othervalue, otherid);

    MPI_Finalize();
    return 0;
}
