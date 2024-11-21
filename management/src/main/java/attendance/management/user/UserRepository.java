package attendance.management.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String userid);
    Optional<User> findByNameAndPhoneNumber(String name, String phoneNumber);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByIdx(Long idx);

}
