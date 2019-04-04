package aftorex;

public class Transition {
    private String startState;
    private String nextState;
    private String symbol;

    Transition(String startState, String symbol, String nextState) {
        this.nextState = nextState.replace("@","");
        this.startState = startState.replace("@","");
        this.symbol = symbol.replace("@","");
    }

    String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}
