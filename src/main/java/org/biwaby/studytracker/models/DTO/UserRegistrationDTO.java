package org.biwaby.studytracker.models.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.biwaby.studytracker.models.User}
 */
@Value
public class UserRegistrationDTO implements Serializable {
    Long id;
    String username;
    String password;
}