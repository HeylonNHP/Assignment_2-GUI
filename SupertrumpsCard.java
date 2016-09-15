import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Heylon2 on 31/08/2016.
 * a Card class already exists - so used SupertrumpsCard instead
 */
public class SupertrumpsCard {
    String fileName;
    String imageName;
    String card_type;
    String title;
    String chemistry;
    String classification;
    String crystal_system;
    String[] occurrence;

//Trump categories
    List hardness;
    List specific_gravity;
    String cleavage;
    String crustal_abundance;
    String economic_value;

    public SupertrumpsCard(Map<String, String> cardDetails){
        if(cardDetails.containsKey("fileName")){
            fileName = cardDetails.get("fileName");
        }
        if(cardDetails.containsKey("imageName")){
            imageName = cardDetails.get("imageName");
        }
        if(cardDetails.containsKey("card_type")){
            card_type = cardDetails.get("card_type");
        }
        if(cardDetails.containsKey("title")){
            title = cardDetails.get("title");
        }
        if(cardDetails.containsKey("chemistry")){
            chemistry = cardDetails.get("chemistry");
        }
        if(cardDetails.containsKey("classification")){
            classification = cardDetails.get("classification");
        }
        if(cardDetails.containsKey("crystal_system")){
            crystal_system = cardDetails.get("crystal_system");
        }
        if(cardDetails.containsKey("occurrence")){
            String arrayString = cardDetails.get("occurrence");
            occurrence = arrayString.split(",");
        }
    }

    public String toString(){
        String occurenceTest = "";
        for(int i = 0; i < occurrence.length; i++){
            occurenceTest += occurrence[i];
        }
        return occurenceTest;
    }
}
