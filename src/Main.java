import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.PatternSyntaxException;

import edu.rice.hj.Module1;
import edu.rice.hj.api.SuspendableException;


public class Main {
	
	/**
	 * max size of a circuit
	 */
	static int max;
	
	static List<Juggler> jugglers = new ArrayList<Juggler>();
	static List<Circuit> circuits = new ArrayList<Circuit>();
	static HashMap<String,ArrayList<Juggler>> circuitToJugglers = new HashMap<String,ArrayList<Juggler>>();
	
	
	/**
	 * copy of jugglers list for when jugglers needs to be modified as it is iterated over
	 */
	static ArrayList<Juggler> tbaJugglers = new ArrayList<Juggler>();
	
	/**
	 * list of full circuits
	 */
	static List<String> fullCircuits = new ArrayList<String>();
	
	static List<Juggler> noPreferenceJugglers = new ArrayList<Juggler>();
	
	
	public static void main(String [] args) throws FileNotFoundException, IOException
	{
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	try {
		    	    String[] splitArray = line.split("\\s+");
		    	    if (splitArray[0].contains("C"))
		    	    {
		    	    	Circuit circuit = new Circuit();
		    	    	String name = "";
		    	    	String h = "";
		    	    	String e = "";
		    	    	String p = "";
		    	    	//get name
		    	    	name = splitArray[1];
		    	    	//get h
		    	    	for (int i = 2;i < splitArray[2].length();i++)
		    	    	{
		    	    		h+=splitArray[2].charAt(i);
		    	    	}
		    	    	//get e
		    	    	for (int i = 2;i < splitArray[3].length();i++)
		    	    	{
		    	    		e+=splitArray[3].charAt(i);
		    	    	}
		    	    	//get p
		    	    	for (int i = 2;i < splitArray[4].length();i++)
		    	    	{
		    	    		p+=splitArray[4].charAt(i);
		    	    	}
		    	    	circuit.name = name;
		    	    	circuit.H = Integer.valueOf(h);
		    	    	circuit.E = Integer.valueOf(e);
		    	    	circuit.P = Integer.valueOf(p);
		    	    	circuitToJugglers.put(circuit.name,new ArrayList<Juggler>());
		    	    	circuits.add(circuit);		    	    	
		    	    }
		    	    if (splitArray[0].contains("J"))
		    	    {
		    	    	Juggler juggler = new Juggler();
		    	    	String name = "";
		    	    	String h = "";
		    	    	String e = "";
		    	    	String p = "";
		    	    	//get name
		    	    	name = splitArray[1];
		    	    	
		    	    	//get h
		    	    	for (int i = 2;i < splitArray[2].length();i++)
		    	    	{
		    	    		h+=splitArray[2].charAt(i);
		    	    	}
		    	    	
		    	    	//get e
		    	    	for (int i = 2;i < splitArray[3].length();i++)
		    	    	{
		    	    		e+=splitArray[3].charAt(i);
		    	    	}
		    	    	//get p
		    	    	for (int i = 2;i < splitArray[4].length();i++)
		    	    	{
		    	    		p+=splitArray[4].charAt(i);
		    	    	}
		    	    	//get preference list
		    	    	ArrayList<String> preferenceList =
		    	    			new ArrayList<String>(Arrays.asList(splitArray[5]
		    	    			.split("\\s*,\\s*")));
		    	    	juggler.name = name;
		    	    	juggler.H = Integer.valueOf(h);
		    	    	juggler.E = Integer.valueOf(e);
		    	        juggler.P = Integer.valueOf(p);
		    	        juggler.preferenceList = preferenceList;
		    	        juggler.copyPreferenceList = new ArrayList<String>(preferenceList);
		    	        jugglers.add(juggler);
		    	    }
		    	} catch (PatternSyntaxException ex) {
		    	    
		    	}
		    }
		    //calculate max circuit size
		    max = jugglers.size()/circuits.size();

