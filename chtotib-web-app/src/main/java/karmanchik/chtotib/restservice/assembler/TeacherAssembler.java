package karmanchik.chtotib.restservice.assembler;

import karmanchik.chtotib.entityservice.entity.Teacher;
import karmanchik.chtotib.restservice.assembler.model.TeacherModel;
import karmanchik.chtotib.restservice.rest.TeacherController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TeacherAssembler extends RepresentationModelAssemblerSupport<Teacher, TeacherModel> {
    public TeacherAssembler() {
        super(TeacherController.class, TeacherModel.class);
    }

    @Override
    public TeacherModel toModel(Teacher entity) {
        TeacherModel teacherModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(TeacherController.class).get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(TeacherController.class).getAll())
                        .withRel("teachers"))
                .add(linkTo(methodOn(TeacherController.class).getLessons(entity.getId()))
                        .withRel("lessons"))
                .add(linkTo(methodOn(TeacherController.class).getReplacements(entity.getId()))
                        .withRel("replacements"));;
        teacherModel.setId(entity.getId());
        teacherModel.setName(entity.getName());
        return teacherModel;
    }

    @Override
    public CollectionModel<TeacherModel> toCollectionModel(Iterable<? extends Teacher> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(TeacherController.class).getAll()).withSelfRel());
    }
}
