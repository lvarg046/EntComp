import java.awt.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Tue Mar 15 17:17:42 EDT 2022
 */

/**
 * @author unknown
 */
public class GUIController extends JPanel {
    public GUIController() {
        initComponents();

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
        sqlCommandWindow = new JFormattedTextField();
        sqlClearCommandButton = new JButton();
        sqlExecuteCommandButton = new JButton();
        hSpacer6 = new JPanel(null);
        dbConnectionLabel = new JLabel();
        vSpacer6 = new JPanel(null);
        dbConnectionStatusText = new JTextField();
        dbConnectionButton = new JButton();
        hSpacer7 = new JPanel(null);
        sqlExecutionLabel = new JLabel();
        sqlExecutionText = new JFormattedTextField();
        sqlExecutionClearButton = new JButton();
        hSpacer2 = new JPanel(null);

        //======== sqlClient ========
        {
            sqlClient.setLayout(new GridBagLayout());
            ((GridBagLayout)sqlClient.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)sqlClient.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 117, 0, 0, 0, 0};
            ((GridBagLayout)sqlClient.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)sqlClient.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                contentPanel.add(hSpacer4, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(hSpacer3, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(vSpacer2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== connectionDetailsPane ========
                {
                    connectionDetailsPane.setLayout(new GridBagLayout());
                    ((GridBagLayout)connectionDetailsPane.getLayout()).columnWidths = new int[] {0, 0, 73, 186, 0};
                    ((GridBagLayout)connectionDetailsPane.getLayout()).rowHeights = new int[] {31, 0, 0, 0, 0};
                    ((GridBagLayout)connectionDetailsPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout)connectionDetailsPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- connectionDetailsLabel ----
                    connectionDetailsLabel.setText("Connection Details");
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
                    connectionDetailsPane.add(connectionDetailsUsernameText, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- passwordLabel ----
                    passwordLabel.setText("Password");
                    connectionDetailsPane.add(passwordLabel, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                    connectionDetailsPane.add(connectionDetailsPasswordText, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                contentPanel.add(connectionDetailsPane, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(vSpacer3, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== sqlCommandPane ========
                {
                    sqlCommandPane.setLayout(new GridBagLayout());
                    ((GridBagLayout)sqlCommandPane.getLayout()).columnWidths = new int[] {234, 188, 0, 0};
                    ((GridBagLayout)sqlCommandPane.getLayout()).rowHeights = new int[] {0, 101, 0, 0};
                    ((GridBagLayout)sqlCommandPane.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout)sqlCommandPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //---- sqlCommandLabel ----
                    sqlCommandLabel.setText("Enter An SQL Command");
                    sqlCommandPane.add(sqlCommandLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));
                    sqlCommandPane.add(vSpacer5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                    sqlCommandPane.add(sqlCommandWindow, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- sqlClearCommandButton ----
                    sqlClearCommandButton.setText("Clear SQL Command");
                    sqlCommandPane.add(sqlClearCommandButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- sqlExecuteCommandButton ----
                    sqlExecuteCommandButton.setText("Execute SQL Command");
                    sqlCommandPane.add(sqlExecuteCommandButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                }
                contentPanel.add(sqlCommandPane, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(hSpacer6, new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- dbConnectionLabel ----
                dbConnectionLabel.setText("DB Connection Area");
                dbConnectionLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                contentPanel.add(dbConnectionLabel, new GridBagConstraints(2, 3, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(vSpacer6, new GridBagConstraints(5, 1, 1, 12, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
                contentPanel.add(dbConnectionStatusText, new GridBagConstraints(4, 3, 1, 3, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- dbConnectionButton ----
                dbConnectionButton.setText("Connect To Database");
                contentPanel.add(dbConnectionButton, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(hSpacer7, new GridBagConstraints(0, 6, 5, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- sqlExecutionLabel ----
                sqlExecutionLabel.setText("SQL Execution Results Window");
                contentPanel.add(sqlExecutionLabel, new GridBagConstraints(2, 7, 1, 2, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(sqlExecutionText, new GridBagConstraints(2, 9, 3, 3, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- sqlExecutionClearButton ----
                sqlExecutionClearButton.setText("Clear Results Window");
                contentPanel.add(sqlExecutionClearButton, new GridBagConstraints(2, 12, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(hSpacer2, new GridBagConstraints(2, 13, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            sqlClient.add(contentPanel, new GridBagConstraints(0, 0, 6, 12, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
            sqlClient.pack();
            sqlClient.setLocationRelativeTo(sqlClient.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
//        this.setContentPane(this.contentPanel);
        //
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
    private JFormattedTextField sqlCommandWindow;
    private JButton sqlClearCommandButton;
    private JButton sqlExecuteCommandButton;
    private JPanel hSpacer6;
    private JLabel dbConnectionLabel;
    private JPanel vSpacer6;
    private JTextField dbConnectionStatusText;
    private JButton dbConnectionButton;
    private JPanel hSpacer7;
    private JLabel sqlExecutionLabel;
    private JFormattedTextField sqlExecutionText;
    private JButton sqlExecutionClearButton;
    private JPanel hSpacer2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main( String[] args ){
        JFrame frame = new JFrame();
        frame.setTitle("SQL Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new GUIController().contentPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
