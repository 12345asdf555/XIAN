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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.neethi.util.Service;
import org.aspectj.weaver.ast.Call;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
			json.put("fproduct_number", wjm.getFprefix_number()+wjm.getFproduct_number()+wjm.getFsuffix_number());
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
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			wps.setFjunction(junctionId);
			wps.setFstep_id(stepId);
			if("1".equals(flag)){     //最小单位为焊缝
				//String stepId = jsondata.getString("stepId");
				wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				//String junctionId = jsondata.getString("junctionId");
				//String stepId = jsondata.getString("stepId");
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
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			wps.setFwelded_junction_no(cardId);
			wps.setMacid(new BigInteger(machId));
			wps.setWelderid(new BigInteger(welderId));
			wps.setFwpslib_id(new BigInteger(wpsId));
			wps.setFproduct_name(productId);
			wps.setFemployee_id(employeeId);
			wps.setFstatus(Integer.parseInt(flag));
			wps.setFstep_id(stepId);
			wps.setFjunction(junctionId);
			if("1".equals(flag)){     //最小单位为焊缝
				//String stepId = jsondata.getString("stepId");
				//wps.setFstep_id(stepId);
			}else if("2".equals(flag)){     //最小单位为工步
				//String junctionId = jsondata.getString("junctionId");
				//String stepId = jsondata.getString("stepId");
				//wps.setFjunction(junctionId);
				//wps.setFstep_id(stepId);
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
	 * 任务上传
	 * @Description
	 * @author chen
	 * @date 2020年8月21日下午7:10:17
	 * @param request 任务上传
	 * @param response 
	 */
	@RequestMapping("/TaskUpload")
	public void TaskUpload(HttpServletRequest request,HttpServletResponse response){
		Wps wps = new Wps();
		JSONObject serrespon = new JSONObject();
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String search = "";
		double ele = 0.0,vol = 0.0;
		try {
			//String flag = request.getParameter("flag");
			String cardId = jsondata.getString("cardId");
			String machId = jsondata.getString("machId");
			String welderId = jsondata.getString("welderId");
			String wpsId = jsondata.getString("wpsId");
			String productId = jsondata.getString("productId");
			String employeeId = jsondata.getString("employeeId");
			String stepId = jsondata.getString("stepId");
			String junctionId = jsondata.getString("junctionId");
			if(cardId != ""){
				if(search == ""){
					search += " t.FCARD_ID LIKE "+"'%" + cardId + "%'";
				}else{
					search += " AND t.FCARD_ID LIKE "+"'%" + cardId + "%'";
				}
			}
			if(productId != ""){
				if(search == ""){
					search += " t.FPRODUCT_NUMBER_ID LIKE "+"'%" + productId + "%'";
				}else{
					search += " AND t.FPRODUCT_NUMBER_ID LIKE "+"'%" + productId + "%'";
				}
			}
			if(employeeId != ""){
				if(search == ""){
					search += " t.FEMPLOYEE_ID LIKE "+"'%" + employeeId + "%'";
				}else{
					search += " AND t.FEMPLOYEE_ID LIKE "+"'%" + employeeId + "%'";
				}
			}
			if(stepId != ""){
				if(search == ""){
					search += " t.FSTEP_ID LIKE "+"'%" + stepId + "%'";
				}else{
					search += " AND t.FSTEP_ID LIKE "+"'%" + stepId + "%'";
				}
			}
			if(junctionId != ""){
				if(search == ""){
					search += " t.FJUNCTION_ID LIKE "+"'%" + junctionId + "%'";
				}else{
					search += " AND t.FJUNCTION_ID LIKE "+"'%" + junctionId + "%'";
				}
			}
			List<Wps> list = wpsService.getTaskParameter(search);
			try {
				for(Wps w:list){
					ele = list.get(0).getFweld_ele();
					vol = list.get(0).getFweld_vol();
				}
			}
			catch (Exception e) {
				// TODO: handle exception
			}
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
	
		
		// TODO Auto-generated method stub
		
        String path = "C:\\Users\\Admin\\My Job\\Tools\\apache-tomcat-8.0.53\\webapps\\CardList"+"\\"+"25-202007-008501-2020072910243930.xml";
        //String path = "E:\\Xmlfile\\card.xml";
        byte[] buffer = null;  
        try {  
        	File file = new File(path);  
        	SAXReader reader = new SAXReader();
			// 读取xml文件到Document中
			Document doc = null;
			try {
				doc = reader.read(file);
				// 获取xml文件的根节点
				Element rootElement = doc.getRootElement();
				Element tableContent = rootElement.element("tableContent");
				Element starttime = rootElement.element("starttime");
				Element endtime = rootElement.element("endtime");
				starttime.setText("2020年8月4日");
				endtime.setText("2020年8月4日");
				Element contentvalue = tableContent.addElement("contentvalue");
				Element content = contentvalue.addElement("content");
				content.setText("电流");
				Element value = contentvalue.addElement("value");
				value.setText(String.valueOf(ele));
				contentvalue = tableContent.addElement("contentvalue");
				content = contentvalue.addElement("content");
				content.setText("电压");
				value = contentvalue.addElement("value");
				value.setText(String.valueOf(vol));
				//Format格式输出格式刷
				
				OutputFormat format = OutputFormat.createPrettyPrint();
				//设置xml编码
				format.setEncoding("utf-8");
				//另一个参数表示设置xml的格式
				XMLWriter xw = new XMLWriter(new FileWriter(file),format);
				//将组合好的xml封装到已经创建好的document对象中，写出真实存在的xml文件中
				xw.write(doc);
				//清空缓存关闭资源
				xw.flush();
				xw.close();
				/*测试
				OutputStream os = new FileOutputStream("E:\\Xmlfile\\MES2.xml");
				//Format格式输出格式刷
				OutputFormat format = OutputFormat.createPrettyPrint();
				//设置xml编码
				format.setEncoding("utf-8");
		 
				//写：传递两个参数一个为输出流表示生成xml文件在哪里
				//另一个参数表示设置xml的格式
				XMLWriter xw = new XMLWriter(os,format);
				//将组合好的xml封装到已经创建好的document对象中，写出真实存在的xml文件中
				xw.write(doc);
				//清空缓存关闭资源
				xw.flush();
				xw.close();*/
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            Client client = dcf.createClient("http://192.168.0.99/WeldToMes/MesToWeldServices.asmx?WSDL");
//            http://192.168.0.96:8080/CIWJN_Service/cIWJNWebService?wsdl
//			util.Authority(client);
            Object[] objects = null;
            try {
				objects = client.invoke(new QName("http://tempuri.org/", "UploadWeldFile"), new Object[]{"25-202007-008501-2020072910243930.xml",Integer.parseInt(String.valueOf(file.length())),buffer});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if(objects[0].toString().equals("OK")){
            	System.out.println("OK!");
            }else{
            	System.out.println("NO!");
            }
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
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
				List<Wps> empList = wpsService.getEmployeStep(search);//根据工步查询工序
				for(Wps wps:empList){
					json.put("fid", wps.getFid());
					json.put("femployee", wps.getFemployee_id()+"--"+wps.getFemployee_name());
//					json.put("femployee_version", wps.getFemployee_version());
//					json.put("femployee_name", wps.getFemployee_name());
					empAry.add(json);
				}
				
//				List<Wps> junList = wpsService.getJunction(String.valueOf(stepList.get(0).getFid()));//根据工步查询焊缝
//				for(Wps wps:junList){
//					json.put("fid", wps.getFid());
//					json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
////					json.put("fwelding_area", wps.getFwelding_area());
//					junAry.add(json);
//				}
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
			if(valueFlag.equals("1")) {
//				List<Wps> stepList = wpsService.getStep(search);//根据工序查工步
//				for(Wps wps:stepList){
//					json.put("fid", wps.getFid());
//					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
//					json.put("fstep_name", wps.getFstep_name());
//					stepAry.add(json);
//				}
				
				List<Wps> stepList = wpsService.getJunctionStep(search);//根据焊缝查工步
				for(Wps wps:stepList){
					json.put("employid", wps.getFemployee_id());
					json.put("fid", wps.getFid());
					json.put("fstep", wps.getFstep_number()+"--"+wps.getFstep_name());
					json.put("fstep_name", wps.getFstep_name());
					stepAry.add(json);
				}
				List<Wps> junList = wpsService.getJunctionWeld(search);
				for(Wps wps:junList){
					json.put("fid", wps.getFid());
					//json.put("fjunction", wps.getFjunction()+"--"+wps.getFwelding_area());
					json.put("fwelding_area", wps.getFwelding_area());
					junAry.add(json);
				}
				List<Wps> empList = wpsService.getEmployee1(String.valueOf(stepList.get(0).getFemployee_id()));
				for(Wps wps:empList){
					json.put("fid", wps.getFid());
					json.put("femployee", wps.getFemployee_id()+"--"+wps.getFemployee_name());
//					json.put("femployee_version", wps.getFemployee_version());
//					json.put("femployee_name", wps.getFemployee_name());
					empAry.add(json);
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
				
				List<Wps> junList = wpsService.getJunctionByStepid(String.valueOf(stepList.get(0).getFid()));
				for(Wps wps:junList){
					json.put("fid", wps.getFid());
					json.put("fjunction", wps.getFjunction());
					json.put("fwelding_area", wps.getFwelding_area());
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
		obj.put("junAry", junAry);
		obj.put("empAry", empAry);
		obj.put("stepAry", stepAry);
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