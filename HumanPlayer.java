import com.sun.org.apache.xpath.internal.operations.Bool;

import java.nio.InvalidMarkException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by Heylon2 on 20/09/2016.
 */
public class HumanPlayer {
    private CardList myCards = new CardList();
    private Boolean isDealer;
    private Boolean hasPassed = false;
    private Boolean hasFinished = false;

    String[] cleavage = new String[]{"none","poor/none","1 poor","2 poor","1 good","1 good, 1 poor","2 good","3 good","1 perfect","1 perfect, 1 good","1 perfect, 2 good","2 perfect, 1 good","3 perfect","4 perfect","6 perfect"
    };
    String[] crustalAbundance = new String[]{"ultratrace","trace","low","moderate","high","very high"};

    String[] econonmicValue = new String[]{"trivial","low","moderate","high","very high","I'm rich!"};

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

    public Object[] takeInitialTurn(CardList playedCards){
        /*Take the very first turn of a game*/
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Economic value");
        categories.add("Crustal abundance");
        categories.add("Hardness");
        categories.add("Cleavage");
        categories.add("Specific gravity");

        Scanner inputDevice = new Scanner(System.in);

        System.out.println("Your cards are: ");

        displayCardList();

        String newCategory = chooseCategory();

        System.out.println("Choose a card");
        int cardChoice = inputDevice.nextInt();
        while (cardChoice < 0 || cardChoice > myCards.length()-1){
            System.out.println("Out of range\nChoose a card");
            cardChoice = inputDevice.nextInt();
        }

        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(cardChoice);

        stateCard(newCategory, chosenCard);

        playedCards.addCard(chosenCard);
        return new Object[]{playedCards, newCategory};
    }

    public Object[] takeTurn(CardList playedCards, CardList deck, String category){
        Scanner inputDevice = new Scanner(System.in);
        SupertrumpsCard previouslyPlayedCard = playedCards.getCardAtIndex(playedCards.length()-1);

        if (getHasPassed() == true){
            System.out.println("You have passed - your turn is being skipped. Press enter to continue.");
            inputDevice.nextLine();
            return new Object[]{playedCards, deck, category};
        }

        System.out.println("It's your turn. Pick a card to play: ");

        displayCardList();

        //check to see if player has any cards that can be played - if not then skip turn
        Boolean hasPlayableCard = false;
        for (int i = 0; i < myCards.length(); i++){
            if (!playedCards.getCardAtIndex(playedCards.length()-1).getType().equals("trump")){
                if (!myCards.getCardAtIndex(i).getType().equals("trump")){
                    if (cardHasLowerValue(previouslyPlayedCard, myCards.getCardAtIndex(i), category)){
                        hasPlayableCard = true;
                    }
                }else{
                    if (!myCards.getCardAtIndex(i).getSubtitle().equals(category)){
                        hasPlayableCard = true;
                    }
                }
            }else{
                hasPlayableCard = true;
            }
        }

        if (hasPlayableCard == true){
            System.out.println("You have playable cards - Type the number of the card you wish to play or enter -1 to pass.");
        }else {
            setHasPassed(true);

            if (deck.length() != 0){
                myCards.addCard(deck.takeCardAtIndex(deck.length()-1));
                System.out.println("You don't have any playable cards, you pick up one card from the deck and pass. Press enter to continue.");
            }else {
                System.out.println("You don't have any playable cards, you pass without picking up any cards. Press enter to continue.");
            }

            inputDevice.nextLine();
            return new Object[]{playedCards, deck, category};
        }

        //Get card choice
        int cardChoice = -1;
        Boolean validChoice;
        try {
            System.out.print(">>> ");
            cardChoice = inputDevice.nextInt();
            validChoice = validCardChoice(cardChoice,previouslyPlayedCard, category, true);
        }catch (InputMismatchException e){
            inputDevice.next();
            validChoice = false;
        }

        while (validChoice == false){
            System.out.println("Invalid choice");

            try {
                System.out.print(">>> ");
                cardChoice = inputDevice.nextInt();
                validChoice = validCardChoice(cardChoice,previouslyPlayedCard, category, true);
            }catch (InputMismatchException e){
                inputDevice.next();
                validChoice = false;
            }
        }

        if (cardChoice == -1){
            setHasPassed(true);
            System.out.println("You have chosen to pass. Press enter to continue.");
            inputDevice.nextLine();
            return new Object[]{playedCards, deck, category};
        }

        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(cardChoice);

        if (chosenCard.getType().equals("trump")){
            if (chosenCard.getTitle().equals("The Geologist")){
                System.out.println("You played The Geologist trump card! You're free to choose which category to change into.");
                category = chooseCategory();
            }else{
                category = chosenCard.getSubtitle();
            }
        }
        stateCard(category, chosenCard);
        playedCards.addCard(chosenCard);

        //if chosen card is trump - force player to play another card here
        if (chosenCard.getType().equals("trump")) {
            previouslyPlayedCard = playedCards.getCardAtIndex(playedCards.length()-1);

            if (!hasCards()){
                System.out.println("You have no other cards to play...");
                return new Object[]{playedCards, deck, category};
            }

            System.out.println("You just played a trump card. Please pick another mineral card to play (-1 to pass):");

            displayCardList();

            try{
                System.out.print(">>> ");
                cardChoice = inputDevice.nextInt();
                validChoice = validCardChoice(cardChoice, previouslyPlayedCard, category, false);
            }catch (InputMismatchException e){
                inputDevice.next();
                validChoice = false;
            }

            while (validChoice == false) {
                System.out.println("Invalid choice");

                try{
                    System.out.print(">>> ");
                    cardChoice = inputDevice.nextInt();
                    validChoice = validCardChoice(cardChoice, previouslyPlayedCard, category, false);
                }catch (InputMismatchException e){
                    inputDevice.next();
                    validChoice = false;
                }
            }

            if (cardChoice == -1){
                setHasPassed(true);
                System.out.println("You have chosen to pass. Press enter to continue.");
                inputDevice.nextLine();
                return new Object[]{playedCards, deck, category};
            }

            chosenCard = myCards.takeCardAtIndex(cardChoice);
            stateCard(category, chosenCard);
            playedCards.addCard(chosenCard);
        }

        return new Object[]{playedCards, deck, category};
    }

