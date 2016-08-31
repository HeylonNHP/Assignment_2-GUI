import java.util.ArrayList;

/**
 * Created by Heylon2 on 31/08/2016.
 */
public class CardList {
    private ArrayList listOfCards;

    public CardList(){
        listOfCards = new ArrayList();
    }

    public void addCard(Object card){
        listOfCards.add(card);
    }
}
