package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>
{
	String SELECT_QUERY1 = "SELECT o.date_of_purchase, c.description, c.image_url, c.name, c.updated_on FROM orders o JOIN course c ON o.course_name=c.name WHERE o.user_email=:email";
	@Query(value = SELECT_QUERY1, nativeQuery = true)
	List<Object[]> findPurchasedCoursesByEmail(@Param("email") String email);
	
	
	String SELECT_QUERY2 = "SELECT c.image_url, o.course_name, o.course_amount, o.date_of_purchase, o.order_id, o.payment_id FROM orders o JOIN course c ON o.course_name=c.name WHERE o.user_email=:email";
	@Query(value = SELECT_QUERY2, nativeQuery = true)
	List<Object[]> findCustomerCoursesByEmail(@Param("email") String email);
}