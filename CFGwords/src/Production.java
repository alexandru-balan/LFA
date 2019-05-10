import java.util.Vector;

/**
 * The production class is used to create and operate with the productions of a CFG
 */
public class Production {
    private String start;
    private Vector<String> nextOptions;


    Production() {
        this.start = "0";
        this.nextOptions = new Vector<>();
        this.nextOptions.add("0");
    }

    /**
     * Initializing a Production object
     * @param start = The start symbol for a given production
     * @param options = A set of possible terminals, non-terminals or both for the CFG to go into
     */
    Production(String start, Vector<String> options) {
        this.nextOptions = new Vector<>();
        this.nextOptions = options;

        this.start = start;
    }

    String getStart() {
        return start;
    }

    Vector<String> getNextOptions() {
        return nextOptions;
    }

    /**
     *
     * @return The production object in a more user friendly, easily readable format
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.start).append(" -> ");
        for (String option : this.nextOptions) {
            stringBuilder.append(option).append(" | ");
        }
        // Deleting the last occurrence "| " because it is the last option
        stringBuilder.delete(stringBuilder.lastIndexOf("| "),stringBuilder.toString().length());

        return stringBuilder.toString();
    }

    public static int parseOption (String option, ContextFreeGrammar cfg) {
        int legth = 0;
        for (int i = 0; i < option.length(); i++) {
            if (cfg.getTerminals().contains(String.valueOf(option.charAt(i)))) {
                legth++;
            }
        }

        return legth;
    }

    public static boolean canStopHere (String option, ContextFreeGrammar cfg) {
        boolean canStop = true;
        for (int i = 0; i < option.length(); i++) {
            if (cfg.getNonTerminals().contains(String.valueOf(option.charAt(i)))) {
                canStop = false;
            }
        }

        return canStop;
    }
}
