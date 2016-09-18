import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

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

    public SupertrumpsCard takeCardAtIndex(int index){
        SupertrumpsCard selectedCard = listOfCards.get(index);
        listOfCards.remove(index);
        return selectedCard;
    }

    public SupertrumpsCard getCardAtIndex(int index){
        return listOfCards.get(index);
    }

    public void removeCardAtIndex(int index){
        listOfCards.remove(index);
    }

    public int length(){
        return listOfCards.size();
    }

    public void shuffle(){
        ArrayList<SupertrumpsCard> shuffledList = new ArrayList<>();
        ArrayList availableCards = new ArrayList<>();
        Random randomGenerator = new Random();

        for(int i = 0; i < listOfCards.size(); i++){
            availableCards.add(i);
        }

        for(int i = 0; i < listOfCards.size(); i++){
            int chosenCardIndex = randomGenerator.nextInt(availableCards.size());
            int chosenCard = (int)availableCards.get(chosenCardIndex);
            shuffledList.add(listOfCards.get(chosenCard));
            availableCards.remove(chosenCardIndex);
        }
        listOfCards = shuffledList;
    }
}
