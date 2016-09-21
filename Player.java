import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heylon2 on 16/09/2016.
 */
public class Player {
    private CardList myCards = new CardList();
    private Boolean isDealer;
    public Player(){
        isDealer = false;
    }
    public Player(Boolean dealer){
        isDealer = dealer;
    }

    public Object[] dealCards(ArrayList<Object> playerList, CardList deck){
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

    public CardList takeCard(CardList cards, int index){
        myCards.addCard(cards.takeCardAtIndex(index));
        return cards;
    }

    public String toString(){
        return "CPU player - is dealer: " + isDealer.toString() + " has " + myCards.length() + " cards";
    }
}
