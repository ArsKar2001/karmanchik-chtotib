package karmanchik.chtotib.restservice.utils;


import karmanchik.chtotib.entityservice.entity.Group;
import karmanchik.chtotib.entityservice.entity.Teacher;
import karmanchik.chtotib.restservice.assembler.model.GroupModel;
import karmanchik.chtotib.restservice.assembler.model.TeacherModel;
import karmanchik.chtotib.restservice.rest.GroupController;
import karmanchik.chtotib.restservice.rest.TeacherController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Класс - помощник. Реальзует статические методы для преобразования сущностей в RepresentationModel
 */
public class ModelHelperUtils {
    private ModelHelperUtils() { }

    /**
     * Статический метод-фабрика для преобразования коллекции сущностей-Teachers в коллекцию RepresentationModel
     *
     * @param teachers Коллекция @see Teachers
     * @return
     */
    public static List<TeacherModel> toTeachersModel(List<Teacher> teachers) {
        if (teachers.isEmpty()) {
            return Collections.emptyList();
        }
        return teachers.stream()
                .map(teacher -> TeacherModel.builder()
                        .id(teacher.getId())
                        .name(teacher.getName())
                        .build()
                        .add(linkTo(methodOn(TeacherController.class)
                                .get(teacher.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    public static GroupModel toGroupModel(Group group) {
        return GroupModel.builder()
                .id(group.getId())
                .name(group.getName())
                .build()
                .add(linkTo(methodOn(GroupController.class)
                        .get(group.getId()))
                        .withSelfRel());
    }
}
