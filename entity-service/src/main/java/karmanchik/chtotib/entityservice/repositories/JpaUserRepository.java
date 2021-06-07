package karmanchik.chtotib.entityservice.repositories;

import com.sun.istack.NotNull;
import karmanchik.chtotib.entityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByLoginAndPassword(@NotNull String login, @NotNull String password);
}
