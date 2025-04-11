package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Employee;
import in.sp.main.repositories.EmployeeRepository;

@Service
public class EmployeeService 
{
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public boolean loginEmpService(String email, String password)
	{
		Employee employee = employeeRepository.findByEmail(email);
		if(employee != null)
		{
			return password.equals(employee.getPassword());
		}
		return false;
	}
	
	public void addEmployee(Employee employee)
	{
		employeeRepository.save(employee);
	}
	
	public Employee getEmployeeDetails(String employeeEmail)
	{
		return employeeRepository.findByEmail(employeeEmail);
	}
	
	public Page<Employee> getAllEmployeeDetailsByPagination(Pageable pageable)
	{
		return employeeRepository.findAll(pageable);
	}
	
	public void updateEmployeeDetails(Employee employee)
	{
		employeeRepository.save(employee);
	}
	
	public void deleteEmployeeDetails(String employeeEmail)
	{
		Employee employee = employeeRepository.findByEmail(employeeEmail);
		if(employee != null)
		{
			employeeRepository.delete(employee);
		}
		else
		{
			throw new RuntimeException("Employee not found with email : "+employeeEmail);
		}
	}
}