package ru.kpfu.itis.persistence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.persistence.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByEmail(String email);

    User findOneByUsername(String username);

    @Query("select case when count(a) > 0 then true else false end from User a where a.email     = :email")
    boolean emailExists(@Param("email") String email);


}
