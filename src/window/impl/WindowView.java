package window.impl;

import algo.AStar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Station;
import models.Transition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class WindowView {

    private static java.util.List<String> stationsList;
    private static JComboBox<String> fromStation, toStation;
    private static final String NOT_SELECTABLE_OPTION = "Seleccione una estación";
    private static Map<String, Station> stations;
    private static JPanel itineraryPanel;
    private static JSpinner timeSpinner;



    private static void initializeFrame() throws IOException {

        JFrame frame = new JFrame();
        Dimension windowDimensions = new Dimension(950, 650);

        // Set graphics settings, like size and position.
        frame.setSize(windowDimensions);
        frame.setPreferredSize(windowDimensions);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Metro Monterrey - IA 2016/17 - Grupo 3");


        // Set options of the bar buttons.
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set options to the menuBar.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");

        JMenuItem exitItem = new JMenuItem(new AbstractAction("Cerrar") {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenuItem aboutUsItem = new JMenuItem(new AbstractAction("Acerca de...") {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Acerca de Metro Monterrey\n\nCreado por @Frildoren, @MrGarri, @senar052 & @diegofpb\ny programado con ❤ para Inteligencia Artificial [ETSIINF UPM]\nAño 2016",
                        "Acerca de...",
                        JOptionPane.INFORMATION_MESSAGE
                );

            }
        });

        fileMenu.add(aboutUsItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        JPanel panel = new JPanel();

        // Create the panel for the right side.
        panel.setLayout(new BorderLayout());

        // Provide size pane.
        panel.setPreferredSize(windowDimensions);
        panel.setSize(windowDimensions);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.white);

        // Right Panel

        JLayeredPane rightPanel = new JLayeredPane();
        rightPanel.setBackground(Color.white);
        rightPanel.setPreferredSize(new Dimension(700,650));

        ImageIcon metroMap = new ImageIcon(ImageIO.read( WindowView.class.getResourceAsStream( "resources/map.png" ) ) );

        Image imageMap = metroMap.getImage(); // transform it
        Image newimg2 = imageMap.getScaledInstance(700, 520,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        metroMap = new ImageIcon(newimg2);  // transform it back

        JLabel metroLabel = new JLabel("", metroMap, JLabel.CENTER);
        metroLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        metroLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getStopFromMap(e.getX(),e.getY());
            }
        });



        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.add(metroLabel, BorderLayout.CENTER);
        backgroundPanel.setBackground(Color.WHITE);
        backgroundPanel.setBounds(0, 0, 700, 606);


        rightPanel.add(backgroundPanel, new Integer(3));

        rightPanel.repaint();

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setPreferredSize(new Dimension(250,295));
        searchPanel.setBackground(new Color(41, 42, 48));

        JPanel spacePanel1 = new JPanel();
        spacePanel1.setPreferredSize(new Dimension(250,5));
        spacePanel1.setBackground(new Color(41, 42, 48));

        JPanel spacePanel2 = new JPanel();
        spacePanel2.setPreferredSize(new Dimension(250,5));
        spacePanel2.setBackground(new Color(41, 42, 48));

        JPanel spacePanel3 = new JPanel();
        spacePanel3.setPreferredSize(new Dimension(250,5));
        spacePanel3.setBackground(new Color(41, 42, 48));

        JPanel spacePanel4 = new JPanel();
        spacePanel4.setPreferredSize(new Dimension(250,5));
        spacePanel4.setBackground(new Color(41, 42, 48));

        ImageIcon imageIcon = new ImageIcon(ImageIO.read( WindowView.class.getResourceAsStream( "resources/logo.png" ) ) );
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(90, 45,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel iconLabel = new JLabel(imageIcon);

        JLabel startLabel = new JLabel("  Comenzar en:");
        startLabel.setPreferredSize(new Dimension(250,15));
        startLabel.setForeground(Color.WHITE);

        fromStation = new JComboBox(stationsList.toArray());
        fromStation.setPreferredSize(new Dimension(250,20));

        JLabel endLabel = new JLabel("  Finalizar en:");
        endLabel.setPreferredSize(new Dimension(250,15));
        endLabel.setForeground(Color.WHITE);

        toStation = new JComboBox(stationsList.toArray());
        toStation.setPreferredSize(new Dimension(250,20));

        JLabel hourLabel = new JLabel("  Hora de salida:");
        hourLabel.setPreferredSize(new Dimension(250,15));
        hourLabel.setForeground(Color.WHITE);

        timeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // will only show the current time
        timeSpinner.setPreferredSize(new Dimension(235,20));

        JButton searchRoute = new JButton(new AbstractAction("Buscar Itinerario") {
            public void actionPerformed(ActionEvent e) {
                try {
                    newItinerary((String) fromStation.getSelectedItem(), (String) toStation.getSelectedItem());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        searchRoute.setPreferredSize(new Dimension(235,25));

        JButton clearRoute = new JButton(new AbstractAction("Reiniciar Búsqueda") {
            public void actionPerformed(ActionEvent e) {
                itineraryPanel.removeAll();
                itineraryPanel.updateUI();
                fromStation.setSelectedItem(NOT_SELECTABLE_OPTION);
                toStation.setSelectedItem(NOT_SELECTABLE_OPTION);
            }
        });
        clearRoute.setPreferredSize(new Dimension(235,25));



        searchPanel.add(iconLabel);
        searchPanel.add(spacePanel1);
        searchPanel.add(startLabel);
        searchPanel.add(fromStation);
        searchPanel.add(spacePanel2);
        searchPanel.add(endLabel);
        searchPanel.add(toStation);
        searchPanel.add(spacePanel3);
        searchPanel.add(hourLabel);
        searchPanel.add(timeSpinner);
        searchPanel.add(spacePanel4);
        searchPanel.add(searchRoute);
        searchPanel.add(clearRoute);

        // ITINERARY PANEL
        itineraryPanel = new JPanel();
        itineraryPanel.setPreferredSize(new Dimension(250,310));
        itineraryPanel.setBackground(Color.WHITE);


        leftPanel.add(searchPanel,BorderLayout.NORTH);
        leftPanel.add(itineraryPanel,BorderLayout.SOUTH);


        panel.add(leftPanel,BorderLayout.WEST);
        panel.add(rightPanel,BorderLayout.CENTER);

        // Show the frame.
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    private static JPanel createResult(int myMinutes, String fromStop, List<String> linesList, int stops) throws IOException {

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setPreferredSize(new Dimension(240,80));
        resultPanel.setMaximumSize(new Dimension(240,80));

        // MINUTES RESUME
        JPanel minutesResume = new JPanel();
        minutesResume.setPreferredSize(new Dimension(50,80));
        minutesResume.setBackground(Color.WHITE);

        JLabel minutes = new JLabel(Integer.toString(myMinutes));
        Font boldFont18 = new Font(minutes.getFont().getFontName(), Font.BOLD, 18);
        minutes.setFont(boldFont18);
        minutes.setForeground(new Color(240, 99, 52));

        JLabel min = new JLabel("MIN");
        Font boldFont14 = new Font(minutes.getFont().getFontName(), Font.BOLD, 14);
        min.setFont(boldFont14);
        min.setForeground(new Color(240, 99, 52));

        minutesResume.add(minutes);
        minutesResume.add(min);

        minutes.setHorizontalTextPosition(SwingConstants.CENTER);
        minutes.setVerticalTextPosition(SwingConstants.CENTER);


        // INFO RESUME

        JPanel infoResume = new JPanel(new BorderLayout());
        infoResume.setPreferredSize(new Dimension(190,90));
        infoResume.setBackground(Color.WHITE);


        JPanel spacePanel1 = new JPanel();
        spacePanel1.setPreferredSize(new Dimension(10,90));
        spacePanel1.setBackground(Color.WHITE);


        JPanel lines = new JPanel();
        lines.setBackground(Color.WHITE);



        linesList.forEach(item->{
            try {
                lines.add(createLineIcon(item));
                lines.add(new JLabel(">"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        infoResume.add(lines,BorderLayout.NORTH);
        JLabel stopName = new JLabel("Salida desde "+fromStop);
        infoResume.add(stopName,BorderLayout.CENTER);


        String startHour = new SimpleDateFormat("HH:mm").format(timeSpinner.getValue());

        String endHour = new SimpleDateFormat("HH:mm").format(timeSpinner.getValue());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        try {
            d = df.parse(endHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, myMinutes);
        String newTime = df.format(cal.getTime());


        JPanel detailedInfo = new JPanel(new BorderLayout());
        detailedInfo.setBackground(Color.WHITE);
        detailedInfo.add(new JLabel(startHour+" - "+newTime),BorderLayout.NORTH);
        detailedInfo.add(new JLabel(stops+" paradas"),BorderLayout.SOUTH);

        infoResume.add(detailedInfo,BorderLayout.SOUTH);

        resultPanel.add(minutesResume,BorderLayout.WEST);
        resultPanel.add(infoResume,BorderLayout.CENTER);
        resultPanel.setBorder(BorderFactory.createLineBorder(new Color(47, 47, 47)));

        return resultPanel;
    }

    private static JPanel createLineIcon(String line) throws IOException {

        JPanel linePanel = new JPanel(new BorderLayout());
        linePanel.setPreferredSize(new Dimension(40,25));
        linePanel.setMaximumSize(new Dimension(40,25));

        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(30,4));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        JLabel lineLabel = new JLabel(line);

        ImageIcon imageIcon = new ImageIcon(ImageIO.read( WindowView.class.getResourceAsStream( "resources/subwayBlackIcon.png" ) ) );
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel iconLabel = new JLabel(imageIcon);

        lineLabel.setForeground(Color.black);

        infoPanel.add(lineLabel,BorderLayout.EAST);
        infoPanel.add(iconLabel,BorderLayout.WEST);

        switch (line){
            case "L1":
                colorPanel.setBackground(new Color(255, 208, 8));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 208, 8)));
                break;
            case "L2":
                colorPanel.setBackground(new Color(79, 139, 38));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(79, 139, 38)));
                break;
            case "L3":
                colorPanel.setBackground(new Color(255, 60, 190));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 60, 190)));
                break;

        }

        linePanel.add(infoPanel,BorderLayout.CENTER);
        linePanel.add(colorPanel,BorderLayout.SOUTH);

        return linePanel;

    }

    private static void newItinerary (String from, String to) throws IOException {

        if ((!Objects.equals(from, NOT_SELECTABLE_OPTION) && !Objects.equals(to, NOT_SELECTABLE_OPTION) &&!Objects.equals(from, to))){

            itineraryPanel.removeAll();
            Map<String, Station> copyOfMap;
            copyOfMap = stations;

            AStar a = new AStar(copyOfMap, from, to);
            a.solve();

            JLabel suggestedLabel = new JLabel("Ruta sugerida:");
            suggestedLabel.setPreferredSize(new Dimension(250,20));
            suggestedLabel.setHorizontalAlignment(SwingConstants.CENTER);
            Font boldFont = new Font(suggestedLabel.getFont().getFontName(), Font.BOLD, suggestedLabel.getFont().getSize());
            suggestedLabel.setFont(boldFont);

            java.util.List<String> lineList = new java.util.ArrayList<>();

            a.getTransitions().forEach(item->{
                if (!lineList.contains(item.getLine().toString())){
                    lineList.add(item.getLine().toString());
                }
            });


            java.util.List<Transition> transitionList;
            transitionList = a.getTransitions();

            DefaultListModel listModel = new DefaultListModel();
            JList list = new JList(listModel);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setLayoutOrientation(JList.VERTICAL);
            list.setVisibleRowCount(-1);

            listModel.addElement("Salida desde "+from);
            for (int i = 0; i < transitionList.size();i++){
                if (i != transitionList.size()-1){
                    listModel.addElement(transitionList.get(i).getDestination()+" - ("+transitionList.get(i).getLine()+")");
                }else{
                    listModel.addElement("LLegada a destino " + transitionList.get(i).getDestination() + " ("+transitionList.get(i).getLine() + ")");
                }

            }


            JScrollPane listScroller = new JScrollPane(list);
            listScroller.setPreferredSize(new Dimension(240, 150));


            JLabel itinerayLabel = new JLabel("Itinerario sugerido:");
            itinerayLabel.setPreferredSize(new Dimension(275,20));
            itinerayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            itinerayLabel.setFont(boldFont);


            itineraryPanel.add(suggestedLabel);
            itineraryPanel.add(createResult(a.getCost(),from,lineList,a.getTransitions().size()));
            itineraryPanel.add(itinerayLabel);
            itineraryPanel.add(listScroller);
            itineraryPanel.updateUI();

        }else{
            String infoMessage = "Una de las paradas no ha sido seleccionada o el inicio y el destino son el mismo.";
            JOptionPane.showMessageDialog(null, infoMessage, "Error" ,JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void getStopFromMap (double x, double y){

        double distance = 1000;
        String selectedStation="";

        for(Station station : stations.values()) {
            Double dis2 = station.getPoint().distance(x,y);

            if (dis2<distance){
                distance=dis2;
                selectedStation=station.getName();
            }
        }

        if (fromStation.getSelectedItem()==NOT_SELECTABLE_OPTION){
            fromStation.setSelectedItem(selectedStation);
        }else{
            toStation.setSelectedItem(selectedStation);
        }

    }

    public static void main(String[] args) throws FileNotFoundException {

        URL url = WindowView.class.getClassLoader().getResource("stations.json");
        File file = new File(url.getPath());

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Station>>(){}.getType();
        stations = gson.fromJson(new InputStreamReader(new FileInputStream(file)), type);


        stationsList = new ArrayList<>(stations.keySet());
        stationsList.add(0,NOT_SELECTABLE_OPTION);



        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                initializeFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
