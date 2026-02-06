package com.api.spring_marketplace_api.service;

import com.api.spring_marketplace_api.exception.UserAlreadyExistException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final Keycloak keycloak;
    @Value("${keycloak.admin.realm}")
    private String realm;

    public void registerNewUser(String email, String password, String name, String lastName) {
        RealmResource resource = keycloak.realm(realm);
        List<UserRepresentation> existingUser = resource.users().search(email, true);

        if (!existingUser.isEmpty()) {
            throw new UserAlreadyExistException(email);
        }

        UserRepresentation newUser = new UserRepresentation();
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setFirstName(name);
        newUser.setLastName(lastName);
        newUser.setEnabled(true);

        Response response = resource.users().create(newUser);
        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);

        resource.users().get(userId).resetPassword(cred);
    }

    public void registerSeller(String email) {
        RealmResource resource = keycloak.realm(realm);

        RoleRepresentation sellerRole = resource
                .roles()
                .get("SELLER")
                .toRepresentation();

        List<UserRepresentation> users = resource.users().search(email, true);

        if (users.isEmpty()) {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }

        UserResource user = resource.users().get(users.getFirst().getId());

        user.roles().realmLevel().add(List.of(sellerRole));
    }
}
