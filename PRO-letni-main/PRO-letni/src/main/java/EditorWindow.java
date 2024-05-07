
import component.BaseComponent;
import component.Circle;
import component.Line;
import component.Rectangle;
import listener.ComponentChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorWindow extends JFrame implements ComponentChangeListener {

    private final DrawingCanvas drawingCanvas;

    private ComponentTableModel componentTableModel;

    private final JTable tableComponents;

    private final ComponentList componentList;

    private final int TOOLBAR_WIDTH = 300;

    private final ProjectSaver projectSaver;

    private JTextArea svgTextArea;


    public EditorWindow(int w, int h) throws HeadlessException {

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("GITOFSON");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.drawingCanvas = new DrawingCanvas(w, h, this);
        this.componentList = ComponentList.getINSTANCE();
        this.projectSaver = new ProjectSaver(componentList);

        setVisible(true);

        setLayout(new BorderLayout());


        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);

        add(toolBar, BorderLayout.LINE_END);

        add(drawingCanvas, BorderLayout.CENTER);

        JMenuBar mainMenuBar = new JMenuBar();
        add(mainMenuBar, BorderLayout.PAGE_START);

        JMenu fileMenu = new JMenu("File");

        mainMenuBar.add(fileMenu);

        JMenuItem openProject = new JMenuItem("Open project");
        openProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openProject.addActionListener(click -> {

            JFileChooser openChooser = new JFileChooser();
            openChooser.setDialogTitle("Choose file to open");
            openChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

            FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON projects", "json");
            openChooser.setFileFilter(filter);

            int result = openChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFilePath = openChooser.getSelectedFile().getAbsolutePath();
                System.out.println(selectedFilePath);
                projectSaver.loadProject(selectedFilePath);
                updateAll();
            }
        });

        JMenuItem saveProject = new JMenuItem("Save project");
        saveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveProject.addActionListener(click -> {
            JFileChooser saveChooser = new JFileChooser();
            saveChooser.setDialogTitle("Specify a project to save");
            int result = saveChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String saveLocation = saveChooser.getSelectedFile().getAbsolutePath();
                projectSaver.saveProject(saveLocation + ".json");
                System.out.println("Save: " + saveLocation + ".json");
            }

        });

        fileMenu.add(openProject);
        fileMenu.add(saveProject);

        JPanel panObjects = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panObjects.setMaximumSize(new Dimension(TOOLBAR_WIDTH * 2, 35));

        JButton btnAddRectangle = new JButton("Rectangle");
        JButton btnAddLine = new JButton("Line");
        JButton btnAddOval = new JButton("Circle");

        panObjects.add(btnAddOval);
        panObjects.add(btnAddRectangle);
        panObjects.add(btnAddLine);


        JPanel panRgb = new JPanel(new FlowLayout());

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);

        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0);
        numberFormatter.setMaximum(255);

        JLabel labRed = new JLabel("R: ");
        JFormattedTextField txtRed = new JFormattedTextField(numberFormatter);
        txtRed.setText("0");
        txtRed.setColumns(3);
        panRgb.add(labRed);
        panRgb.add(txtRed);

        JLabel labGreen = new JLabel("G: ");
        JFormattedTextField txtGreen = new JFormattedTextField(numberFormatter);
        txtGreen.setText("0");
        txtGreen.setColumns(3);
        panRgb.add(labGreen);
        panRgb.add(txtGreen);

        JLabel labBlue = new JLabel("B: ");
        JFormattedTextField txtBlue = new JFormattedTextField(numberFormatter);
        txtBlue.setText("0");
        txtBlue.setColumns(3);

        panRgb.add(labBlue);
        panRgb.add(txtBlue);


        panRgb.setMaximumSize(new Dimension(TOOLBAR_WIDTH, 35));


        toolBar.add(panObjects);
        toolBar.add(panRgb);


        componentTableModel = new ComponentTableModel(componentList);

        tableComponents = new JTable(componentTableModel);

        tableComponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableComponents.setPreferredSize(new Dimension(300, 100));
        tableComponents.setPreferredScrollableViewportSize(tableComponents.getPreferredSize());


        tableComponents.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                drawingCanvas.updateComponent(tableComponents.getSelectedRow());
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableComponents);

        scrollTable.setMaximumSize(new Dimension(TOOLBAR_WIDTH, 100));

        toolBar.add(scrollTable);

        btnAddRectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Rectangle c = new Rectangle();

                c.setColor(new Color(Integer.parseInt(txtRed.getText()), Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText()))); //todo try

                componentList.add(c);
                drawingCanvas.addComponent(componentList.getComponents().size() - 1);
            }
        });
        btnAddLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Line c = new Line();

                c.setColor(new Color(Integer.parseInt(txtRed.getText()), Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText()))); //todo try

                componentList.add(c);
                drawingCanvas.addComponent(componentList.getComponents().size() - 1);
            }
        });
        btnAddOval.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Circle c = new Circle();

                c.setColor(new Color(Integer.parseInt(txtRed.getText()), Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText()))); //todo try

                componentList.add(c);
                drawingCanvas.addComponent(componentList.getComponents().size() - 1);
            }
        });


        JButton confirm = new JButton("Confirm");
        JButton delete = new JButton("Delete");

        panObjects.add(confirm);
        panObjects.add(delete);


        svgTextArea = new JTextArea();
        svgTextArea.setEditable(true);
        svgTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDrawingFromSVG();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDrawingFromSVG();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDrawingFromSVG();
            }
        });
        toolBar.add(svgTextArea);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseComponent p = componentList.getComponents().get(tableComponents.getSelectedRow());
                p.setColor(new Color(Integer.parseInt(txtRed.getText()), Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText())));
                updateSvgTextArea();
            }
        });
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                componentList.remove(componentList.getComponents().get(tableComponents.getSelectedRow()));
                drawingCanvas.repaint();
                componentTableModel = new ComponentTableModel(componentList);
                tableComponents.setModel(componentTableModel);
            }
        });

    }

    @Override
    public void updateSvgTextArea() {
        StringBuilder svgContent = new StringBuilder();
        svgContent.append("<?xml version=\"1.0\" standalone=\"no\"?>\n");
        svgContent.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" "
                + "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
        svgContent.append("<svg width=\"4000\" height=\"4000\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        for (BaseComponent component : componentList.getComponents()) {
            svgContent.append(component.toSVG()).append("\n");
        }
        svgContent.append("</svg>");
        svgTextArea.setText(svgContent.toString());
    }



    private void updateDrawingFromSVG() {
        String svgContent = svgTextArea.getText();
        componentList.getComponents().clear();
        Pattern pattern = Pattern.compile("<(rect|circle|line)\\s+(.*?)\\s*/?>");
        Matcher matcher = pattern.matcher(svgContent);
        while (matcher.find()) {
            String shapeType = matcher.group(1);
            String attributes = matcher.group(2);
            switch (shapeType) {
                case "rect":
                    parseRectangle(attributes);
                    break;
                case "circle":
                    parseCircle(attributes);
                    break;
                case "line":
                    parseLine(attributes);
                    break;
            }
        }
        updateTableRow();
        drawingCanvas.repaint();
    }


    private void parseRectangle(String attributes) {
        Matcher matcher = Pattern.compile("(\\w+)=\"([^\"]*)\"").matcher(attributes);
        int x = 0, y = 0, width = 0, height = 0;
        Color color = Color.BLACK;
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            switch (key) {
                case "x":
                    x = Integer.parseInt(value);
                    break;
                case "y":
                    y = Integer.parseInt(value);
                    break;
                case "width":
                    width = Integer.parseInt(value);
                    break;
                case "height":
                    height = Integer.parseInt(value);
                    break;
                case "fill":
                    color = Color.decode(value);
                    break;
            }
        }
        componentList.getComponents().add(new Rectangle(x, y, width, height, color));
    }

    private void parseCircle(String attributes) {
        Matcher matcher = Pattern.compile("(\\w+)=\"([^\"]*)\"").matcher(attributes);
        int cx = 0, cy = 0, r = 0;
        Color color = Color.BLACK;
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            switch (key) {
                case "cx":
                    cx = Integer.parseInt(value);
                    break;
                case "cy":
                    cy = Integer.parseInt(value);
                    break;
                case "r":
                    r = Integer.parseInt(value);
                    break;
                case "fill":
                    color = Color.decode(value);
                    break;
            }
        }
        componentList.getComponents().add(new Circle(cx, cy, r, color));
    }

    private void parseLine(String attributes) {
        Matcher matcher = Pattern.compile("(\\w+)=\"([^\"]*)\"").matcher(attributes);
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        Color color = Color.BLACK;
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            switch (key) {
                case "x1":
                    x1 = Integer.parseInt(value);
                    break;
                case "y1":
                    y1 = Integer.parseInt(value);
                    break;
                case "x2":
                    x2 = Integer.parseInt(value);
                    break;
                case "y2":
                    y2 = Integer.parseInt(value);
                    break;
                case "stroke":
                    color = Color.decode(value);
                    break;
            }
        }
        componentList.getComponents().add(new Line(x1, y1, x2, y2, color));
    }

    @Override
    public void onComponentsChange() {
        tableComponents.repaint();
    }

    @Override
    public void updateTableRow() {
        tableComponents.changeSelection(componentTableModel.getRowCount() - 1, 0, true, false);
    }


    public void updateAll() {
        drawingCanvas.repaint();
        onComponentsChange();
        updateTableRow();
    }

    public void updateTime() {
        componentList.addEditTime();

        long editTime = componentList.getEditTime();
        int hours = (int) (editTime / 3600);
        int minutes = (int) (editTime % 3600) / 60;
        int seconds = (int) (editTime % 60);

    }

}
