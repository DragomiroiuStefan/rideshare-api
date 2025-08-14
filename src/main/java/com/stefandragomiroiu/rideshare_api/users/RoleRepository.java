package com.stefandragomiroiu.rideshare_api.users;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends ListCrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Modifying
    @Query("""
            INSERT INTO user_roles (user_id, role_id, granted_by)
            VALUES (:userId, :roleId, :grantedBy)
            """)
    void assignRoleToUser(@Param("userId") Long userId, @Param("roleId") Long roleId, @Param("grantedBy") Long grantedBy);


}

