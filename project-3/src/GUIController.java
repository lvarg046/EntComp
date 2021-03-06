/*
 * Name: Luis Vargas
 * Course: CNT 4714 - Spring 2022
 * Assignment Title: Project 3 - Two-Tier Client-Server Application Development With MySQL and JDBC
 * Due Date: Sunday, March 20, 2022
 */
package src;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class GUIController extends JFrame {
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


    private boolean dbConnected = false;
    private Connection connection;
    private DatabaseMetaData dbMetaData;
    private MysqlDataSource dataSource;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData RSmetaData;
    private int colCount;
    private String[] colName;
    private PreparedStatement pstmt1;
    private PreparedStatement pstmt2;
    private int num_queries;
    private int num_updates;
    private MysqlDataSource ops_log_ds;
    private Connection ops_log_conn;

    public GUIController() {
        initComponents();
        initializeDB();
        GUIController.this.sqlClearCommandButton.setEnabled(true);
        GUIController.this.sqlExecuteCommandButton.setEnabled(true);
        GUIController.this.sqlExecutionClearButton.setEnabled(true);
        GUIController.this.dbConnectionStatusText.setText("WAITING FOR INPUT TO ESTABLISH CONNECTION");
        GUIController.this.dbConnectionStatusText.setFont(GUIController.this.dbConnectionStatusText.getFont().deriveFont(GUIController.this.dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
        GUIController.this.dbDisconnectButton.setEnabled(false);

        String[] properties = {"root.properties", "client.properties", "project3.properties"};
        for (String i : properties) {
            propertiesFileComboBox.addItem(i);
        }


        sqlClearCommandButton.addActionListener(e -> sqlCommandWindow.setText(""));

        sqlExecutionClearButton.addActionListener(e -> {
            if (!dbConnected)
                throw new IllegalStateException("Not Connected to Database");

            sqlResults.setModel(new DefaultTableModel());
        });

        sqlExecuteCommandButton.addActionListener(e -> {
            if (!dbConnected) {
                JOptionPane.showMessageDialog(null, "Unable to execute SQL Statement. No database connection detected.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                throw new IllegalStateException("Not Connected to the Database");
            }
            try {
                sqlResults.setModel(new DefaultTableModel());
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String query = sqlCommandWindow.getText();
                String q2 = query.toLowerCase();
                if (q2.contains("insert") || q2.contains("delete") || q2.contains("update")) {
                    statement.executeUpdate(query);
                    int num_updates_out = num_updates + 1;
                    String update = "update operationscount set num_updates = " + num_updates_out + " where num_updates = " + num_updates;
                    pstmt2 = ops_log_conn.prepareStatement(update);
                    pstmt2.execute();
                    num_updates = num_updates_out;
                } else {
                    int num_queries_out = num_queries + 1;
                    resultSet = statement.executeQuery(query);
                    String queries = "update operationscount set num_queries = " + num_queries_out + " where num_queries = " + num_queries;
                    pstmt1 = ops_log_conn.prepareStatement(queries);
                    pstmt1.execute();
                    num_queries = num_queries_out;
                    RSmetaData = resultSet.getMetaData();

                    DefaultTableModel model = (DefaultTableModel) sqlResults.getModel();
                    colCount = RSmetaData.getColumnCount();
                    colName = new String[colCount];
                    for (int i = 0; i < colCount; i++) {
                        colName[i] = RSmetaData.getColumnName(i + 1);
                    }
                    sqlResults.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                    model.setColumnIdentifiers(colName);
                    String[] row = new String[colCount + 1];
                    while (resultSet.next()) {
                        for (int i = 1; i <= colCount; i++) {
                            row[i - 1] = resultSet.getString(i);
                        }
                        model.addRow(row);
                    }
                }

                statement.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

        });

        dbConnectionButton.addActionListener(e -> {
            Properties properties1 = new Properties();
            FileInputStream filein;
            if (dbConnected)
                JOptionPane.showMessageDialog(contentPanel, "Already connected to the database", "SQL Client - DATABASE CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
            try {
                filein = new FileInputStream("src/" + propertiesFileComboBox.getSelectedItem());
                properties1.load(filein);
                dataSource = new MysqlDataSource();
                dataSource.setURL(properties1.getProperty("MYSQL_DB_URL"));
                if (Objects.equals(properties1.getProperty("MYSQL_DB_USERNAME"), String.valueOf(connectionDetailsUsernameText.getText()))
                        && Objects.equals(properties1.getProperty("MYSQL_DB_PASSWORD"), String.valueOf(connectionDetailsPasswordText.getPassword()))) {
                    dataSource.setUser(connectionDetailsUsernameText.getText());
                    dataSource.setPassword(String.valueOf(connectionDetailsPasswordText.getPassword()));

                    try {
                        connection = dataSource.getConnection();
                        dbMetaData = connection.getMetaData();
                        dbConnected = true;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        dbConnected = false;
                    }
                    if (dbConnected) {
                        try {
                            assert dbMetaData != null;
                            System.out.println("Database connected");
                            dbConnectionStatusText.setText(dbMetaData.getUserName() + " Connected to the " + connection.getCatalog() + " database");
                            dbConnectionStatusText.setBackground(Color.green);
                            dbConnectionStatusText.setForeground(Color.black);
                            dbConnectionStatusText.setFont(dbConnectionStatusText.getFont().deriveFont(dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
                            dbDisconnectButton.setEnabled(true);

                            /* This below is for console logging/troubleshooting */
                            System.out.println("JDBC Driver Name: " + dbMetaData.getURL()); // Returns URL for DB connected to from properties file
                            System.out.println("JDBC Driver version: " + dbMetaData.getDriverVersion());
                            System.out.println("USER: " + dbMetaData.getUserName());
                            System.out.println("Driver Major version: " + dbMetaData.getDriverMajorVersion());
                            System.out.println("Driver Minor version: " + dbMetaData.getDriverMinorVersion());
                            System.out.println();

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            dbConnected = false;
                        }

                    } else {
                        dbConnectionStatusText.setText("Unable to establish a connection to the database " + connection.getCatalog());
                        dbConnectionStatusText.setBackground(Color.black);
                        dbConnectionStatusText.setForeground(Color.red);
                        dbConnectionStatusText.setFont(dbConnectionStatusText.getFont().deriveFont(dbConnectionStatusText.getFont().getStyle() | Font.BOLD));
                        System.out.println("Unable to establish a connection to the database");
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPanel, "Invalid Username/Password for this properties file", "SQL Client - ERROR", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException | SQLException ioe) {
                ioe.printStackTrace();
                dbConnected = false;
            }
            java.util.Date date = new java.util.Date();
            System.out.println(date);
            System.out.println();
        });

        dbDisconnectButton.addActionListener(e -> {
            try {
                dataSource.getConnection();
                dbMetaData = connection.getMetaData();
                dbConnected = false;
                dbConnectionStatusText.setText(dbMetaData.getUserName() + ": Successfully disconnected from the " + connection.getCatalog() + " database");
                connection.close();
                dbDisconnectButton.setEnabled(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {


        JFrame frame = new JFrame();
        frame.setTitle("SQL Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GUIController().contentPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void initializeDB() {
        try {
            ops_log_ds = new MysqlDataSource();
            ops_log_ds.setURL("jdbc:mysql://localhost:3306/operationslog?useTimezone=true&serverTimezone=UTC");
            ops_log_ds.setUser("root");
            ops_log_ds.setPassword("root");
            ops_log_conn = ops_log_ds.getConnection();
            String ops_log_query = "select num_queries from operationscount";
            String ops_log_update = "select num_updates from operationscount";

            pstmt1 = ops_log_conn.prepareStatement(ops_log_query);
            pstmt2 = ops_log_conn.prepareStatement(ops_log_update);
            ResultSet ops_query = pstmt1.executeQuery();
            ResultSet ops_update = pstmt2.executeQuery();
            if (ops_query.next()) {
                String q = ops_query.getString(1);
                num_queries = Integer.parseInt(q);
            }
            if (ops_update.next()) {
                String q = ops_update.getString(1);
                num_updates = Integer.parseInt(q);
            }
            System.out.println("Queries: " + num_queries);
            System.out.println("Updates: " + num_updates);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
            ((GridBagLayout) sqlClient.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) sqlClient.getLayout()).rowHeights = new int[]{479, 0};
            ((GridBagLayout) sqlClient.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) sqlClient.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout) contentPanel.getLayout()).columnWidths = new int[]{0, 329, 13, 0, 0, 0};
                ((GridBagLayout) contentPanel.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout) contentPanel.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};
                ((GridBagLayout) contentPanel.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
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
                    ((GridBagLayout) connectionDetailsPane.getLayout()).columnWidths = new int[]{0, 0, 73, 186, 0};
                    ((GridBagLayout) connectionDetailsPane.getLayout()).rowHeights = new int[]{31, 0, 0, 0, 0};
                    ((GridBagLayout) connectionDetailsPane.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0E-4};
                    ((GridBagLayout) connectionDetailsPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

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
                    ((GridBagLayout) sqlCommandPane.getLayout()).columnWidths = new int[]{250, 270, 0, 0};
                    ((GridBagLayout) sqlCommandPane.getLayout()).rowHeights = new int[]{0, 101, 0, 0};
                    ((GridBagLayout) sqlCommandPane.getLayout()).columnWeights = new double[]{0.0, 1.0, 0.0, 1.0E-4};
                    ((GridBagLayout) sqlCommandPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};

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
                    ((GridBagLayout) sqlResultsPanel.getLayout()).columnWidths = new int[]{0, 0, 0, 653, 0, 0, 0, 0};
                    ((GridBagLayout) sqlResultsPanel.getLayout()).rowHeights = new int[]{27, 38, 50, 0, 0};
                    ((GridBagLayout) sqlResultsPanel.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout) sqlResultsPanel.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0, 0.0, 1.0E-4};

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
}
