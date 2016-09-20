import javax.smartcardio.Card;
import java.util.ArrayList;

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

    public ArrayList<Player> dealCards(ArrayList<Player> playerList, CardList deck){
        return new ArrayList<>();
    }
    public String toString(){
        return "CPU player - is dealer: " + isDealer.toString();
    }
}
