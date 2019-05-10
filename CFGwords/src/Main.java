import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ContextFreeGrammar contextFreeGrammar = new ContextFreeGrammar("CFG.txt");

        System.out.println(contextFreeGrammar);

        contextFreeGrammar.findWordsOfMaxLength(3);
    }
}