    private Boolean validCardChoice(int cardChoice, SupertrumpsCard previousCard, String category, Boolean allowTrump){
        if (cardChoice == -1){
            return true;
        }

        if(cardChoice < 0 || cardChoice > myCards.length()-1){
            System.out.println("Selection out of range!");
            return false;
        }

        SupertrumpsCard selectedCard = myCards.getCardAtIndex(cardChoice);

        if (selectedCard.getType().equals("trump")){
            if(selectedCard.getSubtitle().equals(category)){
                System.out.println("We are already playing in the same category as the chosen trump card.");
                return false;
            }
            if (allowTrump){
                return true;
            }else{
                return false;
            }
        }

        if(previousCard.getType().equals("trump")){
            return true;
        }

        if (cardHasLowerValue(previousCard, selectedCard, category)){
            return true;
        }else{
            System.out.println("Please choose a card with a higher value than the one which was just played.");
            return false;
        }
    }

    private String chooseCategory(){
        Scanner inputDevice = new Scanner(System.in);
        System.out.println("Choose a category: \ne) Economic value\na) Crustal abundance\nh) Hardness\n" +
                "c) Cleavage\ns) Specific gravity");
        System.out.print(">>> ");
        String newCategory = inputDevice.next();
        switch (newCategory){
            case "e":
                return "Economic value";
            case "a":
                return "Crustal abundance";
            case "h":
                return "Hardness";
            case "c":
                return "Cleavage";
            case "s":
                return "Specific gravity";
            default:
                System.out.println("Invalid input please try again.");
        }
        while (true){
            System.out.println("Choose a category: \ne) Economic value\na) Crustal abundance\nh) Hardness\n" +
                    "c) Cleavage\ns) Specific gravity");
            System.out.print(">>> ");
            newCategory = inputDevice.next();
            switch (newCategory){
                case "e":
                    return "Economic value";
                case "a":
                    return "Crustal abundance";
                case "h":
                    return "Hardness";
                case "c":
                    return "Cleavage";
                case "s":
                    return "Specific gravity";
                default:
                    System.out.println("Invalid input please try again.");
            }
        }
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

    private void displayCardList(){
        /*
        Display list of held cards to user
         */
        for(int i = 0; i < myCards.length(); i++){
            SupertrumpsCard currentCard = myCards.getCardAtIndex(i);
            System.out.print("Card #" + i + " ");

            if (!myCards.getCardAtIndex(i).getType().equals("trump")) {
                String cardAttributes = String.format("Mineral: %1$-13s Economic value: %2$-10s Crustal abundance: %3$-10s " +
                                "Hardness: %4$-9s Cleavage: %5$-17s Specific gravity: %6$-10s",
                        currentCard.getTitle(), currentCard.getEconomicValue(), currentCard.getCrustalAbundance(),
                        currentCard.getHardness(), currentCard.getCleavage(), currentCard.getSpecificGravity());
                System.out.println(cardAttributes);
            }else{
                String cardAttributes = String.format("Trump card: %1$-16s Category: %2$-15s", currentCard.getTitle(),
                        currentCard.getSubtitle());
                System.out.println(cardAttributes);
            }
        }
    }

    private void stateCard(String category, SupertrumpsCard card){
        System.out.print("You played card " + card.getTitle() + " ");
        if (!card.getType().equals("trump")) {
            if (category.equals("Economic value")) {
                System.out.println("Economic value " + card.getEconomicValue());

            } else if (category.equals("Crustal abundance")) {
                System.out.println("Crustal abundance " + card.getCrustalAbundance());
            } else if (category.equals("Hardness")) {
                System.out.println("Hardness " + card.getHardness());
            } else if (category.equals("Cleavage")) {
                System.out.println("Cleavage " + card.getCleavage());
            } else if (category.equals("Specific gravity")) {
                System.out.println("Specific gravity " + card.getSpecificGravity());
            }
        }else{
            System.out.println(card.getSubtitle());
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

    public Boolean hasCards(){
        if (myCards.length() == 0){
            return false;
        }else{
            return true;
        }
    }

    public Boolean getHasPassed(){
        return hasPassed;
    }
    public void setHasPassed(boolean value){
        hasPassed = value;
    }
    public void setHasFinished(Boolean won){
        hasFinished = true;
        if (!won){
            System.out.print("You have lost all of your cards.");
        }else{
            System.out.print("You have won the game.");
        }
        System.out.println(" The game will continue until there's one loser.");
        setHasPassed(true);
    }
    public Boolean getHasFinished(){
        return hasFinished;
    }

    public String toString(){
        return "Human player - is dealer: " + isDealer.toString() + " has " + myCards.length() + " cards";
    }
}
