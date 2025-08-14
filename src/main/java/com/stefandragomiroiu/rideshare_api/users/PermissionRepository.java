package com.stefandragomiroiu.rideshare_api.users;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PermissionRepository extends ListCrudRepository<Permission, Long> {

    @Query("""
            SELECT DISTINCT p.name
            FROM permission p
            INNER JOIN role_permissions rp ON p.permission_id = rp.permission_id
            INNER JOIN role r ON rp.role_id = r.role_id
            INNER JOIN user_roles ur ON r.role_id = ur.role_id
            WHERE ur.user_id = :userId
            """)
    Set<String> findAllByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT DISTINCT p.name
            FROM permission p
            INNER JOIN role_permissions rp ON p.permission_id = rp.permission_id
            INNER JOIN role r ON rp.role_id = r.role_id
            WHERE r.role_id = :roleId
            AND r.active = true
            """)
    Set<String> findAllByRoleId(@Param("roleId") Long roleId);


}

