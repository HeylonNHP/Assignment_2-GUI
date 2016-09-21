import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Heylon2 on 20/09/2016.
 */
public class HumanPlayer {
    private CardList myCards = new CardList();
    private Boolean isDealer;
    public HumanPlayer(){
        isDealer = false;
    }
    public HumanPlayer(Boolean dealer){
        isDealer = dealer;
    }

    public CardList takeCard(CardList cards, int index){
        myCards.addCard(cards.takeCardAtIndex(index));
        return cards;
    }

    public CardList takeInitialTurn(CardList playedCards){
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Economic value");
        categories.add("Crustal abundance");
        categories.add("Hardness");
        categories.add("Cleavage");
        categories.add("Specific gravity");

        Scanner inputDevice = new Scanner(System.in);

        System.out.println("You're starting the game");
        System.out.println("Your cards are: ");

        for(int i = 0; i < myCards.length(); i++){
            SupertrumpsCard currentCard = myCards.getCardAtIndex(i);
            System.out.print("Card #" + (i+1) + " ");

            System.out.print("Mineral: " + currentCard.getTitle() + " ");
            System.out.print("Economic value: " + currentCard.getEconomicValue() + " ");
            System.out.print("Crustal abundance: " + currentCard.getCrustalAbundance() + " ");
            System.out.print("Hardness: " + currentCard.getHardness() + " ");
            System.out.print("Cleavage: " + currentCard.getCleavage() + " ");
            System.out.println("Specific gravity: " + currentCard.getSpecificGravity());
        }

        System.out.println("Choose a category");
        String categoryChoice = inputDevice.next();
        while (!categories.contains(categoryChoice)){
            System.out.println("Incorrect input, please try again\n Choose a category");
            categoryChoice = inputDevice.next();
        }

        System.out.println("Choose a card");
        int cardChoice = inputDevice.nextInt();
        while (cardChoice < 0 || cardChoice > myCards.length()-1){
            System.out.println("Out of range\nChoose a card");
            cardChoice = inputDevice.nextInt();
        }

        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(cardChoice);

        stateCard(categoryChoice, chosenCard);

        playedCards.addCard(chosenCard);
        return playedCards;
    }

    private void stateCard(String category, SupertrumpsCard card){
        System.out.print("Played card " + card.getTitle() + " ");
        if(category.equals("Economic value")){
            System.out.println("Economic value " + card.getEconomicValue());

        }else if(category.equals("Crustal abundance")){
            System.out.println("Crustal abundance " + card.getCrustalAbundance());
        }else if(category.equals("Hardness")){
            System.out.println("Hardness " + card.getHardness());
        }else if(category.equals("Cleavage")){
            System.out.println("Cleavage " + card.getCleavage());
        }else if(category.equals("Specific gravity")){
            System.out.println("Specific gravity " + card.getSpecificGravity());
        }
    }

    public Object[] dealCards(ArrayList<Object> playerList, CardList deck){
        Scanner inputDevice = new Scanner(System.in);
        System.out.println("You are the dealer - press enter to deal cards");
        inputDevice.nextLine();

        if(isDealer == false){
            return new Object[]{playerList,deck};
        }
        deck.shuffle();
        for (int i = 0; i < playerList.size(); i++){

            if(playerList.get(i) instanceof Player){
                Player currentPlayer = (Player)playerList.get(i);
                for (int j = 0; j < 8; j++){
                    currentPlayer.takeCard(deck,0);
                }
            }else{
                HumanPlayer currentPlayer = (HumanPlayer)playerList.get(i);
                for (int j = 0; j < 8; j++){
                    currentPlayer.takeCard(deck,0);
                }
            }

        }
        return new Object[]{playerList,deck};
    }

    public String toString(){
        return "Human player - is dealer: " + isDealer.toString() + " has " + myCards.length() + " cards";
    }
}
