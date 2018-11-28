package businesscontacts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import JavaBase2.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javax.swing.table.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class BusinessContacts extends JApplet 
{
    private static final int JFXPANEL_WIDTH_INT = 1000;
    private static final int JFXPANEL_HEIGHT_INT = 600;
    private static JFrame frame;
    private static JFrame addFrame;
    private static JFrame editFrame;
    private static JFrame viewFrame;
    private static JFXPanel mainContainer;
    private static JFXPanel addContainer;
    private static JFXPanel editContainer;
    private static JFXPanel viewContainer;
    private static JavaBase_V2 db = new JavaBase_V2();
    private static String firstID = "";
    private static String lastID = "";
    private static DefaultTableModel model;
    
    public static void main(String[] args) throws JBInputException_V2 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {         
            @Override
            public void run() 
            {
                try 
                {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } 
                catch (Exception e) { }
                
                frame = new JFrame("Business Contacts");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                addFrame = new JFrame("Business Contacts");
                addFrame.setPreferredSize(new Dimension(500,600));
                addFrame.setMinimumSize(new Dimension(500,600));
                addFrame.setMaximumSize(new Dimension(500,600));
                addFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                
                editFrame = new JFrame("Business Contacts");
                addFrame.setPreferredSize(new Dimension(500,600));
                editFrame.setMinimumSize(new Dimension(500,600));
                editFrame.setMaximumSize(new Dimension(500,600));
                editFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                
                viewFrame = new JFrame("Business Contacts");
                viewFrame.setPreferredSize(new Dimension(500,600));
                viewFrame.setMinimumSize(new Dimension(500,600));
                viewFrame.setMaximumSize(new Dimension(500,600));
                viewFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                
                JApplet applet = new BusinessContacts();
                applet.init();
                
                frame.setContentPane(applet.getContentPane());
                addFrame.setContentPane(addContainer);
                editFrame.setContentPane(editContainer);
                viewFrame.setContentPane(viewContainer);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                addFrame.setLocationRelativeTo(frame);
                addFrame.setVisible(false);
                editFrame.setLocationRelativeTo(frame);
                editFrame.setVisible(false);
                viewFrame.setLocationRelativeTo(frame);
                viewFrame.setVisible(false);
                
                applet.start();
            }
        });
        
        //Creates the table in the database and gives it the appropriate fields
        db.createTable("Contacts", "First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes");
        db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
                    "Zach,Simmons,Lander,zach@lander.edu,1231234567,123,This is just a test",0);
        db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
                "Will,Avery,Lander,will@lander.edu,9879876543,456,This is just a test",0);
        db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
                "Bryant,Roach,Lander,bryant@lander.edu,1800000000,789,This is just a test",0);
    }
    
    // <editor-fold desc="Defualt Swing Setup">
    @Override
    public void init() {
        mainContainer = new JFXPanel();
        mainContainer.setPreferredSize(new Dimension(JFXPANEL_WIDTH_INT, JFXPANEL_HEIGHT_INT));
        addContainer = new JFXPanel();
        addContainer.setPreferredSize(new Dimension(500,600));
        addContainer.setMinimumSize(new Dimension(500,600));
        addContainer.setMaximumSize(new Dimension(500,600));
        editContainer = new JFXPanel();
        editContainer.setPreferredSize(new Dimension(500,600));
        editContainer.setMinimumSize(new Dimension(500,600));
        editContainer.setMaximumSize(new Dimension(500,600));
        viewContainer = new JFXPanel();
        viewContainer.setPreferredSize(new Dimension(500,600));
        viewContainer.setMinimumSize(new Dimension(500,600));
        viewContainer.setMaximumSize(new Dimension(500,600));
        add(mainContainer, BorderLayout.CENTER);
        //add(subContainer, BorderLayout.CENTER);
        // create JavaFX scene
        Platform.runLater(new Runnable() 
        {          
            @Override
            public void run() 
            {
                try {
                    createGUI();
                } catch (JBInputException_V2 ex) {
                    Logger.getLogger(BusinessContacts.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // </editor-fold>
    
    //Creation of GUI Elements
    private void createGUI() throws JBInputException_V2 
    {
        // <editor-fold desc="Add GUI">
        VBox subBody = new VBox();
        subBody.setSpacing(25);
        subBody.setAlignment(Pos.CENTER);
        subBody.setPrefWidth(300);
        subBody.setMinWidth(300);
        subBody.setMaxWidth(300);
        Text subTitle = new Text("Add A New Contact");
        subTitle.setFont(Font.font ("Arial", 24));
        subTitle.setFill(Color.BLACK);
        
        HBox firstBox = new HBox();
        firstBox.setSpacing(25);
        firstBox.setAlignment(Pos.CENTER_LEFT);
        Text firstText = new Text("      First Name      ");
        firstText.setFont(Font.font ("Arial", 12));
        firstText.setFill(Color.BLACK);
        TextField firstField = new TextField();
        firstField.setPrefWidth(150);
        firstField.setMaxWidth(150);
        firstField.setMinWidth(150);
        firstBox.getChildren().addAll(firstText, firstField);
        
        HBox lastBox = new HBox();
        lastBox.setSpacing(25);
        lastBox.setAlignment(Pos.CENTER_LEFT);
        Text lastText = new Text("      Last Name      ");
        lastText.setFont(Font.font ("Arial", 12));
        lastText.setFill(Color.BLACK);
        TextField lastField = new TextField();
        lastField.setPrefWidth(150);
        lastField.setMaxWidth(150);
        lastField.setMinWidth(150);
        lastBox.getChildren().addAll(lastText, lastField);
        
        HBox companyBox = new HBox();
        companyBox.setSpacing(25);
        companyBox.setAlignment(Pos.CENTER_LEFT);
        Text companyText = new Text("      Company        ");
        companyText.setFont(Font.font ("Arial", 12));
        companyText.setFill(Color.BLACK);
        TextField companyField = new TextField();
        companyField.setPrefWidth(150);
        companyField.setMaxWidth(150);
        companyField.setMinWidth(150);
        companyBox.getChildren().addAll(companyText, companyField);

        HBox emailBox = new HBox();
        emailBox.setSpacing(25);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        Text emailText = new Text("      Email               ");
        emailText.setFont(Font.font ("Arial", 12));
        emailText.setFill(Color.BLACK);
        TextField emailField = new TextField();
        emailField.setPrefWidth(150);
        emailField.setMaxWidth(150);
        emailField.setMinWidth(150);
        emailBox.getChildren().addAll(emailText, emailField);
        
        HBox phoneBox = new HBox();
        phoneBox.setSpacing(25);
        phoneBox.setAlignment(Pos.CENTER_LEFT);
        Text phoneText = new Text("      Phone Number");
        phoneText.setFont(Font.font ("Arial", 12));
        phoneText.setFill(Color.BLACK);
        TextField phoneField = new TextField();
        phoneField.setPrefWidth(150);
        phoneField.setMaxWidth(150);
        phoneField.setMinWidth(150);
        phoneBox.getChildren().addAll(phoneText, phoneField);
        
        HBox officeBox = new HBox();
        officeBox.setSpacing(25);
        officeBox.setAlignment(Pos.CENTER_LEFT);
        Text officeText = new Text("      Office Number ");
        officeText.setFont(Font.font ("Arial", 12));
        officeText.setFill(Color.BLACK);
        TextField officeField = new TextField();
        officeField.setPrefWidth(150);
        officeField.setMaxWidth(150);
        officeField.setMinWidth(150);
        officeBox.getChildren().addAll(officeText, officeField);
        
        HBox notesBox = new HBox();
        notesBox.setSpacing(25);
        notesBox.setAlignment(Pos.CENTER_LEFT);
        Text notesText = new Text("      Notes               ");
        notesText.setFont(Font.font ("Arial", 12));
        notesText.setFill(Color.BLACK);
        TextArea notesArea = new TextArea();
        notesArea.setPrefWidth(150);
        notesArea.setMaxWidth(150);
        notesArea.setMinWidth(150);
        notesArea.setPrefHeight(70);
        notesArea.setMaxHeight(70);
        notesArea.setMinHeight(70);
        notesBox.getChildren().addAll(notesText, notesArea);
        
        HBox buttonsBox = new HBox();
        buttonsBox.setSpacing(30);
        buttonsBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button();
        submitButton.setText("Add Contact");
        submitButton.setFont(Font.font ("Arial", 12));
        submitButton.setMinWidth(100);
        submitButton.setPrefWidth(100);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    addEntry(firstField.getText(),lastField.getText(),companyField.getText(),emailField.getText(),phoneField.getText(),officeField.getText(),notesArea.getText());
                    Object[] entry = {firstField.getText(),lastField.getText(),companyField.getText(),emailField.getText(),phoneField.getText(),officeField.getText(),notesArea.getText()};
                    model.addRow(entry);
                    addFrame.setVisible(false);
                    editFrame.setVisible(false);
                    viewFrame.setVisible(false);
                    firstField.clear();
                    lastField.clear();
                    companyField.clear();
                    emailField.clear();
                    phoneField.clear();
                    officeField.clear();
                    notesArea.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button cancelButton = new Button();
        cancelButton.setText("Cancel");
        cancelButton.setFont(Font.font ("Arial", 12));
        cancelButton.setMinWidth(100);
        cancelButton.setPrefWidth(100);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addFrame.setVisible(false);
                editFrame.setVisible(false);
                viewFrame.setVisible(false);
                firstField.clear();
                lastField.clear();
                companyField.clear();
                emailField.clear();
                phoneField.clear();
                officeField.clear();
                notesArea.clear();
            }
        });
        buttonsBox.getChildren().addAll(submitButton, cancelButton);
        
        subBody.getChildren().addAll(subTitle, firstBox, lastBox, companyBox, emailBox, phoneBox, officeBox, notesBox, buttonsBox);
                
        StackPane addRoot = new StackPane();
        addRoot.setPrefWidth(500);
        addRoot.setMinWidth(500);
        addRoot.setMaxWidth(500);
        addRoot.setPrefHeight(600);
        addRoot.setMinHeight(600);
        addRoot.setMaxHeight(600);
        addRoot.getChildren().add(subBody);
    
        addContainer.setScene(new Scene(addRoot));
        //</editor-fold>      
        //<editor-fold desc="Edit GUI">
        VBox subBody2 = new VBox();
        subBody2.setSpacing(25);
        subBody2.setAlignment(Pos.CENTER);
        subBody2.setPrefWidth(300);
        subBody2.setMinWidth(300);
        subBody2.setMaxWidth(300);
        Text subTitle2 = new Text("Edit An Exsisting Contact");
        subTitle2.setFont(Font.font ("Arial", 24));
        subTitle2.setFill(Color.BLACK);
        
        HBox firstBox2 = new HBox();
        firstBox2.setSpacing(25);
        firstBox2.setAlignment(Pos.CENTER_LEFT);
        Text firstText2 = new Text("      First Name      ");
        firstText2.setFont(Font.font ("Arial", 12));
        firstText2.setFill(Color.BLACK);
        TextField firstField2 = new TextField();
        firstField2.setPrefWidth(150);
        firstField2.setMaxWidth(150);
        firstField2.setMinWidth(150);
        firstBox2.getChildren().addAll(firstText2, firstField2);
        
        HBox lastBox2 = new HBox();
        lastBox2.setSpacing(25);
        lastBox2.setAlignment(Pos.CENTER_LEFT);
        Text lastText2 = new Text("      Last Name      ");
        lastText2.setFont(Font.font ("Arial", 12));
        lastText2.setFill(Color.BLACK);
        TextField lastField2 = new TextField();
        lastField2.setPrefWidth(150);
        lastField2.setMaxWidth(150);
        lastField2.setMinWidth(150);
        lastBox2.getChildren().addAll(lastText2, lastField2);
        
        HBox companyBox2 = new HBox();
        companyBox2.setSpacing(25);
        companyBox2.setAlignment(Pos.CENTER_LEFT);
        Text companyText2 = new Text("      Company        ");
        companyText2.setFont(Font.font ("Arial", 12));
        companyText2.setFill(Color.BLACK);
        TextField companyField2 = new TextField();
        companyField2.setPrefWidth(150);
        companyField2.setMaxWidth(150);
        companyField2.setMinWidth(150);
        companyBox2.getChildren().addAll(companyText2, companyField2);

        HBox emailBox2 = new HBox();
        emailBox2.setSpacing(25);
        emailBox2.setAlignment(Pos.CENTER_LEFT);
        Text emailText2 = new Text("      Email               ");
        emailText2.setFont(Font.font ("Arial", 12));
        emailText2.setFill(Color.BLACK);
        TextField emailField2 = new TextField();
        emailField2.setPrefWidth(150);
        emailField2.setMaxWidth(150);
        emailField2.setMinWidth(150);
        emailBox2.getChildren().addAll(emailText2, emailField2);
        
        HBox phoneBox2 = new HBox();
        phoneBox2.setSpacing(25);
        phoneBox2.setAlignment(Pos.CENTER_LEFT);
        Text phoneText2 = new Text("      Phone Number");
        phoneText2.setFont(Font.font ("Arial", 12));
        phoneText2.setFill(Color.BLACK);
        TextField phoneField2 = new TextField();
        phoneField2.setPrefWidth(150);
        phoneField2.setMaxWidth(150);
        phoneField2.setMinWidth(150);
        phoneBox2.getChildren().addAll(phoneText2, phoneField2);
        
        HBox officeBox2 = new HBox();
        officeBox2.setSpacing(25);
        officeBox2.setAlignment(Pos.CENTER_LEFT);
        Text officeText2 = new Text("      Office Number ");
        officeText2.setFont(Font.font ("Arial", 12));
        officeText2.setFill(Color.BLACK);
        TextField officeField2 = new TextField();
        officeField2.setPrefWidth(150);
        officeField2.setMaxWidth(150);
        officeField2.setMinWidth(150);
        officeBox2.getChildren().addAll(officeText2, officeField2);
        
        HBox notesBox2 = new HBox();
        notesBox2.setSpacing(25);
        notesBox2.setAlignment(Pos.CENTER_LEFT);
        Text notesText2 = new Text("      Notes               ");
        notesText2.setFont(Font.font ("Arial", 12));
        notesText2.setFill(Color.BLACK);
        TextArea notesArea2 = new TextArea();
        notesArea2.setPrefWidth(150);
        notesArea2.setMaxWidth(150);
        notesArea2.setMinWidth(150);
        notesArea2.setPrefHeight(70);
        notesArea2.setMaxHeight(70);
        notesArea2.setMinHeight(70);
        notesBox2.getChildren().addAll(notesText2, notesArea2);
        
        HBox buttonsBox2 = new HBox();
        buttonsBox2.setSpacing(30);
        buttonsBox2.setAlignment(Pos.CENTER);
        Button submitButton2 = new Button();
        submitButton2.setText("Confirm");
        submitButton2.setFont(Font.font ("Arial", 12));
        submitButton2.setMinWidth(100);
        submitButton2.setPrefWidth(100);
        submitButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editEntry(firstID, lastID,firstField2.getText(),lastField2.getText(),companyField2.getText(),emailField2.getText(),phoneField2.getText(),officeField2.getText(),notesArea2.getText());
                addFrame.setVisible(false);
                editFrame.setVisible(false);
                viewFrame.setVisible(false);
                firstField2.clear();
                lastField2.clear();
                companyField2.clear();
                emailField2.clear();
                phoneField2.clear();
                officeField2.clear();
                notesArea2.clear();
            }
        });
        Button cancelButton2 = new Button();
        cancelButton2.setText("Cancel");
        cancelButton2.setFont(Font.font ("Arial", 12));
        cancelButton2.setMinWidth(100);
        cancelButton2.setPrefWidth(100);
        cancelButton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addFrame.setVisible(false);
                editFrame.setVisible(false);
                viewFrame.setVisible(false);
                firstField2.clear();
                lastField2.clear();
                companyField2.clear();
                emailField2.clear();
                phoneField2.clear();
                officeField2.clear();
                notesArea2.clear();
            }
        });
        buttonsBox2.getChildren().addAll(submitButton2, cancelButton2);
        
        subBody2.getChildren().addAll(subTitle2, firstBox2, lastBox2, companyBox2, emailBox2, phoneBox2, officeBox2, notesBox2, buttonsBox2);
                
        StackPane editRoot = new StackPane();
        editRoot.setPrefWidth(500);
        editRoot.setMinWidth(500);
        editRoot.setMaxWidth(500);
        editRoot.setPrefHeight(600);
        editRoot.setMinHeight(600);
        editRoot.setMaxHeight(600);
        editRoot.getChildren().add(subBody2);
        
        editContainer.setScene(new Scene(editRoot));
        //</editor-fold>       
        //<editor-fold desc="View GUI">
        VBox subBody3 = new VBox();
        subBody3.setSpacing(25);
        subBody3.setAlignment(Pos.CENTER);
        subBody3.setPrefWidth(300);
        subBody3.setMinWidth(300);
        subBody3.setMaxWidth(300);
        Text subTitle3 = new Text("View Contact");
        subTitle3.setFont(Font.font ("Arial", 24));
        subTitle3.setFill(Color.BLACK);
        
        HBox firstBox3 = new HBox();
        firstBox3.setSpacing(25);
        firstBox3.setAlignment(Pos.CENTER_LEFT);
        Text firstText3 = new Text("      First Name      ");
        firstText3.setFont(Font.font ("Arial", 12));
        firstText3.setFill(Color.BLACK);
        TextField firstField3 = new TextField();
        firstField3.setPrefWidth(150);
        firstField3.setMaxWidth(150);
        firstField3.setMinWidth(150);
        firstBox3.getChildren().addAll(firstText3, firstField3);
        
        HBox lastBox3 = new HBox();
        lastBox3.setSpacing(25);
        lastBox3.setAlignment(Pos.CENTER_LEFT);
        Text lastText3 = new Text("      Last Name      ");
        lastText3.setFont(Font.font ("Arial", 12));
        lastText3.setFill(Color.BLACK);
        TextField lastField3 = new TextField();
        lastField3.setPrefWidth(150);
        lastField3.setMaxWidth(150);
        lastField3.setMinWidth(150);
        lastBox3.getChildren().addAll(lastText3, lastField3);
        
        HBox companyBox3 = new HBox();
        companyBox3.setSpacing(25);
        companyBox3.setAlignment(Pos.CENTER_LEFT);
        Text companyText3 = new Text("      Company        ");
        companyText3.setFont(Font.font ("Arial", 12));
        companyText3.setFill(Color.BLACK);
        TextField companyField3 = new TextField();
        companyField3.setPrefWidth(150);
        companyField3.setMaxWidth(150);
        companyField3.setMinWidth(150);
        companyBox3.getChildren().addAll(companyText3, companyField3);

        HBox emailBox3 = new HBox();
        emailBox3.setSpacing(25);
        emailBox3.setAlignment(Pos.CENTER_LEFT);
        Text emailText3 = new Text("      Email               ");
        emailText3.setFont(Font.font ("Arial", 12));
        emailText3.setFill(Color.BLACK);
        TextField emailField3 = new TextField();
        emailField3.setPrefWidth(150);
        emailField3.setMaxWidth(150);
        emailField3.setMinWidth(150);
        emailBox3.getChildren().addAll(emailText3, emailField3);
        
        HBox phoneBox3 = new HBox();
        phoneBox3.setSpacing(25);
        phoneBox3.setAlignment(Pos.CENTER_LEFT);
        Text phoneText3 = new Text("      Phone Number");
        phoneText3.setFont(Font.font ("Arial", 12));
        phoneText3.setFill(Color.BLACK);
        TextField phoneField3 = new TextField();
        phoneField3.setPrefWidth(150);
        phoneField3.setMaxWidth(150);
        phoneField3.setMinWidth(150);
        phoneBox3.getChildren().addAll(phoneText3, phoneField3);
        
        HBox officeBox3 = new HBox();
        officeBox3.setSpacing(25);
        officeBox3.setAlignment(Pos.CENTER_LEFT);
        Text officeText3 = new Text("      Office Number ");
        officeText3.setFont(Font.font ("Arial", 12));
        officeText3.setFill(Color.BLACK);
        TextField officeField3 = new TextField();
        officeField3.setPrefWidth(150);
        officeField3.setMaxWidth(150);
        officeField3.setMinWidth(150);
        officeBox3.getChildren().addAll(officeText3, officeField3);
        
        HBox notesBox3 = new HBox();
        notesBox3.setSpacing(25);
        notesBox3.setAlignment(Pos.CENTER_LEFT);
        Text notesText3 = new Text("      Notes               ");
        notesText3.setFont(Font.font ("Arial", 12));
        notesText3.setFill(Color.BLACK);
        TextArea notesArea3 = new TextArea();
        notesArea3.setPrefWidth(150);
        notesArea3.setMaxWidth(150);
        notesArea3.setMinWidth(150);
        notesArea3.setPrefHeight(70);
        notesArea3.setMaxHeight(70);
        notesArea3.setMinHeight(70);
        notesBox3.getChildren().addAll(notesText3, notesArea3);
        
        Button cancelButton3 = new Button();
        cancelButton3.setText("Close");
        cancelButton3.setFont(Font.font ("Arial", 12));
        cancelButton3.setAlignment(Pos.CENTER);
        cancelButton3.setMinWidth(100);
        cancelButton3.setPrefWidth(100);
        cancelButton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addFrame.setVisible(false);
                editFrame.setVisible(false);
                viewFrame.setVisible(false);
                firstField3.clear();
                lastField3.clear();
                companyField3.clear();
                emailField3.clear();
                phoneField3.clear();
                officeField3.clear();
                notesArea3.clear();
            }
        });
        
        firstField3.setEditable(false);
        lastField3.setEditable(false);
        companyField3.setEditable(false);
        emailField3.setEditable(false);
        phoneField3.setEditable(false);
        officeField3.setEditable(false);
        notesArea3.setEditable(false);
        
        subBody3.getChildren().addAll(subTitle3, firstBox3, lastBox3, companyBox3, emailBox3, phoneBox3, officeBox3, notesBox3, cancelButton3);
                
        StackPane viewRoot = new StackPane();
        viewRoot.setPrefWidth(500);
        viewRoot.setMinWidth(500);
        viewRoot.setMaxWidth(500);
        viewRoot.setPrefHeight(600);
        viewRoot.setMinHeight(600);
        viewRoot.setMaxHeight(600);
        viewRoot.getChildren().add(subBody3);
        
        viewContainer.setScene(new Scene(viewRoot));
        //</editor-fold>
        
        //<editor-fold desc="Main Header">
        StackPane mainHeaderPane = new StackPane();
        VBox mainHeader = new VBox();
        mainHeader.setSpacing(15);
        mainHeader.setAlignment(Pos.CENTER);
        mainHeader.setPadding(new Insets(25, 50, 5, 50));
        Text mainTitle = new Text("Business Contacts");
        mainTitle.setFont(Font.font ("Arial", 24));
        mainTitle.setFill(Color.BLACK);
        //<editor-fold desc="Search Header">
        HBox searchHeader = new HBox();
        searchHeader.setSpacing(15);
        searchHeader.setAlignment(Pos.CENTER);
        searchHeader.setPadding(new Insets(5, 50, 25, 50));
        TextField searchBox = new TextField();
        searchBox.setPrefWidth(300);
        searchBox.setMinWidth(300);
        Button searchButton = new Button();
        searchButton.setText("Search");
        searchButton.setFont(Font.font ("Arial", 12));
        searchButton.setMinWidth(75);
        searchButton.setPrefWidth(75);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    searchEntries("*",searchBox.getText());
                } catch (Exception e) {
                   e.printStackTrace();
                }
                searchBox.clear();
            }
        });
        Button refreshButton = new Button();
        refreshButton.setText("Refresh");
        refreshButton.setFont(Font.font ("Arial", 12));
        refreshButton.setMinWidth(75);
        refreshButton.setPrefWidth(75);
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try
                {
                    showEntries();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        //</editor-fold>
        searchHeader.getChildren().addAll(searchBox,searchButton, refreshButton);
        mainHeader.getChildren().addAll(mainTitle,searchHeader);
        mainHeaderPane.getChildren().addAll(mainHeader);
        //</editor-fold>
        //<editor-fold desc="Main Body">
        StackPane mainBodyPane = new StackPane();
        mainBodyPane.setPrefWidth(900);
        mainBodyPane.setMinWidth(900);
        mainBodyPane.setMaxWidth(900);
        
        VBox tableStuff = new VBox();
        
        Text tableHeader = new Text();
        tableHeader.setText("First Name\t     Last Name\t\t Company\t\tEmail\t\t   Phone Number\tOffice Number\t\tNotes");
        tableHeader.setFont(Font.font ("Arial", 16));
        
        Table_V2 result = db.retrieve("Contacts", "*", "*");
        String[][] items = db.getMultiArray(result);
        String[] header = {"First Name","Last Name","Company","Email","Phone Number","Office Number","Notes"};
        model = new DefaultTableModel(items, header);
        model.setColumnIdentifiers(header);
        JTable mainView = new JTable(model);
        mainView.setMinimumSize(new Dimension(900,370));
        mainView.setPreferredSize(new Dimension(900,370));
        mainView.setRowHeight(30);
        
        JPanel temp = new JPanel();
        temp.setMinimumSize(new Dimension(900,370));
        temp.setPreferredSize(new Dimension(900,370));
        temp.add(mainView);
        final SwingNode swingNode = new SwingNode();
        swingNode.setContent(temp);
        tableStuff.getChildren().addAll(tableHeader, swingNode);
        mainBodyPane.getChildren().add(tableStuff);
        //</editor-fold>     
        //<editor-fold desc="Main Footer">
        StackPane mainFooterPane = new StackPane();
        HBox mainFooter = new HBox();
        mainFooter.setSpacing(100);
        mainFooter.setAlignment(Pos.CENTER);
        mainFooter.setPadding(new Insets(25, 50, 50, 50));
        // <editor-fold desc="Main Footer Buttons">
        Button addButton = new Button();
        addButton.setText("Add Contact");
        addButton.setFont(Font.font ("Arial", 12));
        addButton.setMinWidth(100);
        addButton.setPrefWidth(100);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addFrame.setVisible(true);
                frame.setFocusable(false);
            }
        });
        Button editButton = new Button();
        editButton.setText("Edit Contact");
        editButton.setFont(Font.font ("Arial", 12));
        editButton.setMinWidth(100);
        editButton.setPrefWidth(100);
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                firstID = "";
                lastID = "";
                editFrame.setVisible(true);
                frame.setFocusable(false);
            }
        });
        Button viewButton = new Button();
        viewButton.setText("View Contact");
        viewButton.setFont(Font.font ("Arial", 12));
        viewButton.setMinWidth(100);
        viewButton.setPrefWidth(100);
        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                viewFrame.setVisible(true);
                int rowIndex = -1;
                rowIndex = mainView.getSelectedRow();
                if(rowIndex >= 0)
                {
                    
                }
                frame.setFocusable(false);
            }
        });
        Button deleteButton = new Button();
        deleteButton.setText("Delete Contact");
        deleteButton.setFont(Font.font ("Arial", 12));
        deleteButton.setMinWidth(100);
        deleteButton.setPrefWidth(100);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int rowIndex = -1;
                rowIndex = mainView.getSelectedRow();
                if(rowIndex >= 0)
                {
                    deleteEntry(rowIndex);
                    model.removeRow(rowIndex);
                }
                ((AbstractTableModel) mainView.getModel()).fireTableDataChanged();
            }
        });
        // </editor-fold>
        mainFooter.getChildren().addAll(addButton, editButton, viewButton, deleteButton);
        mainFooterPane.getChildren().add(mainFooter);
        // </editor-fold>
        
        BorderPane mainRoot = new BorderPane();
        mainRoot.setTop(mainHeaderPane);
        mainRoot.setCenter(mainBodyPane);
        mainRoot.setBottom(mainFooterPane);
        mainContainer.setScene(new Scene(mainRoot));
    }
    
    private void showEntries() throws JBInputException_V2
    {
        Table_V2 results = db.retrieve("Contacts", "*" , "*");
        System.out.println(results);
    }
    
    private void addEntry(String fName, String lName, String company, String email, String phoneNum, String officeNum, String notes) throws JBInputException_V2
    {
        db.insert("Contacts", "First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
                fName + "," + lName + "," + company + "," + email + "," + phoneNum + "," + officeNum + "," + notes, 0);
    }
    
    private void editEntry(String idFName, String idLName,String fName, String lName, String company, String email, String phoneNum, String officeNum, String notes)
    {
        db.update("Contacts", "First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
                fName + "," + lName + "," + company + "," + email + "," + phoneNum + "," + officeNum + "," + notes, "First_NameEQUALSIGNORECASE"+idFName+" AND Last_NameEQUALSIGNORECASE"+idLName);
    }
    
    private void deleteEntry(int selectedRow)
    {
        db.delete("Contacts", selectedRow);
    }
    
    private void searchEntries(String optionInput, String searchInput) throws JBInputException_V2
    {
        Table_V2 temp = db.retrieve("Contacts","*","*");
        System.out.println(temp.retrieve("*", optionInput+"EQUALSSIGNORECASE"+searchInput));
    }
}


