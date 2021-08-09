package karmanchik.chtotib.telegrambot.services;

import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Replacement;
import karmanchik.chtotib.models.entity.Teacher;
import karmanchik.chtotib.models.repositories.JpaReplacementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReplacementService {
    private final JpaReplacementRepository replacementRepository;

    public ReplacementService(JpaReplacementRepository replacementRepository) {
        this.replacementRepository = replacementRepository;
    }

    public List<Replacement> findReplacements(Group group, LocalDate date) {
        if (group == null) {
            return replacementRepository.findAllByDateOrderByPairNumber(date);
        }
        if (date == null) {
            return replacementRepository.findByGroupOrderByPairNumber(group);
        }
        return replacementRepository.findByGroupAndDateOrderByDateAscPairNumberAsc(group, date);
    }

    public List<Replacement> findReplacements(Teacher teacher, LocalDate date) {
        if (teacher == null) {
            return replacementRepository.findAllByDateOrderByPairNumber(date);
        }
        return replacementRepository.findByTeacherAndDateOrderByDateAscPairNumberAsc(teacher, date);
    }
}
