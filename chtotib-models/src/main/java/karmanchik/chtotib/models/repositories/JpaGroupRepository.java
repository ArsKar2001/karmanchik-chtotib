package karmanchik.chtotib.models.repositories;

import com.sun.istack.NotNull;
import karmanchik.chtotib.models.entity.ChatUser;
import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Lesson;
import karmanchik.chtotib.models.entity.Replacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> getByName(@NotNull String groupName);

    @Query("SELECT g FROM Group g " +
            "WHERE :chatUser member of g.chatUsers")
    Optional<Group> findByChatUser(ChatUser chatUser);

    @Query("SELECT g FROM Group g " +
            "WHERE lower(g.name) LIKE %:academicYearSuffix% " +
            "ORDER BY g.name")
    List<Group> findAllByYearSuffix(String academicYearSuffix);

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.group = :group ")
    List<Lesson> getLessonsByGroup(Group group);

    @Query("SELECT r FROM Replacement r " +
            "WHERE r.group = :group ")
    List<Replacement> getReplacementsByGroup(Group group);

    Boolean existsByName(@NotNull String name);
}
