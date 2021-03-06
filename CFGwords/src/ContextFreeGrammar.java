import com.sun.jdi.VMCannotBeModifiedException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * This is the main class for the CFG. Here all the major processing is done
 */
public class ContextFreeGrammar {
    private Set<String> NonTerminals = new HashSet<>();
    private Set<String> Terminals = new HashSet<>();
    private String StartState;
    private Vector<Production> Productions = new Vector<>();
    private Vector<String> generatedWords = new Vector<>();

    /**
     * @param path_to_file = Path in filesystem where the CFG is retained
     */
    ContextFreeGrammar(String path_to_file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader(path_to_file));

        /*Reading the non-terminals from the file and retaining them*/
        String line = scanner.nextLine();
        this.NonTerminals.addAll(Arrays.asList(line.split(" ")));

        /*Reading the terminals from the file and retaining them*/
        line = scanner.nextLine();
        this.Terminals.addAll(Arrays.asList(line.split(" ")));

        /*Reading the start state*/
        line = scanner.nextLine();
        this.StartState = line;

        /*Reading all the productions and creating Production objects*/
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            String[] parts = line.split("->|\\|");

            Production production;
            Vector<String> options = new Vector<>(Arrays.asList(parts).subList(1, parts.length));
            production = new Production(parts[0].trim(),options);

            /*Adding the production to our internal Productions vector*/
            this.Productions.add(production);
        }
    }

    Set<String> getTerminals() {
        return Terminals;
    }

    private Production findProduction (String startState) {
        for (Production production : this.Productions) {
            if(production.getStart().equals(startState)) {
                return production;
            }
        }

        return null;
    }

    void findWordsOfMaxLength (int maxLength) {

        /*Finding the start production*/
        Production start;

        start = findProduction(this.StartState);

        traverseGrammar(start, maxLength, 0, "");

        System.out.println(generatedWords);
    }

    /**
     * This function will call itself recursively and will generate words of a given max-length
     * @param start = The root production, our point of start
     */
    private void traverseGrammar (Production start, int maxLength, int currentLength, String partialWord) {



        if(partialWord.equals("?")) {
            partialWord = "";
        }

        if (currentLength > maxLength) {
            return;
        }

        for (String option:start.getNextOptions()) {
            String newstring = "";
            option = option.trim();
            StringBuilder word = new StringBuilder();
            word.append(partialWord);
            currentLength += Production.parseOption(option,this);
            Production production = new Production();

            if (currentLength > maxLength) {
                currentLength -= Production.parseOption(option,this);
                continue;
            }

            for (int i = 0; i < option.length(); i++) {
                if(Terminals.contains(String.valueOf(option.charAt(i)))) {
                    if(partialWord.contains("?")) {
                        newstring += option.charAt(i);
                    }
                    else {
                        word.append(option.charAt(i));
                    }
                }


                if (NonTerminals.contains(String.valueOf(option.charAt(i)))) {
                    if(partialWord.contains("?")) {
                        newstring += "?";
                    }
                    else {
                        word.append("?");
                    }
                    production = findProduction(String.valueOf(option.charAt(i)));
                }

            }

            String result;



            if (partialWord.contains("?")) {
                result = word.toString().replaceFirst("\\?", newstring);
            }
            else {
                result = word.toString();
            }


            if (result.contains("?")) {
                traverseGrammar(production,maxLength,currentLength,result);
            }
            else {
                if (result.equals("")){
                    result = "@";
                }
                generatedWords.add(result);
            }
            currentLength -= Production.parseOption(option,this);
        }
    }

    /**
     *
     * @return A more friendly format of the CFG
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        /*Printing non-terminals*/
        stringBuilder.append("N = { ");
        for (String nont : this.NonTerminals) {
            stringBuilder.append(nont).append(" ");
        }
        stringBuilder.append("}\n");

        /*Printing terminals*/
        stringBuilder.append("T = { ");
        for (String t : this.Terminals) {
            stringBuilder.append(t).append(" ");
        }
        stringBuilder.append("}\n");

        /*Printing start symbol*/
        stringBuilder.append("S = ").append(this.StartState).append("\n");

        /*Printing the productions*/
        for (Production production : Productions) {
            stringBuilder.append(production.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}

