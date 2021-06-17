package karmanchik.chtotib.models.entity;

import karmanchik.chtotib.models.models.BaseModel;
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
@Table(name = "groups")
@EqualsAndHashCode(callSuper = true)
public class Group extends BaseEntity implements BaseModel {
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "group")
    private List<ChatUser> chatUsers;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Replacement> replacements;

    public Group() { }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