		    //GATHERING DATA FINISHED NOW PROCESSING BEGINS
			bestMatches();
		    assignJuggler();
		    assignNoPreferenceJugglers();
		    printResults();
		}
	}
	
	public static void bestMatches()
	{	
			for (int i = 0; i < jugglers.size();i++)
			{
				int i2 = i;
				jugglers.get(i2).circuits = new String [circuits.size()];
				
					Juggler juggler = jugglers.get(i2);
					juggler.circuitToScore = new int[circuits.size()];
					for (int j = 0; j < circuits.size();j++)
					{
						Circuit circuit = circuits.get(j);
						int dotProduct = 0;
						dotProduct+= circuit.H * juggler.H;
						dotProduct+= circuit.E * juggler.E;
						dotProduct+= circuit.P * juggler.P;
						juggler.circuits[j] = circuit.name;
						juggler.circuitToScore[j] = dotProduct;
					}
			}
	}
	
	public static void assignJuggler()
	{
		//make copy of jugglers so we can modify jugglers as we iterate
		tbaJugglers = new ArrayList<Juggler>(jugglers);
		
		while (tbaJugglers.size() > 0)
		{
			
			for (Juggler juggler: jugglers)
			{
				String currentPref = null;
				if (juggler.preferenceList.size() != 0)
				{
					currentPref = (juggler.preferenceList.get(0));
				}
				
				//if run out of preferences
				else
				{
					noPreferenceJugglers.add(juggler);
					tbaJugglers.remove(juggler);
					jugglers = new ArrayList(tbaJugglers);
					
					continue;
				}
				
				//if top choice isnt full, put it in, pop juggler and pop juggler's preference
				if (circuitToJugglers.get(currentPref).size() < max)
				{
					circuitToJugglers.get(juggler.preferenceList.get(0)).add(juggler);
					tbaJugglers.remove(juggler);
					juggler.preferenceList.remove(0);
					jugglers = new ArrayList(tbaJugglers);
					continue;
				}
				
				//if top choice is full
				if (circuitToJugglers.get(juggler.preferenceList.get(0)).size() == max)
				{
					//add to list of full circuits in every case
					if (!fullCircuits.contains(juggler.preferenceList.get(0)))
					{
						fullCircuits.add(juggler.preferenceList.get(0));
					}
					//calculate juggler with lowest score if circuit is full
					Juggler worstJuggler = worstJuggler(juggler.preferenceList.get(0), circuitToJugglers.get(juggler.preferenceList.get(0)));
					
					//if juggler has higher score than worst juggler, add to circuit
					if (juggler.circuitToScore[Integer.valueOf(juggler.preferenceList
							.get(0).substring(1, juggler.preferenceList.get(0).length()))] 
									> worstJuggler.circuitToScore[Integer.valueOf(juggler.preferenceList.get(0)
											.substring(1, juggler.preferenceList.get(0).length()))])
					{
						circuitToJugglers.get(juggler.preferenceList.get(0)).remove(worstJuggler);
						circuitToJugglers.get(juggler.preferenceList.get(0)).add(juggler);
						tbaJugglers.add(worstJuggler);
						tbaJugglers.remove(juggler);
						juggler.preferenceList.remove(0);
						
					}
					
					//if it does not have higher score, remove circuit from consideration
					else
					{
						juggler.preferenceList.remove(0);
					}
					
				}
			}
			//update juggler list with the copy
			jugglers = new ArrayList(tbaJugglers);
			
		}
	}
	
	public static void assignNoPreferenceJugglers()
	{
		ArrayList<String> nonFullCircuits = new ArrayList<String>();
		for (int i = 0;i < circuits.size(); i++)
		{
			if (!fullCircuits.contains(circuits.get(i).name))
			{
				if (!nonFullCircuits.contains(circuits.get(i).name))
				{
					nonFullCircuits.add(circuits.get(i).name);
				}
			}
		}
		
		//pop off first nonfull circuit
		String circuit = nonFullCircuits.remove(0);
		
		//make copy so we can modify as we iterate
		ArrayList<Juggler> copyJugglers = new ArrayList(noPreferenceJugglers);
		
		for (int i = 0; i < noPreferenceJugglers.size();i++)
		{
			if (i%max == 0 && i > max)
	        {
	        	circuit = nonFullCircuits.remove(0);
	        }
			Juggler juggler = noPreferenceJugglers.get(i);
	        circuitToJugglers.get(circuit).add(juggler);
	        copyJugglers.remove(juggler);
	        
	        //update copy
	        noPreferenceJugglers = new ArrayList(copyJugglers);
		}
	}
	
	static Juggler worstJuggler(String circuit, ArrayList<Juggler> jugs)
	{
		Juggler worst = new Juggler();
		int lowestScore = Integer.MAX_VALUE;
		
		for (Juggler j : jugs)
		{
			if (j.circuitToScore[Integer.valueOf(circuit.substring(1, circuit.length()))] < lowestScore)
			{
				lowestScore = j.circuitToScore[(Integer.valueOf(circuit.substring(1, circuit.length())))];
				worst = j;
			}
		}
		return worst;
	}
	static void printResults() throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("C:/Users/Simi_/workspace2/Juggle/src/YODLE_OUTPUT.txt", "UTF-8");
		int c = 1;
		for (Map.Entry<String,ArrayList<Juggler>> entry: circuitToJugglers.entrySet())
		{
			writer.print(entry.getKey() + " ");
			
			for (int i = 0; i < entry.getValue().size();i++)
			{
				Juggler juggler = entry.getValue().get(i);
				writer.print(juggler + " ");
				for (int j = 0; j < juggler.copyPreferenceList.size();j++)
				{
					String preference = juggler.copyPreferenceList.get(j);
					for (int k = 0;k < juggler.circuits.length;k++)
					{
						if (preference.equals(juggler.circuits[k]))
						{
							writer.print(juggler.circuits[k]+ ":" + juggler
									.circuitToScore[Integer.valueOf(juggler.circuits[k].substring(1, juggler.circuits[k].length()))]);
							if (j != juggler.copyPreferenceList.size()-1)
							{
								writer.print(" ");
							}
							
						}
					}
					
				}
				if (i != entry.getValue().size()-1)
				{
					writer.print(", ");
				}
			}
			if (c!=circuits.size())
			{
				writer.println();
			}
			c++;
		}
		writer.close();
	}

}
