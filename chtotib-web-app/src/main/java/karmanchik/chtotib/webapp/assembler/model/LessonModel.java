package karmanchik.chtotib.webapp.assembler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import karmanchik.chtotib.models.enums.WeekType;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * Представляет web-модель сущности - Lesson
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation("lessons")
@JsonRootName("lesson")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonModel extends RepresentationModel<LessonModel> {
    private Integer id;
    private Integer pairNumber;
    private Integer day;
    private String discipline;
    private String auditorium;
    private WeekType weekType;
    private GroupModel group;
    private List<TeacherModel> teachers;
}
