package karmanchik.chtotib.entityservice.repositories;

import karmanchik.chtotib.entityservice.entity.Group;
import karmanchik.chtotib.entityservice.entity.Lesson;
import karmanchik.chtotib.entityservice.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface JpaLessonsRepository extends JpaRepository<Lesson, Integer> {
    @Query("SELECT l FROM Lesson l " +
            "WHERE :teacher member of l.teachers " +
            "ORDER BY l.pairNumber ASC")
    List<Lesson> findByTeacherOrderByPairNumberAsc(Teacher teacher);

    List<Lesson> findByGroup(Group group);

    List<Lesson> findAllByGroup(Group group);

    List<Lesson> findAllByDayOrderByPairNumber(Integer day);
}
