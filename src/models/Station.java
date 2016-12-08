package models;

import java.awt.geom.Point2D;
import java.util.List;

public class Station {

    private String name;

    private List<Transition> transitions;
    private double x;
    private double y;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public Point2D getPoint () {
        return new Point2D.Double(x, y);
    }
}
