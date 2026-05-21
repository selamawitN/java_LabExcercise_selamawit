# java_LabExcercise_selamawit 
 simple report about the projects

 1. chatapp
    these chatapp is a group chat anyone can join by connecting to same server and the server it handles multiple clients. they can connect simultaneously and recent 10 messages from the database are dispalyed so they can know what is being said and this is displayed when they connect to the server. so what the server does is when clients connect, their message will be broadcasted to all other connected clients and also the server saves messages with the username to the database. the outer interface for clients is simple. first it asks them for username after that they will be connected to the server but first the server must start so the start button must be clicked. so after that clients can send message and recieve it. and it only allows text to be sent.
   
2. noteapp
   these one is also simple text editor it has features like open, save as, clear, exit, copy,paste, select all and such. all this are found when you click bittons there are 3 buttons named file, edit and about. the interface is a little different from the traditional notepad because i placed the buttons in the left side and placed them vertically and the features under them will be displayed next to them as a drop down because these buttons are defined using ContextMenu class(this is mostly seen when we right-click and it needs action to appear), the feature under the buttons appear as dropdown when it is clicked next to the buttons. 

3. 5 card draw poker
   In this a player competes against the computer in 5-Card Draw Poker with fixed limit betting. The game implements rankings including Royal Flush, Straight Flush, Four of a Kind, Full House, Flush, Straight, Three of a Kind, Two Pair, Pair, and High Card. to start the game, the player and the computer each should bet 10 unit. after that player card is compared against the computer and the winner takes the pot money. players can discard up to three cards and draw new ones to improve their hand. The game continues after every round when user clicks Next Hand button but only until the player or computer is not able to bet means the money is less than 10. The interface has green background and before betting no card is shown but after bet both computer and players card are shown because it makes it easier for players to see if the win or lost fairly and to know the hand rules easily. for instance, they can see they have pair and computer has flush so computer will win so flush is > than a pair and such. 

       -- to run the chatapp go to chatapp folder and use the following commands
   
     mvn javafx:run@client   - to open the clients(need running this in different terminal for different clients)
     mvn javafx:run@server   - to open the server

to run the noteapp and poker, go to their respective folder and use the following command 
     
     mvn javafx:run

general folder structure 

        Java_LabExcercise_selamawit\folder_name
    