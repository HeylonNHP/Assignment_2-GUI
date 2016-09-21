import javax.smartcardio.Card;
import java.util.*;

/**
 * Created by Heylon2 on 16/09/2016.
 */
public class Player {
    private CardList myCards = new CardList();
    private Boolean isDealer;

    String[] cleavage = new String[]{"none","poor/none","1 poor","2 poor","1 good","1 good, 1 poor","2 good","3 good","1 perfect","1 perfect, 1 good","1 perfect, 2 good","2 perfect, 1 good","3 perfect","4 perfect","6 perfect"
    };
    String[] crustalAbundance = new String[]{"ultratrace","trace","low","moderate","high","very high"};

    String[] econonmicValue = new String[]{"trivial","low","moderate","high","very high","I'm rich!"};



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

    public void takeTurn(CardList availableCards, CardList playedCards){

    }

    public void takeInitialTurn(){
        /*Take first turn in game i.e.
        * play first card and choose playing category*/

    }

    private String pickBestCardCategory(){
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Economic value");
        categories.add("Crustal abundance");
        categories.add("Hardness");
        categories.add("Cleavage");
        categories.add("Specific gravity");
        ArrayList<String> trumpCardsInPossession = new ArrayList<>();
        for(int i = 0; i < myCards.length(); i++){
            if(myCards.getCardAtIndex(i).getType().equals("trump")){
                trumpCardsInPossession.add(myCards.getCardAtIndex(i).getSubtitle());
            }
        }
        //Select a random category that isn't one of the trump cards we may have
        Random randomGenerator = new Random();

        int selectedCategory = 0;
        selectedCategory = randomGenerator.nextInt(categories.size()-1);
        while (trumpCardsInPossession.contains(categories.get(selectedCategory))){
            selectedCategory = randomGenerator.nextInt(categories.size()-1);
        }
        return categories.get(selectedCategory);
    }

    public String toString(){
        return "CPU player - is dealer: " + isDealer.toString() + " has " + myCards.length() + " cards";
    }
}
