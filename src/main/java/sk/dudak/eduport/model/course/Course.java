package sk.dudak.eduport.model.course;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.dudak.eduport.model.user.User;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String abbreviation;
    private String title;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Contribution> contributions;

    @ManyToMany(mappedBy = "coursesEnrolled", cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JsonBackReference
    private List<User> users;

    public boolean addContrib(Contribution contribution) {
        return this.contributions.add(contribution);
    }
//
//    @Override
//    public String toString() {
//        return "Course{ }";
//    }
}
