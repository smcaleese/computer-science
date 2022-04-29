# Design document

## Program description
The program is run from the Main class in Main.java. In this class, an instance of FactoryManager is created and run as a thread.

When the FactoryManager instance is initialized it sets up the production lines in the factory and initializes 
the delivery trucks. When it is run, it initializes and starts the warehouse thread which continually adds random cars to the warehouse. 
The class then makes orders at random intervals until all orders have been made.

Each order is created by creating a new instance of the car class. Each order has a random color and model and 
comes from a random dealership. If there is a car from the warehouse with the same model as the order, the order is accepted, the car is 
removed from the warehouse and added to a random production line. If there is no car of the right type in the warehouse, the order is 
rejected.

Every car instance is a thread that is started immediately after it is created to allow it to move independently along the assembly line.

Once a new car thread is running, it moves along the assembly line. Each robot has a lock and cars must acquire a 
robot's lock to move to that robot. After acquiring the lock, the car takes 100ms to move to the robot. The robot 
then works for a delay where the delay depends on the model and the position of the robot. The state of the 
production lines is also printed every time a car moves.

When a car has been painted, it is added to the delivery truck of the dealership where the order for the car came 
from. If the delivery truck is full after adding the car, the delivery truck delivers its cars back to its dealership.

### Program variables
Variables in the program that can be adjusted include the probability of an order arriving in every tick, the amount of time between each tick and the number of factory lines, dealerships and orders.

## How to run the program
Command: `javac *.java && java Main > output.dat`

## Division of work
Gytis created the initial implementation of the assignment. Stephen added improvements to the implementation 
implementation and wrote the design documentation.

## Concurrency patterns
The main concurrency pattern used in the program is the monitor which provides mutual exclusion and cooperation.

Locks, implemented in Java as reentrant locks, provide mutual exclusion and conditions allow threads to cooperate with 
each other to achieve a common goal.

## Concurrency problems and solutions
A common problem that affects concurrent programs involving multiple threads and shared memory is the race condition 
where the order of execution of threads affects the output of the program or can create an invalid output.

For example, in this program, it should not be possible for multiple threads to move a car to the same robot. The 
problem is solved with locks which provide mutual exclusion. To move to a robot, a car must first acquire the 
robot's lock. If another car then tries to move to that robot, it will wait until the lock is free. Therefore, locks 
ensure that each robot is only ever working on a single car.

The bounded buffer problem occurs in the program when managing the delivery trucks and the warehouse.

For example, to implement the delivery truck, it needs to be possible for several threads to have the ability to fill or empty 
delivery trucks. But it should not be possible for any thread to add to a full truck or remove from an empty truck.

The problem can be solved using the monitor pattern which provides cooperation between the threads via conditions which 
are shared between threads.

In the program, each truck has a condition. Threads are prevented from adding to full trucks by conditions. If any 
thread attempts to add a car to the truck when the truck is full, the thread will be paused by calling await() on the 
condition.

When another thread empties the truck, any threads waiting to add to the truck are woken by calling signalAll().

The threads wait on a FIFO queue which provides fairness as the thread that has been waiting longest gets to start 
first.
