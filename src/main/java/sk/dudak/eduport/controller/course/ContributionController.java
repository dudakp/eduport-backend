package sk.dudak.eduport.controller.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.dudak.eduport.model.course.Contribution;
import sk.dudak.eduport.service.course.ContributionService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/v1/contributions")
public class ContributionController {

    private final ContributionService contributionService;

    @Autowired
    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @GetMapping(value = "/byUsername")
    public List<Contribution> getAll(@RequestParam String username) {
        return this.contributionService.getAllForUser(username);
    }

    @GetMapping(value = "/byCourseName")
    public ResponseEntity<List<Contribution>> getContributionsByCourse(@RequestParam String courseName) throws Exception {
        return ok(this.contributionService.getAllForCourse(courseName));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<Contribution>> getAllContributions() {
        return ok(this.contributionService.getAll());
    }

    @GetMapping(value = "/byId")
    public ResponseEntity<List<Contribution>> getAllById(@RequestParam long id) {
        return ok(this.contributionService.getAllById(id));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity addContribution(@RequestBody Contribution contribution, @RequestParam String courseName) throws Exception {
        if (this.contributionService.addContribution(contribution, courseName))
            return new ResponseEntity(HttpStatus.OK);
        else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity deleteContribution(@RequestParam String title) throws Exception {
        if (this.contributionService.deleteContribution(title))
            return new ResponseEntity(HttpStatus.OK);
        else {
            throw new Exception("Constrinution does not exists!");
        }
    }

}
