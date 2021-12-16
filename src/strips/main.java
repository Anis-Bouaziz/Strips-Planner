package strips;


public class main {

	public static void main(String[] args) throws Exception {
		World w = new World("cubes3.txt");
		World w2=new World("cubes3.txt");
		//w.printState(); 
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ WORLD ////////////////////////////////////////");
		w.displayWorld();
		System.out.println("");
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ BFS /////////////////////////////////////////////////////////////////////");
		BFS sol= new BFS();
		System.out.println("////////////////////////////////////////////////// Forward Chaining \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
		sol.bfs_Search(w);
		System.out.println("////////////////////////////////////////////////// Backward Chaining \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
		//has probs with monkey
	//	BFS sol2= new BFS();
		//sol2.bfs_bc_search(w2);
		
		
		
		
	}

}
