package strips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BFS {

	private Queue<Node> queue;
	static ArrayList<Node> nodes = new ArrayList<Node>();

	static class Node {
		Node prev;
		Action data;
		ArrayList<Condition> currentState;
		boolean visited;

		Node(Action data, Node prev, ArrayList<Condition> currentState) {
			this.data = data;
			this.prev = prev;
			this.currentState = currentState;

		}

	}

	public BFS() {
		queue = new LinkedList<Node>();
	}

	
	public void bfs_Search(World w) {
		queue.add(new Node(null, null, w.state));
		Node next = null;
	while (!queue.isEmpty() && (w.goalSatisfied(w.state) == false) ){

			
			Node n = queue.remove();
			
			for (Action a : w.actions) {
				
				
				if (w.is_valid(a.preconditions, n.currentState) == true) {
				
					
				ArrayList<Action> possibles = a.generateActualAction(w,n.currentState,w.literals);

				for(Action possible:possibles) {
						
					if (w.possibleActions(possible.preconditions, n.currentState)) {
						System.out.println("////////////////   POSSIBLE ACTION   ////////////////////////");
						System.out.println(possible.name+possible.params.toString());
						
						ArrayList<Condition> curr_state = w.updateState(possible, n.currentState);
						System.out.println("////////////////    CURRENT STATE     ////////////////////////");
				curr_state.forEach(s->System.out.println(s.toString())); 
						System.out.println("////////////////////////////////////////");
						next = new Node(possible, n, curr_state);
						if (n.prev!=null && curr_state.equals(n.prev.currentState)) continue;
						else {
							queue.add(next);
						
						w.state = new ArrayList<Condition>(curr_state);
						}
						
						
					}
					
				}
					

				}
			}
			
		}

		if (w.goalSatisfied(w.state)) {
			System.out.println();
			System.out.println("/////////////////////////////////////////////////////////////");
			System.out.println("Solution");
			System.out.println("/////////////////////////////////////////////////////////////");
			ArrayList<Action> solution = new ArrayList<Action>();
			while (next != null && next.data!=null) {
				solution.add(next.data);
				next = next.prev;
			}
			Collections.reverse(solution);
			for (Action a : solution) {
				
				System.out.println(a.name+a.params.toString());
			}
			
		}
		else {
			System.out.println("No solution was found");
		}
	
	}

}
