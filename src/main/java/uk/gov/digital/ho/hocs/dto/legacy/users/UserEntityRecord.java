package uk.gov.digital.ho.hocs.dto.legacy.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.model.User;

@AllArgsConstructor
@Getter
public class UserEntityRecord {

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    public static UserEntityRecord create(User user) {
        return new UserEntityRecord(user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmailAddress());
    }
}
