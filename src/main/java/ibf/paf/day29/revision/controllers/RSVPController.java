package ibf.paf.day29.revision.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import ibf.paf.day29.revision.models.RSVP;

@Controller
@RequestMapping("/api/v1")
public class RSVPController {

    @Value("${rest.url.get}")
    private String getUrl;

    @GetMapping("/rsvps")
    public ModelAndView getAllRSVP() {
        ModelAndView mav = new ModelAndView();
        List<RSVP> rsvps = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.getForEntity(getUrl,String.class);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + resp.getStatusCode().value());
        if (resp.getStatusCode().value() == 200) {
            rsvps = RSVP.toRSVPs(resp.getBody());
        } 
        mav.setViewName("list");
        mav.addObject("rsvps", rsvps);
        return mav;
    }
    
}
