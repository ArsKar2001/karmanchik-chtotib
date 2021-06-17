package karmanchik.chtotib.models.entity;

import karmanchik.chtotib.models.enums.WeekType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "lessons")
@EqualsAndHashCode(callSuper = true)
public class Lesson extends BaseEntity {
    @Column(name = "pair_number")
    private Integer pairNumber;

    @Column(name = "day")
    private Integer day;

    @Column(name = "auditorium", nullable = false)
    private String auditorium;

    @Column(name = "discipline", nullable = false)
    private String discipline;

    @Column(name = "week_type")
    private WeekType weekType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    public Lesson() {

    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", pairNumber=" + pairNumber +
                ", day=" + day +
                ", auditorium='" + auditorium + '\'' +
                ", discipline='" + discipline + '\'' +
                ", weekType=" + weekType +
                ", group=" + group +
                ", teachers=" + teachers +
                '}';
    }
}
