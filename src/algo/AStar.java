package algo;

import models.Station;
import models.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AStar {

    private Map<String, Station> data;
    private Station destination;

    private int cost;
    private TreeMap<Double, Transition> availableTransitions;
    private List<Station> visitedStations;
    private List<Transition> usedTransitions;

    public AStar(Map<String, Station> data, String from, String to){
        this.data = data;
        this.destination = data.get(to);

        cost = 0;
        availableTransitions = new TreeMap<>();
        visitedStations = new ArrayList<>();
        usedTransitions = new ArrayList<>();

        Station f = data.get(from);
        visitedStations.add(f);
        addTransitions(f);
    }

    public boolean solve(){
        boolean res = false;

        while(!res && availableTransitions.size() > 0) {
            Transition transition = availableTransitions.firstEntry().getValue();
            if(!visitedStations.contains(data.get(transition.getDestination()))) {
                visitStation(transition);
                res = transition.getDestination().equals(destination.getName());
            }

            availableTransitions.remove(availableTransitions.firstKey());
        }

        return res;
    }

    public List<Transition> getResult(){
        return usedTransitions;
    }

    private void visitStation(Transition transition){
        Station station = data.get(transition.getDestination());

        visitedStations.add(station);
        usedTransitions.add(transition);
        cost += transition.getCost();

        addTransitions(station);
        availableTransitions.remove(transition);
    }

    private void addTransitions(Station station){
        for (Transition t : station.getTransitions()) {
            Station destStation = data.get(t.getDestination());
            if(!visitedStations.contains(destStation)) {
                availableTransitions.put(cost + destStation.getPoint().distance(destination.getPoint()), t);
            }
        }
    }

}
