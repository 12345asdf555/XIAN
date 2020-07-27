package com.spring.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spring.model.Person;
import com.spring.model.WeldedJunction;
import com.spring.model.WeldingMachine;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.PersonService;
import com.spring.service.UserService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WeldingMachineService;
import com.spring.service.WpsService;

import net.sf.json.JSONArray;


@Controller
@RequestMapping(value = "/terminal",produces = { "text/json;charset=UTF-8" })
public class TerminalController {
	
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	
	@Autowired
	private UserService userService;
	@Autowired
	private WeldedJunctionService wjm;
	@Autowired
	private PersonService welderService;
	@Autowired
	private WeldedJunctionService wjs;
	@Autowired
	private WpsService wpsService;
	@Autowired
	private WeldingMachineService wmm;
	/**
	 * 验证登陆
	 * @param request
	 */
	@RequestMapping("/login")
	@ResponseBody
	public void login(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		JSONObject data = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String username = JSON.parseObject(jsondata.getString("data")).getString("username");
		String password = JSON.parseObject(jsondata.getString("data")).getString("password");
		
        try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			md5.update(password.getBytes());
			String passwordmd5 = new BigInteger(1, md5.digest()).toString(16);
			
			com.spring.model.User user = userService.LoadUser(username);
			
			if(null == user){
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "default");
				data.put("datalength", "0");
				serrespon.put("data", data);
			}else if(null != user && passwordmd5.equals(user.getUserPassword())){
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "succeed");
				data.put("datalength", "2");
				data.put("name", user.getUserName());
				data.put("userid", user.getId());
				serrespon.put("data", data);
			}else{
				serrespon.put("type", "loginrespond");
				serrespon.put("respondtype", "default");
				data.put("datalength", "0");
				serrespon.put("data", data);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
        String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	/**
	 * 扫描跟踪卡获取任务信息
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/scan")
	public void scan(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject json = new JSONObject();
		
		String cardnumber = request.getParameter("cardnumber");
		String search = "";
		search = " AND p.FCARD_ID="+cardnumber;
		List<WeldedJunction> productList = wjm.getProductList(search);
		for(WeldedJunction wjm:productList){
			json.put("fid", wjm.getId());
			json.put("fproduct_number", wjm.getFprefix_number()+"-"+wjm.getFproduct_number());
			json.put("fwps_lib_name", wjm.getFwps_lib_name());
			json.put("fwps_lib_version", wjm.getFwps_lib_version());
			json.put("fwps_lib_id", wjm.getFwpslib_id());
			json.put("fproduct_drawing_no", wjm.getFproduct_drawing_no());
			json.put("fproduct_name", wjm.getFproduct_name());
			json.put("fproduct_version", wjm.getFproduct_version());
			json.put("fstatus", wjm.getFstatus());
			ary.add(json);
		}
		
		serrespon.put("rows", ary);

        String respondata = JSON.toJSONString(serrespon);
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 开始任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/starttask")
	public void starttask(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		try {
			String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("1".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				String junctionId = jsondata.getString("junctionId");
				String stepId = jsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.addTaskresultRow(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 下一工步任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/nextstep")
	public void nextstep(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("oldJson"));
		JSONObject newJsondata = JSON.parseObject(request.getParameter("newJson"));
		try {
			String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("0".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("1".equals(flag)){     //最小单位为工步
				String junctionId = jsondata.getString("junctionId");
				String stepId = jsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.updateTaskresult(wps);
			cardId = newJsondata.getString("cardId");
			machId = newJsondata.getString("machId");
			welderId = newJsondata.getString("welderId");
			wpsId = newJsondata.getString("wpsId");
			productId = newJsondata.getString("productId");
			employeeId = newJsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("0".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("1".equals(flag)){     //最小单位为工步
				String junctionId = newJsondata.getString("junctionId");
				String stepId = newJsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.addTaskresultRow(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 结束任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/over")
	public void over(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		try {
			String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			if("1".equals(flag)){     //最小单位为焊缝
				String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				String junctionId = jsondata.getString("junctionId");
				String stepId = jsondata.getString("stepId");
				wps.setFjunction(junctionId);
				wps.setFstep_id(stepId);
			}
			wpsService.updateTaskresult(wps);
			wpsService.overTaskresult(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 任务数据上传
	 * @param request
	 * @return
	 */
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		try {
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wpsService.overCard(wps);
			serrespon.put("success", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			serrespon.put("success", false);
		}
		String respondata = JSON.toJSONString(serrespon);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Description 获取所有电子跟踪卡
	 * @author Bruce
	 * @date 2020年6月23日下午7:19:13
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllCards")
	public void getAllCards(HttpServletRequest request,HttpServletResponse response){
		String search = request.getParameter("search");
		search = "(t.fid IN (SELECT MAX(fid) FROM tb_taskresult GROUP BY FCARD_ID) OR t.foperatetype IS null) ";
		List<WeldedJunction> cardList = wjm.getCardList(search);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldedJunction wjm:cardList){
				json.put("fid", wjm.getId());
				json.put("fwelded_junction_no", wjm.getWeldedJunctionno());
				json.put("type", wjm.getRoomNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有焊工
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:46
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllWelders")
	public void getAllWelders(HttpServletRequest request,HttpServletResponse response){
		List<Person> findAll = welderService.getWelder();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Person welder:findAll){
				json.put("id", welder.getId());
				json.put("name", welder.getName());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取所有焊机
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:46
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getAllMachine")
	public void getAllMachine(HttpServletRequest request,HttpServletResponse response){
		List<WeldingMachine> list = wmm.getWeldingMachine(null);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(WeldingMachine wm:list){
				json.put("id", wm.getId());
				json.put("equipmentNo", wm.getEquipmentNo());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据工艺规程id获取焊缝、工序、工步信息
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午9:49:15
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getParams")
	public void getParams(HttpServletRequest request,HttpServletResponse response){
		String search = request.getParameter("search");
		String valueFlag = request.getParameter("valueFlag");
		JSONObject json = new JSONObject();
		JSONArray junAry = new JSONArray();
		JSONArray empAry = new JSONArray();
		JSONArray stepAry = new JSONArray();
		JSONArray paramAry = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			if(valueFlag.equals("0")) {
				List<Wps> empList = wpsService.getEmployee(search);
				for(Wps wps:empList){
					json.put("fid", wps.getFid());
					json.put("femployee", wps.getFemployee_id()+"--"+wps.getFemployee_name());
//					json.put("femployee_version", wps.getFemployee_version());
//					json.put("femployee_name", wps.getFemployee_name());
					empAry.add(json);
				}
				List<Wps> stepList = wpsService.getStep(String.valueOf(empList.get(0).getFid()));
				for(Wps wps:stepList){
					json.put("fid", wps.getFid());
					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
//					json.put("fstep_name", wps.getFstep_name());
					stepAry.add(json);
				}
				List<Wps> junList = wpsService.getJunction(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:junList){
					json.put("fid", wps.getFid());
					json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
//					json.put("fwelding_area", wps.getFwelding_area());
					junAry.add(json);
				}
				List<Wps> paramList = wpsService.getDetail(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:paramList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					paramAry.add(json);
				}
				
			}
			if(valueFlag.equals("1")) {
				List<Wps> stepList = wpsService.getStep(search);
				for(Wps wps:stepList){
					json.put("fid", wps.getFid());
					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
//					json.put("fstep_name", wps.getFstep_name());
					stepAry.add(json);
				}
				List<Wps> junList = wpsService.getJunction(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:junList){
					json.put("fid", wps.getFid());
					json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
//					json.put("fwelding_area", wps.getFwelding_area());
					junAry.add(json);
				}
				List<Wps> paramList = wpsService.getDetail(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:paramList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					paramAry.add(json);
				}
			}
			if(valueFlag.equals("3")) {
				List<Wps> junList = wpsService.getJunction(search);
				for(Wps wps:junList){
					json.put("fid", wps.getFid());
					json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
//					json.put("fwelding_area", wps.getFwelding_area());
					junAry.add(json);
				}
				List<Wps> paramList = wpsService.getDetail(search);
				for(Wps wps:paramList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					paramAry.add(json);
				}
			}
//			if(valueFlag.equals("3")) {
//				List<Wps> wpsList = wpsService.getDetail(search);
//				for(Wps wps:wpsList){
//					json.put("fid", wps.getFid());
//					json.put("fquantitative_project", wps.getFquantitative_project());
//					json.put("frequired_value", wps.getFrequired_value());
//					json.put("fupper_deviation", wps.getFupper_deviation());
//					json.put("flower_deviation", wps.getFlower_deviation());
//					json.put("funit_of_measurement", wps.getFunit_of_measurement());
//					paramAry.add(json);
//				}
//			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("stepAry", junAry);
		obj.put("junAry", empAry);
		obj.put("empAry", stepAry);
		obj.put("paramAry", paramAry);
		String respondata = JSON.toJSONString(obj);
        
		//构造回调函数格式jsonpCallback(数据)
        try {
            String jsonpCallback = request.getParameter("jsonpCallback");
			response.getWriter().println(jsonpCallback+"("+respondata+")");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}