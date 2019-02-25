package secondHandMarket;


import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shm.pojo.Goods;
import com.shm.pojo.Orders;
import com.shm.pojo.Purse;
import com.shm.pojo.User;
import com.shm.service.AdminService;
import com.shm.service.GoodsService;
import com.shm.service.OrdersService;
import com.shm.service.PurseService;
import com.shm.service.UserService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:conf/applicationContext.xml"})
public class secondHandMarketTest {
		@Resource
		private UserService userService;
		
		@Resource
		private GoodsService goodsService;

		@Resource
		private OrdersService ordersService;

		@Resource
		private PurseService purseService;

		@Resource
		private AdminService adminService;

		@Before
		public void beforeTest() {
			System.out.println("测试-star");
		}
		
		@Test
		public void TestUsers() {
			List<User> rows = userService.getPageUser(1, 10);
			System.out.println("用户："+rows);
		}
		
		@Test
		public void TestGoods() {
			List<Goods> rows = goodsService.getAllGoods();
			System.out.println("商品："+rows);
		}
		
		@Test
		public void TestOrders() {
			List<Orders> rows = ordersService.getPageOrders(1, 10);
			System.out.println("订单："+rows);
		}
		
		@Test
		public void TestPurse() {
			List<Purse> rows = purseService.getPagePurse(1, 10);
			System.out.println("钱包："+rows);
		}

		@After
		public void afterTest() {
			System.out.println("测试-end");
		}

		

	}
