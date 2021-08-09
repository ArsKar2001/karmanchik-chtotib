package karmanchik.chtotib.telegrambot.services;

import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Lesson;
import karmanchik.chtotib.models.entity.Teacher;
import karmanchik.chtotib.models.repositories.JpaLessonsRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LessonsService {
    private final JpaLessonsRepository lessonsRepository;

    public LessonsService(JpaLessonsRepository lessonsRepository) {
        this.lessonsRepository = lessonsRepository;
    }

    public List<Lesson> findLessons(Group group) {
        if (group == null) {
            return Collections.emptyList();
        }
        return lessonsRepository.findAllByGroup(group);
    }

    public List<Lesson> findLessons(Teacher teacher) {
        if (teacher == null) {
            return Collections.emptyList();
        }
        return lessonsRepository.findByTeacherOrderByPairNumberAsc(teacher);
    }
}
