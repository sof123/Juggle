import java.util.ArrayList;
import java.util.List;


public class Circuit implements Comparable<Circuit> {
	
	 String name;
	 int H;
	 int E;
	 int P;
	 int score;
	 List<Juggler> assignedJugglers;
	 
	 public Circuit()
	 {
		 assignedJugglers = new ArrayList<Juggler>();
	 }
	 public Circuit(Circuit circuit)
	 {
		 name = circuit.name;
		 H = circuit.H;
		 E = circuit.E;
		 P = circuit.P;
		 score = circuit.score;
	 }

	@Override
	public int compareTo(Circuit o) {
		// TODO Auto-generated method stub
		return score - o.score;
	}
	 
	 
	 
	 


}
