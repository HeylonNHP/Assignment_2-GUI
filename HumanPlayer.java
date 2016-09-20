/**
 * Created by Heylon2 on 20/09/2016.
 */
public class HumanPlayer {
    private Boolean isDealer;
    public HumanPlayer(){
        isDealer = false;
    }
    public HumanPlayer(Boolean dealer){
        isDealer = dealer;
    }
    public String toString(){
        return "Human player - is dealer: " + isDealer.toString();
    }
}
