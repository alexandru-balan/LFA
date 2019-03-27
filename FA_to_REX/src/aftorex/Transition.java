package aftorex;

public class Transition {
    private String startState;
    private String nextState;
    private String symbol;

    public Transition(String startState, String symbol, String nextState) {
        this.nextState = nextState;
        this.startState = startState;
        this.symbol = symbol;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


}
