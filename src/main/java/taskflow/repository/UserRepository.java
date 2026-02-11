package taskflow.repository;

import org.springframework.data.jpa.repository.Query;
import taskflow.dto.UserResponseDto;
import taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query(value = """
    SELECT u.id,
           u.username,
           u.email,
           u.role,
           u.createdAt,
           COUNT(t.id)
    FROM users u
    LEFT JOIN task t ON t.user_id = u.id
    GROUP BY u.id, u.username, u.email, u.role, u.createdAt
""", nativeQuery = true)
    List<Object[]> findAllWithTaskCountRaw();

}
