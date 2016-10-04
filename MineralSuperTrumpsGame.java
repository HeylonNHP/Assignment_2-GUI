/**
 * Created by Heylon2 on 24/08/2016.
 */
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.*;

public class MineralSuperTrumpsGame {
    public static void main(String[] args){
        final String MENU = "Please choose an option:\ns) Start game\nq) Quit\n>>> ";
        Scanner inputDevice = new Scanner(System.in);
        System.out.println("Welcome to mineral super trumps");

        System.out.print(MENU);
        String userInput = inputDevice.next();
        while (!userInput.toLowerCase().equals("q")){
            switch (userInput.toLowerCase()){
                case "s":
                    System.out.print("Number of players (including you) (3-5) ");
                    int players = inputDevice.nextInt();
                    startGame(players);
                    break;
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

        //Print card types of all cards DEBUG
        for (int i = 0; i < playingCards.length(); i++){
           System.out.println(playingCards.getCardAtIndex(i).getType() + " " + playingCards.getCardAtIndex(i).getTitle());
            if (playingCards.getCardAtIndex(i).getSpecificGravity() != null) {
                if (!atList.contains(playingCards.getCardAtIndex(i).getSpecificGravity())) {
                    atList.add(playingCards.getCardAtIndex(i).getSpecificGravity());
                }
            }
        }
        System.out.println("--------------------");
        for(String val : atList){
            System.out.println(val);
        }
        //-----------------------------------------

        //Create players
        ArrayList<Object> playerList = new ArrayList<>();
        //Add the dealer
        int dealerType = randomGenerator.nextInt(2);
        Boolean humanPlayerCreated = false;
        switch (dealerType){
            case 0:
                playerList.add(new Player(true));
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
                playerList.add(new Player());
            }else{
                playerList.add(new HumanPlayer());
                humanPlayerCreated = true;
            }
        }

        //System.out.println(humanPlayerPosition + " " + humanPlayerCreated);

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

        //DEBUG
        System.out.println(playerList.size());
        for(int i = 0; i < playerList.size(); i++){
            System.out.println(playerList.get(i).toString());
        }


        //Play game
        Boolean gameWon = false;
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

            Object[] returned = initialPlayer.takeInitialTurn(playedCards);
            playedCards = (CardList)returned[0];
            category = (String)returned[1];

            playerList.set(playerPosition, initialPlayer);
        }

        playerPosition += 1;

        while (gameWon == false){
            if(playerList.get(playerPosition) instanceof Player){
                Player currentPlayer = (Player)playerList.get(playerPosition);

                Object[] returned = currentPlayer.takeTurn(playedCards, playingCards, category, playerPosition);
                playedCards = (CardList)returned[0];
                playingCards = (CardList)returned[1];
                category = (String)returned[2];
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(playerPosition);

                Object[] returned = currentPlayer.takeTurn(playedCards,playingCards,category);
                playedCards = (CardList)returned[0];
                playingCards = (CardList)returned[1];
                category = (String)returned[2];
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

            if (nonPassedPlayerAmount <= 1){
                //Start new round
                System.out.println("Starting new round");
                for (int i = 0; i < playerList.size(); i++){
                    if (playerList.get(i) instanceof Player){
                        Player currentPlayer = (Player)playerList.get(i);
                        if (currentPlayer.getHasPassed()){
                            currentPlayer.setHasPassed(false);
                        }else{
                            Object[] returned = currentPlayer.takeInitialTurn(playingCards);
                            playerPosition = i;
                            playedCards = (CardList)returned[0];
                            category = (String)returned[1];
                            playerList.set(playerPosition, currentPlayer);
                        }
                    }else{
                        HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                        if (currentPlayer.getHasPassed()){
                            currentPlayer.setHasPassed(false);
                        }else{
                            Object[] returned = currentPlayer.takeInitialTurn(playingCards);
                            playerPosition = i;
                            playedCards = (CardList)returned[0];
                            category = (String)returned[1];
                            playerList.set(playerPosition, currentPlayer);
                        }
                    }
                }
            }

            //Execute at end of while block
            if (playerPosition == playerList.size() -1){
                playerPosition = 0;
            }else{
                playerPosition += 1;
            }
        }
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

