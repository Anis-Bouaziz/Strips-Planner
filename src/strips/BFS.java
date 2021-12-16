package strips;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import java.util.Queue;
import java.util.stream.Collectors;

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
		while (!queue.isEmpty() && (w.goalSatisfied(w.state) == false)) {

			Node n = queue.remove();

			for (Action a : w.actions) {

				if (w.is_valid(a.preconditions, n.currentState) == true) {

					ArrayList<Action> possibles = a.generateActualAction(w);

					for (Action possible : possibles) {

						if (w.possibleActions(possible.preconditions, n.currentState)) {
							System.out.println("////////////////   POSSIBLE ACTION   ////////////////////////");
							System.out.println(possible.name + possible.params.toString());

							ArrayList<Condition> curr_state = w.updateState(possible, n.currentState);
							System.out.println("////////////////    CURRENT STATE     ////////////////////////");
							curr_state.forEach(s -> System.out.println(s.toString()));
							System.out.println("////////////////////////////////////////");
							next = new Node(possible, n, curr_state);
							if (n.prev != null && curr_state.equals(n.prev.currentState))
								continue;
							else {
								queue.add(next);

								w.state = new ArrayList<Condition>(curr_state);

							}
							if (w.goalSatisfied(curr_state))
								break;
						}

					}

				}
			}

		}

		if (w.goalSatisfied(w.state)) {
			System.out.println();
			System.out.println("////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////  SOLUTION  ////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////");
			ArrayList<Action> solution = new ArrayList<Action>();
			while (next != null && next.data != null) {
				solution.add(next.data);
				next = next.prev;
			}
			Collections.reverse(solution);
			for (Action a : solution) {

				System.out.println(a.name + a.params.toString());
			}

		} else {
			System.out.println("No solution was found");
		}

	}

	public void bfs_bc_search(World w) {
		Node next = null;
		ArrayList<Condition> g = new ArrayList<Condition>();
		g.add(w.goal);

		/// First iteration
		outerloop1: for (Action a : w.actions) {
			if ((a.add.stream().map(x -> x.name).collect(Collectors.toList())).contains(w.goal.name)) {

				ArrayList<Action> possibles = a.generateActualAction(w);
				for (Action possible : possibles) {

					if (possible.add.contains(w.goal)) {
						ArrayList<Condition> curr_state = possible.preconditions;

						if (curr_state.equals(g))
							continue;
						else {
							next = new Node(possible, new Node(null, null, g), curr_state);
							g = new ArrayList<Condition>(curr_state);
							if (!w.stateSatisfied(curr_state))
								queue.add(next);
							else
								break outerloop1;

						}

					}
				}

			}

		}

		outerloop2: while (!queue.isEmpty() && (!w.stateSatisfied(g))) {

			Node n = queue.remove();

			for (Action a : w.actions) {

				if (w.is_valid2(n.currentState, a)) {
					// System.out.println(a.name + " valid");
					ArrayList<Action> possibles = a.generateActualAction(w);
					for (Action possible : possibles) {

						if (w.possibleActions2(possible, n.currentState)) {

							System.out.println("////////////////   POSSIBLE ACTION   ////////////////////////");
							System.out.println(possible.name + possible.params.toString());

							ArrayList<Condition> curr_state = w.updateGoal(possible, n.currentState);

							System.out.println("////////////////    Previous STATE     ////////////////////////");
							curr_state.forEach(s -> System.out.println(s.toString()));
							System.out.println("////////////////////////////////////////");

							if (curr_state.equals(n.prev.currentState))
								continue;
							else {
								next = new Node(possible, n, curr_state);
								g = new ArrayList<Condition>(curr_state);
								if (!w.stateSatisfied(curr_state))
									queue.add(next);
								else
									break outerloop2;

							}

						}

					}
				}
			}

		}

		if (w.stateSatisfied(g)) {
			System.out.println();
			System.out.println("////////////////////////////////////////////////////////////////////////");
			System.out.println("/////////////////////////////  SOLUTION  ////////////////////////////////");
			System.out.println("////////////////////////////////////////////////////////////////////////");
			ArrayList<Action> solution = new ArrayList<Action>();
			while (next != null && next.data != null) {
				solution.add(next.data);
				next = next.prev;
			}

			for (Action a : solution) {

				System.out.println(a.name + a.params.toString());
			}

		} else {
			System.out.println("No solution was found");
		}

	}
}
