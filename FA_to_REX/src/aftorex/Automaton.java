package aftorex;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class Automaton {
    private int nbOfTransitions;
    private String initialState;
    private TreeSet Q = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet E = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet F = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet nextStates = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet lambdaStates = new TreeSet<String>(Comparator.naturalOrder());
    private Transition[] Transitions;

    Automaton(String path_to_file) throws InputMismatchException, FileNotFoundException {
        File file = new File(path_to_file);
        Scanner scanner = new Scanner(file);

        nbOfTransitions = scanner.nextInt();
        Transitions = new Transition[nbOfTransitions];

        String reminder = scanner.next();
        System.out.println(reminder);

        Q.add(reminder.replace("@",""));
        initialState = reminder.replace("@","");
        if(reminder.contains("@")) {
            F.add(reminder.replace("@",""));
        }

        boolean first = true;


        for(int i = 0; i < nbOfTransitions; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            Transition transition;
            if(first) {
                transition = new Transition(reminder,parts[1],parts[2]);
                first = false;
            }
            else {
                transition = new Transition(parts[0],parts[1],parts[2]);
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

    boolean delta(String symbol) {
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
