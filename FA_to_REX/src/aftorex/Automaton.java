package aftorex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;

public class Automaton {
    private int nbOfTransitions;
    private String initialState;
    private TreeSet Q = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet E = new TreeSet<String>(Comparator.naturalOrder());
    private TreeSet F = new TreeSet<String>(Comparator.naturalOrder());
    private Transition[] Transitions;

    public Automaton(String path_to_file) throws InputMismatchException, FileNotFoundException {
        File file = new File(path_to_file);
        Scanner scanner = new Scanner(file);

        nbOfTransitions = scanner.nextInt();

        System.out.println(nbOfTransitions);
    }
}
