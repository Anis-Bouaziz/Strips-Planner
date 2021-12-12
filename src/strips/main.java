package strips;


public class main {

	public static void main(String[] args) throws Exception {
		World w = new World("src/cubes2.txt");
		//w.printState(); 
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ WORLD ////////////////////////////////////////");
		w.displayWorld();
		System.out.println("");
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ WORLD ////////////////////////////////////////");
		BFS sol= new BFS();
		sol.bfs_Search(w);
		
		
		
		
	}

}
