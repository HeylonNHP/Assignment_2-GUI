import java.util.*;

/**
 * Created by Heylon2 on 16/09/2016.
 */
public class Player {
    private CardList myCards = new CardList();
    private Boolean isDealer;
    private Boolean hasPassed = false;

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

    public Object[] takeInitialTurn(CardList playedCards){
        /*Take first turn in game i.e.
        * play first card and choose playing category*/
        String chosenCategory = pickCardCategory();

        //Play lowest value card in chosen category
        int chosenCardIndex = getLowestValueCardInCategory(chosenCategory);
        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(chosenCardIndex);
        stateCard(chosenCategory, chosenCard, "");
        playedCards.addCard(chosenCard);
        return new Object[]{playedCards, chosenCategory};
    }

    public Object[] takeTurn(CardList playedCards, CardList deck, String category, int playerNumber){
        SupertrumpsCard previouslyPlayedCard = playedCards.getCardAtIndex(playedCards.length()-1);
        String playerName = "CPU player " + playerNumber;

        if (hasPassed){
            System.out.println(playerName + " has passed.");
            return new Object[]{playedCards, deck, category};
        }

        Boolean hasPlayableCard = false;
        for (int i = 0; i < myCards.length(); i++){
            if (!myCards.getCardAtIndex(i).getType().equals("trump")){
                if (cardHasLowerValue(previouslyPlayedCard, myCards.getCardAtIndex(i), category)){
                    hasPlayableCard = true;
                }
            }else{
                if (!myCards.getCardAtIndex(i).getSubtitle().equals(category)){
                    hasPlayableCard = true;
                }
            }
        }

        if (!hasPlayableCard){
            setHasPassed(true);
            if (deck.length() != 0){
                myCards.addCard(deck.takeCardAtIndex(deck.length()-1));
            }
            System.out.println(playerName + " doesn't have any playable cards and has passed.");
            return new Object[]{playedCards, deck, category};
        }

        int chosenCardIndex = chooseCardToPlay(previouslyPlayedCard,category);

        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(chosenCardIndex);

        if (chosenCard.getType().equals("trump")){
            if (chosenCard.getTitle().equals("The Geologist")){
                //Include smart logic to choose category
                System.out.println(playerName + " played a wild trump card");
                String newCategory = pickCardCategory();

                while (newCategory.equals(category)){
                    newCategory = pickCardCategory();
                }

                category = newCategory;
            }else{
                category = chosenCard.getSubtitle();
            }
        }

        stateCard(category, chosenCard, playerName);
        playedCards.addCard(chosenCard);

        previouslyPlayedCard = chosenCard;

        if (chosenCard.getType().equals("trump")){
            //play a second card
            if (myCards.length() != 0){
                chosenCardIndex = chooseCardToPlay(previouslyPlayedCard,category);
                chosenCard = myCards.takeCardAtIndex(chosenCardIndex);
                stateCard(category,chosenCard, playerName);
                playedCards.addCard(chosenCard);

            }
        }

        return new Object[]{playedCards, deck, category};
    }

    private int chooseCardToPlay(SupertrumpsCard previouslyPlayedCard, String category){
        ArrayList playableCardIndexes = new ArrayList();
        Boolean playableNonTrumpCards = false;
        if (!previouslyPlayedCard.getType().equals("trump")) {
            for (int i = 0; i < myCards.length(); i++) {
                if (myCards.getCardAtIndex(i).getType().equals("trump")) {
                    if (!myCards.getCardAtIndex(i).getSubtitle().equals(category)){
                        playableCardIndexes.add(i);
                    }
                } else if (cardHasLowerValue(previouslyPlayedCard, myCards.getCardAtIndex(i), category)) {
                    playableCardIndexes.add(i);
                    playableNonTrumpCards = true;
                }
            }
        }else{
            System.out.println("Previous card was trump");
            for (int i = 0; i < myCards.length(); i++){
                if (!myCards.getCardAtIndex(i).getType().equals("trump")) {
                    playableCardIndexes.add(i);
                    playableNonTrumpCards = true;
                }
            }
        }

        if (playableNonTrumpCards){
            //Find lowest value mineral card in category and play it
            int lowestValuePlayableCardIndex = (int)playableCardIndexes.get(0);

            //Get non-trump card
            for (int i = 0; i < playableCardIndexes.size(); i++){
                int currentIndex = (int)playableCardIndexes.get(i);
                if (!myCards.getCardAtIndex(currentIndex).getType().equals("trump")){
                    lowestValuePlayableCardIndex = currentIndex;
                    break;
                }
            }

            for (int i = 0; i < playableCardIndexes.size(); i++){
                int currentIndex = (int)playableCardIndexes.get(i);
                if (!myCards.getCardAtIndex(currentIndex).getType().equals("trump")){
                    if (cardHasLowerValue(myCards.getCardAtIndex(currentIndex), myCards.getCardAtIndex(lowestValuePlayableCardIndex),category)){
                        lowestValuePlayableCardIndex = currentIndex;
                    }
                }
            }
            //System.out.println("Lowest playable card index " + lowestValuePlayableCardIndex);
            return lowestValuePlayableCardIndex;
        }else{
            ArrayList availableTrumpCards = new ArrayList();
            for (int i = 0; i < playableCardIndexes.size(); i++){
                int currentIndex = (int)playableCardIndexes.get(i);
                if (myCards.getCardAtIndex(currentIndex).getType().equals("trump")){
                    availableTrumpCards.add(currentIndex);
                }
            }

            //Replace with smart logic to choose trump card
            System.out.println("Playing trump");
            return (int)availableTrumpCards.get(0);
        }
    }

