package karmanchik.chtotib.telegrambot.services;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.models.repositories.JpaGroupRepository;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    private final JpaGroupRepository groupRepository;

    public GroupService(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group findGroup(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException(groupId, Group.class));
    }

    public Group findGroup(ChatUser chatUser) {
        return groupRepository.findByChatUser(chatUser)
                .orElseThrow();
    }
}
