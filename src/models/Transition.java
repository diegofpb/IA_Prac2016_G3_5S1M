package models;

public class Transition {

    private Line line;
    private int cost;
    private String destination;
    private Double heuristic;

    private Transition parent;

    public Transition getParent() {
        return parent;
    }

    public void setParent(Transition parent) {
        this.parent = parent;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Double heuristic) {
        this.heuristic = heuristic;
    }
}
