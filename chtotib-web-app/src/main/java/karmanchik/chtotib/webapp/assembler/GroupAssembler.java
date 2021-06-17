package karmanchik.chtotib.webapp.assembler;

import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.webapp.assembler.model.GroupModel;
import karmanchik.chtotib.webapp.rest.GroupController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GroupAssembler extends RepresentationModelAssemblerSupport<Group, GroupModel> {

    public GroupAssembler() {
        super(GroupController.class, GroupModel.class);
    }

    @Override
    public GroupModel toModel(Group entity) {
        GroupModel groupModel = this.instantiateModel(entity)
                .add(linkTo(methodOn(GroupController.class)
                        .get(entity.getId()))
                        .withSelfRel())
                .add(linkTo(methodOn(GroupController.class).getAll())
                        .withRel("groups"))
                .add(linkTo(methodOn(GroupController.class).getLessons(entity.getId()))
                        .withRel("lessons"))
                .add(linkTo(methodOn(GroupController.class).getReplacements(entity.getId()))
                        .withRel("replacements"));
        groupModel.setName(entity.getName());
        groupModel.setId(entity.getId());
        return groupModel;
    }

    @Override
    public CollectionModel<GroupModel> toCollectionModel(Iterable<? extends Group> entities) {
        return super.toCollectionModel(entities)
                .add(linkTo(methodOn(GroupController.class).getAll()).withSelfRel());
    }
}
