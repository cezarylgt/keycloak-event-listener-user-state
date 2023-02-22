package com.cezarylgt.keycloak.userstate;

import lombok.Data;
import org.keycloak.models.UserModel;

/**
 * @author <a href="https://github.com/cezarylgt">Cezary Michałkiewicz</a>
 */
@Data
public  class UserModelDto {
    private String id;
    private String username;
    private Long createdTimestamp;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;

    public UserModelDto(UserModel userModel) {
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.createdTimestamp = userModel.getCreatedTimestamp();
        this.enabled = userModel.isEnabled();
        this.firstName = userModel.getFirstName();
        this.lastName = userModel.getLastName();
        this.email = userModel.getEmail();

    }


}
