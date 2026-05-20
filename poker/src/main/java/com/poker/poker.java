package com.poker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class poker extends Application {
    
    int myMoney = 50, pcMoney = 50, pot = 0;
    List<Card> myCards = new ArrayList<>(), pcCards = new ArrayList<>();
    Deck deck;
    
    Label msg = new Label("Click BET 10");
    TextArea myArea = new TextArea(), pcArea = new TextArea();
    Button bet = new Button("BET 10"), fold = new Button("FOLD"), draw = new Button("DRAW"), again = new Button("NEW");
    CheckBox[] cb = new CheckBox[5];
    Button nextHandBtn = new Button("NEXT HAND");
    boolean waitingForNext = false;

   @Override 
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-background-color: green; -fx-padding: 10;");
        
        myArea.setPrefSize(400, 100);
        myArea.setEditable(false);
        pcArea.setPrefSize(400, 100);
        pcArea.setEditable(false);
        
        HBox topRow = new HBox(20);
        topRow.setAlignment(javafx.geometry.Pos.CENTER);
        
        Label youLabel = new Label("YOU: " + myMoney);
        Label potLabel = new Label("POT: " + pot);
        Label pcLabel = new Label("PC: " + pcMoney);
        
        youLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        potLabel.setStyle("-fx-text-fill: yellow; -fx-font-weight: bold; -fx-font-size: 14px;");
        pcLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        
        youLabel.setPrefWidth(100);
        potLabel.setPrefWidth(100);
        pcLabel.setPrefWidth(100);
        
        youLabel.setAlignment(javafx.geometry.Pos.CENTER);
        potLabel.setAlignment(javafx.geometry.Pos.CENTER);
        pcLabel.setAlignment(javafx.geometry.Pos.CENTER);
        
        topRow.getChildren().addAll(youLabel, potLabel, pcLabel);
        
        msg.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-wrap-text: true;");
        
        Label yourCardsLabel = new Label("YOUR CARDS:");
        Label pcCardsLabel = new Label("PC CARDS:");
        yourCardsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        pcCardsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        
        Label note = new Label("Note: 5H = 5 of Hearts, QC = Queen of Clubs, KS = King of Spades, AD = Ace of Diamonds");
        note.setStyle("-fx-font-size: 10px; -fx-text-fill: white;");
        
        myArea.setStyle("-fx-text-fill: #00ff00; -fx-background-color: #2d2d2d; -fx-font-weight: bold;");
        pcArea.setStyle("-fx-text-fill: #ff6666; -fx-background-color: #2d2d2d; -fx-font-weight: bold;");
        
        HBox boxRow = new HBox(10);
        for (int i = 0; i < 5; i++) {
            cb[i] = new CheckBox("" + (i+1));
            cb[i].setStyle("-fx-text-fill: white;");
            boxRow.getChildren().add(cb[i]);
            cb[i].setDisable(true);
        }
        
        HBox btnRow = new HBox(10);
        nextHandBtn.setStyle("-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;");
        nextHandBtn.setDisable(true);
        btnRow.getChildren().addAll(bet, fold, draw, nextHandBtn, again);
        
        root.getChildren().addAll(
            topRow, msg, yourCardsLabel, myArea, pcCardsLabel, pcArea, note, boxRow, btnRow
        );
        
        bet.setOnAction(e -> doBet());
        fold.setOnAction(e -> doFold());
        draw.setOnAction(e -> doDraw());
        nextHandBtn.setOnAction(e -> newGame());
        again.setOnAction(e -> { myMoney = 50; pcMoney = 50; newGame(); });
        
        stage.setScene(new Scene(root, 500, 700));
        stage.setTitle("5 CARD DRAW POKER GAME WITH FIXED MONEY BET");
        stage.show();
        newGame();
    }
    
    void newGame() {
        deck = new Deck();
        deck.shuffle();
        myCards = deck.deal(5);
        pcCards = deck.deal(5);
        pot = 0;
        waitingForNext = false;
        updateLabels();
        msg.setText("=== NEW HAND ===\nClick BET 10 to start");
        bet.setDisable(false);
        fold.setDisable(true);
        draw.setDisable(true);
        nextHandBtn.setDisable(true);
        myArea.setText("???\n???\n???\n???\n???");
        pcArea.setText("???\n???\n???\n???\n???");
        for (CheckBox c : cb) { c.setSelected(false); c.setDisable(true); }
        
        if (myMoney < 10 || pcMoney < 10) {
            endGame();
        }
    }
    
    void doBet() {
        if (waitingForNext) return;
        if (myMoney < 10) { 
            endGame();
            return; 
        }
        myMoney -= 10;
        pot += 10;
        updateLabels();
        msg.setText("You bet 10! Computer betting...");
        bet.setDisable(true);
        
        javafx.animation.PauseTransition p = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        p.setOnFinished(e -> pcBet());
        p.play();
    }
    
    void pcBet() {
        if (pcMoney < 10) {
            msg.setText("PC has no money! You win " + pot);
            myMoney += pot;
            pot = 0;
            updateLabels();
            showRoundResult("YOU WIN THIS ROUND!");
            return;
        }
        pcMoney -= 10;
        pot += 10;
        updateLabels();
        msg.setText("PC bets 10! Pot: " + pot);
        
        javafx.animation.PauseTransition p = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1));
        p.setOnFinished(e -> showCards());
        p.play();
    }
    
    void showCards() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myCards.size(); i++) sb.append((i+1) + ". " + myCards.get(i) + "\n");
        myArea.setText(sb.toString());
        
        sb = new StringBuilder();
        for (Card c : pcCards) sb.append(c + "\n");
        pcArea.setText(sb.toString());
        
        msg.setText("=== DISCARD PHASE ===\nSelect cards to discard (0-3), then DRAW");
        fold.setDisable(false);
        draw.setDisable(false);
        for (CheckBox c : cb) c.setDisable(false);
    }
    
    void doDraw() {
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < myCards.size(); i++) if (cb[i].isSelected()) toRemove.add(i);
        if (toRemove.size() > 3) { msg.setText("Max 3 cards!"); return; }
        
        for (int i = toRemove.size()-1; i >= 0; i--) myCards.remove((int)toRemove.get(i));
        myCards.addAll(deck.deal(5 - myCards.size()));
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < myCards.size(); i++) sb.append((i+1) + ". " + myCards.get(i) + "\n");
        myArea.setText(sb.toString());
        
        msg.setText("Computer drawing...");
        draw.setDisable(true);
        fold.setDisable(true);
        for (CheckBox c : cb) c.setDisable(true);
        
        javafx.animation.PauseTransition p = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
        p.setOnFinished(e -> pcDraw());
        p.play();
    }
    
    void pcDraw() {
        int handStrength = getHandValue(pcCards);
        List<Integer> toRemove = new ArrayList<>();
        
        if (handStrength < 2) {
            for (int i = 0; i < pcCards.size(); i++) {
                if (getRankValue(pcCards.get(i).rank) < 11) toRemove.add(i);
            }
            while (toRemove.size() > 3) toRemove.remove(toRemove.size()-1);
        } else if (handStrength == 2) {
            String pairRank = "";
            for (Card c : pcCards) {
                int cnt = 0;
                for (Card cc : pcCards) if (cc.rank.equals(c.rank)) cnt++;
                if (cnt == 2) { pairRank = c.rank; break; }
            }
            for (int i = 0; i < pcCards.size(); i++) {
                if (!pcCards.get(i).rank.equals(pairRank)) toRemove.add(i);
            }
        }
        
        for (int i = toRemove.size()-1; i >= 0; i--) pcCards.remove((int)toRemove.get(i));
        if (toRemove.size() > 0) pcCards.addAll(deck.deal(5 - pcCards.size()));
        
        StringBuilder sb = new StringBuilder();
        for (Card c : pcCards) sb.append(c + "\n");
        pcArea.setText(sb.toString());
        
        compare();
    }
    
    void compare() {
        int myVal = getHandValue(myCards);
        int pcVal = getHandValue(pcCards);
        String myName = getHandName(myVal);
        String pcName = getHandName(pcVal);
        
        String result = "";
        
        if (myVal > pcVal) {
            result = " YOU WIN " + pot + "! \nYour " + myName + " beats " + pcName;
            myMoney += pot;
        } else if (pcVal > myVal) {
            result = "💻 PC WINS " + pot + " 💻\nPC's " + pcName + " beats your " + myName;
            pcMoney += pot;
        } else {
            int myHigh = getHighCard(myCards);
            int pcHigh = getHighCard(pcCards);
            if (myHigh > pcHigh) {
                result = " YOU WIN " + pot + "! (Higher card) ";
                myMoney += pot;
            } else if (pcHigh > myHigh) {
                result = " PC WINS " + pot + " (Higher card) ";
                pcMoney += pot;
            } else {
                int split = pot / 2;
                result = " TIE! Split " + pot + " - You get " + split ;
                myMoney += split;
                pcMoney += pot - split;
            }
        }
        
        pot = 0;
        updateLabels();
        showRoundResult(result);
    }
    
    void doFold() {
        String result = "You fold! PC wins " + pot;
        pcMoney += pot;
        pot = 0;
        updateLabels();
        showRoundResult(result);
    }
    
    void showRoundResult(String result) {
        msg.setText("=== ROUND COMPLETE ===\n" + result + "\n\nMoney - You: " + myMoney + " | PC: " + pcMoney);
        bet.setDisable(true);
        fold.setDisable(true);
        draw.setDisable(true);
        
        if (myMoney < 10 || pcMoney < 10) {
            endGame();
        } else {
            waitingForNext = true;
            nextHandBtn.setDisable(false);
            msg.setText(msg.getText() + "\n\nClick NEXT HAND to continue");
        }
    }
    
    void endGame() {
        bet.setDisable(true);
        fold.setDisable(true);
        draw.setDisable(true);
        nextHandBtn.setDisable(true);
        again.setDisable(false);
        
        if (myMoney < 10 && pcMoney < 10) {
            msg.setText(" GAME OVER! BOTH PLAYERS ARE BROKE! \nClick NEW to start over");
        } else if (myMoney < 10) {
            msg.setText(" GAME OVER! YOU LOSE! PC WINS! \nClick NEW to start over");
        } else if (pcMoney < 10) {
            msg.setText(" GAME OVER! YOU WIN THE GAME! \nClick NEW to start over");
        }
    }
    
    int getHandValue(List<Card> hand) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card c : hand) rankCount.put(c.rank, rankCount.getOrDefault(c.rank, 0) + 1);
        
        boolean has3 = rankCount.containsValue(3);
        int pairs = 0;
        for (int v : rankCount.values()) if (v == 2) pairs++;
        
        if (rankCount.containsValue(4)) return 8;
        if (has3 && pairs == 1) return 7;
        
        boolean flush = true;
        String suit = hand.get(0).suit;
        for (Card c : hand) if (!c.suit.equals(suit)) flush = false;
        if (flush) return 6;
        
        List<Integer> ranks = new ArrayList<>();
        for (Card c : hand) ranks.add(getRankValue(c.rank));
        Collections.sort(ranks);
        boolean straight = (ranks.get(4) - ranks.get(0) == 4) || ranks.equals(Arrays.asList(2,3,4,5,14));
        if (straight) return 5;
        
        if (has3) return 4;
        if (pairs == 2) return 3;
        if (pairs == 1) return 2;
        return 1;
    }
    
    String getHandName(int val) {
        String[] names = {"", "HIGH CARD", "PAIR", "TWO PAIR", "THREE KIND", "STRAIGHT", "FLUSH", "FULL HOUSE", "FOUR KIND"};
        return val <= 8 ? names[val] : "ROYAL";
    }
    
    int getRankValue(String r) {
        if (r.equals("J")) return 11;
        if (r.equals("Q")) return 12;
        if (r.equals("K")) return 13;
        if (r.equals("A")) return 14;
        return Integer.parseInt(r);
    }
    
    int getHighCard(List<Card> hand) {
        int high = 0;
        for (Card c : hand) high = Math.max(high, getRankValue(c.rank));
        return high;
    }
    
    void updateLabels() {
        HBox topRow = (HBox) ((VBox) myArea.getParent()).getChildren().get(0);
        ((Label) topRow.getChildren().get(0)).setText("YOU: " + myMoney);
        ((Label) topRow.getChildren().get(1)).setText("POT: " + pot);
        ((Label) topRow.getChildren().get(2)).setText("PC: " + pcMoney);
    }
    
    static class Card {
        String rank, suit;
        Card(String r, String s) { rank = r; suit = s; }
        public String toString() { return rank + suit; }
    }
    
    static class Deck {
        List<Card> cards = new ArrayList<>();
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] suits = {"H","D","C","S"};
        Deck() { for (String s : suits) for (String r : ranks) cards.add(new Card(r, s)); }
        void shuffle() { Collections.shuffle(cards); }
        List<Card> deal(int n) { List<Card> h = new ArrayList<>(); for (int i=0; i<n && !cards.isEmpty(); i++) h.add(cards.remove(0)); return h; }
    }
    
    public static void main(String[] args) { launch(args); }
}