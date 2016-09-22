import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

/**
 * Created by Heylon2 on 20/09/2016.
 */
public class HumanPlayer {
    private CardList myCards = new CardList();
    private Boolean isDealer;

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
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Economic value");
        categories.add("Crustal abundance");
        categories.add("Hardness");
        categories.add("Cleavage");
        categories.add("Specific gravity");

        Scanner inputDevice = new Scanner(System.in);

        System.out.println("You're starting the game");
        System.out.println("Your cards are: ");

        displayCardList();

        System.out.println("Choose a category");
        String categoryChoice = inputDevice.next();
        while (!categories.contains(categoryChoice)){
            System.out.println("Incorrect input, please try again\n Choose a category");
            categoryChoice = inputDevice.next();
        }

        System.out.println("Choose a card");
        int cardChoice = inputDevice.nextInt();
        while (cardChoice < 0 || cardChoice > myCards.length()-1){
            System.out.println("Out of range\nChoose a card");
            cardChoice = inputDevice.nextInt();
        }

        SupertrumpsCard chosenCard = myCards.takeCardAtIndex(cardChoice);

        stateCard(categoryChoice, chosenCard);

        playedCards.addCard(chosenCard);
        return new Object[]{playedCards, categoryChoice};
    }

    public Object[] takeTurn(CardList playedCards, CardList deck, String category){
        Scanner inputDevice = new Scanner(System.in);
        SupertrumpsCard previouslyPlayedCard = playedCards.getCardAtIndex(playedCards.length()-1);
        System.out.println("It's your turn. Pick a card to play: ");

        displayCardList();

        //Add check to see if player has any cards that can be played - if not allow them to pass
        Boolean hasPlayableCard = false;
        for (int i = 0; i < myCards.length(); i++){
            if (!myCards.getCardAtIndex(i).getType().equals("trump")){
                if (cardHasLowerValue(previouslyPlayedCard, myCards.getCardAtIndex(i), category)){
                    hasPlayableCard = true;
                }
            }else{
                hasPlayableCard = true;
            }

        }
        if (hasPlayableCard = true){
            System.out.println("You have playable cards");
        }
        //Get card choice
        int cardChoice = inputDevice.nextInt();
        while (cardChoice < 0 || cardChoice > myCards.length()-1){
            System.out.println("Out of range\nChoose a card");
            cardChoice = inputDevice.nextInt();
        }

        return new Object[]{};
    }

    private Boolean cardHasLowerValue(SupertrumpsCard card1, SupertrumpsCard card2, String category){
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
        for(int i = 0; i < myCards.length(); i++){
            SupertrumpsCard currentCard = myCards.getCardAtIndex(i);
            System.out.print("Card #" + (i + 1) + " ");

            if (!myCards.getCardAtIndex(i).getType().equals("trump")) {
                System.out.print("Mineral: " + currentCard.getTitle() + " ");
                System.out.print("Economic value: " + currentCard.getEconomicValue() + " ");
                System.out.print("Crustal abundance: " + currentCard.getCrustalAbundance() + " ");
                System.out.print("Hardness: " + currentCard.getHardness() + " ");
                System.out.print("Cleavage: " + currentCard.getCleavage() + " ");
                System.out.println("Specific gravity: " + currentCard.getSpecificGravity());
            }else{
                System.out.println("Trump card: " + currentCard.getSubtitle());
            }
        }
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
