package aftorex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Automaton {
    private String initialState;
    private TreeSet Q = new TreeSet<String>(Comparator.naturalOrder()); //States set
    private TreeSet E = new TreeSet<String>(Comparator.naturalOrder()); //Symbols set
    private TreeSet F = new TreeSet<String>(Comparator.naturalOrder()); //Final states set
    private TreeSet nextStates = new TreeSet<String>(Comparator.naturalOrder()); //Set that retains the next possible states after reading one symbol
    private TreeSet lambdaStates = new TreeSet<String>(Comparator.naturalOrder()); //Set that retains the next possible states after reading one symbol
                                                                                    //and passing though multiple lambda transitions
    private Transition[] Transitions;

    /***
     *
     * @param path_to_file = path in filesystem where the automaton is defined
     * @throws InputMismatchException because each automaton file MUST begin with an integer (the number of transitions)
     * @throws FileNotFoundException because path may be wrong
     */
    Automaton(String path_to_file) throws InputMismatchException, FileNotFoundException {
        File file = new File(path_to_file);
        Scanner scanner = new Scanner(file);

        int nbOfTransitions = scanner.nextInt();
        Transitions = new Transition[nbOfTransitions];

        /*reminder should have been the new_line character left over from reading the nb. of transitions, but instead
        * it is also the first string from the next line
        * BUG -> FIX ME*/
        String reminder = scanner.next();
        System.out.println(reminder);

        Q.add(reminder.replace("@",""));
        initialState = reminder.replace("@","");
        if(reminder.contains("@")) {
            F.add(reminder.replace("@",""));
        }

        /*Because of the bug above, special treatment has to be given to the first line in the file*/
        boolean first = true;

        /*This for loop reads the automaton from the file and places the states and symbols in their respective sets*/
        for(int i = 0; i < nbOfTransitions; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            Transition transition;
            if(first) { //special treatment for first line only
                transition = new Transition(reminder,parts[1],parts[2]);
                first = false;
            }
            else {
                transition = new Transition(parts[0],parts[1],parts[2]); //default treatment
            }
            Transitions[i] = transition;
            Q.add(parts[0].replace("@",""));
            Q.add(parts[2].replace("@",""));
            E.add(parts[1]);
            if(parts[0].contains("@")) {
                F.add(parts[0].replace("@",""));
            }
            if(parts[2].contains("@")) {
                F.add(parts[2].replace("@",""));
            }
        }
    }

    /***
     *
     * @return = a string with all the info about the read automaton
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Q={");
        Iterator iterator;
        iterator = this.Q.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            builder.append(" ");
        }
        builder.append("}\n");
        builder.append("E={");
        iterator = this.E.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            builder.append(" ");
        }
        builder.append("}\n");
        builder.append("q0 = ");
        builder.append(this.initialState);
        builder.append("\nF={");
        iterator = this.F.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            builder.append(" ");
        }
        builder.append("}\n");

        return builder.toString();
    }

    /***
     *
     * @param symbol = a symbol from the word that has to be accepted
     * @return = -> true if the automaton can change states by reading this symbol
     *           -> false if there are no transitions from the current states to other states with this symbol
     */
    private boolean delta(String symbol) {
        TreeSet<String> replacer = new TreeSet<>(Comparator.naturalOrder());
        Iterator<String> iterator = nextStates.iterator();

        while (iterator.hasNext()) {
            String stateToEvaluate = iterator.next();

            for (Transition t: Transitions) {
                if(t.getStartState().equals(stateToEvaluate) && t.getSymbol().equals(symbol)) {
                    replacer.add(t.getNextState());
                }
                if (t.getStartState().equals(stateToEvaluate) && t.getSymbol().equals("!")) {
                    lambdaTraversal(stateToEvaluate, symbol);
                    replacer.addAll(lambdaStates);

                    /*
                    Clean the lambdaStates set so it can be used by other states to make a lambdaTraversal
                     */
                    lambdaStates.clear();
                }
            }
        }

        if(replacer.isEmpty()) {
            return false;
        }

        nextStates = replacer;
        return true;
    }

    private void lambdaTraversal(String state, String symbol) {

        for (Transition t: Transitions) {
            if(t.getStartState().equals(state)) {
                if(t.getSymbol().equals(symbol)) {
                    lambdaStates.add(t.getNextState());
                }
                if (t.getSymbol().equals("!")) {
                    lambdaTraversal(t.getNextState(),symbol);
                }
            }
        }

    }

    boolean acceptWord(String word) {
        this.nextStates.clear();
        nextStates.add(initialState);
        String[] dest;

        dest = word.split("");

        for (String s : dest) {
            if (!delta(s)) {
                System.out.println("Word " + word + " is not accepted");
                return false;
            }
        }

        boolean hasFinalStates = false;
        Iterator<String> iterator = this.nextStates.iterator();
        while (iterator.hasNext()) {
            String toCompare = iterator.next();
            Iterator<String> iterator1 = this.F.iterator();
            while (iterator1.hasNext()){
                if(toCompare.equals(iterator1.next())) {
                    hasFinalStates = true;
                }
            }
        }

        if(!hasFinalStates) {
            System.out.println("Word " + word + " is not accepted");
            return false;
        }

        System.out.println("Word " + word + " is accepted");
        return true;
    }
}
