/*
 * Name: Luis Vargas
 * Course: CNT 4714 - Spring 2022
 * Assignment Title: Project 3 - Two-Tier Client-Server Application Development With MySQL and JDBC
 * Due Date: Sunday, March 20, 2022
 */
package src;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;


/**
 * @author unknown
 */
public class GUIController extends JFrame {

    public GUIController() throws ClassNotFoundException, SQLException, IOException  {
        initComponents();
        GUIController.this.sqlClearCommandButton.setEnabled(true);
        GUIController.this.sqlExecuteCommandButton.setEnabled(true);
        GUIController.this.sqlExecutionClearButton.setEnabled(true);
        GUIController.this.dbConnectionStatusText.setText("WAITING FOR INPUT TO ESTABLISH CONNECTION");
        GUIController.this.dbConnectionStatusText.setFont(GUIController.this.dbConnectionStatusText.getFont().deriveFont(GUIController.this.dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
        GUIController.this.dbDisconnectButton.setEnabled(false);

        String[] properties = {"root.properties", "client.properties", "project3.properties"};
        for( String i : properties ){
            propertiesFileComboBox.addItem(i);
        }


        sqlClearCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sqlCommandWindow.setText("");
            }
        });

        sqlExecutionClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // TODO: execute SQL command from sqlCommandWindow
        // TODO: Parse SQL Command to execute using JDBC connection and resultSet
        sqlExecuteCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    resultSet = statement.executeQuery(sqlCommandWindow.getText());
                    metaData = resultSet.getMetaData();
                    DefaultTableModel model = new DefaultTableModel();


                    int numberOfCols =  metaData.getColumnCount();
                    String colName;
                    sqlResults.setModel(model);
                    sqlResults.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                    for( int i = 1; i <= numberOfCols; i++){
//                        System.out.printf("%-20s\t", metaData.getColumnName(i));
                        sqlResults.addColumn((JTable)metaData.getColumnName(i) );
                    }
                    System.out.println();
                    for( int i = 1; i<= numberOfCols; i++){
                        System.out.printf("%-20s\t", "-------------");
                    }
                    System.out.println();

//                    System.out.println("WE HERE");
                    while(resultSet.next()){
                        for( int i = 1; i <= numberOfCols; i++){
                            System.out.printf("%-20s\t", resultSet.getObject(i));
                        }
                        System.out.println();
                    }
                    System.out.println();System.out.println();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // TODO: Connect to database using JDBC -> Done
        // TODO: Use USERNAME & PASSWORD to connect to DB -> Done
        // TODO: Update the DB Status field -> Done
        dbConnectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Properties properties = new Properties();
                FileInputStream filein;