    private void stateCard(String category, SupertrumpsCard card, String playerName){
        System.out.print(playerName + " played card " + card.getTitle() + " ");
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
        /*Get lowest value card in chosen category*/
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
                        currentValue = Double.parseDouble(range[1]);
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
                        currentValue = Double.parseDouble(range[1]);
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

    private Boolean cardHasLowerValue(SupertrumpsCard card1, SupertrumpsCard card2, String category){
        /*
        Check to see whether card1 has a lower value than card2 in the specified category
        */
        if(category.equals("Economic value")){
            int card1Value = -1;
            int card2Value = -1;
            for(int i = 0; i < econonmicValue.length; i++){
                if(card1.getEconomicValue().equals(econonmicValue[i])){
                    card1Value = i;
                }
                if (card2.getEconomicValue().equals(econonmicValue[i])){
                    card2Value = i;
                }
            }
            return card1Value < card2Value;

        }else if(category.equals("Crustal abundance")){
            int card1Value = -1;
            int card2Value = -1;
            for(int i = 0; i < crustalAbundance.length; i++){
                if(card1.getCrustalAbundance().equals(crustalAbundance[i])){
                    card1Value = i;
                }
                if (card2.getCrustalAbundance().equals(crustalAbundance[i])){
                    card2Value = i;
                }
            }
            return card1Value < card2Value;

        }else if(category.equals("Cleavage")){
            int card1Value = -1;
            int card2Value = -1;
            for(int i = 0; i < cleavage.length; i++){
                if(card1.getCleavage().equals(cleavage[i])){
                    card1Value = i;
                }
                if (card2.getCleavage().equals(cleavage[i])){
                    card2Value = i;
                }
            }
            return card1Value < card2Value;

        }else if(category.equals("Hardness")){
            double card1Value = -1;
            double card2Value = -1;
            if (!card1.getHardness().contains("-")){
                card1Value = Double.parseDouble(card1.getHardness());
            }else{
                String[] range = card1.getHardness().split("-");
                card1Value = Double.parseDouble(range[1]);
            }
            if (!card2.getHardness().contains("-")){
                card2Value = Double.parseDouble(card2.getHardness());
            }else{
                String[] range = card2.getHardness().split("-");
                card2Value = Double.parseDouble(range[1]);
            }
            return card1Value < card2Value;
        }else if(category.equals("Specific gravity")){
            double card1Value = -1;
            double card2Value = -1;
            if (!card1.getSpecificGravity().contains("-")){
                card1Value = Double.parseDouble(card1.getSpecificGravity());
            }else{
                String[] range = card1.getSpecificGravity().split("-");
                card1Value = Double.parseDouble(range[1]);
            }
            if (!card2.getSpecificGravity().contains("-")){
                card2Value = Double.parseDouble(card2.getSpecificGravity());
            }else{
                String[] range = card2.getSpecificGravity().split("-");
                card2Value = Double.parseDouble(range[1]);
            }
            return card1Value < card2Value;
        }
        return false;
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

    public Boolean hasCards(){
        if (myCards.length() == 0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean getHasPassed(){
        return hasPassed;
    }
    public void setHasPassed(Boolean value){
        hasPassed = value;
    }
    public String toString(){
        return "CPU player - is dealer: " + isDealer.toString() + " has " + myCards.length() + " cards";
    }
}
