package controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tokenizer.Tokenizer;
import parser.Parser;

public class Controller {

	private static ArrayList<String> NTerminals;
	public static String RunProgram(String fileName) {
		String[][] tokens= Tokenizer.getTokens(fileName);
		NTerminals=readNonTerminals("src/controller/NonTerminals.txt");
		String[][] productions= getproductionRules("src/controller/production_rules.txt");
		Map<String, Map<String, Integer>> parserTable = getParserTable(productions, "src/controller/LL1table.txt");
		Parser parser= new Parser(parserTable, productions);
		String result=parser.parse(tokens);

//		traverseMap(parserTable);
//		traverseProductions(productions);
//		traverseTokens(tokens);
		return result;
	}
	
	private static String[][] getproductionRules(String productionRulesFileName) {
		String fileName = productionRulesFileName; // name of the file that contains the production rules
	    ArrayList<String[]> productionsList = new ArrayList<>(); // to store the production rules

	    try {
	      Scanner scanner = new Scanner(new File(fileName));
	      while (scanner.hasNextLine()) {
	        String line = scanner.nextLine().trim();
	        line=line.replaceAll("\"", "");
	        String[] production = line.split(" ::= ");
	        String[] productions2=production[1].split(" ");
	        for(int i=0;i<productions2.length;i++) {
	        	if(productions2[i].equals("''")) {
	        		productions2[i]="";
	        	}
	        }
//	        productionsList.add(production[0]);
	        productionsList.add(productions2);
	      }
	      scanner.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("File not found: " + fileName);
	    }
	    String[][] productions = productionsList.toArray(new String[0][0]);
	    return productions;
	}

	private static Map<String, Map<String, Integer>> getParserTable(String[][] productions, String fileName){
    	Map<String, Map<String, Integer>> parserTable = new HashMap<>();
    	String [] terminals=null;
    	try {
  	      Scanner scanner = new Scanner(new File(fileName));
  	      int lineNumber=0;
  	      while (scanner.hasNextLine()) {
  	    	  if(lineNumber==0) {
  	    		  String line = scanner.nextLine().trim();
  	    		  terminals=line.split(" ");
  	    	  }else {
  	    		Map<String, Integer> E_entries = new HashMap<>();
  	    		String line = scanner.nextLine().trim();
	    		String[] linee=line.split(" ");
	    		for(int i=0;i<linee.length;i++) {
	    			if(linee[i].equals("dd")) {
	    				continue;
	    			}else {
	      	        	E_entries.put(terminals[i], Integer.parseInt(linee[i]));
	    			}
	    		}
  	        	parserTable.put(NTerminals.get(lineNumber-1), E_entries);
  	    	  }
  	    	  lineNumber++;
  	      }
  	      
  	      scanner.close();
  	    } catch (FileNotFoundException e) {
  	      System.out.println("File not found: " + fileName);
  	    }

		return parserTable;
    }
    public static void traverseMap(Map<String, Map<String, Integer>> parserTable) {
		System.out.println("\n\n\n\n");
        for (String outerKey : parserTable.keySet()) {
            Map<String, Integer> innerMap = parserTable.get(outerKey);
            for (String innerKey : innerMap.keySet()) {
                Integer value = innerMap.get(innerKey);
                System.out.println("Outer Key: " + outerKey + ", Inner Key: " + innerKey + ", Value: " + value);
            }
        }
		System.out.println("\n\n\n\n");
    }
    
    public static void traverseProductions(String[][] productionsList) {
		System.out.println("\n\n\n\n");
//	     print the production rules
	    System.out.print("Production Rules:\n::= ");
	    for (String[] production : productionsList) {
		    for (String p : production) {
			      System.out.print(p);
		    }
	      System.out.print("\n::= ");
	    }
		System.out.println("\n\n\n\n");
    }
    
    public static void traverseTokens(String[][] tokens) {
		System.out.println("\n\n\n\n");
//	     print the production rules
		System.out.println("\n\nTokens:");
		for(int i=0;i<tokens.length;i++) {
			System.out.println(tokens[i][0]+" : "+tokens[i][1]);			
		}
		System.out.println("\n\n\n\n");
    }
    
    private static ArrayList<String> readNonTerminals(String fileName) {
    	ArrayList<String> nt= new ArrayList<>();
    	int i=0;
    	 try {
   	      Scanner scanner = new Scanner(new File(fileName));
   	      while (scanner.hasNextLine()) {
   	        String line = scanner.nextLine().trim();
   	        nt.add(line);
   	      }
   	      scanner.close();
   	    } catch (FileNotFoundException e) {
   	      System.out.println("File not found: " + fileName);
   	    }
    	 return nt;
    }
}
