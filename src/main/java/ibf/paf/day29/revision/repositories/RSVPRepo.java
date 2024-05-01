package ibf.paf.day29.revision.repositories;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ibf.paf.day29.revision.models.AggregatedRSVP;
import ibf.paf.day29.revision.models.RSVP;
import static ibf.paf.day29.revision.utils.Constants.*;
import ibf.paf.day29.revision.utils.Queries;

@Repository
public class RSVPRepo implements Queries {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    // get all OR search by string
    public List<RSVP> getAllRSVP(String q) {
        List<RSVP> rsvps = new LinkedList<>();
        SqlRowSet rs = null;
        if (q.isEmpty()) {
            rs = jdbcTemplate.queryForRowSet(SQL_GET_ALL_RSVP);
        } else {
            String _q = "%" + q + "%";
            rs = jdbcTemplate.queryForRowSet(SQL_GET_RSVP_BY_NAME, _q);
        }
        while (rs.next()) {
            rsvps.add(RSVP.toRSVP(rs));
        }
        return rsvps;
    }

    public RSVP getRSVPByEmail(String email) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_GET_RSVP_BY_EMAIL, email);
        while (rs.next()) {
            return RSVP.toRSVP(rs);
        }
        return null;
    }

    public RSVP insertNewRSVP(RSVP rsvp) {
        GeneratedKeyHolder keyholder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT_NEW_RSVP, Statement.RETURN_GENERATED_KEYS); 
                ps.setString(1, rsvp.getName());
                ps.setString(2, rsvp.getEmail());
                ps.setString(3, rsvp.getPhone());
                ps.setDate(4, rsvp.getConfirmationDate());
                ps.setString(5, rsvp.getComments());
                ps.setString(6, rsvp.getFoodType());
                return ps;
            }, keyholder);
            BigInteger primaryKeyValue = (BigInteger) keyholder.getKey();
            rsvp.setCustomerId(primaryKeyValue.intValue());
            if (rsvp.getCustomerId() > 0) {
                // set fields to null to omit fields in mongo
                // rsvp.setConfirmationDate(null)
                // inserts if objectId is not present, updates if objectId is present
                mongoTemplate.save(rsvp, C_RSVP);
            }
        } catch (DataIntegrityViolationException dive) {
            // Get existing record, update rsvp object
            RSVP existingRSVP = getRSVPByEmail(rsvp.getEmail());
            rsvp.setName(existingRSVP.getName());
            rsvp.setEmail(existingRSVP.getEmail());
            rsvp.setPhone(existingRSVP.getPhone());
            rsvp.setConfirmationDate(existingRSVP.getConfirmationDate());
            rsvp.setComments(existingRSVP.getComments());
            rsvp.setFoodType(existingRSVP.getFoodType());
            rsvp.setCustomerId(existingRSVP.getCustomerId());
            Boolean isUpdated = updateRSVP(rsvp);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> isUpdated: " + isUpdated);
            
        }
        return rsvp;
    }

    public Boolean updateRSVP(RSVP rsvp) {
        Integer updatedCount = jdbcTemplate.update(SQL_UPDATE_RSVP, rsvp.getName(), rsvp.getEmail(), rsvp.getPhone(), new Timestamp(rsvp.getConfirmationDate().getTime()), rsvp.getComments(), rsvp.getFoodType(), rsvp.getEmail());
        if (updatedCount > 0 ) {
            // set fields to null to omit fields in mongo
            // rsvp.setConfirmationDate(null)
            rsvp.setCustomerId(getRSVPByEmail(rsvp.getEmail()).getCustomerId());
            mongoTemplate.save(rsvp, C_RSVP);
            return true;
        }
        return false;
    }

    public Integer countAllRSVP() {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_COUNT_ALL_RSVP);
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }

    // Aggregration of food_type with MongoDB
    // db.rsvp.aggregate([
    // {$group: {
    //     _id: "$foodType",
    //     count: {$sum: 1},
    //     name: {$push: "$name"}
    //     }},
    //    {$sort: {count -1}}
    // ])
    public List<AggregatedRSVP> aggregateByFoodType() {
        GroupOperation groupByFoodType = Aggregation.group(F_FOODTYPE)
            .push(F_NAME).as("name")
            .count().as("count");
        SortOperation sortByCount = Aggregation.sort(Sort.by(Direction.DESC, "count"));
        Aggregation pipeline = Aggregation.newAggregation(groupByFoodType, sortByCount);
        return mongoTemplate.aggregate(pipeline, C_RSVP ,AggregatedRSVP.class).getMappedResults();
        
    }
    
}
