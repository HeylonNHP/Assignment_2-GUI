import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Heylon2 on 31/08/2016.
 * a Card class already exists - so used SupertrumpsCard instead
 */
public class SupertrumpsCard {

    Map<String,String> cardAttributes;

    public SupertrumpsCard(Map<String, String> cardDetails){
        cardAttributes = cardDetails;
    }

    public SupertrumpsCard clone(){
        return new SupertrumpsCard(cardAttributes);
    }

    public String getType(){
        return cardAttributes.get("card_type");
    }

    public String getTitle(){
        return cardAttributes.get("title");
    }

    public String getChemistry(){
        return cardAttributes.get("chemistry");
    }

    public String getClassification(){
        return cardAttributes.get("classification");
    }

    public String getCrystalSystem(){
        return cardAttributes.get("crystal_system");
    }

    public String[] getOccurrence(){
        return cardAttributes.get("occurrence").split(",");
    }

    public String getHardness(){
        return cardAttributes.get("hardness");
    }

    public String getSpecificGravity(){
        return cardAttributes.get("specific_gravity");
    }

    public String getCleavage(){
        return cardAttributes.get("cleavage");
    }

    public String getEconomicValue(){
        return cardAttributes.get("economic_value");
    }

    public String getCrustalAbundance(){
        return cardAttributes.get("crustal_abundance");
    }

    public String getSubtitle(){
        return cardAttributes.get("subtitle");
    }

    public String toString(){
        String occurenceTest = "";
        if(cardAttributes.containsKey("occurrence")){
            for(int i = 0; i < cardAttributes.get("occurrence").split(",").length; i++){
                occurenceTest += cardAttributes.get("occurrence").split(",")[i];
            }
        }


        return occurenceTest;
    }
}
