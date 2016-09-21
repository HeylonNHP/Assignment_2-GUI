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
