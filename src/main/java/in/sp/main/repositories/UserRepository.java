package in.sp.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
	User findByEmail(String email);
}