package aftorex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Automaton {
    private int nbOfTransitions;
    private String initialState;
    private TreeSet<String> Q = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet E = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet F = new TreeSet<String>(Comparator.naturalOrder());
    private Transition[] Transitions;

    public Automaton(String path_to_file) throws InputMismatchException, FileNotFoundException {
        File file = new File(path_to_file);
        Scanner scanner = new Scanner(file);

        nbOfTransitions = scanner.nextInt();
        Transitions = new Transition[nbOfTransitions];
        String reminder = scanner.next();
        Q.add(reminder.replace("@",""));
        initialState = reminder.replace("@","");
        if(reminder.contains("@")) {
            F.add(reminder.replace("@",""));
        }


        for(int i = 0; i < nbOfTransitions; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            Transition transition = new Transition(parts[0],parts[1],parts[2]);
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

            /*
             * purpose_of_next_part = the next part defines how the automaton should behave if it has an "!"(lambda)
             * in its composition. Since the NFA and DFA are particular cases of lambda-NFA there would be no problem
             * in retaining all three of them through the same class if the behaviours are handled correctly.
             */
            /*if (parts[1].equals("!")) {

            }*/
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Q={");
        Iterator<String> iterator;
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
}
