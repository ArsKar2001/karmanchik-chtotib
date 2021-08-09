package karmanchik.chtotib.telegrambot.services;

import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Teacher;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.models.repositories.JpaTeacherRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TeacherService {
    private final JpaTeacherRepository teacherRepository;

    public TeacherService(JpaTeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher findTeacher(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException(teacherId, Teacher.class));
    }

    public List<Teacher> findTeachersByNameLike(String name) {
        if (name.isEmpty()) {
            return Collections.emptyList();
        } else {
            return teacherRepository.findAllByName(name);
        }
    }

    public Teacher findTeacher(ChatUser chatUser) {
        return teacherRepository.findByChatUser(chatUser)
                .orElseThrow();
    }
}
