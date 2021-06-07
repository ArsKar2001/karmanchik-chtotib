package karmanchik.chtotib.entityservice.entity;

import karmanchik.chtotib.entityservice.models.BaseModel;
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
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true)
public class Teacher extends BaseEntity implements BaseModel {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<ChatUser> chatUsers;

    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "teachers")
    private List<Replacement> replacements;

    public Teacher() {

    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
