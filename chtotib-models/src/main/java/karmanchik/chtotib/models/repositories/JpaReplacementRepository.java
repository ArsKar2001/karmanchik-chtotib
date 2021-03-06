package karmanchik.chtotib.models.repositories;

import com.sun.istack.NotNull;
import karmanchik.chtotib.models.entity.Group;
import karmanchik.chtotib.models.entity.Replacement;
import karmanchik.chtotib.models.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface JpaReplacementRepository extends JpaRepository<Replacement, Integer> {
    Optional<Replacement> getByGroup(Group group);

    Optional<Replacement> getByGroupAndDate(Group group, @NotNull LocalDate date);

    List<Replacement> findByGroupAndDateOrderByDateAscPairNumberAsc(Group group, LocalDate date);

    List<Replacement> findByGroupOrderByPairNumber(Group group);

    @Query("SELECT r FROM Replacement r " +
            "WHERE :teacher member of r.teachers AND r.date = :date " +
            "ORDER BY r.date, r.pairNumber")
    List<Replacement> findByTeacherAndDateOrderByDateAscPairNumberAsc(@Param("teacher") Teacher teacher, LocalDate date);

    List<Replacement> findAllByDateOrderByPairNumber(LocalDate date);
}
