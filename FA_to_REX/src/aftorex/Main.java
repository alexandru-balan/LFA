package aftorex;

import java.io.IOException;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) throws IOException, InputMismatchException{
        Automaton automaton = new Automaton("out/production/FA_to_REX/aftorex/automat.txt");

        System.out.println(automaton.toString());

        automaton.acceptWord("ab");
    }
}
