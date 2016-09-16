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

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;
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
        CardList playingCards = getPlayingCards();
        System.out.println(playingCards.length());

        //Print card types of all cards
        for (int i = 0; i < playingCards.length() -1; i++){
           System.out.println( playingCards.getCardAtIndex(i).getType());
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

                    //Get get value corresponding to the current attributes key (nextAttribute) ignoring #Text nodes
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
        Get CardList of playing (play & trump) cards only from the PLIST file
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

