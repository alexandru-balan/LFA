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
        lambdaStates.clear();
        TreeSet replace = new TreeSet<String>(Comparator.naturalOrder());
        for (Transition t: Transitions) {
            Iterator<String> iterator = nextStates.iterator();
            while (iterator.hasNext()) {
                String state = iterator.next();
                if (t.getStartState().equals(state) && t.getSymbol().equals(symbol)) {
                    replace.add(t.getNextState());
                }
                if (t.getStartState().equals(state) && t.getSymbol().equals("!")) {
                    String currentState = t.getStartState();
                    String currentSymbol = t.getSymbol();
                    while (!currentState.equals(symbol)){

                    }
                }

            }
        }

        if(replace.isEmpty()){
            return false;
        }
        else {
            this.nextStates = replace;
            return true;
        }
    }

    boolean acceptWord(String word) {
        this.nextStates.clear();
        nextStates.add(initialState);
        String[] dest;

        dest = word.split("");

        for (String s : dest) {
            if (!delta(s)) {
                System.out.println("Word is not accepted");
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
            System.out.println("Word not accepted");
            return false;
        }

        System.out.println("Word is accepted");
        return true;
    }
}
