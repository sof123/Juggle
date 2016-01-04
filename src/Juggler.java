import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


public class Juggler {
	
	String name;
	int H;
	int E;
	int P;
	String [] circuits;
	ArrayList<String> preferenceList;
	ArrayList<String> copyPreferenceList;
	Circuit assignedCircuit;
	int [] circuitToScore;
	
	public Juggler()
	{
		circuits = new String [1];
	}
	public String toString()
	{
		return name;
	}

}
