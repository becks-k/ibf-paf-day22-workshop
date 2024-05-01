package ibf.paf.day29.revision.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class AggregatedRSVP {

    @Field("_id")
    private String foodType;
    private Integer count;
    private List<String> name;
    
    public String getFoodType() {
        return foodType;
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public List<String> getName() {
        return name;
    }
    public void setName(List<String> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AggregatedRSVP [foodType=" + foodType + ", count=" + count + ", name=" + name + "]";
    }

    public static JsonObject toJson(AggregatedRSVP arsvp) {
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (String n : arsvp.getName()) {
            jsonArray.add(n);
        }
        return Json.createObjectBuilder()
            .add("food_type", arsvp.getFoodType())
            .add("count", arsvp.getCount())
            .add("name", jsonArray.build())
            .build();
    }
}
