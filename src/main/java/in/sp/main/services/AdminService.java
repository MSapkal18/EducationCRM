package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Admin;

import in.sp.main.repositories.AdminRepository;

@Service
public class AdminService
{
	@Autowired
	private AdminRepository adminRepository;
	
	
	
	public boolean loginAdminService(String email, String password)
	{
		Admin admin = adminRepository.findByEmail(email);
		if(admin != null)
		{
			if(password.equals(admin.getPassword()))
			{
				return true;
			}
			else
			{
				return false;
			}
		
		}
		else
		{
			return false;
		}
	}
}
