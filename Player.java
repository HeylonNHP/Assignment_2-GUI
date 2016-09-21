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

    public CardList takeInitialTurn(CardList playedCards){
        /*Take first turn in game i.e.
        * play first card and choose playing category*/
        String chosenCategory = pickCardCategory();

        //Play lowest value card in chosen category
        int chosenCardIndex = getLowestValueCardInCategory(chosenCategory);
        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(chosenCardIndex);
        stateCard(chosenCategory, chosenCard);
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

    private int getLowestValueCardInCategory(String category){
        if (category.equals("Economic value")){
            int lowestValueCardID = 0;
            int lowestValue = econonmicValue.length -1;
            for (int i = 0; i < myCards.length(); i++){

                if(!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    for (int j = 0; j < econonmicValue.length; j++) {
                        if (myCards.getCardAtIndex(i).getEconomicValue().equals(econonmicValue[j])) {
                            if (j < lowestValue) {
                                lowestValueCardID = i;
                                lowestValue = j;
                            }
                        }
                    }
                }
            }
            return lowestValueCardID;
        }else if(category.equals("Crustal abundance")){
            int lowestValueCardID = 0;
            int lowestValue = crustalAbundance.length-1;

            for (int i = 0; i < myCards.length(); i++){

                if(!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    for (int j = 0; j < crustalAbundance.length; j++) {
                        //System.out.println("i: " + i + "j: " + j + "ca:" + crustalAbundance.length);
                        if (myCards.getCardAtIndex(i).getCrustalAbundance().equals(crustalAbundance[j])) {
                            if (j < lowestValue) {
                                lowestValueCardID = i;
                                lowestValue = j;
                            }
                        }
                    }
                }
            }
            return lowestValueCardID;
        }else if(category.equals("Cleavage")){
            int lowestValueCardID = 0;
            int lowestValue = cleavage.length-1;

            for (int i = 0; i < myCards.length(); i++){

                if(!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    for (int j = 0; j < cleavage.length; j++) {
                        //System.out.println("i: " + i + "j: " + j + "ca:" + cleavage.length);
                        if (myCards.getCardAtIndex(i).getCleavage().equals(cleavage[j])) {
                            if (j < lowestValue) {
                                lowestValueCardID = i;
                                lowestValue = j;
                            }
                        }
                    }
                }
            }
            return lowestValueCardID;
        }else if(category.equals("Hardness")){
            int lowestValueCardID = 0;
            double lowestValue = -1;
            for (int i = 0; i < myCards.length(); i++){
                if(!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    double currentValue = 0.0;
                    if(!myCards.getCardAtIndex(i).getHardness().contains("-")){
                        currentValue = Double.parseDouble(myCards.getCardAtIndex(i).getHardness());
                    }else {
                        String currentString = myCards.getCardAtIndex(i).getHardness();
                        String[] range = currentString.split("-");
                        currentValue = Double.parseDouble(range[0]);
                    }
                    //System.out.println("i: " + i + "cv: " + currentValue);
                    if(currentValue < lowestValue || lowestValue == -1){
                        lowestValue = currentValue;
                        lowestValueCardID = i;
                    }
                }
            }
            return lowestValueCardID;
        }else if(category.equals("Specific gravity")){
            int lowestValueCardID = 0;
            double lowestValue = -1;
            for (int i = 0; i < myCards.length(); i++){
                if(!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    double currentValue = 0.0;
                    if(!myCards.getCardAtIndex(i).getSpecificGravity().contains("-")){
                        currentValue = Double.parseDouble(myCards.getCardAtIndex(i).getSpecificGravity());
                    }else {
                        String currentString = myCards.getCardAtIndex(i).getSpecificGravity();
                        String[] range = currentString.split("-");
                        currentValue = Double.parseDouble(range[0]);
                    }
                    //System.out.println("i: " + i + "cv: " + currentValue);
                    if(currentValue < lowestValue || lowestValue == -1){
                        lowestValue = currentValue;
                        lowestValueCardID = i;
                    }
                }
            }
            return lowestValueCardID;
        }
        return 0;
    }

    private String pickCardCategory(){
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
