package org.rushi.app;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class App  implements ActionListener
{
//    private static JButton bt1;
    private static JButton bt1;
    private static JButton bt2;
    private static JTextField tf1, tf2,tf3, tf4, tf5;
    private static JLabel l1,l2,l3,l4;

    protected File selectedFile;
    private String filePath;

    public void createGUI(){
        JFrame frame = new JFrame("FrameDemo");
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bt1 = new JButton("Load");
        bt1.addActionListener(this);
//        bt1.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                // display/center the jdialog when the button is pressed
//
//            }
//        });

        tf1 = new JTextField("Path");
        bt2 = new JButton("Submit");
        bt2.addActionListener(this);

        l1 = new JLabel("ID");
        tf2 = new JTextField("Id");
        l2 = new JLabel("Name");
        tf3 = new JTextField("Name");
        l3 = new JLabel("LastName");
        tf4 = new JTextField("LastName");
        l4 = new JLabel("Salary");
        tf5 = new JTextField("Salary");

        bt1.setPreferredSize(new Dimension(80, 20));
        bt2.setPreferredSize(new Dimension(80, 20));
        tf1.setPreferredSize(new Dimension(200, 20));

        tf2.setPreferredSize(new Dimension(200, 20));
        tf3.setPreferredSize(new Dimension(200, 20));
        tf4.setPreferredSize(new Dimension(200, 20));
        tf5.setPreferredSize(new Dimension(200, 20));


        tf1.setEnabled(false);

        panel.add(bt1);
        panel.add(tf1);
        panel.add(bt2);

        panel1.add(l1);
        panel1.add(tf2);

        panel2.add(l2);
        panel2.add(tf3);

        panel3.add(l3);
        panel3.add(tf4);

        panel4.add(l4);
        panel4.add(tf5);

        frame.getContentPane().setLayout(new GridLayout(5,1));

        frame.add(panel);
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel4);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }


    public String selectFile(){
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();

        }
        return selectedFile.getAbsolutePath();
    }

    protected void clicked(String path) {

        Connection connection = null;

        String filePath = path;
        File file = new File(filePath);

        try {

            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5433/postgres", "postgres",
                    "tiger");
            connection.setSchema("employee");
            System.out.println("Connection Successfull");


            BufferedReader br = new BufferedReader(new FileReader(file));
            // get lines from txt file
            Object[] tableLines = br.lines().toArray();

            // extratct data from lines
            // set data to jtable model
            for (int i = 0; i < tableLines.length; i++) {
                String line = tableLines[i].toString().trim();
                String[] dataRow = line.split(",");
//                System.out.println(Arrays.asList(dataRow));
//                System.out.println();
                System.out.println(dataRow[0]+ " "+ dataRow[1]+ " "+ dataRow[2]+ " "+ dataRow[3]);
                String query = "insert into employee.employee (eid, fname, lname, salary) values (?, ?, ?, ?)";
                PreparedStatement st = connection.prepareStatement(query);
                st.setInt(1, Integer.parseInt(dataRow[0]));
                st.setString(2, dataRow[1]);
                st.setString(3, dataRow[2]);
                st.setInt(4, Integer.parseInt(dataRow[3]));
                int res = st.executeUpdate();
                st.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    protected void dbConnection(){
//        try {
//
//            Class.forName("org.postgresql.Driver");
//
//        } catch (ClassNotFoundException e) {
//
//            System.out.println("Where is your PostgreSQL JDBC Driver? "
//                    + "Include in your library path!");
//            e.printStackTrace();
//            return;
//
//        }
//
//        System.out.println("PostgreSQL JDBC Driver Registered!");
//
//        Connection connection = null;
//
//        try {
//
//            connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost/postgres", "postgres",
//                    "tiger");
//
//        } catch (SQLException e) {
//
//            System.out.println("Connection Failed! Check output console");
//            e.printStackTrace();
//            return;
//
//        }
//
//        if (connection != null) {
//            System.out.println("You made it, take control your database now!");
//        } else {
//            System.out.println("Failed to make connection!");
//        }
//    }


    public static void main( String[] args )
    {
        final App a = new App();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.createGUI();
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==bt1) {
            this.filePath = selectFile();
            tf1.setText(this.filePath);
        }
        if(e.getSource()==bt2){
            clicked(this.filePath);
        }
    }
}
