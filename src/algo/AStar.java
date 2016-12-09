package algo;

import models.Station;
import models.Transition;

import java.util.*;

public class AStar {

    private Map<String, Station> data;
    private Station destination;

    private List<Transition> availableTransitions;
    private List<Station> visitedStations;
    private List<Transition> usedTransitions;

    private List<Transition> result;

    public AStar(Map<String, Station> data, String from, String to){
        this.data = data;
        this.destination = data.get(to);

        availableTransitions = new ArrayList<>();
        visitedStations = new ArrayList<>();
        usedTransitions = new ArrayList<>();

        Station f = data.get(from);
        visitedStations.add(f);
        addTransitions(f, null);
    }

    public boolean solve(){
        boolean res = false;

        while(!res && availableTransitions.size() > 0) {
            Transition transition = firstTransition();
            availableTransitions.remove(transition);

            if(!visitedStations.contains(data.get(transition.getDestination()))) {
                visitStation(transition);
                res = transition.getDestination().equals(destination.getName());
            }
        }

        if(res){
            result = new ArrayList<>();
            Transition t = usedTransitions.get(usedTransitions.size() - 1);
            while(t.getParent() != null){
                result.add(t);
                t = t.getParent();
            }

            Collections.reverse(result);
        }

        return res;
    }

    private Transition firstTransition(){
        return availableTransitions.stream().min(Comparator.comparing(Transition::getHeuristic)).get();
    }

    private void visitStation(Transition transition){
        Station station = data.get(transition.getDestination());

        visitedStations.add(station);
        usedTransitions.add(transition);

        addTransitions(station, transition);
    }

    private void addTransitions(Station station, Transition usedTransition){
        for (Transition t : station.getTransitions()) {
            Station destStation = data.get(t.getDestination());

            t.setParent(usedTransition);
            t.setHeuristic((usedTransition != null ? usedTransition.getCost() : 0) + destStation.getPoint().distance(destination.getPoint()));
            t.setCost((usedTransition != null ? usedTransition.getCost() : 0) + t.getCost());

            if(!visitedStations.contains(destStation)) {
                Optional<Transition> rival = availableTransitions.stream()
                        .filter(transition -> transition.getDestination().equals(destStation.getName()))
                        .findFirst();

                if(rival.isPresent()){
                    if(rival.get().getCost() > t.getCost()){
                        availableTransitions.remove(rival.get());
                        availableTransitions.add(t);
                    }
                } else
                    availableTransitions.add(t);
            }
        }
    }

    public List<Transition> getTransitions(){
        return result;
    }

    public int getCost() {
        return usedTransitions.get(usedTransitions.size()).getCost();
    }

}
