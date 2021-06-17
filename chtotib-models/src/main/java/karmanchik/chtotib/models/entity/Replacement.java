package karmanchik.chtotib.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "replacement")
@EqualsAndHashCode(callSuper = true)
public class Replacement extends BaseEntity {
    @Column(name = "pair_number")
    private String pairNumber;

    @Column(name = "date_value")
    private LocalDate date;

    @Column(name = "auditorium", nullable = false)
    private String auditorium;

    @Column(name = "discipline", nullable = false)
    private String discipline;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    public Replacement() {

    }

    @Override
    public String toString() {
        return "Replacement{" +
                "id=" + id +
                ", pairNumber=" + pairNumber +
                ", date=" + date +
                ", auditorium='" + auditorium + '\'' +
                ", discipline='" + discipline + '\'' +
                ", group=" + group +
                ", teachers=" + teachers +
                '}';
    }
}
