package br.com.gomesar.jwtdecode.services.jwt.enums;

import java.util.Arrays;
import java.util.List;

public enum ERole {
    ADMIN("Admin"),
    MEMBER("Member"),
    EXTERNAL("External");

    public final String roleName;

    ERole(final String roleName) {
        this.roleName = roleName;
    }

    public static List<String> getAllowedRoles() {
        return Arrays.stream(values()).map(ERole::getRoleName).toList();
    }

    public String getRoleName() {
        return this.roleName;
    }
}
