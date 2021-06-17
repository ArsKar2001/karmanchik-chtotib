package karmanchik.chtotib.webapp.rest;

import karmanchik.chtotib.models.entity.User;
import karmanchik.chtotib.models.exception.ResourceNotFoundException;
import karmanchik.chtotib.models.repositories.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Log4j2
@RestController("loginController")
@RequestMapping("api")
@RequiredArgsConstructor
public class LoginController {
    private final JpaUserRepository userRepository;

    @GetMapping("/login")
    private ResponseEntity<?> login(@RequestBody User user) {
        return userRepository.getByLoginAndPassword(user.getLogin(), user.getPassword())
                .map(user1 -> ResponseEntity.ok("OK"))
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с таким логином или паролем не найден."));
    }
}
