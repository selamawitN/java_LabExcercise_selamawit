package com.noteapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;

public class App extends Application {
    
    @Override
    public void start(Stage st) {
        
        TextArea ta = new TextArea();
        ta.setPrefHeight(500);
        ta.setPrefWidth(600);
        
        Button file = new Button("File");
        Button edit = new Button("Edit");
        Button help = new Button("Help");
        
        file.setPrefWidth(70);
        edit.setPrefWidth(70);
        help.setPrefWidth(70);

        ContextMenu co = new ContextMenu();
        MenuItem o = new MenuItem("Open");
        MenuItem sa = new MenuItem("Save As");
        MenuItem c = new MenuItem("Clear");
        MenuItem ex = new MenuItem("Exit");
        co.getItems().addAll(o, sa, c, ex);
        
        ContextMenu eo = new ContextMenu();
        MenuItem cu = new MenuItem("Cut");
        MenuItem cop = new MenuItem("Copy");
        MenuItem p = new MenuItem("Paste");
        MenuItem sai = new MenuItem("Select All");
        eo.getItems().addAll(cu, cop, p, sai);
        
        ContextMenu ho = new ContextMenu();
        MenuItem ab = new MenuItem("About");
        ho.getItems().add(ab);
        
        file.setOnAction(e -> co.show(file, Side.RIGHT, 0, 0));
        edit.setOnAction(e -> eo.show(edit, Side.RIGHT, 0, 0));
        help.setOnAction(e -> ho.show(help, Side.RIGHT, 0, 0));
        
    
        
        o.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Open File");
            File fi = fc.showOpenDialog(st);
            if (fi != null) {
                try (BufferedReader r = new BufferedReader(new FileReader(fi))) {
                    ta.clear();
                    String line;
                    while ((line = r.readLine()) != null) {
                        ta.appendText(line + "\n");
                    }
                } catch (Exception exe) {
                    System.out.println("Error opening file: " + exe.getMessage());
                }
            }
        });
        
        
        sa.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save File As");
            File fi = fc.showSaveDialog(st);
            if (fi != null) {
                String filePath = fi.getPath();
                if (!filePath.toLowerCase().endsWith(".txt")) {
                    fi = new File(filePath + ".txt");
                }
                try (BufferedWriter w = new BufferedWriter(new FileWriter(fi))) {
                    w.write(ta.getText());
                    System.out.println("Success! File saved as: " + fi.getName());
                } catch (Exception exe) {
                    System.out.println("Error saving file: " + exe.getMessage());
                }
            }
        });
        
        c.setOnAction(e -> ta.clear());
        ex.setOnAction(e -> st.close());
        cu.setOnAction(e -> ta.cut());
        cop.setOnAction(e -> ta.copy());
        p.setOnAction(e -> ta.paste());
        sai.setOnAction(e -> ta.selectAll());
        
        ab.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setContentText("Simple notepad application with menus on the side than regular menus ");
            alert.showAndWait();
        });
         
           VBox V = new VBox(15);
        V.setAlignment(Pos.TOP_CENTER);
        V.setPadding(new Insets(20));
        V.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");
          V.getChildren().addAll(file, edit, help);
        BorderPane b = new BorderPane();
        b.setLeft(V); 
        b.setCenter(ta);       
        b.setPadding(new Insets(10));
        
        Scene scene = new Scene(b, 800, 600);
        st.setTitle("Note App");
        st.setScene(scene);
        st.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}