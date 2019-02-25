package com.shm.controller;

import com.shm.pojo.Focus;
import com.shm.pojo.Goods;
import com.shm.pojo.GoodsExtend;
import com.shm.pojo.Image;
import com.shm.pojo.Notice;
import com.shm.pojo.Purse;
import com.shm.pojo.User;
import com.shm.service.FocusService;
import com.shm.service.GoodsService;
import com.shm.service.ImageService;
import com.shm.service.NoticeService;
import com.shm.service.PurseService;
import com.shm.service.UserService;
import com.shm.util.DateUtil;
import com.shm.util.MD5;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	private UserService userService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private ImageService imageService;

	@Resource
	private FocusService focusService;

	@Resource
	private PurseService purseService;

	@Resource
	private NoticeService noticeService;
	

	/**
	 * 用户注册
	 * 
	 * @param user1
	 * @return
	 */
	@RequestMapping(value = "/addUser")
	public String addUser(HttpServletRequest request, @ModelAttribute("user") User user1) {
		String url = request.getHeader("Referer");
		User user = userService.getUserByPhone(user1.getPhone());
		if (user == null) {// 检测该用户是否已经注册
			String t = DateUtil.getNowDate();
			// 对密码进行MD5加密
			String str = MD5.md5(user1.getPassword());
			user1.setCreateAt(t);// 创建开始时间
			user1.setPassword(str);
			user1.setGoodsNum(0);
			user1.setStatus((byte) 1);// 初始正常状态
			user1.setPower(100);
			userService.addUser(user1);
			purseService.addPurse(user1.getId());// 注册的时候同时生成钱包
		}
		return "redirect:" + url;
	}

	/**
	 * 注册验证账号
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(HttpServletRequest request) {
		String phone = request.getParameter("phone");
		User user = userService.getUserByPhone(phone);
		if (user == null) {
			return "{\"success\":true,\"flag\":false}";// 用户存在，注册失败
		} else {
			return "{\"success\":true,\"flag\":true}";// 用户不存在，可以注册
		}
	}

	/**
	 * 登陆验证密码
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "/password",method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String password(HttpServletRequest request){ String
	 * phone=request.getParameter("phone"); String
	 * password=request.getParameter("password");
	 * if((phone==null||phone=="")&&(password==null||password=="")) { return
	 * "{\"success\":false,\"flag\":true}"; }else { User user =
	 * userService.getUserByPhone(phone); if(user==null) { return
	 * "{\"success\":false,\"flag\":false}";//账号错误 } String pwd = MD5.md5(password);
	 * if (pwd.equals(user.getPassword())) { return
	 * "{\"success\":true,\"flag\":false}";//密码正确 }else { return
	 * "{\"success\":true,\"flag\":true}";//密码错误 } }
	 * 
	 * }
	 */

	/**
	 * 验证登录
	 * 
	 * @param request
	 * @param user
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/login")
	public ModelAndView loginValidate(HttpServletRequest request, HttpServletResponse response, User user,
			ModelMap modelMap) throws IOException {
		User cur_user = userService.getUserByPhone(user.getPhone());
		String url = request.getHeader("Referer");
		if (cur_user != null) {
			String pwd = MD5.md5(user.getPassword());
			if (pwd.equals(cur_user.getPassword())) {
				if (cur_user.getStatus() == 1) {
					request.getSession().setAttribute("cur_user", cur_user);
					return new ModelAndView("redirect:" + url);
				}
			}
		}else{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out= response.getWriter();
			out.print("<script language='javascript'>alert('账号密码错误')</script>");
		}
		return new ModelAndView("redirect:" + url);
	}

	/**
	 * 更改用户名
	 * 
	 * @param request
	 * @param user
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/changeName")
	public ModelAndView changeName(HttpServletRequest request, User user, ModelMap modelMap) {
		String url = request.getHeader("Referer");
		// 从session中获取出当前用户
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		cur_user.setUsername(user.getUsername());// 更改当前用户的用户名
		userService.updateUserName(cur_user);// 执行修改操作
		request.getSession().setAttribute("cur_user", cur_user);// 修改session值
		return new ModelAndView("redirect:" + url);
	}

	/**
	 * 完善或修改信息
	 * 
	 * @param request
	 * @param user
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/updateInfo")
	public ModelAndView updateInfo(HttpServletRequest request, User user, ModelMap modelMap) {
		// 从session中获取出当前用户
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		cur_user.setUsername(user.getUsername());
		cur_user.setQq(user.getQq());
		userService.updateUserName(cur_user);// 执行修改操作
		request.getSession().setAttribute("cur_user", cur_user);// 修改session值
		return new ModelAndView("redirect:/user/basic");
	}

	/**
	 * 用户退出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().setAttribute("cur_user", null);
		return "redirect:/goods/homeGoods";
	}

	/**
	 * 个人中心
	 * 
	 * @return
	 */
	@RequestMapping(value = "/home")
	public ModelAndView home(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer userId = cur_user.getId();
		Purse myPurse = purseService.getPurseByUserId(userId);
		List<User> users = userService.getUserOrderByDate(5);
		List<Notice> notice = noticeService.getNoticeList();
        mv.addObject("notice", notice);
		mv.addObject("myPurse", myPurse);
		mv.addObject("users", users);
		mv.setViewName("/user/home");
		return mv;
	}
	
	/**
	 * 个人信息设置
	 * 
	 * @return
	 */
	@RequestMapping(value = "/basic")
	public ModelAndView basic(HttpServletRequest request) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer userId = cur_user.getId();
		Purse myPurse = purseService.getPurseByUserId(userId);
		ModelAndView mv = new ModelAndView();
		List<User> users = userService.getUserOrderByDate(5);
		mv.addObject("users", users);
		mv.addObject("myPurse", myPurse);
		mv.setViewName("/user/basic");
		return mv;
	}

	/**
	 * 我的闲置 查询出所有的用户商品以及商品对应的图片
	 * 
	 * @return 返回的model为 goodsAndImage对象,该对象中包含goods 和 images，参考相应的类
	 */
	@RequestMapping(value = "/allGoods")
	public ModelAndView goods(HttpServletRequest request) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer userId = cur_user.getId();
		List<Goods> goodsList = goodsService.getGoodsByUserId(userId);
		List<GoodsExtend> goodsAndImage = new ArrayList<GoodsExtend>();
		for (int i = 0; i < goodsList.size(); i++) {
			// 将用户信息和image信息封装到GoodsExtend类中，传给前台
			GoodsExtend goodsExtend = new GoodsExtend();
			Goods goods = goodsList.get(i);
			List<Image> images = imageService.getImagesByGoodsPrimaryKey(goods.getId());
			goodsExtend.setGoods(goods);
			goodsExtend.setImages(images);
			goodsAndImage.add(i, goodsExtend);
		}
		Purse myPurse = purseService.getPurseByUserId(userId);
		ModelAndView mv = new ModelAndView();
		List<User> users = userService.getUserOrderByDate(5);
		mv.addObject("users", users);
		mv.addObject("goodsAndImage", goodsAndImage);
		mv.setViewName("/user/goods");
		mv.addObject("myPurse", myPurse);
		return mv;
	}

	/**
	 * 我的关注 查询出所有的用户商品以及商品对应的图片
	 * 
	 * @return 返回的model为 goodsAndImage对象,该对象中包含goods 和 images，参考相应的类
	 */
	@RequestMapping(value = "/allFocus")
	public ModelAndView focus(HttpServletRequest request) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer userId = cur_user.getId();
		List<Focus> focusList = focusService.getFocusByUserId(userId);
		List<GoodsExtend> goodsAndImage = new ArrayList<GoodsExtend>();
		for (int i = 0; i < focusList.size(); i++) {
			// 将用户信息和image信息封装到GoodsExtend类中，传给前台
			GoodsExtend goodsExtend = new GoodsExtend();
			Focus focus = focusList.get(i);
			Goods goods = goodsService.getGoodsByPrimaryKey(focus.getGoodsId());
			List<Image> images = imageService.getImagesByGoodsPrimaryKey(focus.getGoodsId());
			goodsExtend.setGoods(goods);
			goodsExtend.setImages(images);
			goodsAndImage.add(i, goodsExtend);
		}
		Purse myPurse = purseService.getPurseByUserId(userId);
		ModelAndView mv = new ModelAndView();
		List<User> users = userService.getUserOrderByDate(5);
		mv.addObject("users", users);
		mv.addObject("goodsAndImage", goodsAndImage);
		mv.addObject("myPurse", myPurse);
		mv.setViewName("/user/focus");
		return mv;
	}

	/**
	 * 删除我的关注
	 * 
	 * @return
	 */
	@RequestMapping(value = "/deleteFocus/{id}")
	public String deleteFocus(HttpServletRequest request, @PathVariable("id") Integer goods_id) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer user_id = cur_user.getId();
		focusService.deleteFocusByUserIdAndGoodsId(goods_id, user_id);
		return "redirect:/user/allFocus";
	}

	/**
	 * 添加我的关注
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addFocus/{id}")
	public String addFocus(HttpServletRequest request, @PathVariable("id") Integer goods_id) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer user_id = cur_user.getId();
		List<Integer> n =null;
		int goodsId = 0;
		List<Focus> focus = focusService.getFocusByUserId(user_id);
		if (focus.isEmpty()) {
			focusService.addFocusByUserIdAndId(goods_id, user_id);
		}
		for (Focus myfocus : focus) {
			 goodsId = myfocus.getGoodsId();
			 n = new LinkedList<Integer>();
			 n.add(goodsId);
		}
		 if(n.contains(goods_id)) {
				return "redirect:/user/allFocus";
		 }else {
			 focusService.addFocusByUserIdAndId(goods_id, user_id);
		 }
		return "redirect:/user/allFocus";
	}

	/**
	 * 我的钱包
	 * 
	 * @return 返回的model为 goodsAndImage对象
	 */
	@RequestMapping(value = "/myPurse")
	public ModelAndView getMoney(HttpServletRequest request) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer user_id = cur_user.getId();
		Purse purse = purseService.getPurseByUserId(user_id);
		ModelAndView mv = new ModelAndView();
		List<User> users = userService.getUserOrderByDate(5);
		mv.addObject("users", users);
		mv.addObject("myPurse", purse);
		mv.setViewName("/user/purse");
		return mv;
	}

	/**
	 * 充值与提现 根据传过来的两个值进行判断是充值还是提现
	 * 
	 * @return 返回的model为 goodsAndImage对象
	 */
	@RequestMapping(value = "/updatePurse")
	public String updatePurse(HttpServletRequest request, Purse purse) {
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Integer user_id = cur_user.getId();
		purse.setUserId(user_id);
		purse.setState(0);
		if (purse.getRecharge() != null) {
			purseService.updatePurse(purse);
		}
		if (purse.getWithdrawals() != null) {
			purseService.updatePurse(purse);
		}
		return "redirect:/user/myPurse";
	}
	
	/*发布动态*/
	@RequestMapping(value = "/insertSelective", method = RequestMethod.POST)
	@ResponseBody
	public String insertSelective(HttpServletRequest request) {
		String context = request.getParameter("context");
		User cur_user = (User) request.getSession().getAttribute("cur_user");
		Notice notice = new Notice();
		notice.setContext(context);
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		notice.setCreateAt(sdf.format(dt));
		notice.setStatus((byte) 0);
		notice.setUser(cur_user);
		if (context == null || context == "") {
			return "{\"success\":false,\"msg\":\"发布失败，请输入内容!\"}";
		}
		try {
			noticeService.insertSelective(notice);
		} catch (Exception e) {
			return "{\"success\":false,\"msg\":\"发布失败!\"}";
		}
		return "{\"success\":true,\"msg\":\"发布成功!\"}";
	}
	
		/*获取信息动态*/
		@RequestMapping(value = "/creatTable", method = RequestMethod.GET)
		@ResponseBody
		public Map<String,Object> creatTable(HttpServletRequest request){
			  String page = request.getParameter("page");
	          int pageNum =Integer.parseInt(page);//当前页
	          int pageSize = 5;//这里用来设置每页要展示的数据数量，建议把这个写到web.config中来全局控制
	          int total = noticeService.getNoticeNum();
	          Map<String,Object> map = new HashMap<String,Object>();
	          List<Notice> notices = noticeService.getPageNoticeList(pageNum, pageSize);
	          map.put("notices", notices);
	          map.put("pageCount", total);
	          map.put("CurrentPage", page);
	          return map;
		     }
}
