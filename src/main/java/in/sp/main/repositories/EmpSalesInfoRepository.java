package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.EmployeeOrders;

@Repository
public interface EmpSalesInfoRepository extends JpaRepository<EmployeeOrders, Long>
{
	String SQL_QUERY1 = "SELECT SUM(course_amount) AS total_sum_of_course_amount FROM orders WHERE order_id NOT LIKE 'order_%'";
	@Query(value = SQL_QUERY1, nativeQuery = true)
	String findTotalSalesByAllEmployees();
	
	
	String SQL_QUERY2 = "SELECT e.name AS employee_name, e.email AS employee_email, e.phoneno AS employee_phoneno, SUM(o.course_amount) AS total_sale FROM employee e JOIN employee_orders eo ON e.email=eo.employee_email JOIN orders o ON eo.order_id=o.order_id GROUP BY e.name, e.email, e.phoneno";
	@Query(value = SQL_QUERY2, nativeQuery = true)
	List<Object[]> findTotalSalesByEachEmployee();
}