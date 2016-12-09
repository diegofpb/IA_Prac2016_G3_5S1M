package window.impl;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;


public class WindowView {

    private static JLabel metroLabel;
    private static java.util.List<String> stationsList = new ArrayList<>();
    private static JComboBox<String> fromStation,toStation;
    private static final String NOT_SELECTABLE_OPTION = "Seleccione una estación";


    private static void initializeFrame() throws IOException {

        JFrame frame = new JFrame();
        Dimension windowDimensions = new Dimension(950, 650);

        stationsList.add(NOT_SELECTABLE_OPTION);
        stationsList.add("Estación 1");
        stationsList.add("Estación 2");

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
                        "Acerca de Metro Monterrey\n\nCreado por @Frildoren, @Garri23_23 & @diegofpb\ny programado con ❤ para Inteligencia Artificial [ETSIINF UPM]\nAño 2016",
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

        metroLabel = new JLabel("", metroMap, JLabel.CENTER);
        metroLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        metroLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getStopFromMap(e);
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
        searchPanel.setPreferredSize(new Dimension(250,280));
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

        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // will only show the current time
        timeSpinner.setPreferredSize(new Dimension(235,20));

        JButton searchRoute = new JButton("Buscar itinerario");
        searchRoute.setPreferredSize(new Dimension(235,25));


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


        // ITINERARY PANEL
        JPanel itineraryPanel = new JPanel();
        itineraryPanel.setPreferredSize(new Dimension(250,320));
        itineraryPanel.setBackground(Color.WHITE);

        JLabel suggestedLabel = new JLabel("Ruta sugerida:");
        suggestedLabel.setPreferredSize(new Dimension(250,20));
        suggestedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Font boldFont = new Font(suggestedLabel.getFont().getFontName(), Font.BOLD, suggestedLabel.getFont().getSize());
        suggestedLabel.setFont(boldFont);

        itineraryPanel.add(suggestedLabel);
        itineraryPanel.add(createResult(27,"San Genaro de Rodolfo"));


        leftPanel.add(searchPanel,BorderLayout.NORTH);
        leftPanel.add(itineraryPanel,BorderLayout.SOUTH);


        panel.add(leftPanel,BorderLayout.WEST);
        panel.add(rightPanel,BorderLayout.CENTER);

        // Show the frame.
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

    }

    private static JPanel createResult(int myMinutes, String stop) throws IOException {

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

        lines.add(createLineIcon(1));
        lines.add(new JLabel(">"));
        lines.add(createLineIcon(2));
        lines.add(new JLabel(">"));
        lines.add(createLineIcon(3));



        infoResume.add(lines,BorderLayout.NORTH);
        JLabel stopName = new JLabel("Salida desde "+stop);
        infoResume.add(stopName,BorderLayout.CENTER);

        JPanel detailedInfo = new JPanel(new BorderLayout());
        detailedInfo.setBackground(Color.WHITE);
        detailedInfo.add(new JLabel("12:17 - 12:44"),BorderLayout.NORTH);
        detailedInfo.add(new JLabel("7 paradas"),BorderLayout.SOUTH);

        infoResume.add(detailedInfo,BorderLayout.SOUTH);

        resultPanel.add(minutesResume,BorderLayout.WEST);
        resultPanel.add(infoResume,BorderLayout.CENTER);
        resultPanel.setBorder(BorderFactory.createLineBorder(new Color(47, 47, 47)));

        return resultPanel;
    }

    private static JPanel createLineIcon(int line) throws IOException {

        JPanel linePanel = new JPanel(new BorderLayout());
        linePanel.setPreferredSize(new Dimension(40,25));
        linePanel.setMaximumSize(new Dimension(40,25));

        JPanel colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(30,4));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        JLabel lineLabel = new JLabel("L"+line);

        ImageIcon imageIcon = new ImageIcon(ImageIO.read( WindowView.class.getResourceAsStream( "resources/subwayBlackIcon.png" ) ) );
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        JLabel iconLabel = new JLabel(imageIcon);

        lineLabel.setForeground(Color.black);

        infoPanel.add(lineLabel,BorderLayout.EAST);
        infoPanel.add(iconLabel,BorderLayout.WEST);

        switch (line){
            case 1:
                colorPanel.setBackground(new Color(255, 208, 8));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 208, 8)));
                break;
            case 2:
                colorPanel.setBackground(new Color(79, 139, 38));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(79, 139, 38)));
                break;
            case 3:
                colorPanel.setBackground(new Color(255, 60, 190));
                linePanel.setBorder(BorderFactory.createLineBorder(new Color(255, 60, 190)));
                break;

        }

        linePanel.add(infoPanel,BorderLayout.CENTER);
        linePanel.add(colorPanel,BorderLayout.SOUTH);

        return linePanel;

    }

    private static void setStationsList(java.util.List<String> list){
        stationsList = list;
    }

    private static void newItinerary (String from, String to){

        Integer minutes;
        ArrayList<Transition> transitions = new ArrayList<Transition>();

        // Here is a List of Transitions.
        for (int i=0;i<=transitions.size();i++){
            minutes =~ transitions.get(i).minutes;
            minutes =~ transitions.get(i).minutes;

            // Verify Lines and add to New List.

        }



    }

    private static void getStopFromMap (MouseEvent e){

        System.out.println(e.getX()+" , "+e.getY());
        String stationOne,stationTwo;

        // Here we get the stations that are around the point given.



        stationOne = "StationOne";
        stationTwo = "StationTwo";



        distance = Math.sqrt((x1-x2)(x1-x2) + (y1-y2)(y1-y2));


        for (int i = 0; i < fromStation.getItemCount() ; i++) {

            Object currentStationObject = fromStation.getItemAt(i);
            String selectedStation = fromStation.getSelectedItem().toString();





        }

    }

    public static void main(String[] args) {

        URL url = WindowView.class.getResource("stations.json");
        File file = new File(url.getPath());

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Station>>(){}.getType();
        Map<String, Station> stations = gson.fromJson(new InputStreamReader(new FileInputStream(file)), type);



        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                initializeFrame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
