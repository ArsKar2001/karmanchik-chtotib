package karmanchik.chtotib.entityservice.entity;

import karmanchik.chtotib.entityservice.entity.converter.PasswordDecodeEncodeConverter;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "login_unique", columnNames = "login"),
                @UniqueConstraint(name = "password_unique", columnNames = "password")
        })
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
        @Column(name = "login")
        private String login;

        @Column(name = "password")
        @Convert(converter = PasswordDecodeEncodeConverter.class)
        private String password;
}
