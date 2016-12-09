import algo.AStar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Station;
import models.Transition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        URL url = Main.class.getResource("stations.json");
        File file = new File(url.getPath());

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Station>>(){}.getType();
        Map<String, Station> stations = gson.fromJson(new InputStreamReader(new FileInputStream(file)), type);

        AStar a = new AStar(stations, "San Bernabé", "Lerdo de Tejada");
        a.solve();
        List<Transition> result = a.getTransitions();

        System.out.print(true);
    }

}
