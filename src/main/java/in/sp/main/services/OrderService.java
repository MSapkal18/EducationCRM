package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Orders;
import in.sp.main.repositories.OrdersRepository;

@Service
public class OrderService
{
	@Autowired
	private OrdersRepository ordersRepository;
	
	public void storeUserOrders(Orders orders)
	{
		ordersRepository.save(orders);
	}
}