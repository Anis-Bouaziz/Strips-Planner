****************************************************************************
# Stanford Research Institute Problem Solver (STRIPS) :
*****************************************************************************
STRIPS is an automated planning technique that works by executing a domain and problem to find a goal.
* A STRIPS instance is composed of: :

	1- An initial state. 

	2- The specification of the goal state – situation which the planner is trying to reach .

	3- A set of actions:
	
* For each action, the following are included:     

    1-preconditions (what must be established before the action is performed) or PRE.

    2-postconditions (what is established after the action is performed) or ADD and DEL.


*****************************************************************************
This repo is a java implemtation of  a parser that takes in a text file containing a strips problem and constructs the world , and a solver that then uses Breadth first search alogirthm to find a solution. 
