package in.anuj.moneymanager.repository;

import in.anuj.moneymanager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity,Long>{

    Optional<ProfileEntity>findByEmail(String email);
    Optional<ProfileEntity> findFirstByEmailOrderByIdDesc(String email);
    @Query("SELECT CASE WHEN COUNT(profile) > 0 THEN TRUE ELSE FALSE END "
            + "FROM ProfileEntity profile "
            + "WHERE LOWER(TRIM(profile.email)) = :email")
    boolean existsByNormalizedEmail(@Param("email") String email);
    Optional<ProfileEntity>findByActivationToken(String activationToken);
}
