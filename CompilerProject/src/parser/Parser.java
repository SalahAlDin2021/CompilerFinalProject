package parser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
    private final Map<String, Map<String, Integer>> parserTable;
    private final String[][] productions;
    public String[][] table;

    public Parser(Map<String, Map<String, Integer>> parserTable, String[][] productions) {
        this.parserTable = parserTable;
        this.productions = productions;
    }

    public String parse(String[][] tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("project-declaration");

        int i = 0;
        while (!stack.empty()) {
            String top = stack.pop();
            if(top.equals("$") && i>=tokens.length-1) {
                return "Successful parsing.";
            }
            if(top.equals("")) {
            	continue;
            }else if (top.equals(tokens[i][1])) {
                i++;
            } else if (parserTable.containsKey(top)) {
                Map<String, Integer> row = parserTable.get(top);
                if (row.containsKey(tokens[i][1])) {
                    int productionIndex = row.get(tokens[i][1]);
                    String[] production = productions[productionIndex];
                    for (int j = production.length - 1; j >= 0; j--) {
                        stack.push(production[j]);
                    }
                } else {
                    return "line "+(1+Integer.parseInt(tokens[i][0]))+" :Error: got "+tokens[i][1]+", but expected " + row.keySet();
                }
            } else {
            	return "line "+(1+Integer.parseInt(tokens[i][0]))+" :Error:  got "+tokens[i][1]+", but expected " + top ;
            }
        }
        return "Successful parsing.";
    }
    
}
