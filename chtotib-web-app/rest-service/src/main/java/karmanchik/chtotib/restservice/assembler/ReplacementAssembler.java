package karmanchik.chtotib.restservice.assembler;

import karmanchik.chtotib.entityservice.entity.Replacement;
import karmanchik.chtotib.restservice.utils.ModelHelperUtils;
import karmanchik.chtotib.restservice.assembler.model.ReplacementModel;
import karmanchik.chtotib.restservice.rest.ReplacementController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReplacementAssembler extends RepresentationModelAssemblerSupport<Replacement, ReplacementModel> {
    public ReplacementAssembler() {
        super(ReplacementController.class, ReplacementModel.class);
    }

    @Override
    public ReplacementModel toModel(Replacement entity) {
        ReplacementModel replacementModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(ReplacementController.class).get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(ReplacementController.class).getAll())
                        .withRel("replacements"));
        replacementModel.setId(entity.getId());
        replacementModel.setGroup(ModelHelperUtils.toGroupModel(entity.getGroup()));
        replacementModel.setTeachers(ModelHelperUtils.toTeachersModel(entity.getTeachers()));
        replacementModel.setDate(entity.getDate());
        replacementModel.setDiscipline(entity.getDiscipline());
        replacementModel.setAuditorium(entity.getAuditorium());
        replacementModel.setPairNumber(entity.getPairNumber());
        return replacementModel;
    }

    @Override
    public CollectionModel<ReplacementModel> toCollectionModel(Iterable<? extends Replacement> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(ReplacementController.class).getAll()).withSelfRel());
    }
}
