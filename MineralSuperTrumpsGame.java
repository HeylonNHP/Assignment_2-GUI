/**
 * Created by Heylon2 on 24/08/2016.
 */
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.*;

public class MineralSuperTrumpsGame extends JFrame implements ActionListener {
    JMenuBar topMenu = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem exitMenuItem = new JMenuItem("Exit");

    JMenu gameMenu = new JMenu("Game");
    JMenuItem startGameMenuItem = new JMenuItem("Start game");

    public MineralSuperTrumpsGame(){
        super("Mineral Super Trumps");
        setLayout(new FlowLayout());

        add(topMenu);
        setJMenuBar(topMenu);

        topMenu.add(fileMenu);
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(this);

        topMenu.add(gameMenu);
        gameMenu.add(startGameMenuItem);
        startGameMenuItem.addActionListener(this);

        add(new JButton("1"));
    }

    public void actionPerformed(ActionEvent e){
        //Exit clicked
        if (e.getSource() == exitMenuItem){
            System.exit(0);
        }
    }

    public static void main(String[] args){
        MineralSuperTrumpsGame mainFrame = new MineralSuperTrumpsGame();
        mainFrame.setSize(750,550);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);


        final String MENU = "Please choose an option:\ns) Start game\nq) Quit\n>>> ";
        Scanner inputDevice = new Scanner(System.in);
        System.out.println("Welcome to mineral super trumps");

