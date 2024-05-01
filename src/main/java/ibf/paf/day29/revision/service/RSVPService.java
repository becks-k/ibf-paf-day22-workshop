package ibf.paf.day29.revision.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf.paf.day29.revision.models.AggregatedRSVP;
import ibf.paf.day29.revision.models.RSVP;
import ibf.paf.day29.revision.repositories.RSVPRepo;

@Service
public class RSVPService {
    
    @Autowired
    private RSVPRepo rsvpRepo;

    public List<RSVP> getAllRSVP(String q) {
        return rsvpRepo.getAllRSVP(q);
    }

    public RSVP getRSVPByEmail(String email) {
        return rsvpRepo.getRSVPByEmail(email);
    }

    public RSVP insertNewSVP(RSVP rsvp) {
        return rsvpRepo.insertNewRSVP(rsvp);
    }

    public Boolean updateRSVP(RSVP rsvp) {
        return rsvpRepo.updateRSVP(rsvp);
    }

    public Integer countAllRSVP() {
        return rsvpRepo.countAllRSVP();
    }

    public List<AggregatedRSVP> aggregateByFoodType() {
        return rsvpRepo.aggregateByFoodType();
    }
    
}
