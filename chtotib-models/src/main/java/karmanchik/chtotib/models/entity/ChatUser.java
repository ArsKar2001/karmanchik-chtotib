package karmanchik.chtotib.models.entity;

import karmanchik.chtotib.models.enums.BotState;
import karmanchik.chtotib.models.enums.Role;
import karmanchik.chtotib.models.enums.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "chat_users")
@EqualsAndHashCode(callSuper = true)
public class ChatUser extends BaseEntity {
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "bot_state_id")
    private BotState botState;

    @Column(name = "user_state_id")
    private UserState userState;

    @Column(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_group",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)})
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_teacher",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)})
    private Teacher teacher;

    public ChatUser() {}

    @Override
    public String toString() {
        return "ChatUser{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", botState=" + botState +
                ", userState=" + userState +
                ", role=" + role +
                '}';
    }
}
