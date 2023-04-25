package com.example.football_api.services;


import com.example.football_api.models.ERole;
import com.example.football_api.models.Role;
import com.example.football_api.models.User;

import java.util.Set;

public interface RoleService {
    Set<Role> getRolesForUser();
    Role getRole(ERole role);
    boolean checkIfUserHasRole(User user, Role role);
}