        System.out.print(MENU);
        String userInput = inputDevice.next();
        while (!userInput.toLowerCase().equals("q")){
            switch (userInput.toLowerCase()){
                case "s":
                    System.out.print("Number of players (including you) (3-5) ");
                    int players = -1;
                    Boolean validInput = false;
                    try{
                        players = inputDevice.nextInt();
                        if (players < 3 || players > 5) {
                            System.out.println("Out of range. Try again.");
                            validInput = false;
                        }else{
                            validInput = true;
                        }
                    }catch (InputMismatchException e){
                        System.out.println("Invalid input.");
                        validInput = false;
                        inputDevice.next();
                    }

                    while (!validInput){
                        try{
                            System.out.print(">>> ");
                            players = inputDevice.nextInt();
                            if (players < 3 || players > 5) {
                                System.out.println("Out of range. Try again.");
                                validInput = false;
                            }else{
                                validInput = true;
                            }
                        }catch (InputMismatchException e){
                            System.out.println("Invalid input.");
                            validInput = false;
                            inputDevice.next();
                        }
                    }

                    startGame(players);
                    break;
                default:
                    System.out.println("Invalid input");
            }
            System.out.print(MENU);
            userInput = inputDevice.next();
        }
    }

    public static void startGame(int players){
        Random randomGenerator = new Random();
        CardList playingCards = getPlayingCards();
        playingCards.shuffle();

        ArrayList<String> atList = new ArrayList<>();

        //-----------------------------------------

        //Create players
        ArrayList<Object> playerList = new ArrayList<>();
        //Add the dealer
        int dealerType = randomGenerator.nextInt(2);
        Boolean humanPlayerCreated = false;
        switch (dealerType){
            case 0:
                playerList.add(new Player(true, "0"));
                break;
            case 1:
                playerList.add(new HumanPlayer(true));
                humanPlayerCreated = true;
                break;
        }
        //Add other players
        int humanPlayerPosition = randomGenerator.nextInt(players-1);
        for(int i = 0; i < players - 1; i++){
            if(i != humanPlayerPosition || humanPlayerCreated == true){
                playerList.add(new Player(false, (i + 1) + ""));
            }else{
                playerList.add(new HumanPlayer());
                humanPlayerCreated = true;
            }
        }

        //Deal cards
        if(playerList.get(0) instanceof Player){
            Player currentPlayer = (Player)playerList.get(0);
            Object[] returned = currentPlayer.dealCards(playerList,playingCards);
            playerList = (ArrayList<Object>) returned[0];
            playingCards = (CardList) returned[1];
        }else{
            HumanPlayer currentPlayer = (HumanPlayer)playerList.get(0);
            Object[] returned = currentPlayer.dealCards(playerList,playingCards);
            playerList = (ArrayList<Object>) returned[0];
            playingCards = (CardList) returned[1];
        }

        //Play game
        Boolean gameFinished = false;
        Boolean gameWon = false;
        int winnerIndex = -1;
        CardList playedCards = new CardList();
        int playerPosition = 1;
        String category = "";

        if (playerList.get(playerPosition) instanceof Player){
            Player initialPlayer = (Player)playerList.get(playerPosition);

            Object[] returned = initialPlayer.takeInitialTurn(playedCards);
            playedCards = (CardList)returned[0];
            category = (String)returned[1];

            playerList.set(playerPosition, initialPlayer);
        }else{
            HumanPlayer initialPlayer = (HumanPlayer)playerList.get(playerPosition);

            System.out.println("You're starting the game");
            Object[] returned = initialPlayer.takeInitialTurn(playedCards);
            playedCards = (CardList)returned[0];
            category = (String)returned[1];

            playerList.set(playerPosition, initialPlayer);
        }

        displayPlayerAndCardStats(playerList, playingCards, playedCards);

        playerPosition += 1;

        while (gameFinished == false){
            if(playerList.get(playerPosition) instanceof Player){
                Player currentPlayer = (Player)playerList.get(playerPosition);
                if (!currentPlayer.getHasFinished()){
                    Object[] returned = currentPlayer.takeTurn(playedCards, (CardList)playingCards, category);
                    playedCards = (CardList) returned[0];
                    playingCards = (CardList) returned[1];

                    String previousCategory = category;
                    category = (String) returned[2];
                    if (!previousCategory.equals(category)) {
                        allowAllPlayersToPlayAgain(playerList);
                    }

                    if (!currentPlayer.hasCards()){
                        if (gameWon){
                            currentPlayer.setHasFinished(false);
                        }else{
                            gameWon = true;
                            winnerIndex = playerPosition;
                            currentPlayer.setHasFinished(true);
                        }
                    }
                    playerList.set(playerPosition,currentPlayer);
                }
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(playerPosition);
                if (!currentPlayer.getHasFinished()){
                    Object[] returned = currentPlayer.takeTurn(playedCards, (CardList)playingCards, category);
                    playedCards = (CardList) returned[0];
                    playingCards = (CardList) returned[1];

                    String previousCategory = category;
                    category = (String) returned[2];
                    if (!previousCategory.equals(category)) {
                        allowAllPlayersToPlayAgain(playerList);
                    }

                    if (!currentPlayer.hasCards()){
                        if (gameWon){
                            currentPlayer.setHasFinished(false);
                        }else{
                            gameWon = true;
                            winnerIndex = playerPosition;
                            currentPlayer.setHasFinished(true);
                        }
                    }
                    playerList.set(playerPosition,currentPlayer);
                }
            }

            displayPlayerAndCardStats(playerList, playingCards, playedCards);

            if (isGameFinished(playerList)){
                break;
            }

            //Check if all but one or less players have passed
            int nonPassedPlayerAmount = playerList.size();
            int nonPassedPlayer = -1;
            for (int i = 0; i < playerList.size(); i++){
                if (playerList.get(i) instanceof Player){
                    Player currentPlayer = (Player)playerList.get(i);
                    if (currentPlayer.getHasPassed() == true){
                        nonPassedPlayerAmount--;
                    }else{
                        nonPassedPlayer = i;
                    }
                }else{
                    HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                    if (currentPlayer.getHasPassed() == true){
                        nonPassedPlayerAmount--;
                    }else{
                        nonPassedPlayer = i;
                    }
                }
            }
            //NEW
            if (nonPassedPlayerAmount == 1){
                System.out.println("Starting new round");
                for (int i = 0; i < playerList.size(); i++){
                    if (playerList.get(i) instanceof Player){
                        Player currentPlayer = (Player)playerList.get(i);
                        if (!currentPlayer.getHasPassed() && !currentPlayer.getHasFinished()){

                            Object[] returned = currentPlayer.takeInitialTurn(playedCards);
                            playerPosition = i;
                            playedCards = (CardList) returned[0];

                            String previousCategory = category;
                            category = (String) returned[1];
                            if (!previousCategory.equals(category)) {
                                allowAllPlayersToPlayAgain(playerList);
                            }

                            if (!currentPlayer.hasCards()){
                                if (gameWon){
                                    currentPlayer.setHasFinished(false);
                                }else{
                                    gameWon = true;
                                    winnerIndex = playerPosition;
                                    currentPlayer.setHasFinished(true);
                                }
                            }

                            playerList.set(i, currentPlayer);
                            break;
                        }
                    }else{
                        HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                        if (!currentPlayer.getHasPassed() && !currentPlayer.getHasFinished()){

                            Object[] returned = currentPlayer.takeInitialTurn(playedCards);
                            playerPosition = i;
                            playedCards = (CardList) returned[0];

                            String previousCategory = category;
                            category = (String) returned[1];
                            if (!previousCategory.equals(category)) {
                                allowAllPlayersToPlayAgain(playerList);
                            }

                            if (!currentPlayer.hasCards()){
                                if (gameWon){
                                    currentPlayer.setHasFinished(false);
                                }else{
                                    gameWon = true;
                                    winnerIndex = playerPosition;
                                    currentPlayer.setHasFinished(true);
                                }
                            }

                            playerList.set(i, currentPlayer);
                            break;
                        }
                    }
                }
                playerList = allowAllPlayersToPlayAgain(playerList);
                displayPlayerAndCardStats(playerList, playingCards, playedCards);
            }

            if (isGameFinished(playerList)){
                break;
            }

            //Execute at end of while block
            if (playerPosition == playerList.size() -1){
                playerPosition = 0;
            }else{
                playerPosition += 1;
            }
        }
        System.out.println("The game has finished.");
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i) instanceof Player){
                Player currentPlayer = (Player)playerList.get(i);
                if (!currentPlayer.getHasFinished()) {
                    System.out.println(currentPlayer.getName() + " Lost the game. Haha you suck " + currentPlayer.getName() + ".");
                    //break;
                }else{
                    if (winnerIndex != i) {
                        System.out.println(currentPlayer.getName() + " finished");
                    }else{
                        System.out.println(currentPlayer.getName() + " won");
                    }
                }
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                if (!currentPlayer.getHasFinished()) {
                    System.out.println("You lost. Better luck next time.");
                    //break;
                }else{
                    if (winnerIndex != i) {
                        System.out.println("You finished");
                    }else{
                        System.out.println("You won");
                    }
                }
            }
        }

    }
    public static ArrayList<Object> allowAllPlayersToPlayAgain(ArrayList<Object> playerList){
        /*
        If any player has passed - set their passed status to false so they can play again
        */
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i) instanceof Player){
                Player currentPlayer = (Player)playerList.get(i);
                if (!currentPlayer.getHasFinished()) {
                    currentPlayer.setHasPassed(false);
                }
                playerList.set(i, currentPlayer);
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                if (!currentPlayer.getHasFinished()) {
                    currentPlayer.setHasPassed(false);
                }
                playerList.set(i, currentPlayer);
            }
        }
        return playerList;
    }

    public static Boolean isGameFinished(ArrayList<Object> playerList){
        int playersWithCards = 0;
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i) instanceof Player){
                Player currentPlayer = (Player)playerList.get(i);
                if (currentPlayer.hasCards()){
                    playersWithCards++;
                }
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                if (currentPlayer.hasCards()){
                    playersWithCards++;
                }
            }
        }
        if (playersWithCards <= 1){
            return true;
        }else{
            return false;
        }
    }

    public static void displayPlayerAndCardStats(ArrayList<Object> playerList, CardList playingCards, CardList playedCards){
        System.out.println("");
        for(int i = 0; i < playerList.size(); i++){
            System.out.println(playerList.get(i).toString());
        }
        System.out.println("-----Cards left " + playingCards.length() + " -----Cards played " + playedCards.length());
        System.out.println("");
    }

    public static ArrayList loadCards(String xmlPath){

        try{
            File inputFile = new File(xmlPath);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(inputFile);
            xmlDocument.getDocumentElement().normalize();

            ArrayList cardList = new ArrayList();
            
            //Get cards from XML
            NodeList cards = xmlDocument.getElementsByTagName("dict");

            // for each card (<dict>)
            for (int i = 1; i < cards.getLength(); i++){
                Node currentItem = cards.item(i);
                NodeList cardAttributeList = currentItem.getChildNodes();

                Map<String, String> cardAttributeDictionary = new HashMap<String, String>();

                //for each card attribute (element inside <dict> e.g. <key>, <string>, <array>)
                for (int j = 0; j < cardAttributeList.getLength() - 2; j++){
                    Node currentAttribute = cardAttributeList.item(j);
                    Node nextAttribute = currentAttribute;

                    //Get value corresponding to the current attributes key (nextAttribute) ignoring #Text nodes
                    for (int h = (j+1); h < cardAttributeList.getLength(); h++){
                        if(cardAttributeList.item(h).getNodeType() == Node.ELEMENT_NODE){
                            nextAttribute = cardAttributeList.item(h);
                            break;
                        }
                    }

                    if(currentAttribute.getNodeType() == Node.ELEMENT_NODE){
                        if(currentAttribute.getNodeName().equals("key") && !nextAttribute.getNodeName().equals("key")){
                            //If current element is key and next element isn't key

                            if(nextAttribute.getNodeName().equals("string")){
                                cardAttributeDictionary.put(currentAttribute.getTextContent(),nextAttribute.getTextContent());

                            }else if(nextAttribute.getNodeName().equals("array")){
                                NodeList arrayItems = nextAttribute.getChildNodes();
                                String arrayString = "";

                                for (int h = 0; h < arrayItems.getLength(); h++){
                                    if(arrayItems.item(h).getNodeType() == Node.ELEMENT_NODE) {
                                        arrayString += arrayItems.item(h).getTextContent() + ",";
                                    }
                                }

                                //Remove "," from end of array string
                                arrayString = arrayString.substring(0,arrayString.length()-1);

                                cardAttributeDictionary.put(currentAttribute.getTextContent(), arrayString);
                            }
                        }else if(currentAttribute.getNodeName().equals("key") && currentAttribute.getTextContent().equals("card_type")){
                            //If current element is key and contains card_type
                            cardAttributeDictionary.put(currentAttribute.getTextContent(),nextAttribute.getTextContent());
                        }

                    }
                }
                //System.out.println("-------------------------------------------");
                //cardAttributeDictionary.forEach((k,v) -> System.out.println("Key: " + k + " Value: " + v));
                //System.out.println("-------------------------------------------");
                cardList.add(cardAttributeDictionary);
            }

            return cardList;
        }catch (Exception e){
            System.out.println("Something went wrong while loading the cards.");
            return new ArrayList();
        }
    }

    public static CardList getPlayingCards(){
        /*
        Get CardList of playing (play & trump) cards only (NOT rule) from the PLIST file
         */
        ArrayList cardList = loadCards("C:\\Users\\Heylon2\\OneDrive\\JCU\\CP2406\\Assignment 1\\project_mineral_super_trumps_game\\MstCards_151021.plist");
        CardList playingCardList = new CardList(cardList);

        for(int i = playingCardList.length() - 1; i > 0; i--){
            SupertrumpsCard currentCard = playingCardList.getCardAtIndex(i);

            if(!currentCard.getType().equals("play") && !currentCard.getType().equals("trump")){
                playingCardList.removeCardAtIndex(i);
            }
        }

        return playingCardList;
    }
}

