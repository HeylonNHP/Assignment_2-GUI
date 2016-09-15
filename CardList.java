import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Heylon2 on 31/08/2016.
 */
public class CardList {
    private ArrayList<SupertrumpsCard> listOfCards;

    public CardList(){
        listOfCards = new ArrayList<>();
    }
    public CardList(ArrayList cardList){
        listOfCards = new ArrayList<>();
        for(int i = 0; i < cardList.size(); i++) {
            Map<String, String> currentMap = (Map) cardList.get(i);
            SupertrumpsCard newCard = new SupertrumpsCard(currentMap);
            addCard(newCard);
        }
    }

    public void addCard(SupertrumpsCard card){
        listOfCards.add(card);
    }

    public SupertrumpsCard getCardAtIndex(int index){
        return listOfCards.get(index);
    }
}
