package aftorex;

import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) throws IOException, InputMismatchException{
        Automaton automaton = new Automaton("out/production/FA_to_REX/aftorex/automat_proiect.txt");

        System.out.println(automaton.toString());

        automaton.acceptWord("abbbbbabbbbba");
        automaton.acceptWord("abbbbbbbbbbbb");
        automaton.acceptWord("ababababababa");
        automaton.acceptWord("bbbbbbbbaaaaaa");
        automaton.acceptWord("");
    }
}
