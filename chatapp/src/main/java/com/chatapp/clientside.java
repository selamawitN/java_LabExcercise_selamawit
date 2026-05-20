package com.chatapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class clientside extends Application {
    
    TextArea ta = new TextArea();
    TextField tf = new TextField();
    TextField nf = new TextField();
    PrintWriter out;
    
    public void start(Stage st) {
        VBox root = new VBox();
        
        Button connect = new Button("connect");
        nf.setPromptText("name");
        
        ta.setEditable(false);
        tf.setPromptText("message");
        tf.setDisable(true);
        Button send = new Button("send");
        send.setDisable(true);
        
        root.getChildren().addAll(nf, connect, ta, tf, send);
        
        connect.setOnAction(e -> {
            connect.setDisable(true);
            nf.setDisable(true);
            tf.setDisable(false);
            send.setDisable(false);
            
            new Thread(() -> {
                try {
                    Socket s = new Socket("localhost", 1112);
                    out = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    out.println(nf.getText());
                    
                    String line;
                    while ((line = in.readLine()) != null) {
                        String msg = line;
                        javafx.application.Platform.runLater(() -> ta.appendText(msg + "\n"));
                    }
                } catch (Exception ex) {
                    javafx.application.Platform.runLater(() -> ta.appendText("server down\n"));
                }
            }).start();
        });
        
        send.setOnAction(e -> {
            out.println(tf.getText());
            tf.clear();
        });
        
        st.setScene(new Scene(root, 400, 400));
        st.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}