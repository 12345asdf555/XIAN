package com.spring.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.spring.model.Dictionarys;
import com.spring.model.Insframework;
import com.spring.model.MyUser;
import com.spring.model.User;
import com.spring.model.WeldedJunction;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.DictionaryService;
import com.spring.service.InsframeworkService;
import com.spring.service.UserService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WpsService;
import com.spring.util.IsnullUtil;

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

	@Autowired
	private WeldedJunctionService wjs;
	@Autowired
	private WpsService wpsService;
	
	@RequestMapping("/scan")
	public void scan(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject basicdata = new JSONObject();
		JSONObject weldlinedata = new JSONObject();
		JSONObject workstepdata = new JSONObject();
		JSONObject detailsdata = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String cardnumber = JSON.parseObject(jsondata.getString("data")).getString("cardnumber");
		BigInteger cardid = null;
		
		//查电子跟踪卡
		page = new Page(1,1,total);
		String search1 = " fwelded_junction_no = '" + cardnumber + "'";
		List<WeldedJunction> cardList = wjs.getCardList(page,search1);

		if(cardList.size()!=0){
			//查出电子跟踪卡id
			for(WeldedJunction wjm:cardList){
				cardid = wjm.getId();
				break;
			}
			
			//根绝电子跟踪卡查工艺规程编号
			String search2 = " AND p.fcard_id = '" + cardid + "'";
			List<WeldedJunction> productList = wjs.getProductList(page,search2);
			
			if(productList.size() != 0){
				//跟据工艺规程编号查询剩余信息
				String fwps_lib_name = "";
				String fwps_lib_version = "";
				String fproduct_number = "";
				String fproduct_drawing_no = "";
				
				//查出对应工艺规程编号和版本号
				for(WeldedJunction wjm:productList){
					fwps_lib_name = wjm.getFwps_lib_name();
					fwps_lib_version = wjm.getFwps_lib_version();
					break;
				}
				String search3 = " fwps_lib_name = '" + fwps_lib_name + "' AND fwps_lib_version = '" + fwps_lib_version + "'";
				List<Wps> wpsList = wpsService.getWpsList(page,search3);
				
				//查出产品图号
				for(Wps wps:wpsList){
					fproduct_drawing_no = wps.getFproduct_drawing_no();
				}
				//查出所有产品序号，因界面设计为输出框不是下拉框，暂无处理，界面只显示最后只显示最后一个产品序号
				for(WeldedJunction wjm:productList){
					fproduct_number = wjm.getFprefix_number()+"-"+wjm.getFproduct_number();
				}
				serrespon.put("type", "scanrespond");
				serrespon.put("respondtype", "succeed");
				data.put("datalength", "5");
				data.put("workprocedure", "焊接");
				basicdata.put("datalength", "3");
				basicdata.put("workprocedure", "焊接");
				basicdata.put("productnumber", fproduct_drawing_no);
				basicdata.put("cardnumber", cardnumber);
				basicdata.put("serialnumber", fproduct_number);
				data.put("basicdata", basicdata);
				serrespon.put("data", data);
				
				//兄弟，后面具体的WPS恕我能力不足，看不懂你们的代码，帮不到你们了，先用模拟数据代替
				weldlinedata.put("datalength", "1");
				weldlinedata.put("1", "1焊缝");
				serrespon.put("weldlinedata", weldlinedata);
				workstepdata.put("datalength", "2");
				workstepdata.put("1", "1工步");
				workstepdata.put("2", "2工步");
				serrespon.put("workstepdata", workstepdata);
				
				//假设共有两组数据(1：1焊缝+1工步    2：1焊缝+2工步)
				int detailsdatalength = 2;
				detailsdata.put("datalength", Integer.toString(detailsdatalength));
				for(int i=0;i<detailsdatalength;i++){
					if(i == 0){
						JSONObject buf = new JSONObject();
						buf.put("weldline", "1焊缝");
						buf.put("workstep", "1工步");
						buf.put("quantizationproject", "a");
						buf.put("requiredvalue", "10");
						buf.put("updeviation", "2");
						buf.put("downdeviation", "3");
						buf.put("measurement", "b");
						detailsdata.put(Integer.toString(i+1), buf);
					}else if(i == 1){
						JSONObject buf = new JSONObject();
						buf.put("weldline", "1焊缝");
						buf.put("workstep", "2工步");
						buf.put("quantizationproject", "a");
						buf.put("requiredvalue", "15");
						buf.put("updeviation", "3");
						buf.put("downdeviation", "2");
						buf.put("measurement", "b");
						detailsdata.put(Integer.toString(i+1), buf);
					}
				}
				serrespon.put("detailsdata", detailsdata);

			}else{
				//该卡号无任务
				serrespon.put("type", "scanrespond");
				serrespon.put("respondtype", "succeed");
				data.put("datalength", "0");
				serrespon.put("data", data);
			}
			
			
		}else{
			//无此卡号
			serrespon.put("type", "scanrespond");
			serrespon.put("respondtype", "default");
			data.put("datalength", "0");
			serrespon.put("data", data);
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
	 * 开始任务
	 * @param request
	 * @return
	 */
	@RequestMapping("/starttask")
	public void starttask(HttpServletRequest request,HttpServletResponse response){
		JSONObject serrespon = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String tasktype = jsondata.getString("tasktype");
		
		if("product".equals(tasktype)){   //最小单位为产品
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			serrespon.put("type", "beginrespond");
			serrespon.put("respondtype", "succeed");
		}else if("weldline".equals(tasktype)){     //最小单位为焊缝
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			String hanfenghao = JSON.parseObject(jsondata.getString("data")).getString("hanfenghao");
			serrespon.put("type", "beginrespond");
			serrespon.put("respondtype", "succeed");
		}else if("workstep".equals(tasktype)){     //最小单位为工步
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			String hanfenghao = JSON.parseObject(jsondata.getString("data")).getString("hanfenghao");
			String gongbuhao = JSON.parseObject(jsondata.getString("data")).getString("gongbuhao");
			serrespon.put("type", "beginrespond");
			serrespon.put("respondtype", "succeed");
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
		JSONObject serrespon = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String tasktype = jsondata.getString("tasktype");
		String newworkstep = jsondata.getString("data");    //下一工步
		
		String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
		String hanfenghao = JSON.parseObject(jsondata.getString("data")).getString("hanfenghao");
		String gongbuhao = JSON.parseObject(jsondata.getString("data")).getString("gongbuhao");
		
		serrespon.put("type", "worksteprespond");
		serrespon.put("respondtype", "succeed");
		serrespon.put("tasktype", "weldline");
	
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
		JSONObject serrespon = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String tasktype = jsondata.getString("tasktype");
		
		if("product".equals(tasktype)){   //最小单位为产品
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			serrespon.put("type", "overrespond");
			serrespon.put("respondtype", "succeed");
		}else if("weldline".equals(tasktype)){     //最小单位为焊缝
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			String hanfenghao = JSON.parseObject(jsondata.getString("data")).getString("hanfenghao");
			serrespon.put("type", "overrespond");
			serrespon.put("respondtype", "succeed");
		}else if("workstep".equals(tasktype)){     //最小单位为工步
			String chanpinxuhao = JSON.parseObject(jsondata.getString("data")).getString("chanpinxuhao");
			String hanfenghao = JSON.parseObject(jsondata.getString("data")).getString("hanfenghao");
			String gongbuhao = JSON.parseObject(jsondata.getString("data")).getString("gongbuhao");
			serrespon.put("type", "overrespond");
			serrespon.put("respondtype", "succeed");
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
		JSONObject serrespon = new JSONObject();
		
		JSONObject jsondata = JSON.parseObject(request.getParameter("json"));
		String product = jsondata.getString("product");     //产品号
		String weldline = jsondata.getString("weldline");   //焊缝号
		String ele = jsondata.getString("ele");			    //电流
		String vol = jsondata.getString("vol");				//电压

		serrespon.put("type", "uploadrespond");
		serrespon.put("respondtype", "succeed");
		
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
}