/**
 * Created by Heylon2 on 24/08/2016.
 */
import java.util.Scanner;
public class MineralSuperTrumpsGame {
    public static void main(String[] args){
        final String MENU = "Please choose an option:\ns) Start game\nq) Quit\n>>> ";
        Scanner inputDevice = new Scanner(System.in);
        System.out.println("Welcome to mineral super trumps");

        System.out.print(MENU);
        String userInput = inputDevice.next();
        while (!userInput.toLowerCase().equals("q")){
            switch (userInput.toLowerCase()){
                case "s":
                    System.out.print("Number of players (including you) (3-5) ");
                    int players = inputDevice.nextInt();
                    startGame(players);
                    break;
            }
            System.out.print(MENU);
            userInput = inputDevice.next();
        }
    }
    public static void startGame(int players){

    }
}
