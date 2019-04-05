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
    private ArrayList<Transition> Transitions = new ArrayList<>();

    /***
     *
     * @param path_to_file = path in filesystem where the automaton is defined
     * @throws InputMismatchException because each automaton file MUST begin with an integer (the number of transitions)
     * @throws FileNotFoundException because path may be wrong
     */
    Automaton(String path_to_file) throws InputMismatchException, FileNotFoundException{
        File file = new File(path_to_file);
        Scanner scanner = new Scanner(file);


        /*Because of the bug above, special treatment has to be given to the first line in the file*/
        boolean first = true;

        /*This for loop reads the automaton from the file and places the states and symbols in their respective sets*/
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            Transition transition = new Transition(parts[0],parts[1],parts[2]); //default treatment;
            if(first) { //special treatment for first line only
                initialState = parts[0].replace("@","");
                first = false;
            }

            Transitions.add(transition);
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

    String makeRegex () {
        StringBuilder result = new StringBuilder();

        /*Clean the automaton and make it ready for reaping*/
        if(F.contains(initialState)) {
            Transition transition = new Transition("nss", "!", initialState);
            initialState = transition.getStartState();
            Transitions.add(transition);
            Q.add(transition.getStartState());
        }
        if (F.size() > 1) {
            Iterator<String> iterator = F.iterator();
            for (int i = 0; i < F.size(); i++) {
                Transition transition = new Transition(iterator.next(),"!","nes");
                Transitions.add(transition);
                F.remove(transition.getStartState());
            }
            F.add("nes");
        }

        Iterator<String> iterator = Q.iterator();
        while (iterator.hasNext()) {
            String state = iterator.next();
            if(!state.equals(initialState) && !F.contains(state)) {
                stateReaperMethod(state);
            }
        }

        for (Transition t: Transitions
             ) {
            result.append(t.getSymbol());
        }

        return result.toString();
    }

    private void stateReaperMethod (String state) {
        StringBuilder part = new StringBuilder();
        String directExpresion = new String(), inExpression = new String(), outExpression = new String(), loopExpression = new String();
        ArrayList<Transition> toBeRemoved = new ArrayList<>();
        ArrayList<Transition> toBeAdded = new ArrayList<>();

        for (Transition t: Transitions) {
            if(t.getStartState().equals(state) && t.getNextState().equals(state)) {
                loopExpression = t.getSymbol();
                toBeRemoved.add(t);
            }
        }

        for (Transition t: Transitions
             ) {
            if(!t.getStartState().equals(state) && t.getNextState().equals(state)) {

                if(!t.getSymbol().equals("!")) {
                    inExpression = t.getSymbol();
                }
                toBeRemoved.add(t);
                for (Transition t2 : Transitions) {

                    if (t2.getStartState().equals(state) && !t2.getNextState().equals(state)) {
                        if(!t2.getSymbol().equals("!")) {
                            outExpression = t2.getSymbol();
                        }
                        toBeRemoved.add(t2);

                        /*Checking if there is already a connection between some nodes at each side of state*/
                        for (Transition t3: Transitions
                             ) {
                            if(t3.getNextState().equals(t2.getNextState()) && t3.getStartState().equals(t.getStartState())) {
                                if(!t3.getSymbol().equals("!")) {
                                    directExpresion = t3.getSymbol();
                                }
                                toBeRemoved.add(t3);
                            }
                        }

                        if(!directExpresion.isEmpty()) {
                            part.append(directExpresion);
                            part.append(" + ");
                        }
                        part.append(inExpression);
                        if(!loopExpression.isEmpty()) {
                            part.append(loopExpression);
                            part.append("*");
                        }
                        part.append(outExpression);

                        Transition transition = new Transition(t.getStartState(),part.toString(),t2.getNextState());
                        toBeAdded.add(transition);
                    }
                }
            }


        }



        /*Sanitize the automaton by removing the state, the adjacent transitions*/

        Transitions.removeAll(toBeRemoved);
        Transitions.addAll(toBeAdded);

        /*Adding a new transition*/
    }

}