                try{
                    filein = new FileInputStream( "src/" + propertiesFileComboBox.getSelectedItem() );
                    properties.load(filein);
                    dataSource = new MysqlDataSource();
                    dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
                    if( Objects.equals(properties.getProperty("MYSQL_DB_USERNAME"), String.valueOf(connectionDetailsUsernameText.getText()))
                            && Objects.equals(properties.getProperty("MYSQL_DB_PASSWORD"), String.valueOf(connectionDetailsPasswordText.getPassword())) )
                    {
                        dataSource.setUser(connectionDetailsUsernameText.getText());
                        dataSource.setPassword(String.valueOf(connectionDetailsPasswordText.getPassword()));

                        try {
                            connection = dataSource.getConnection();
                            dbMetaData = connection.getMetaData();
                            dbConnected = true;
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        if( dbConnected ){
                            try {
                                assert dbMetaData != null;
                                System.out.println("Database connected");
                                dbConnectionStatusText.setText("Connected to: "+dbMetaData.getURL()+" as USER: "+dbMetaData.getUserName());
                                dbConnectionStatusText.setBackground(Color.green);
                                dbConnectionStatusText.setForeground(Color.black);
                                dbConnectionStatusText.setFont(dbConnectionStatusText.getFont().deriveFont(dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
                                dbDisconnectButton.setEnabled(true);
                                System.out.println("JDBC Driver Name: " + dbMetaData.getURL()); // Returns URL for DB connected to from properties file
                                System.out.println("JDBC Driver version: " + dbMetaData.getDriverVersion());
                                System.out.println("USER: "+dbMetaData.getUserName());
                                System.out.println("Driver Major version: " +dbMetaData.getDriverMajorVersion());
                                System.out.println("Driver Minor version: " +dbMetaData.getDriverMinorVersion() );
                                System.out.println();

                            } catch (SQLException ex) {
                                ex.printStackTrace();
                                dbConnected = false;
                            }

                        } else {
                            dbConnectionStatusText.setText("Unable to establish a connection to the database");
                            dbConnectionStatusText.setBackground(Color.black);
                            dbConnectionStatusText.setForeground(Color.red);
                            dbConnectionStatusText.setFont(dbConnectionStatusText.getFont().deriveFont(dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
                            System.out.println("Unable to establish a connection to the database");
                        }
                    } else {
                        JOptionPane.showMessageDialog(contentPanel, "Invalid Username/Password for this properties file", "SQL Client - ERROR", JOptionPane.ERROR_MESSAGE);
                    }

                } catch(IOException ioe ){
                    ioe.printStackTrace();
                    dbConnected = false;
                }
                java.util.Date date = new java.util.Date();
                System.out.println(date);
                System.out.println();
            }
        });

        dbDisconnectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if( dbConnected ){
                    try {
                        dataSource.getConnection();
                        dbMetaData = connection.getMetaData();
                        dbConnected = false;
                        dbConnectionStatusText.setText("SUCCESSFULLY DISCONNECTED FROM DATABASE "+ dbMetaData.getURL());
                        connection.close();
                        dbDisconnectButton.setEnabled(false);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

        private void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            sqlClient = new Frame();
            contentPanel = new JPanel();
            hSpacer4 = new JPanel(null);
            hSpacer3 = new JPanel(null);
            vSpacer2 = new JPanel(null);
            connectionDetailsPane = new JPanel();
            connectionDetailsLabel = new JLabel();
            vSpacer4 = new JPanel(null);
            propertiesFileLabel = new JLabel();
            propertiesFileComboBox = new JComboBox();
            usernameLabel = new JLabel();
            connectionDetailsUsernameText = new JTextField();
            passwordLabel = new JLabel();
            connectionDetailsPasswordText = new JPasswordField();
            vSpacer3 = new JPanel(null);
            sqlCommandPane = new JPanel();
            sqlCommandLabel = new JLabel();
            vSpacer5 = new JPanel(null);
            scrollPane1 = new JScrollPane();
            sqlCommandWindow = new JTextArea();
            sqlClearCommandButton = new JButton();
            sqlExecuteCommandButton = new JButton();
            hSpacer6 = new JPanel(null);
            dbConnectionLabel = new JLabel();
            vSpacer6 = new JPanel(null);
            dbConnectionStatusText = new JTextField();
            splitPane1 = new JSplitPane();
            dbConnectionButton = new JButton();
            dbDisconnectButton = new JButton();
            hSpacer7 = new JPanel(null);
            sqlResultsPanel = new JPanel();
            sqlExecutionLabel = new JLabel();
            scrollPane2 = new JScrollPane();
            sqlResults = new JTable();
            sqlExecutionClearButton = new JButton();
            hSpacer2 = new JPanel(null);

            //======== sqlClient ========
            {
                sqlClient.setLayout(new GridBagLayout());
                ((GridBagLayout)sqlClient.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)sqlClient.getLayout()).rowHeights = new int[] {479, 0};
                ((GridBagLayout)sqlClient.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)sqlClient.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                //======== contentPanel ========
                {
                    contentPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 329, 13, 0, 0, 0};
                    ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
                    ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                    contentPanel.add(hSpacer4, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(hSpacer3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(vSpacer2, new GridBagConstraints(0, 1, 1, 7, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //======== connectionDetailsPane ========
                    {
                        connectionDetailsPane.setLayout(new GridBagLayout());
                        ((GridBagLayout)connectionDetailsPane.getLayout()).columnWidths = new int[] {0, 0, 73, 186, 0};
                        ((GridBagLayout)connectionDetailsPane.getLayout()).rowHeights = new int[] {31, 0, 0, 0, 0};
                        ((GridBagLayout)connectionDetailsPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 1.0E-4};
                        ((GridBagLayout)connectionDetailsPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

                        //---- connectionDetailsLabel ----
                        connectionDetailsLabel.setText("Connection Details");
                        connectionDetailsLabel.setForeground(Color.blue);
                        connectionDetailsLabel.setFont(connectionDetailsLabel.getFont().deriveFont(connectionDetailsLabel.getFont().getStyle() | Font.BOLD));
                        connectionDetailsPane.add(connectionDetailsLabel, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(0, 0, 5, 0), 0, 0));
                        connectionDetailsPane.add(vSpacer4, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- propertiesFileLabel ----
                        propertiesFileLabel.setText("Properties File");
                        connectionDetailsPane.add(propertiesFileLabel, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 5, 5), 0, 0));
                        connectionDetailsPane.add(propertiesFileComboBox, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                            GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- usernameLabel ----
                        usernameLabel.setText("Username");
                        connectionDetailsPane.add(usernameLabel, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- connectionDetailsUsernameText ----
                        connectionDetailsUsernameText.setToolTipText("Enter the username to connect to the database with");
                        connectionDetailsPane.add(connectionDetailsUsernameText, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- passwordLabel ----
                        passwordLabel.setText("Password");
                        connectionDetailsPane.add(passwordLabel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- connectionDetailsPasswordText ----
                        connectionDetailsPasswordText.setToolTipText("Enter the password that goes with the username entered");
                        connectionDetailsPane.add(connectionDetailsPasswordText, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    contentPanel.add(connectionDetailsPane, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(vSpacer3, new GridBagConstraints(2, 1, 1, 5, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //======== sqlCommandPane ========
                    {
                        sqlCommandPane.setLayout(new GridBagLayout());
                        ((GridBagLayout)sqlCommandPane.getLayout()).columnWidths = new int[] {250, 270, 0, 0};
                        ((GridBagLayout)sqlCommandPane.getLayout()).rowHeights = new int[] {0, 101, 0, 0};
                        ((GridBagLayout)sqlCommandPane.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
                        ((GridBagLayout)sqlCommandPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                        //---- sqlCommandLabel ----
                        sqlCommandLabel.setText("Enter An SQL Command");
                        sqlCommandLabel.setForeground(Color.blue);
                        sqlCommandLabel.setFont(sqlCommandLabel.getFont().deriveFont(sqlCommandLabel.getFont().getStyle() | Font.BOLD));
                        sqlCommandPane.add(sqlCommandLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(0, 0, 5, 5), 0, 0));
                        sqlCommandPane.add(vSpacer5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //======== scrollPane1 ========
                        {

                            //---- sqlCommandWindow ----
                            sqlCommandWindow.setToolTipText("Enter an SQL Command to execute");
                            scrollPane1.setViewportView(sqlCommandWindow);
                        }
                        sqlCommandPane.add(scrollPane1, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- sqlClearCommandButton ----
                        sqlClearCommandButton.setText("Clear SQL Command");
                        sqlClearCommandButton.setFont(sqlClearCommandButton.getFont().deriveFont(sqlClearCommandButton.getFont().getStyle() | Font.BOLD));
                        sqlClearCommandButton.setForeground(Color.red);
                        sqlClearCommandButton.setToolTipText("Clear the SQL Command textbox");
                        sqlCommandPane.add(sqlClearCommandButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                        //---- sqlExecuteCommandButton ----
                        sqlExecuteCommandButton.setText("Execute SQL Command");
                        sqlExecuteCommandButton.setFont(sqlExecuteCommandButton.getFont().deriveFont(sqlExecuteCommandButton.getFont().getStyle() | Font.BOLD));
                        sqlExecuteCommandButton.setForeground(Color.green);
                        sqlExecuteCommandButton.setToolTipText("Execute SQL Command on the database");
                        sqlCommandPane.add(sqlExecuteCommandButton, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    contentPanel.add(sqlCommandPane, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(hSpacer6, new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- dbConnectionLabel ----
                    dbConnectionLabel.setText("DB Connection Area");
                    dbConnectionLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                    contentPanel.add(dbConnectionLabel, new GridBagConstraints(1, 3, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(vSpacer6, new GridBagConstraints(4, 1, 1, 6, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                    contentPanel.add(dbConnectionStatusText, new GridBagConstraints(3, 3, 1, 2, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //======== splitPane1 ========
                    {

                        //---- dbConnectionButton ----
                        dbConnectionButton.setText("Connect To Database");
                        dbConnectionButton.setBackground(new Color(51, 0, 255));
                        dbConnectionButton.setForeground(Color.yellow);
                        dbConnectionButton.setFont(dbConnectionButton.getFont().deriveFont(dbConnectionButton.getFont().getStyle() | Font.BOLD));
                        dbConnectionButton.setToolTipText("Connects to available database using username and password");
                        splitPane1.setLeftComponent(dbConnectionButton);

                        //---- dbDisconnectButton ----
                        dbDisconnectButton.setText("Disconnect From Database");
                        splitPane1.setRightComponent(dbDisconnectButton);
                    }
                    contentPanel.add(splitPane1, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(hSpacer7, new GridBagConstraints(1, 5, 4, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== sqlResultsPanel ========
                    {
                        sqlResultsPanel.setLayout(new GridBagLayout());
                        ((GridBagLayout)sqlResultsPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 653, 0, 0, 0, 0};
                        ((GridBagLayout)sqlResultsPanel.getLayout()).rowHeights = new int[] {27, 38, 50, 0, 0};
                        ((GridBagLayout)sqlResultsPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)sqlResultsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0, 0.0, 1.0E-4};

                        //---- sqlExecutionLabel ----
                        sqlExecutionLabel.setText("SQL Execution Results Window");
                        sqlResultsPanel.add(sqlExecutionLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //======== scrollPane2 ========
                        {
                            scrollPane2.setViewportView(sqlResults);
                        }
                        sqlResultsPanel.add(scrollPane2, new GridBagConstraints(0, 1, 5, 2, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 5), 0, 0));

                        //---- sqlExecutionClearButton ----
                        sqlExecutionClearButton.setText("Clear Results Window");
                        sqlResultsPanel.add(sqlExecutionClearButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    }
                    contentPanel.add(sqlResultsPanel, new GridBagConstraints(1, 6, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                    contentPanel.add(hSpacer2, new GridBagConstraints(1, 7, 4, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                sqlClient.add(contentPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.ABOVE_BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
                sqlClient.pack();
                sqlClient.setLocationRelativeTo(sqlClient.getOwner());
            }
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        private Frame sqlClient;
        private JPanel contentPanel;
        private JPanel hSpacer4;
        private JPanel hSpacer3;
        private JPanel vSpacer2;
        private JPanel connectionDetailsPane;
        private JLabel connectionDetailsLabel;
        private JPanel vSpacer4;
        private JLabel propertiesFileLabel;
        private JComboBox propertiesFileComboBox;
        private JLabel usernameLabel;
        private JTextField connectionDetailsUsernameText;
        private JLabel passwordLabel;
        private JPasswordField connectionDetailsPasswordText;
        private JPanel vSpacer3;
        private JPanel sqlCommandPane;
        private JLabel sqlCommandLabel;
        private JPanel vSpacer5;
        private JScrollPane scrollPane1;
        private JTextArea sqlCommandWindow;
        private JButton sqlClearCommandButton;
        private JButton sqlExecuteCommandButton;
        private JPanel hSpacer6;
        private JLabel dbConnectionLabel;
        private JPanel vSpacer6;
        private JTextField dbConnectionStatusText;
        private JSplitPane splitPane1;
        private JButton dbConnectionButton;
        private JButton dbDisconnectButton;
        private JPanel hSpacer7;
        private JPanel sqlResultsPanel;
        private JLabel sqlExecutionLabel;
        private JScrollPane scrollPane2;
        private JTable sqlResults;
        private JButton sqlExecutionClearButton;
        private JPanel hSpacer2;
        // JFormDesigner - End of variables declaration  //GEN-END:variables

        private boolean dbConnected = false;
        private Connection connection;
        private DatabaseMetaData dbMetaData;
        private MysqlDataSource dataSource;
        private Statement statement;
        private ResultSet resultSet;
        private ResultSetMetaData metaData;
        private int numberOfRows;

    public static void main (String[] args ) throws SQLException, IOException, ClassNotFoundException {


        JFrame frame = new JFrame();
        frame.setTitle("SQL Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GUIController().contentPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
