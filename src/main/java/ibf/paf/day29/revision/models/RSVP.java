package ibf.paf.day29.revision.models;

import java.io.StringReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class RSVP {
    
    @Id
    private Integer customerId;
    private String name;
    private String email;
    private String phone;
    private Date confirmationDate;
    private String comments;
    private String foodType;

    

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }


    @Override
    public String toString() {
        return "RSVP [customerId=" + customerId + ", name=" + name + ", email=" + email + ", phone=" + phone
                + ", confirmationDate=" + confirmationDate + ", comments=" + comments + ", foodType=" + foodType + "]";
    }

    // RowSet to RSVP object - get from SQL
    public static RSVP toRSVP(SqlRowSet rs) {
        
        RSVP rsvp = new RSVP();
        rsvp.setCustomerId(rs.getInt("customer_id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        rsvp.setConfirmationDate(rs.getDate("confirmation_date"));
        rsvp.setComments(rs.getString("comments"));
        rsvp.setFoodType(rs.getString("food_type"));
        return rsvp;
    }

    public static List<RSVP> toRSVPs(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        List<RSVP> rsvps = reader.readArray()
            .stream()
            .map(data -> {
                RSVP rsvp = new RSVP();
                JsonObject jo = data.asJsonObject();
                rsvp.setCustomerId(jo.getInt("customer_id"));
                rsvp.setName(jo.getString("name"));
                rsvp.setEmail(jo.getString("email"));
                rsvp.setPhone(jo.getString("phone"));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date juDate;
                try {
                    juDate = sdf.parse(jo.getString("confirmation_date"));
                    rsvp.setConfirmationDate(new Date(juDate.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                rsvp.setComments(jo.getString("comments"));
                rsvp.setFoodType(jo.getString("food_type"));
                return rsvp;
            }).toList();
        return rsvps;
    }

    // RSVP to JsonObject - insert into SQL
    public static JsonObject toJson(RSVP rsvp) {
        return Json.createObjectBuilder()
            .add("customer_id", rsvp.getCustomerId())
            .add("name", rsvp.getName())
            .add("email", rsvp.getEmail())
            .add("phone", rsvp.getPhone())
            .add("confirmation_date", rsvp.getConfirmationDate().toString())
            .add("comments", rsvp.getComments())
            .add("food_type", rsvp.getFoodType())
            .build();
    }
}