//This tests the insert and update functions.
//db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
//        "Jane,Doe,ACME,jDoey@hotmail.com,8675309,123,This is a test entry",0);
//db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
//        "John,Doe,ACME,jDoe@hotmail.com,86759,143,This is a test entry",0);
//db.insert("Contacts","First_Name,Last_Name,Company,Email,Phone_Number,Office_Number,Notes",
//        "That,Boi,ACME,boi1@hotmail.com,812309,163,This is a test entry",0);
//System.out.println(db);//This will show the updated database.
//db.writeFile("save.bin");
//Table_V2 query=db.retrieve("Contacts","*","*");
//String[][] multi=db.getMultiArray(query);


//This tests the retrieve function which returns a query table.
//Table query = jb.retrieve("Dogs", "Breed,Color", "Owner=John AND Color=white");
//System.out.println(query);

//These test EQUALSIGNORECASE, CONTAINSIGNORECASE, and CONTAINS clauses.
//Table_V2 queryEqualsIgnore = jb.retrieve("Dogs", "Owner,Breed", "OwnerEQUALSIGNORECASEjoHN");
//System.out.println(queryEqualsIgnore);
//Table_V2 queryContainsIgnore = jb.retrieve("Dogs", "Owner,Breed", "OwnerCONTAINSIGNORECASEOh");
//System.out.println(queryContainsIgnore);
//Table_V2 queryContains = jb.retrieve("Dogs", "Owner,Breed", "OwnerCONTAINSoh");
//System.out.println(queryContains);

//These test > and <.
//Table_V2 queryGreater = jb.retrieve("Cats","*", "Size>5 OR OwnerCONTAINSIGNORECASEiLl");
//System.out.println(queryGreater);
//Table_V2 queryLess = jb.retrieve("Cats", "*", "Size<10 OR ColorCONTAINSIGNORECASEyELl");
//System.out.println(queryLess);

//This test the delete function which can return a table.
//jb.delete("Dogs", "ColorEQUALSIGNORECASEblue OR OwnerCONTAINSohn");
//System.out.println(jb);