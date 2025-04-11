package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Orders;

@Repository
public interface OrdersChartRepository extends JpaRepository<Orders, Long>
{
	String SQL_QUERY1 = "SELECT SUBSTRING_INDEX(date_of_purchase, ',', 1) AS purchased_date, SUM(course_amount) AS total_sales_amount FROM orders GROUP BY purchased_date ORDER BY purchased_date";
	@Query(value = SQL_QUERY1, nativeQuery = true)
	List<Object[]> findCoursesAmountTotalSales();
	
	
	
	String SQL_QUERY2 = "SELECT course_name, COUNT(*) AS total_sold FROM orders GROUP BY course_name";
	@Query(value = SQL_QUERY2, nativeQuery = true)
	List<Object[]> findCoursesTotalSales();
	
	
	String SQL_QUERY3 = "SELECT SUBSTRING_INDEX(date_of_purchase, ',', 1) AS purchased_date, COUNT(*) AS number_of_courses_sold FROM orders GROUP BY purchased_date ORDER BY purchased_date";
	@Query(value = SQL_QUERY3, nativeQuery = true)
	List<Object[]> findCoursesSoldPerDay();
}
