package tokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

  public static String[][] getTokens(String fileName) {// the name of the file to be tokenized
    ArrayList<String> tokens = new ArrayList<>(); // to store the tokens
    ArrayList<Integer> tokensInline = new ArrayList<>(); //to store number of tokens in each line;
    
    try {
      Scanner scanner = new Scanner(new File(fileName));
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        // remove leading and trailing whitespaces
        line = line.trim();
        // split the line into individual tokens based on the specified symbols and reserved words
        String[] words = line.split("\\s+|(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
        for (int i=0;i<words.length;i++) {
          // check if the token is a symbol
          String word=words[i];
          if (isSymbol(word)) {
        	  //":=", "|=", "=<", "=>"
        	  if(i+1<words.length) {
        		  if(word.equals(":") && words[i+1].equals("=") ) {
                      tokens.add(word+words[i+1]);i++;
        		  }else if(word.equals("|") && words[i+1].equals("=") ) {
                      tokens.add(word+words[i+1]);i++;
        		  }else if(word.equals("=") && (words[i+1].equals("<") || words[i+1].equals(">")) ) {
                      tokens.add(word+words[i+1]);i++;
        		  }else{
                      tokens.add(word);
        		  }
        	  }else {
                  tokens.add(word);
        	  }
          // check if the token is a Reserved Word
          }else if(isReservedWord(word)){
              tokens.add(word);
          }
          // check if the token is a name
          else if (isName(word)) {
            tokens.add("name");
          }
          // check if the token is an integer value
          else if (isIntegerValue(word)) {
            tokens.add("integer-value");
          }
//          else {
//        	  System.out.println("unknown symbol");
//          }
        }
        tokensInline.add(tokens.size());
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + fileName);
    }
    
    // print the tokens
//    System.out.println("Tokens: " + tokens);
    return tokensto2D(tokens,tokensInline);
    
  }

	// method to check if a token is a reserved word
	  private static boolean isReservedWord(String word) {
    String[] reservedWords = {"project", "const", "var", "int", "subroutine", "begin", "end", "scan", "print", "if", "then", "endif", "else", "while", "do"};
    for (String reservedWord : reservedWords) {
      if (word.equals(reservedWord)) {
        return true;
      }
    }
    return false;
  }

  // method to check if a token is a symbol
  private static boolean isSymbol(String word) {
    String[] symbols = {":=", "|=", "=<", "=>", ".", ":", ";", ",", "+", "(", ")", "+", "-", "*", "/", "%", "=", "<", ">"};
    for (String symbol : symbols) {
      if (word.equals(symbol)) {
        return true;
      }
    }
    return false;
  }

  // method to check if a token is a name
  private static boolean isName(String word) {
    Pattern pattern = Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9]*$");
    Matcher matcher = pattern.matcher(word);
    return matcher.matches();
  }

	// method to check if a token is an integer value
	private static boolean isIntegerValue(String word) {
		try {
			Integer.parseInt(word);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	// create a 2D array to store the tokens and their corresponding types
	private static String[][] tokensto2D(ArrayList<String> tokens,ArrayList<Integer> tokensInline){
		int lineNumber=0;
		 String[][] tokenTypes = new String[tokens.size()][2];
		    for (int i = 0; i < tokens.size(); i++) {
		    	if(i>=tokensInline.get(lineNumber)) {
		    		lineNumber++;
		    	}
		      tokenTypes[i][0] = Integer.toString(lineNumber);
		      tokenTypes[i][1] = tokens.get(i);
		    }
		return tokenTypes;
	}
	
  }
