
package com.chatapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.util.*;

public class serverside extends Application {
    
    static Set<PrintWriter> clients = new HashSet<>();
    static TextArea log = new TextArea();
    static int PORT = 1112;
    static ServerSocket ss;
    static boolean running = false;
    static dbconnection db = new dbconnection();  
    
    Button start;
    
    @Override
    public void start(Stage st) {
        VBox root = new VBox(10);
        
        start = new Button("Start Server");
        log.setEditable(false);
        log.setPrefHeight(400);
        
        root.getChildren().addAll(start, log);
        
        start.setOnAction(e -> {
            running = true;
            start.setDisable(true);
            log.appendText("Server started on port " + PORT + "\n");
            
            new Thread(() -> {
                try {
                    ss = new ServerSocket(PORT);
                    while (running) {
                        Socket s = ss.accept();
                        log.appendText("Client connected\n");
                        new ClientHandler(s).start();
                    }
                } catch (IOException ex) {
                    log.appendText("Server error: " + ex.getMessage() + "\n");
                }
            }).start();
        });
        
        st.setScene(new Scene(root, 500, 500));
        st.setTitle("Chat Server");
        st.show();
    }
    
    static class ClientHandler extends Thread {
        Socket s;
        PrintWriter out;
        BufferedReader in;
        String name;
        
        ClientHandler(Socket socket) {
            s = socket;
        }
        
        public void run() {
            try {
                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                
                name = in.readLine();
                clients.add(out);
                log.appendText(name + " joined\n");
                
                List<String> history = db.get();
                out.println("--- start ---");
                for (String h : history) {
                    out.println(h);
                }
                out.println("--- end ---");
                
                String msg;
                while ((msg = in.readLine()) != null) {
                    db.save(name, msg);
                    
                    log.appendText(name + ": " + msg + "\n");
                    for (PrintWriter c : clients) {
                        c.println(name + ": " + msg);
                    }
                }
            } catch (IOException e) {
                log.appendText(name + " left\n");
            } finally {
                clients.remove(out);
                try { s.close(); } catch (IOException e) {}
            }
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}