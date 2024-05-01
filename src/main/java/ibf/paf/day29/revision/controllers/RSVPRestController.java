package ibf.paf.day29.revision.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf.paf.day29.revision.models.AggregatedRSVP;
import ibf.paf.day29.revision.models.RSVP;
import ibf.paf.day29.revision.service.RSVPService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@RestController
@RequestMapping("/api")
public class RSVPRestController {
    
    @Autowired
    private RSVPService rsvpService;

    @GetMapping(path = "/rsvps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllRSVP(@RequestParam(value = "q", defaultValue = "", required = false) String q) {
        System.out.printf(">>>>>>>>>>>>>>>> q: %s\n", q);
        
        List<RSVP> rsvps = rsvpService.getAllRSVP(q);
        Integer total = rsvpService.countAllRSVP();
        if (rsvps.isEmpty() && total == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'error_message': 'No RSVP records'}");
        } else if (rsvps.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{'error_message': 'RSVP not found'}");
        }
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (RSVP rsvp : rsvps) {
            jsonArray.add(RSVP.toJson(rsvp));
        }
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonArray.build().toString());
    }

    @PostMapping(path = "/rsvp", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertRSVP(@RequestBody @ModelAttribute RSVP rsvp) {
        System.out.printf(">>>>>>>>>>>>>>>> rsvp: %s\n", rsvp.toString());

        // if (rsvp == null) {
        //     return ResponseEntity
        //         .status(HttpStatus.NOT_FOUND)
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .body("Invalid RSVP");
        // }

        RSVP insertedRSVP = rsvpService.insertNewSVP(rsvp);
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(String.format("Success! RSVP id is: %s", insertedRSVP.getCustomerId()));
    }

    @PutMapping(path = "/rsvp/{email}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRSVP(@PathVariable(name="email") String email, @RequestBody @ModelAttribute RSVP rsvp) {
        System.out.printf(">>>>>>>>>>>>>>>> rsvp: %s\n", rsvp.toString());

        RSVP existingRSVP = rsvpService.getRSVPByEmail(email);
        if (existingRSVP == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("Email not found");
        }
        rsvpService.updateRSVP(rsvp);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(String.format("RSVP successfully updated"));
    }

    @GetMapping(path = "/rsvps/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> countRSVP() {
        Integer total = rsvpService.countAllRSVP();
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(String.format("Total RSVP is: %d", total));
    }

    @GetMapping(path = "/rsvps/foodtype", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> aggregateByFoodType() {
        List<AggregatedRSVP> results = rsvpService.aggregateByFoodType();
        if (results.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("No RSVP records found");
        }
        JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (AggregatedRSVP arsvp : results) {
            jsonArray.add(AggregatedRSVP.toJson(arsvp));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(jsonArray.build().toString());
    }
}
