package in.sp.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Admin;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>
{
	Admin  findByEmail(String email);
}