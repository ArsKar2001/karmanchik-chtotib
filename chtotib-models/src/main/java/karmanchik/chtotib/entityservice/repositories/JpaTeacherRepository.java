package karmanchik.chtotib.entityservice.repositories;


import com.sun.istack.NotNull;
import karmanchik.chtotib.entityservice.entity.ChatUser;
import karmanchik.chtotib.entityservice.entity.Lesson;
import karmanchik.chtotib.entityservice.entity.Replacement;
import karmanchik.chtotib.entityservice.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface JpaTeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> getByName(@NotNull String name);

    @Query("SELECT g FROM Teacher g " +
            "WHERE :chatUser member of g.chatUsers")
    Optional<Teacher> findByChatUser(ChatUser chatUser);

    @Query("SELECT t FROM Teacher t " +
            "WHERE lower(t.name) LIKE %:name% " +
            "ORDER BY t.name")
    List<Teacher> findAllByName(String name);

    @Query("SELECT l FROM Lesson l " +
            "WHERE :teacher member of l.teachers")
    List<Lesson> getLessonsByTeacher(Teacher teacher);

    @Query("SELECT l FROM Replacement l " +
            "WHERE :teacher member of l.teachers")
    List<Replacement> getReplacementsByTeacher(Teacher teacher);

    boolean existsByName(String name);
}
