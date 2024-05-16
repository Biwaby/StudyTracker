package org.biwaby.studytracker.models.DTO;

import lombok.Value;
import org.biwaby.studytracker.models.Role;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link org.biwaby.studytracker.models.User}
 */
@Value
public class UserDTO implements Serializable {
    Long id;
    String username;
    boolean enabled;
    Set<Role> roles;
}