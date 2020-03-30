package com.spring.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.spring.dto.WeldDto;
import com.spring.model.Dictionarys;
import com.spring.model.Insframework;
import com.spring.model.MyUser;
import com.spring.model.Td;
import com.spring.model.WeldedJunction;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.DictionaryService;
import com.spring.service.TdService;
import com.spring.service.UserService;
import com.spring.service.WpsService;
import com.spring.util.IsnullUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/wps",produces = { "text/json;charset=UTF-8" })
public class WpsController {
	
	private Page page;
	private int pageIndex = 1;
	private int pageSize = 10;
	private int total = 0;
	private String wpsfid;
	private String wpspre;
	@Autowired
	private WpsService wpsService;
	@Autowired
	private TdService tdService;
	@Autowired
	private UserService userService;
	@Autowired
	private DictionaryService dm;
	
    public static final String IP_ADDR = "121.196.222.216";//服务器地址   
    public static final int PORT = 5555;//服务器端口号  
	
	IsnullUtil iutil = new IsnullUtil();
	private SocketChannel socketChannel;
	private String strdata;
	
	/**
	 * 获取所有用户列表
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/AllWps")
	public String AllUser(HttpServletRequest request){
		return "weldwps/allWps";
	}
	
	@RequestMapping("/goWpslib")
	public String goWpslib(HttpServletRequest request){
		return "wpslib/wps";
	}
	
	@RequestMapping("/goWpsdetails")
	public String goWpsdetails(HttpServletRequest request){
		String fid = request.getParameter("fid");
		String fproduct_name = request.getParameter("fproduct_name");
		String status = request.getParameter("status");
		request.setAttribute("fid", fid);
		request.setAttribute("fproduct_name", fproduct_name);
		request.setAttribute("status", status);
		String symbol = "0";
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		List<String> ls = userService.getAuthoritiesByUsername(myuser.getUsername());
		for(int i=0;i<ls.size();i++) {
			if(ls.get(i).equals("ROLE_审核员") || ls.get(i).equals("ROLE_admin")) {
				symbol = "1";
				break;
			}
		}
		request.setAttribute("symbol", symbol);
		return "wpslib/process";
	}
	
	@RequestMapping("/AllSpe")
	public String AllSpe(HttpServletRequest request){
		return "specification/allSpe";
	}

	@RequestMapping("/getAllWps")
	@ResponseBody
	public String getAllWps(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		page = new Page(pageIndex,pageSize,total);
		List<Wps> findAll = wpsService.findAll(page,parent,search);
		long total = 0;
		
		if(findAll != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(findAll);
			total = pageinfo.getTotal();
		}

		request.setAttribute("wpsList", findAll);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				String creat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate());
				String update = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFupdatedate());
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getFwpsnum());
				json.put("Fweld_I", wps.getFweld_i());
				json.put("Fweld_V", wps.getFweld_v());
				json.put("Fweld_I_MAX",wps.getFweld_i_max());
				json.put("Fweld_I_MIN", wps.getFweld_i_min());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFweld_v_min());
				json.put("Fweld_Alter_I", wps.getFweld_alter_i());
				json.put("Fweld_Alter_V", wps.getFweld_alter_v());
				json.put("Fweld_PreChannel", wps.getFweld_prechannel());
				json.put("FCReateDate",creat);
				json.put("FUpdateDate", update);
				json.put("Fowner",wps.getInsname());
				json.put("insid",wps.getInsid());
				json.put("Fback", wps.getFback());
				json.put("Fname", wps.getFname());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getAllSpe")
	@ResponseBody
	public String getAllSpe(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		BigInteger chanel = new BigInteger(request.getParameter("chanel"));
		List<Wps> findAll = wpsService.findAllSpe(machine,chanel);

		request.setAttribute("wpsList", findAll);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			/*if(findAll.size()==0){
				json.put("FWPSNum", 1);//通道号
				json.put("Fweld_I", 62);//初期条件
				json.put("Fweld_V", 62);//溶孔深度
				json.put("Fweld_I_MAX",102);//一元个别
				json.put("Fweld_I_MIN", 111);//收弧模式
				json.put("Fweld_V_MAX", 0);//电弧特性
				json.put("Fweld_V_MIN", 0);//模式
				json.put("Fweld_Alter_I", 91);//材料
				json.put("Fweld_Alter_V", 121);//气体
				json.put("Fweld_PreChannel", 132);//半径
				json.put("ftime", 30.0);
				json.put("fadvance", 1.0);
				json.put("fhysteresis", 1.0);
				json.put("fini_ele", 100.0);//初期电流
				json.put("fweld_ele", 100.0);//焊接电流
				json.put("farc_ele", 100.0);//收弧电流
				json.put("fweld_tuny_ele", 0.0);//焊接微调电流
				json.put("fweld_tuny_vol", 0.0);//焊接微调电压
				json.put("farc_tuny_ele", 0.0);//收弧微调电流
				if(Integer.valueOf(cla.toString())==102){
					json.put("fini_vol", 19.0);//初期电压
					json.put("fweld_vol", 19.0);//焊接电压
					json.put("farc_vol", 19.0);//收弧电压
					json.put("fweld_tuny_vol", 0.0);//焊接微调电压
					json.put("Fdiameter", 0.0);//收弧微调电压
				}else{
					json.put("fini_vol", 0.0);//初期电压
					json.put("fweld_vol", 0.0);//焊接电压
					json.put("farc_vol", 0.0);//收弧电压
					json.put("fweld_tuny_vol", 0.0);//焊接微调电压
					json.put("Fdiameter", 0.0);//收弧微调电压
				}
				ary.add(json);
			}else{*/
			for(Wps wps:findAll){
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getWelderid());
				json.put("Fweld_I", wps.getFinitial());
				json.put("Fweld_V", wps.getFcontroller());
				json.put("Fweld_I_MAX",wps.getInsname());
				json.put("Fweld_I_MIN", wps.getWeldername());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFmode());
				json.put("Fweld_Alter_I", wps.getUpdatename());
				json.put("Fweld_Alter_V", wps.getFback());
				json.put("Fweld_PreChannel", wps.getFname());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/Spe")
	@ResponseBody
	public String Spe(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		String ch = request.getParameter("chanel");
		List<Wps> findAll = wpsService.AllSpe(machine,ch);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				json.put("FID", wps.getFid());
				json.put("FWPSNum", wps.getWelderid());
				json.put("Fweld_I", wps.getFinitial());
				json.put("Fweld_V", wps.getFcontroller());
				json.put("Fweld_I_MAX",wps.getInsname());
				json.put("Fweld_I_MIN", wps.getWeldername());
				json.put("Fweld_V_MAX", wps.getFweld_v_max());
				json.put("Fweld_V_MIN", wps.getFmode());
				json.put("Fweld_Alter_I", wps.getUpdatename());
				json.put("Fweld_Alter_V", wps.getFback());
				json.put("Fweld_PreChannel", wps.getFname());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("Fdiameter", wps.getFdiameter());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/toAddWps")
	public String toAddUser(HttpServletRequest request){
		return "weldwps/addWps";
	}
	
	@RequestMapping("/toAddSpe")
	public String toAddSpe(HttpServletRequest request){
		return "specification/addSpe";
	}
	
	
	@RequestMapping("/toUpdateWps")
	public String toUpdateWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("wps", wps);
		request.setAttribute("update", sdf.format(wps.getFupdatedate()));
		request.setAttribute("create", sdf.format(wps.getFcreatedate()));
		return "weldwps/editWps";
	}
	
	@RequestMapping("/toUpdateSpe")
	public String toUpdateSpe(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findSpeById(fid);
		request.setAttribute("wps", wps);
		return "specification/editSpe";
	}
	
	@RequestMapping("/toDestroyWps")
	public String toDestroyWps(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request.setAttribute("wps", wps);
		request.setAttribute("update", sdf.format(wps.getFupdatedate()));
		request.setAttribute("create", sdf.format(wps.getFcreatedate()));
		return "weldwps/destroyWps";
	}
	
	@RequestMapping("/toDestroySpe")
	public String toDestroySpe(@RequestParam BigInteger fid,HttpServletRequest request){
		Wps wps = wpsService.findSpeById(fid);
		request.setAttribute("wps", wps);
		return "specification/destroySpe";
	}
	
	@RequestMapping("/addMainWps")
	@ResponseBody
	public String addMainWps(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			wps.setFwpsnum(request.getParameter("fwpsnum"));
			wps.setFweld_ele((wps.getFpreset_ele_top()+wps.getFpreset_ele_bottom())/2);
			wps.setFweld_tuny_ele(wps.getFpreset_ele_top()-(wps.getFpreset_ele_top()+wps.getFpreset_ele_bottom())/2);
			wps.setFweld_vol((wps.getFpreset_vol_top()+wps.getFpreset_vol_bottom())/2);
			wps.setFweld_tuny_vol(wps.getFpreset_vol_top()-(wps.getFpreset_vol_top()+wps.getFpreset_vol_bottom())/2);
			wpsService.addWpsDetail(wps);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateMainWps")
	@ResponseBody
	public String updateMainWps(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			wps.setFwpsnum(request.getParameter("fwpsnum"));
			wps.setFweld_ele((wps.getFpreset_ele_top()+wps.getFpreset_ele_bottom())/2);
			wps.setFweld_tuny_ele(wps.getFpreset_ele_top()-(wps.getFpreset_ele_top()+wps.getFpreset_ele_bottom())/2);
			wps.setFweld_vol((wps.getFpreset_vol_top()+wps.getFpreset_vol_bottom())/2);
			wps.setFweld_tuny_vol(wps.getFpreset_vol_top()-(wps.getFpreset_vol_top()+wps.getFpreset_vol_bottom())/2);
			wpsService.updateWpsDetailById(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/removeMainWps")
	@ResponseBody
	public String removeMainWps(HttpServletRequest request){
			BigInteger fid = new BigInteger(request.getParameter("fid"));
			JSONObject obj = new JSONObject();
			try{
				wpsService.deleteMainWps(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/saveCopy")
	@ResponseBody
	public String saveCopy(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject obj = new JSONObject();
		String ch = request.getParameter("chanel");
		String str = request.getParameter("str");
		BigInteger mac = new BigInteger(request.getParameter("mac"));
		List<Wps> findAll = wpsService.findSpe(mac,ch);
		try{
	        if(null!=str&&""!=str){
	        String[] ss = str.split(",");
	        for (int i = 0; i < ss.length; i++) {
			for(Wps spe:findAll){
			if(wpsService.findCount(new BigInteger(ss[i]),String.valueOf(spe.getFweld_i_max()))<=0){
				spe.setMacid(new BigInteger(ss[i]));
				spe.setFcreater(myuser.getId());
				spe.setFupdater(myuser.getId());
				wpsService.saveSpe(spe);
			}else{
				spe.setMacid(new BigInteger(ss[i]));
				spe.setFupdater(myuser.getId());
				wpsService.updateSpe(spe);
			}
			}
	        }
	        }
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/findCount")
	@ResponseBody
	public String findCount(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		String ch = request.getParameter("chanel");
		String str = request.getParameter("str");
		BigInteger mac = new BigInteger(request.getParameter("mac"));
		try{
			int co;
			if(null!=ch&&""!=ch){
				co=1;
			}else{
				co = wpsService.findCount(mac,ch);
			}
			BigInteger parent = null;
			List<Td> getAP = tdService.getAllPosition(parent,null);
			for(Td td:getAP){
	        if(null!=str&&""!=str){
	        String[] ss = str.split(",");
	        for (int i = 0; i < ss.length; i++) {
	        	if(td.getId()==Long.valueOf(ss[i])){
				json.put("machineid", td.getFequipment_no());
				json.put("insname", td.getFcn());
				json.put("num", "1-"+co);
				json.put("readynum", 0);
				ary.add(json);
	        	}
	        }
	        }
		}
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		obj.put("rows", ary);
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
//	@RequestMapping("/addWps")
//	@ResponseBody
//	public String addUser(HttpServletRequest request){
//		Wps wps = new Wps();
//		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
//			    .getAuthentication()  
//			    .getPrincipal();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		JSONObject obj = new JSONObject();
//		try{
//			wps.setFwpsnum(request.getParameter("fwn"));
//			wps.setFweld_i(Integer.valueOf(request.getParameter("Fweld_I")));
//			wps.setFweld_v(Integer.valueOf(request.getParameter("Fweld_V")));
//			wps.setFweld_i_max(Integer.valueOf(request.getParameter("Fweld_I_MAX")));
//			wps.setFweld_i_min(Integer.valueOf(request.getParameter("Fweld_I_MIN")));
//			wps.setFweld_v_max(Integer.valueOf(request.getParameter("Fweld_V_MAX")));
//			wps.setFweld_v_min(Integer.valueOf(request.getParameter("Fweld_V_MIN")));
//			wps.setFweld_alter_i(Integer.valueOf(request.getParameter("Fweld_Alter_I")));
//			wps.setFweld_alter_v(Integer.valueOf(request.getParameter("Fweld_Alter_V")));
//			wps.setFweld_prechannel(Integer.valueOf(request.getParameter("Fweld_PreChannel")));
//			wps.setFname(request.getParameter("Fname"));
//			wps.setFback(request.getParameter("Fback"));
//			wps.setFdiameter(Double.valueOf(request.getParameter("Fdiameter")));
//			wps.setFcreater(myuser.getId());
//			wps.setFupdater(myuser.getId());
//			wps.setFowner(Long.parseLong(request.getParameter("ins")));
//			wps.setFcreatedate(sdf.parse(sdf.format((new Date()).getTime())));
//			wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
//			wpsService.save(wps);
//			obj.put("success", true);
//		}catch(Exception e){
//			obj.put("success", false);
//			obj.put("errorMsg", e.getMessage());
//		}
//		return obj.toString();
/*		return "redirect:/user/AllUser";*/
//	}
	
//	@RequestMapping("/updateWps")
//	@ResponseBody
//	public String updateWps(Wps wps,HttpServletRequest request){
//		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
//			    .getAuthentication()  
//			    .getPrincipal();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		JSONObject obj = new JSONObject();
//		try{
//			wps.setFid(Long.parseLong(request.getParameter("FID")));
//			wps.setFupdater(myuser.getId());
//	        wps.setFowner(Long.parseLong(request.getParameter("ins")));
////	        wps.setFcreatedate(sdf.parse(request.getParameter("FCReateDate")));
//	        wps.setFwpsnum(request.getParameter("FWPSNum"));
//	        wps.setFweld_i( Integer.parseInt(request.getParameter("Fweld_I")));
//	        wps.setFweld_v( Integer.parseInt(request.getParameter("Fweld_V")));
//	        wps.setFweld_i_max(Integer.parseInt(request.getParameter("Fweld_I_MAX")));
//	        wps.setFweld_i_min(Integer.parseInt(request.getParameter("Fweld_I_MIN")));
//	        wps.setFweld_v_max(Integer.parseInt(request.getParameter("Fweld_V_MAX")));
//	        wps.setFweld_v_min(Integer.parseInt(request.getParameter("Fweld_V_MIN")));
//	        wps.setFweld_alter_i(Integer.parseInt(request.getParameter("Fweld_Alter_I")));
//	        wps.setFweld_alter_v(Integer.parseInt(request.getParameter("Fweld_Alter_V")));
//	        wps.setFweld_prechannel(Integer.parseInt(request.getParameter("Fweld_PreChannel")));
//	        wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
//	        wps.setFback(request.getParameter("Fback"));
//	        wps.setFname(request.getParameter("Fname"));
//	        wps.setFdiameter(Double.valueOf(request.getParameter("Fdiameter")));
//		    wpsService.update(wps);
//			obj.put("success", true);
//			}catch(Exception e){
//				obj.put("success", false);
//				obj.put("errorMsg", e.getMessage());
//			}
//			return obj.toString();
//
//	}
	
	@RequestMapping("/destroyWps")
	@ResponseBody
	public String destroyWps(@RequestParam BigInteger fid){

			JSONObject obj = new JSONObject();
			try{
				wpsService.delete(fid);
				wpsService.deleteHistory(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/wpsvalidate")
	@ResponseBody
	private String wpsvalidate(@RequestParam String fwpsnum){
		boolean data = true;
		int count = wpsService.getUsernameCount(fwpsnum);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/selectwps")
	public String selectwps(HttpServletRequest request){
		return "weldwps/selectWps";
	}
	
	@RequestMapping("/selectmachine")
	public String selectmachine(HttpServletRequest request){
		 wpsfid = request.getParameter("fid");
		 wpspre = request.getParameter("pre");
		return "weldwps/selectMachine";
	}
	
	@RequestMapping("/giveWM")
	@ResponseBody
	public String giveWM(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String wpsid = request.getParameter("wpsid");
		String panelnum = request.getParameter("panelnum");
		String machid = request.getParameter("machid");
		String[] wfid = wpsid.split(",");
		String[] wpre = panelnum.split(",");
		String[] mmid = machid.split(",");
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<wfid.length;i++){
				wps.setFid(Long.parseLong(wfid[i]));
		        wps.setFweld_prechannel(Integer.parseInt(wpre[i]));
				wps.setFcreater(myuser.getId());
				wps.setFupdater(myuser.getId());
				wps.setInsid(wpsService.findByUid(myuser.getId()));
				wps.setFcreatedate(sdf.parse(sdf.format((new Date()).getTime())));
				wps.setFupdatedate(sdf.parse(sdf.format((new Date()).getTime())));
				for(int j=0;j<mmid.length;j++){
					wps.setMacid(new BigInteger(mmid[j]));
					wpsService.give(wps);
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/findHistory")
	@ResponseBody
	public String findHistory(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		page = new Page(pageIndex,pageSize,total);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		page = new Page(pageIndex,pageSize,total);
		List<Wps> findHistory = wpsService.findHistory(page,parent);
		long total = 0;	
		if(findHistory != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(findHistory);
			total = pageinfo.getTotal();
		}
		try{
			for(Wps wps:findHistory){
				json.put("FWPSNum", wps.getFwpsnum());
				json.put("Fweld_PreChannel", wps.getFweld_prechannel());
				json.put("FCReateDate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate()));
				json.put("Fname", wps.getFname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
/*		return "redirect:/user/AllUser";*/
	}
	
	@RequestMapping("/getWpslibList")
	@ResponseBody
	public String getWpslibList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		String search = request.getParameter("searchStr");
		page = new Page(pageIndex,pageSize,total);
		List<Wps> getWpslibList = wpsService.getWpslibList(page,search);
		long total = 0;
		if(getWpslibList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(getWpslibList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:getWpslibList){
				json.put("fid", wps.getFid());
				json.put("wpslibName", wps.getFwpsnum());
				json.put("createdate",new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(wps.getFcreatedate()));
				json.put("status", wps.getInsname());
				json.put("statusId", wps.getFstatus());
				json.put("model", wps.getMacid());
				json.put("modelname", wps.getFname());
				json.put("manu", wps.getConname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addWpslib")
	@ResponseBody
	public String addWpslib(HttpServletRequest request){
		Wps wps = new Wps();
		MyUser myuser = (MyUser) SecurityContextHolder.getContext()  
			    .getAuthentication()  
			    .getPrincipal();
		JSONObject obj = new JSONObject();
		String wpslibName = request.getParameter("wpslibName");
		String machineModel = request.getParameter("machineModel");
		int status = Integer.valueOf(request.getParameter("fstatus"));
		try{
			wps.setFwpsnum(wpslibName);
			wps.setFback(machineModel);
			wps.setFcreater(myuser.getId());
			wps.setFstatus(status);
			wpsService.saveWpslib(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateWpslib")
	@ResponseBody
	public String updateWpslib(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject obj = new JSONObject();
		String wpslibName = request.getParameter("wpslibName");
		int status = Integer.valueOf(request.getParameter("fstatus"));
		long fid = new Long(request.getParameter("fid"));
		try{
			wps.setFid(fid);
			wps.setFwpsnum(wpslibName);
			wps.setFstatus(status);
			wpsService.updateWpslib(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/removeWpslib")
	@ResponseBody
	public String removeWpslib(HttpServletRequest request){
			BigInteger fid = new BigInteger(request.getParameter("fid"));
			JSONObject obj = new JSONObject();
			try{
				wpsService.deleteWpslib(fid);
				wpsService.deleteWpsBelongLib(fid);
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/getMainwpsList")
	@ResponseBody
	public String getMainwpsList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String parentId = request.getParameter("parent");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		List<Wps> getMainwpsList = wpsService.getMainwpsList(page,parent);
		long total = 0;
		if(getMainwpsList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(getMainwpsList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:getMainwpsList){
				json.put("fid", wps.getFid());
				json.put("fsolder_layer", wps.getFsolder_layer());
				json.put("fweld_bead", wps.getFweld_bead());
				json.put("fwpsnum", wps.getFwpsnum());
				json.put("fweld_method", wps.getFweld_method());
				json.put("fmaterial", wps.getFmaterial());
				json.put("materialname", wps.getMaterialname());
				json.put("fdiameter", wps.getFdiameter());
				json.put("dianame", wps.getDianame());
				json.put("fpreset_ele_bottom", wps.getFpreset_ele_bottom());
				json.put("fpreset_ele_top", wps.getFpreset_ele_top());
				json.put("fpreset_vol_bottom", wps.getFpreset_vol_bottom());
				json.put("fpreset_vol_top", wps.getFpreset_vol_top());
				json.put("fpower_polarity", wps.getFpower_polarity());
				json.put("fgas_flow", wps.getFgas_flow());
				json.put("fweld_speed", wps.getFweld_speed());
				json.put("fprocessid", wps.getFprocessid());
				json.put("fprocessname", wps.getFprocessname());
				json.put("insid", wps.getInsid());
				json.put("insname", wps.getInsname());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/wlvalidate")
	@ResponseBody
	private String gidvalidate(HttpServletRequest request){
		String wpsName = request.getParameter("wpsName");
		boolean data = true;
		int count = wpsService.getWpslibNameCount(wpsName);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	/**
	 * 获取工艺库状态
	 * @return
	 */
	@RequestMapping("/getStatusAll")
	@ResponseBody
	public String getStatusAll(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Wps> getWpslibStatus = wpsService.getWpslibStatus();
			for(Wps wps:getWpslibStatus){
				json.put("id", wps.getInsid());
				json.put("name", wps.getInsname());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	/**
	 * 获取工艺库状态
	 * @return
	 */
	@RequestMapping("/getCountByWpslibidChanel")
	@ResponseBody
	public String getCountByWpslibidChanel(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		BigInteger wpslibid = new BigInteger(request.getParameter("wpslibid"));
		int chanel = Integer.valueOf(request.getParameter("chanel"));
		int count = 0;
		try{
			count = wpsService.getCountByWpslibidChanel(wpslibid,chanel);
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("count", count);
		return obj.toString();
	}
	

	@RequestMapping("/getSxWpsList")
	@ResponseBody
	public String getSxWpsList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String parentId = request.getParameter("fwpslib_id");
		BigInteger parent = null;
		if(iutil.isNull(parentId)){
			parent = new BigInteger(parentId);
		}
		List<Wps> list = wpsService.getSxWpsList(page, parent);
		long total = 0;
		if(list != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				json.put("fid", list.get(i).getFid());
				json.put("fwpsnum", list.get(i).getFwpsnum());
				json.put("sxfcharacter", list.get(i).getFcharacter());
				json.put("ftime", list.get(i).getFtime());
				json.put("fhysteresis",list.get(i).getFhysteresis());
				json.put("fadvance",list.get(i).getFadvance());
				json.put("fini_ele", list.get(i).getFini_ele());
				json.put("fini_vol", list.get(i).getFini_vol());
				json.put("fini_vol1", list.get(i).getFini_vol1());
				json.put("fweld_ele", list.get(i).getFweld_ele());
				json.put("fweld_vol", list.get(i).getFweld_vol());
				json.put("fweld_vol1", list.get(i).getFweld_vol1());
				json.put("farc_ele", list.get(i).getFarc_ele());
				json.put("farc_vol", list.get(i).getFarc_vol());
				json.put("farc_vol1", list.get(i).getFarc_vol1());
				json.put("fweld_tuny_ele", list.get(i).getFweld_tuny_ele());
				json.put("fweld_tuny_vol", list.get(i).getFweld_tuny_vol());
				json.put("farc_tuny_vol", list.get(i).getFarc_tuny_vol());
				json.put("farc_tuny_ele", list.get(i).getFarc_tuny_ele());
				json.put("fpreset_ele_top", list.get(i).getFpreset_ele_top());
				json.put("fpreset_vol_top", list.get(i).getFpreset_vol_top());
				json.put("fpreset_ele_bottom", list.get(i).getFpreset_ele_bottom());
				json.put("fpreset_vol_bottom", list.get(i).getFpreset_vol_bottom());
				json.put("farc_vol_top", list.get(i).getFarc_vol_top());
				json.put("fpreset_ele_warn_top", list.get(i).getFpreset_ele_warn_top());
				json.put("fpreset_vol_warn_top", list.get(i).getFpreset_vol_warn_top());
				json.put("fpreset_ele_warn_bottom", list.get(i).getFpreset_ele_warn_bottom());
				json.put("fpreset_vol_warn_bottom", list.get(i).getFpreset_vol_warn_bottom());
				json.put("fini_ele_warn_top", list.get(i).getFini_ele_warn_top());
				json.put("fini_vol_warn_top", list.get(i).getFini_vol_warn_top());
				json.put("fini_ele_warn_bottom", list.get(i).getFini_ele_warn_bottom());
				json.put("fini_vol_warn_bottom", list.get(i).getFini_vol_warn_bottom());
				json.put("farc_ele_warn_top", list.get(i).getFarc_ele_warn_top());
				json.put("farc_vol_warn_top", list.get(i).getFarc_vol_warn_top());
				json.put("farc_ele_warn_bottom", list.get(i).getFarc_ele_warn_bottom());
				json.put("farc_vol_warn_bottom", list.get(i).getFarc_vol_warn_bottom());
				json.put("farc_delay_time", list.get(i).getFarc_delay_time());
				json.put("fwarn_delay_time", list.get(i).getFwarn_delay_time());
				json.put("fwarn_stop_time", list.get(i).getFwarn_stop_time());
				json.put("fflow_top", list.get(i).getFflow_top());
				json.put("fflow_bottom", list.get(i).getFflow_bottom());
				json.put("fdelay_time", list.get(i).getFdelay_time());
				json.put("fover_time", list.get(i).getFover_time());
				json.put("ffixed_cycle", list.get(i).getFfixed_cycle());
				json.put("selectname", list.get(i).getSelectname());
				json.put("gasname", list.get(i).getGasname());
				json.put("dianame", list.get(i).getDianame());
				json.put("materialname", list.get(i).getMaterialname());
				json.put("fcontrollername", list.get(i).getConname());
				json.put("farcname", list.get(i).getArcname());
				json.put("ininame", list.get(i).getFinitial());
				json.put("fselect", list.get(i).getFselect());
				json.put("farc", list.get(i).getFarc());
				json.put("fmaterial", list.get(i).getFmaterial());
				json.put("fdiameter", list.get(i).getFdiameter());
				json.put("fcontroller", list.get(i).getFcontroller());
				json.put("finitial", list.get(i).getFini());
				json.put("fgas", list.get(i).getFgas());
				json.put("charactername", list.get(i).getFcharacter()==0?"停机":"不停机");
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addSxWps")
	@ResponseBody
	public String addSxWps(HttpServletRequest request,Wps wps){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject obj = new JSONObject();
		try{
			wps.setFcreater(myuser.getId());
			wps.setFupdater(myuser.getId());
			wps.setFcharacter(Integer.parseInt(request.getParameter("sxfcharacter")));
			wpsService.saveSxWps(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return obj.toString();
	}

	
	@RequestMapping("/editSxWps")
	@ResponseBody
	public String editSxWps(HttpServletRequest request,Wps wps){
		MyUser myuser = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		JSONObject obj = new JSONObject();
		try{
			wps.setFupdater(myuser.getId());
			wps.setFcharacter(Integer.parseInt(request.getParameter("sxfcharacter")));
			wpsService.editSxWps(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	/**
	 * 获取字典值
	 * @return
	 */
	@RequestMapping("/getDictionary")
	@ResponseBody
	public String getMaterial(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Dictionarys> dictionary = dm.getDictionaryValue(Integer.parseInt(request.getParameter("typeid")));
			for(Dictionarys d:dictionary){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}

	@RequestMapping("/CRC7Check")
	@ResponseBody
	public String crc7Check(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		String[] str = request.getParameter("crc7_str").split(",");
		
		byte[] by1 = new byte[str.length];
		for(int i=0;i<str.length;i++){
			by1[i] = (byte)Integer.parseInt(str[i],16);
		}
		byte[] crc7byte = {
				0x00,0x09,0x12,0x1b,0x24,0x2d,0x36,0x3f,
				0x48,0x41,0x5a,0x53,0x6c,0x65,0x7e,0x77,
				0x19,0x10,0x0b,0x02,0x3d,0x34,0x2f,0x26,
				0x51,0x58,0x43,0x4a,0x75,0x7C,0x67,0x6e,
				0x32,0x3b,0x20,0x29,0x16,0x1f,0x04,0x0d,
				0x7a,0x73,0x68,0x61,0x5e,0x57,0x4c,0x45,
				0x2b,0x22,0x39,0x30,0x0f,0x06,0x1d,0x14,
				0x63,0x6a,0x71,0x78,0047,0x4e,0x55,0x5c,
				0x64,0x6d,0x76,0x7f,0x40,0x49,0x52,0x5b,
				0x2c,0x25,0x3e,0x37,0x08,0x01,0x1a,0x13,
				0x7d,0x74,0x6f,0x66,0x59,0x50,0x4b,0x42,
				0x35,0x3c,0x27,0x2e,0x11,0x18,0x03,0x0a,
				0x56,0x5f,0x44,0x4d,0x72,0x7b,0x60,0x69,
				0x1e,0x17,0x0c,0x05,0x3a,0x33,0x28,0x21,
				0x4f,0x46,0x5d,0x54,0x6b,0x62,0x79,0x70,
				0x07,0x0e,0x15,0x1c,0x23,0x2a,0x31,0x38,
				0x41,0x48,0x53,0x5a,0x65,0x6C,0x77,0x7e,
				0x09,0x00,0x1b,0x12,0x2d,0x24,0x3f,0x36,
				0x58,0x51,0x4a,0x43,0x7c,0x75,0x6e,0x67,
				0x10,0x19,0x02,0x0b,0x34,0x3d,0x26,0x2f,
				0x73,0x7a,0x61,0x68,0x57,0x5e,0x45,0x4c,
				0x3b,0x32,0x29,0x20,0x1f,0x16,0x0d,0x04,
				0x6a,0x63,0x78,0x71,0x4e,0x47,0x5c,0x55,
				0x22,0x2b,0x30,0x39,0x06,0x0f,0x14,0x1d,
				0x25,0x2C,0x37,0x3e,0x01,0x08,0x13,0x1a,
				0x6d,0x64,0x7f,0x76,0x49,0x40,0x5b,0x52,
				0x3C,0x35,0x2e,0x27,0x18,0x11,0x0a,0x03,
				0x74,0x7d,0x66,0x6f,0x50,0x59,0x42,0x4b,
				0x17,0x1e,0x05,0x0c,0x33,0x3a,0x21,0x28,
				0x5f,0x56,0x4d,0x44,0x7b,0x72,0x69,0x60,
				0x0e,0x07,0x1c,0x15,0x2a,0x23,0x38,0x31,
				0x46,0x4f,0x54,0x5d,0x62,0x6b,0x70,0x79};
		byte result = 0;  
        for (int i = 0; i < (0 + by1.length); ++i) {  
        	result = crc7byte[(0x00ff & ((result <<1) ^ by1[i]))];  
        }  
        obj.put("CRC7_code", result);
        return obj.toString();
	}
	
	@RequestMapping("/getWpslibMachineHistory")
	@ResponseBody
	public String getWpslibMachineHistory(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String machineNum = request.getParameter("machineNum");
		String wpslibName = request.getParameter("wpslibName");
		String time1 = request.getParameter("dtoTime1");
		String time2 = request.getParameter("dtoTime2");
		if(iutil.isNull(machineNum)){
			machineNum = "'%" + machineNum + "%'";
		}
		if(iutil.isNull(wpslibName)){
			wpslibName = "'%" + wpslibName + "%'";
		}
		WeldDto dto = new WeldDto();
		if(iutil.isNull(time1)){
			dto.setDtoTime1(time1);
		}
		if(iutil.isNull(time2)){
			dto.setDtoTime2(time2);
		}
		List<Wps> list = wpsService.getWpslibMachineHistoryList(page, machineNum, wpslibName, dto);
		long total = 0;
		if(list != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(list);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(int i=0;i<list.size();i++){
				if(String.valueOf(list.get(i).getMacid()).equals("171")){
					json.put("fid", list.get(i).getFid());
					json.put("machineNum", list.get(i).getInsname());
					json.put("wpslibName", list.get(i).getFwpsnum());
					json.put("machineModel",list.get(i).getMacid());
					json.put("chanel",list.get(i).getInsid());
					json.put("updateTime", list.get(i).getUpdatename());
					json.put("weld_ele", list.get(i).getFweld_ele());
					json.put("warn_ele_up", list.get(i).getFwarn_ele_up());
					json.put("warn_ele_down", list.get(i).getFwarn_ele_down());
					json.put("weld_vol", list.get(i).getFweld_vol());
					json.put("warn_vol_up", list.get(i).getFwarn_vol_up());
					json.put("warn_vol_down", list.get(i).getFwarn_vol_down());
					ary.add(json);
				}else{
					json.put("fid", list.get(i).getFid());
					json.put("machineNum", list.get(i).getInsname());
					json.put("wpslibName", list.get(i).getFwpsnum());
					json.put("machineModel",list.get(i).getMacid());
					json.put("chanel",list.get(i).getInsid());
					json.put("updateTime", list.get(i).getUpdatename());
					json.put("weld_ele", "");
					json.put("warn_ele_up", list.get(i).getFpreset_ele_warn_top());
					json.put("warn_ele_down", list.get(i).getFpreset_ele_warn_bottom());
					json.put("weld_vol", "");
					json.put("warn_vol_up", list.get(i).getFpreset_vol_warn_top());
					json.put("warn_vol_down", list.get(i).getFpreset_vol_warn_bottom());
					ary.add(json);
				}
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getSpeDetail")
	@ResponseBody
	public String getSpeDetail(HttpServletRequest request){
		String machineId = request.getParameter("machineId");
		String chanel = request.getParameter("chanel");
		String time = request.getParameter("time");
		String machineModel = request.getParameter("machineModel");
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			if(machineModel.equals("171")){
				Wps wps = wpsService.getOtcDetail(machineId,chanel,time);
				json.put("fid", wps.getFid());
				json.put("fchanel", wps.getWelderid());
				json.put("finitial", "否");
				json.put("initial", "0");
				if(Integer.valueOf(wps.getFinitial())==1){
					json.put("finitial", "是");
					json.put("initial", "1");
				}
				json.put("fcontroller", "否");
				json.put("controller", "0");
				if(Integer.valueOf(wps.getFcontroller())==1){
					json.put("fcontroller", "是");
					json.put("controller", "1");
				}
				json.put("fselect",wps.getInsname());
				json.put("fselectname",wps.getSelectname());
				json.put("farc", wps.getWeldername());
				json.put("farcname", wps.getArcname());
				json.put("fcharacter", wps.getFweld_v_max());
				json.put("fmode", "否");
				json.put("mode", "0");
				if(Integer.valueOf(wps.getFmode())==1){
					json.put("fmode", "是");
					json.put("mode", "1");
				}
				json.put("ftorch", "否");
				json.put("torch", "0");
				if(wps.getFtorch()==1){
					json.put("ftorch", "是");
					json.put("torch", "1");
				}
				json.put("fmaterial", wps.getUpdatename());
				json.put("fmaterialname", wps.getMaterialname());
				json.put("fgas", wps.getFback());
				json.put("fgasname", wps.getGasname());
				json.put("fdiameter", wps.getFname());
				json.put("fdiametername", wps.getDianame());
				json.put("ftime", wps.getFtime());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("fini_ele", wps.getFini_ele());
				json.put("fini_vol", wps.getFini_vol());
				json.put("fini_vol1", wps.getFini_vol1());
				json.put("fweld_ele", wps.getFweld_ele());
				json.put("fweld_vol", wps.getFweld_vol());
				json.put("fweld_vol1", wps.getFweld_vol1());
				json.put("farc_ele", wps.getFarc_ele());
				json.put("farc_vol", wps.getFarc_vol());
				json.put("farc_vol1", wps.getFarc_vol1());
				json.put("fweld_tuny_ele", wps.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", wps.getFweld_tuny_vol());
				json.put("farc_tuny_ele", wps.getFarc_tuny_ele());
				json.put("farc_tuny_vol", wps.getFdiameter());
				json.put("fweldprocess", wps.getFprocessid());
				json.put("fprocessname", wps.getFprocessname());
				json.put("fwarn_ele_up", wps.getFwarn_ele_up());
				json.put("fwarn_ele_down", wps.getFwarn_ele_down());
				json.put("fwarn_vol_up", wps.getFwarn_vol_up());
				json.put("fwarn_vol_down", wps.getFwarn_vol_down());
				ary.add(json);
			}else{
				Wps list = wpsService.getSxDetail(machineId,chanel,time);
				json.put("fid", list.getFid());
				json.put("fwpsnum", list.getFwpsnum());
				json.put("sxfcharacter", list.getFcharacter());
				json.put("ftime", list.getFtime());
				json.put("fhysteresis",list.getFhysteresis());
				json.put("fadvance",list.getFadvance());
				json.put("fini_ele", list.getFini_ele());
				json.put("fini_vol", list.getFini_vol());
				json.put("fini_vol1", list.getFini_vol1());
				json.put("fweld_ele", list.getFweld_ele());
				json.put("fweld_vol", list.getFweld_vol());
				json.put("fweld_vol1", list.getFweld_vol1());
				json.put("farc_ele", list.getFarc_ele());
				json.put("farc_vol", list.getFarc_vol());
				json.put("farc_vol1", list.getFarc_vol1());
				json.put("fweld_tuny_ele", list.getFweld_tuny_ele());
				json.put("fweld_tuny_vol", list.getFweld_tuny_vol());
				json.put("farc_tuny_vol", list.getFarc_tuny_vol());
				json.put("farc_tuny_ele", list.getFarc_tuny_ele());
				json.put("fpreset_ele_top", list.getFpreset_ele_top());
				json.put("fpreset_vol_top", list.getFpreset_vol_top());
				json.put("fpreset_ele_bottom", list.getFpreset_ele_bottom());
				json.put("fpreset_vol_bottom", list.getFpreset_vol_bottom());
				json.put("farc_vol_top", list.getFarc_vol_top());
				json.put("fpreset_ele_warn_top", list.getFpreset_ele_warn_top());
				json.put("fpreset_vol_warn_top", list.getFpreset_vol_warn_top());
				json.put("fpreset_ele_warn_bottom", list.getFpreset_ele_warn_bottom());
				json.put("fpreset_vol_warn_bottom", list.getFpreset_vol_warn_bottom());
				json.put("fini_ele_warn_top", list.getFini_ele_warn_top());
				json.put("fini_vol_warn_top", list.getFini_vol_warn_top());
				json.put("fini_ele_warn_bottom", list.getFini_ele_warn_bottom());
				json.put("fini_vol_warn_bottom", list.getFini_vol_warn_bottom());
				json.put("farc_ele_warn_top", list.getFarc_ele_warn_top());
				json.put("farc_vol_warn_top", list.getFarc_vol_warn_top());
				json.put("farc_ele_warn_bottom", list.getFarc_ele_warn_bottom());
				json.put("farc_vol_warn_bottom", list.getFarc_vol_warn_bottom());
				json.put("farc_delay_time", list.getFarc_delay_time());
				json.put("fwarn_delay_time", list.getFwarn_delay_time());
				json.put("fwarn_stop_time", list.getFwarn_stop_time());
				json.put("fflow_top", list.getFflow_top());
				json.put("fflow_bottom", list.getFflow_bottom());
				json.put("fdelay_time", list.getFdelay_time());
				json.put("fover_time", list.getFover_time());
				json.put("ffixed_cycle", list.getFfixed_cycle());
				json.put("selectname", list.getSelectname());
				json.put("gasname", list.getGasname());
				json.put("dianame", list.getDianame());
				json.put("materialname", list.getMaterialname());
				json.put("fcontrollername", list.getConname());
				json.put("farcname", list.getArcname());
				json.put("ininame", list.getFinitial());
				json.put("fselect", list.getFselect());
				json.put("farc", list.getFarc());
				json.put("fmaterial", list.getFmaterial());
				json.put("fdiameter", list.getFdiameter());
				json.put("fcontroller", list.getFcontroller());
				json.put("finitial", list.getFini());
				json.put("fgas", list.getFgas());
				json.put("charactername", list.getFcharacter()==0?"停机":"不停机");
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/saveGiveWpsHistory")
	@ResponseBody
	public String saveGiveWpsHistory(HttpServletRequest request){
		JSONObject json = new JSONObject();
		JSONArray wpsary = new JSONArray();
		JSONArray machineary = new JSONArray();
		JSONObject obj = new JSONObject();
		String mainwps = request.getParameter("mainwps");
		String machine = request.getParameter("machine");
		String wpslib = request.getParameter("wpslib");
		int flag = Integer.valueOf(request.getParameter("flag"));
		wpsary = JSONArray.fromObject(mainwps);
		machineary = JSONArray.fromObject(machine);
		try{
			Wps wps = new Wps();
			if(flag==0){
				for(int i=0;i<machineary.size();i++){
					for(int j=0;j<wpsary.size();j++){
						wps.setFweld_i_max(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fchanel"))));
						wps.setFweld_i_min(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("initial"))));
						wps.setFweld_alter_i(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("controller"))));
						wps.setFweld_v_min(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("mode"))));
						wps.setFweld_i(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fselect"))));
						wps.setFweld_v(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc"))));
						wps.setFweld_v_max(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fcharacter"))));
						wps.setFweld_prechannel(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fmaterial"))));
						wps.setFweld_alter_v(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fgas"))));
						wps.setInsid(new BigInteger(String.valueOf(wpsary.getJSONObject(j).get("fdiameter"))));
						wps.setFtime(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ftime"))));
						wps.setFadvance(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fadvance"))));
						wps.setFhysteresis(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fhysteresis"))));
						wps.setFini_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele"))));
						wps.setFini_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol"))));
						wps.setFini_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol1"))));
						wps.setFweld_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_ele"))));
						wps.setFweld_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol"))));
						wps.setFweld_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol1"))));
						wps.setFarc_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele"))));
						wps.setFarc_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol"))));
						wps.setFarc_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol1"))));
						wps.setFweld_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_ele"))));
						wps.setFweld_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_vol"))));
						wps.setFarc_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_ele"))));
						wps.setFdiameter(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_vol"))));
						wps.setFid(Long.valueOf(wpslib));
						wps.setFprocessid(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweldprocess"))));
						wps.setFtorch(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("torch"))));
						wps.setFwarn_ele_up(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_ele_up"))));
						wps.setFwarn_ele_down(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_ele_down"))));
						wps.setFwarn_vol_up(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_vol_up"))));
						wps.setFwarn_vol_down(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_vol_down"))));
						wps.setMacid(new BigInteger(String.valueOf(machineary.getJSONObject(i).get("id"))));
						wpsService.saveOtcWpsHistory(wps);
						obj.put("success", true);
					}
				}
			}else{
				for(int i=0;i<machineary.size();i++){
					for(int j=0;j<wpsary.size();j++){
						wps.setFid(Long.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fid"))));
						wps.setFwpsnum(String.valueOf(wpsary.getJSONObject(j).get("fwpsnum")));
						wps.setFcharacter(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("sxfcharacter"))));
						wps.setFtime(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ftime"))));
						wps.setFhysteresis(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fhysteresis"))));
						wps.setFadvance(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fadvance"))));
						wps.setFini_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele"))));
						wps.setFini_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol"))));
						wps.setFini_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol1"))));
						wps.setFweld_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_ele"))));
						wps.setFweld_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol"))));
						wps.setFweld_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_vol1"))));
						wps.setFarc_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele"))));
						wps.setFarc_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol"))));
						wps.setFarc_vol1(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol1"))));
						wps.setFweld_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_ele"))));
						wps.setFweld_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fweld_tuny_vol"))));
						wps.setFarc_tuny_vol(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_vol"))));
						wps.setFarc_tuny_ele(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_tuny_ele"))));
						wps.setFpreset_ele_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_top"))));
						wps.setFpreset_vol_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_top"))));
						wps.setFpreset_ele_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_bottom"))));
						wps.setFpreset_vol_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_bottom"))));
						wps.setFarc_vol_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_top"))));
						wps.setFpreset_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_warn_top"))));
						wps.setFpreset_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_warn_top"))));
						wps.setFpreset_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_ele_warn_bottom"))));
						wps.setFpreset_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fpreset_vol_warn_bottom"))));
						wps.setFini_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele_warn_top"))));
						wps.setFini_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol_warn_top"))));
						wps.setFini_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_ele_warn_bottom"))));
						wps.setFini_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fini_vol_warn_bottom"))));
						wps.setFarc_ele_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele_warn_top"))));
						wps.setFarc_vol_warn_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_warn_top"))));
						wps.setFarc_ele_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_ele_warn_bottom"))));
						wps.setFarc_vol_warn_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_vol_warn_bottom"))));
						wps.setFarc_delay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc_delay_time"))));
						wps.setFwarn_delay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_delay_time"))));
						wps.setFwarn_stop_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fwarn_stop_time"))));
						wps.setFflow_top(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fflow_top"))));
						wps.setFflow_bottom(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fflow_bottom"))));
						wps.setFdelay_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fdelay_time"))));
						wps.setFover_time(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fover_time"))));
						wps.setFfixed_cycle(Double.valueOf(String.valueOf(wpsary.getJSONObject(j).get("ffixed_cycle"))));
						wps.setFselect(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fselect"))));
						wps.setFarc(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("farc"))));
						wps.setFmaterial(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fmaterial"))));
						wps.setFdiameter(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fdiameter"))));
						wps.setFcontroller(String.valueOf(wpsary.getJSONObject(j).get("fcontroller")));
						wps.setFinitial(String.valueOf(wpsary.getJSONObject(j).get("finitial")));
						wps.setFgas(Integer.valueOf(String.valueOf(wpsary.getJSONObject(j).get("fgas"))));
						wps.setMacid(new BigInteger(String.valueOf(machineary.getJSONObject(i).get("id"))));
						wps.setFwpslib_id(new BigInteger(wpslib));
						wpsService.saveSxWpsHistory(wps);
						obj.put("success", true);
					}
				}
			}
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
			e.getMessage();
		}
		return obj.toString();
	}
	
	@RequestMapping("/getFnsDetail")
	@ResponseBody
	public String getFnsDetail(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		String chanel = request.getParameter("chanel");
		List<Wps> findAll = wpsService.getFnsDetail(machine,chanel);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				json.put("f001", wps.getF001());
				json.put("f002", wps.getF002());
				json.put("f003", wps.getF003());
				json.put("fadvance", wps.getFadvance());
				json.put("fhysteresis", wps.getFhysteresis());
				json.put("f004", wps.getF004());
				json.put("f005", wps.getF005());
				json.put("f006", wps.getF006());
				json.put("f007", wps.getF007());
				json.put("f008", wps.getF008());
				json.put("f009", wps.getF009());
				json.put("f010", wps.getF010());
				json.put("f011", wps.getF011());
				json.put("f012", wps.getF012());
				json.put("f013", wps.getF013());
				json.put("f014", wps.getF014());
				json.put("f015", wps.getF015());
				json.put("f016", wps.getF016());
				json.put("f017", wps.getF017());
				json.put("f018", wps.getF018());
				json.put("f019", wps.getF019());
				json.put("f020", wps.getF020());
				json.put("f021", wps.getF021());
				json.put("f022", wps.getF022());
				json.put("f023", wps.getF023());
				json.put("f024", wps.getF024());
				json.put("f025", wps.getF025());
				json.put("f026", wps.getF026());
				json.put("f027", wps.getF027());
				json.put("f028", wps.getF028());
				json.put("f029", wps.getF029());
				json.put("f030", wps.getF030());
				json.put("f031", wps.getF031());
				json.put("f032", wps.getF032());
				json.put("f033", wps.getF033());
				json.put("f034", wps.getF034());
				json.put("f035", wps.getF035());
				json.put("f036", wps.getF036());
				json.put("f037", wps.getF037());
				json.put("f038", wps.getF038());
				json.put("f039", wps.getF039());
				json.put("f040", wps.getF040());
				json.put("f041", wps.getF041());
				json.put("f042", wps.getF042());
				json.put("f043", wps.getF043());
				json.put("f044", wps.getF044());
				json.put("f045", wps.getF045());
				json.put("f046", wps.getF046());
				json.put("f047", wps.getF047());
				json.put("f048", wps.getF048());
				json.put("f049", wps.getF049());
				json.put("f050", wps.getF050());
				if (chanel.length() < 4) {
					int length = 4 - chanel.length();
					for (int i = 0; i < length; i++) {
						chanel = "0" + chanel;
					}
				}
				json.put("jobno", chanel);
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getFnsJobList")
	@ResponseBody
	public String getFnsJobList(HttpServletRequest request){
		BigInteger machine = new BigInteger(request.getParameter("machine"));
		List<Wps> findAll = wpsService.getFnsJobList(machine);
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:findAll){
				String jobno = wps.getFwpsnum();
				if (jobno.length() < 4) {
					int length = 4 - jobno.length();
					for (int i = 0; i < length; i++) {
						jobno = "0" + jobno;
					}
				}
				json.put("jobno", jobno);
				json.put("jobname", wps.getF024());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addJobNoDetail")
	@ResponseBody
	public String addJobNoDetail(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject obj = new JSONObject();
		try{
			wps.setF001("0");
			wps.setF002("0");
			wps.setF003("0");
			wps.setF004("135");
			wps.setF005("0");
			wps.setF006("0");
			wps.setF007("1");
			wps.setF008("1");
			wps.setF009("50");
			wps.setF010("0");
			wps.setF011("0");
			wps.setF012("0");
			wps.setF013("0");
			wps.setF014("0");
			wps.setF015("0");
			wps.setF016("0");
			wps.setF017("0");
			wps.setF018("0");
			wps.setF019("0");
			wps.setF020("-1");
			wps.setF021("1");
			wps.setF022("-10");
			wps.setF023("10");
			wps.setF024(request.getParameter("jobname"));
			wps.setF025("9.0");
			wps.setF027("0");
			wps.setF028("0");
			wps.setF029("10");
			wps.setF030("2");
			wps.setF031("3");
			wps.setF032("50");
			wps.setF033("0");
			wps.setF034("0");
			wps.setF035("0");
			wps.setF036("0");
			wps.setF037("0");
			wps.setF038("0");
			wps.setF039("15");
			wps.setF040("0");
			wps.setF041("0");
			wps.setF042("25");
			wps.setF043("0");
			wps.setF044("250");
			wps.setF045("0");
			wps.setF046("10");
			wps.setF047("-1");
			wps.setF048("1");
			wps.setF049("0");
			wps.setF050("0");
			wps.setFadvance(0.1);
			wps.setFhysteresis(0.5);
			wps.setFwpsnum(request.getParameter("jobno"));
			wps.setMacid(new BigInteger(request.getParameter("machid")));
			wpsService.addJob(wps);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateJobNoDetail")
	@ResponseBody
	public String updateJobNoDetail(HttpServletRequest request){
		Wps wps = new Wps();
		JSONObject obj = new JSONObject();
		int flag = Integer.valueOf(request.getParameter("flag"));
		try{
			wps.setF001(request.getParameter("f0011"));
			wps.setF002(request.getParameter("f0021"));
			wps.setF003(request.getParameter("f0031"));
			wps.setF004(request.getParameter("f0041"));
			wps.setF005(request.getParameter("f0051"));
			wps.setF006(request.getParameter("f0061"));
			wps.setF007(request.getParameter("f0071"));
			wps.setF008(request.getParameter("f0081"));
			wps.setF009(request.getParameter("f0091"));
			wps.setF010(request.getParameter("f0101"));
			wps.setF011(request.getParameter("f0111"));
			wps.setF012(request.getParameter("f0121"));
			wps.setF013(request.getParameter("f0131"));
			wps.setF014(request.getParameter("f0141"));
			wps.setF015(request.getParameter("f0151"));
			wps.setF016(request.getParameter("f0161"));
			wps.setF017(request.getParameter("f0171"));
			wps.setF018(request.getParameter("f0181"));
			wps.setF019(request.getParameter("f0191"));
			wps.setF020(request.getParameter("f0201"));
			wps.setF021(request.getParameter("f0211"));
			wps.setF022(request.getParameter("f0221"));
			wps.setF023(request.getParameter("f0231"));
			wps.setF024(request.getParameter("f0241"));
			wps.setF025(request.getParameter("f0251"));
			wps.setF026(request.getParameter("f0261"));
			wps.setF027(request.getParameter("f0271"));
			wps.setF028(request.getParameter("f0281"));
			wps.setF029(request.getParameter("f0291"));
			wps.setF030(request.getParameter("f0301"));
			wps.setF031(request.getParameter("f0311"));
			wps.setF032(request.getParameter("f0321"));
			wps.setF033(request.getParameter("f0331"));
			wps.setF034(request.getParameter("f0341"));
			wps.setF035(request.getParameter("f0351"));
			wps.setF036(request.getParameter("f0361"));
			wps.setF037(request.getParameter("f0371"));
			wps.setF038(request.getParameter("f0381"));
			wps.setF039(request.getParameter("f0391"));
			wps.setF040(request.getParameter("f0401"));
			wps.setF041(request.getParameter("f0411"));
			wps.setF042(request.getParameter("f0421"));
			wps.setF043(request.getParameter("f0431"));
			wps.setF044(request.getParameter("f0441"));
			wps.setF045(request.getParameter("f0451"));
			wps.setF046(request.getParameter("f0461"));
			wps.setF047(request.getParameter("f0471"));
			wps.setF048(request.getParameter("f0481"));
			wps.setF049(request.getParameter("f0491"));
			wps.setF050(request.getParameter("f0501"));
			wps.setFadvance(Double.valueOf(request.getParameter("fadvance")));
			wps.setFhysteresis(Double.valueOf(request.getParameter("fhysteresis")));
			wps.setFwpsnum(request.getParameter("jobno"));
			wps.setMacid(new BigInteger(request.getParameter("machid")));
			if(flag==0) {
				wpsService.updateJob(wps);
			}else {
				wpsService.addJob(wps);
			}
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/deleteJob")
	@ResponseBody
	public String deleteJob(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			String chanel = request.getParameter("jobno");
			String machine = request.getParameter("machid");
			wpsService.deleteJob(machine,chanel);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/getTpsiMaterial")
	@ResponseBody
	public String getTpsiMaterial(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<String> findAll = wpsService.getTpsiMaterial();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			for(String str:findAll){
				json.put("name", str);
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getTpsiWire")
	@ResponseBody
	public String getTpsiWire(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<String> findAll = wpsService.getTpsiWire();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			for(String str:findAll){
				json.put("name", str);
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getTpsiGas")
	@ResponseBody
	public String getTpsiGas(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<String> findAll = wpsService.getTpsiGas();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			for(String str:findAll){
				json.put("name", str);
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/getDictionaryValue")
	@ResponseBody
	public String getDictionaryValue(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		List<Dictionarys> materialList = dm.getDictionaryValue(9);
		List<Dictionarys> diameterList = dm.getDictionaryValue(13);
		List<Dictionarys> processList = dm.getDictionaryValue(22);
		JSONObject json = new JSONObject();
		JSONArray mary = new JSONArray();
		JSONArray dary = new JSONArray();
		JSONArray pary = new JSONArray();
		try{
			for(Dictionarys d:materialList){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				mary.add(json);
			}
			for(Dictionarys d:diameterList){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				dary.add(json);
			}
			for(Dictionarys d:processList){
				json.put("id", d.getValue());
				json.put("name", d.getValueName());
				pary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("mary", mary);
		obj.put("dary", dary);
		obj.put("pary", pary);
		return obj.toString();
	}
	
	@RequestMapping("/getWpsList")
	@ResponseBody
	public String getWpsList(HttpServletRequest request){
		pageIndex = Integer.parseInt(request.getParameter("page"));
		pageSize = Integer.parseInt(request.getParameter("rows"));
		page = new Page(pageIndex,pageSize,total);
		String search = request.getParameter("search");
		List<Wps> wpsList = wpsService.getWpsList(page,search);
		long total = 0;
		if(wpsList != null){
			PageInfo<Wps> pageinfo = new PageInfo<Wps>(wpsList);
			total = pageinfo.getTotal();
		}
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			for(Wps wps:wpsList){
				json.put("fid", wps.getFid());
				json.put("fproduct_drawing_no", wps.getFproduct_drawing_no());
				json.put("fproduct_name", wps.getFproduct_name());
				json.put("fproduct_version", wps.getFproduct_version());
				json.put("fwps_lib_name", wps.getFwpsnum());
				json.put("fwps_lib_version", wps.getFwps_lib_version());
				int flag = wps.getFlag();
				json.put("flag", flag);
				if(flag == 0) {
					json.put("flag_name", "自建");
				}else {
					json.put("flag_name", "MES");
				}
				int fstatus = wps.getFstatus();
				json.put("fstatus", fstatus);
				json.put("fback", wps.getFback());
				ary.add(json);
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("total", total);
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/addWps")
	@ResponseBody
	public String addWps(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			String fproduct_drawing_no = request.getParameter("fproduct_drawing_no");
			String fproduct_name = request.getParameter("fproduct_name");
			String fproduct_version = request.getParameter("fproduct_version");
			String fwps_lib_name = request.getParameter("fwps_lib_name");
			String fwps_lib_version = request.getParameter("fwps_lib_version");
			String wpsFlag = request.getParameter("wpsFlag");
			wps.setFproduct_drawing_no(fproduct_drawing_no);
			wps.setFproduct_name(fproduct_name);
			wps.setFproduct_version(fproduct_version);
			wps.setFwpsnum(fwps_lib_name);
			wps.setFwps_lib_version(fwps_lib_version);
			wps.setFlag(Integer.parseInt(wpsFlag));
			wpsService.addWps(wps);
			obj.put("wpsId", wps.getFid());
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("wpsId","");
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/updateWps")
	@ResponseBody
	public String updateWps(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String fproduct_drawing_no = request.getParameter("fproduct_drawing_no");
			String fproduct_name = request.getParameter("fproduct_name");
			String fproduct_version = request.getParameter("fproduct_version");
			String fwps_lib_name = request.getParameter("fwps_lib_name");
			String fwps_lib_version = request.getParameter("fwps_lib_version");
			String wpsFlag = request.getParameter("wpsFlag");
			wps.setFid(Long.parseLong(fid));
			wps.setFproduct_drawing_no(fproduct_drawing_no);
			wps.setFproduct_name(fproduct_name);
			wps.setFproduct_version(fproduct_version);
			wps.setFwpsnum(fwps_lib_name);
			wps.setFwps_lib_version(fwps_lib_version);
			wps.setFlag(Integer.parseInt(wpsFlag));
			wpsService.updateWps(wps);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/deleteWps")
	@ResponseBody
	public String deleteWps(HttpServletRequest request){
			String deleteRows = request.getParameter("deleteRows");
			JSONObject obj = new JSONObject();
			JSONArray ary = new JSONArray();
			try{
				ary = JSONArray.fromObject(deleteRows);
				if(ary.size()!=0) {
					for(int i=0;i<ary.size();i++){
						obj = ary.getJSONObject(i); 
						wpsService.deleteWps(String.valueOf(obj.get("fid")));
					}
				}
				 obj.put("success", true);
			}catch(Exception e){
				obj.put("success", false);
				obj.put("errorMsg", e.getMessage());
			}
			return obj.toString();
	}
	
	@RequestMapping("/getInfo")
	@ResponseBody
	public String getInfo(HttpServletRequest request){
		String search = request.getParameter("search");
		String valueFlag = request.getParameter("valueFlag");
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			if(valueFlag.equals("0")) {
				List<Wps> wpsList = wpsService.getEmployee(search);
				for(Wps wps:wpsList){
					json.put("fid", wps.getFid());
					json.put("femployee_id", wps.getFemployee_id());
					json.put("femployee_version", wps.getFemployee_version());
					json.put("femployee_name", wps.getFemployee_name());
					ary.add(json);
				}
			}
			if(valueFlag.equals("1")) {
				List<Wps> wpsList = wpsService.getStep(search);
				for(Wps wps:wpsList){
					json.put("fid", wps.getFid());
					json.put("fstep_number", wps.getFstep_number());
					json.put("fstep_name", wps.getFstep_name());
					ary.add(json);
				}
			}
			if(valueFlag.equals("2")) {
				List<Wps> wpsList = wpsService.getJunction(search);
				for(Wps wps:wpsList){
					json.put("fid", wps.getFid());
					json.put("fjunction", wps.getFjunction());
					json.put("fwelding_area", wps.getFwelding_area());
					ary.add(json);
				}
			}
			if(valueFlag.equals("3")) {
				List<Wps> wpsList = wpsService.getDetail(search);
				for(Wps wps:wpsList){
					json.put("fid", wps.getFid());
					json.put("fquantitative_project", wps.getFquantitative_project());
					json.put("frequired_value", wps.getFrequired_value());
					json.put("fupper_deviation", wps.getFupper_deviation());
					json.put("flower_deviation", wps.getFlower_deviation());
					json.put("funit_of_measurement", wps.getFunit_of_measurement());
					ary.add(json);
				}
			}
		}catch(Exception e){
			e.getMessage();
		}
		obj.put("rows", ary);
		return obj.toString();
	}
	
	@RequestMapping("/saveEmployee")
	@ResponseBody
	public String saveEmployee(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String addRows = request.getParameter("addRows");
			String updateRows = request.getParameter("updateRows");
			String deleteRows = request.getParameter("deleteRows");
			ary = JSONArray.fromObject(addRows);
			if(ary.size()!=0) {				
				String wpsId = request.getParameter("wpsId");
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFemployee_id(String.valueOf(obj.get("femployee_id")));
					wps.setFemployee_version(String.valueOf(obj.get("femployee_version")));
					wps.setFemployee_name(String.valueOf(obj.get("femployee_name")));
					wps.setFwpslib_id(new BigInteger(wpsId));
					wpsService.addEmployee(wps);
				}
			}
			ary = JSONArray.fromObject(updateRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFemployee_id(String.valueOf(obj.get("femployee_id")));
					wps.setFemployee_version(String.valueOf(obj.get("femployee_version")));
					wps.setFemployee_name(String.valueOf(obj.get("femployee_name")));
					wps.setFid(Long.parseLong(String.valueOf(obj.get("fid"))));
					wpsService.updateEmployee(wps);
				}
			}
			ary = JSONArray.fromObject(deleteRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i);
					wpsService.deleteEmployee(String.valueOf(obj.get("fid")));
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/saveStep")
	@ResponseBody
	public String saveStep(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String addRows = request.getParameter("addRows");
			String updateRows = request.getParameter("updateRows");
			String deleteRows = request.getParameter("deleteRows");
			ary = JSONArray.fromObject(addRows);
			if(ary.size()!=0) {				
				String employeeId = request.getParameter("employeeId");
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFstep_number(String.valueOf(obj.get("fstep_number")));
					wps.setFstep_name(String.valueOf(obj.get("fstep_name")));
					wps.setFemployee_id(employeeId);
					wpsService.addStep(wps);
				}
			}
			ary = JSONArray.fromObject(updateRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFstep_number(String.valueOf(obj.get("fstep_number")));
					wps.setFstep_name(String.valueOf(obj.get("fstep_name")));
					wps.setFid(Long.parseLong(String.valueOf(obj.get("fid"))));
					wpsService.updateStep(wps);
				}
			}
			ary = JSONArray.fromObject(deleteRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i);
					wpsService.deleteStep(String.valueOf(obj.get("fid")));
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/saveJunction")
	@ResponseBody
	public String saveJunction(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String addRows = request.getParameter("addRows");
			String updateRows = request.getParameter("updateRows");
			String deleteRows = request.getParameter("deleteRows");
			ary = JSONArray.fromObject(addRows);
			if(ary.size()!=0) {				
				String stepId = request.getParameter("stepId");
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFjunction(String.valueOf(obj.get("fjunction")));
					wps.setFwelding_area(String.valueOf(obj.get("fwelding_area")));
					wps.setFstep_id(stepId);
					wpsService.addJunction(wps);
				}
			}
			ary = JSONArray.fromObject(updateRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFjunction(String.valueOf(obj.get("fjunction")));
					wps.setFwelding_area(String.valueOf(obj.get("fwelding_area")));
					wps.setFid(Long.parseLong(String.valueOf(obj.get("fid"))));
					wpsService.updateJunction(wps);
				}
			}
			ary = JSONArray.fromObject(deleteRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i);
					wpsService.deleteJunction(String.valueOf(obj.get("fid")));
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/saveDetail")
	@ResponseBody
	public String saveDetail(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		try{
			String addRows = request.getParameter("addRows");
			String updateRows = request.getParameter("updateRows");
			String deleteRows = request.getParameter("deleteRows");
			ary = JSONArray.fromObject(addRows);
			if(ary.size()!=0) {
				String stepId = request.getParameter("stepId");
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFquantitative_project(String.valueOf(obj.get("fquantitative_project")));
					wps.setFrequired_value(String.valueOf(obj.get("frequired_value")));
					wps.setFupper_deviation(String.valueOf(obj.get("fupper_deviation")));
					wps.setFlower_deviation(String.valueOf(obj.get("flower_deviation")));
					wps.setFunit_of_measurement(String.valueOf(obj.get("funit_of_measurement")));
					wps.setFstep_id(stepId);
					wpsService.addDetail(wps);
				}
			}
			ary = JSONArray.fromObject(updateRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i); 
					wps.setFquantitative_project(String.valueOf(obj.get("fquantitative_project")));
					wps.setFrequired_value(String.valueOf(obj.get("frequired_value")));
					wps.setFupper_deviation(String.valueOf(obj.get("fupper_deviation")));
					wps.setFlower_deviation(String.valueOf(obj.get("flower_deviation")));
					wps.setFunit_of_measurement(String.valueOf(obj.get("funit_of_measurement")));
					wps.setFid(Long.parseLong(String.valueOf(obj.get("fid"))));
					wpsService.updateDetail(wps);
				}
			}
			ary = JSONArray.fromObject(deleteRows);
			if(ary.size()!=0) {
				for(int i=0;i<ary.size();i++){
					obj = ary.getJSONObject(i);
					wpsService.deleteDetail(String.valueOf(obj.get("fid")));
				}
			}
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 工艺规程树形菜单
	 * @param name
	 * @return
	 */
	@RequestMapping("/getWpsTree")
	@ResponseBody
	public void getWpsTree(HttpServletResponse response,HttpServletRequest request){
        String str ="";  
        StringBuilder json = new StringBuilder();
        String fid = request.getParameter("fid");
        String search = "fid="+fid;
        List<Wps> wpsList = wpsService.getWpsList(search);
        for(Wps wps:wpsList){
	        // 拼接根节点 
	        json.append("[");  
	        json.append("{\"id\":" + Integer.parseInt(fid));
	        json.append(",\"text\":\"" +wps.getFproduct_drawing_no()+":"+wps.getFproduct_version()+ "\"");
	        json.append(",\"state\":\"open\"");  
	        // 获取根节点下的所有子节点  
	        // 遍历子节点下的子节点  
	        json.append(",\"children\":[");  
	        json.append("{\"id\":" + Integer.parseInt(fid));   
	        json.append(",\"text\":\"" +wps.getFwpsnum()+":"+wps.getFwps_lib_version() + "\"");  
	        json.append(",\"state\":\"open\"");   
	          
	        // 该节点有子节点  
	        // 设置为关闭状态,而从构造异步加载tree  
	        List<Wps> tList = wpsService.getEmployee(fid);
	        if(tList!=null && tList.size()!=0){// 存在子节点  
	             json.append(",\"children\":[");  
	             json.append(dealJsonFormat(tList));// 存在子节点的都放在一个工具类里面处理了
	             json.append("]");  
	        }  
	        json.append("},");
	        str = json.toString();  
	        str = str.substring(0, str.length()-1);  
	        str+="]}]";
        }
        try {
            response.getWriter().print(str);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public String dealJsonFormat(List<Wps> tList){  
        StringBuilder json = new StringBuilder();  
        for (Wps tree : tList) {  
            json.append("{\"id\":" +String.valueOf(tree.getFid()));   
            json.append(",\"text\":\"" +tree.getFemployee_id() + ":"+tree.getFemployee_version() + "\"");   
            json.append(",\"state\":\"open\"");
            
            // 获取根节点下的所有子节点  
            List<Wps> treeLists = wpsService.getStep(String.valueOf(tree.getFid()));
            // 遍历子节点下的子节点  
            if(treeLists!=null && treeLists.size()!=0){  
                json.append(",\"children\":["); 
                json.append(dealJsonFormat2(treeLists));// 存在子节点的都放在一个工具类里面处理
                json.append("]");  
            }  
            json.append("},");  
        }  
        String str = json.toString();  
        str = str.substring(0, str.length()-1);
        return str;  
    } 
	
	public String dealJsonFormat2(List<Wps> treeLists){  
        StringBuilder json = new StringBuilder();  
        for (Wps tree : treeLists) {  
            json.append("{\"id\":" +String.valueOf(tree.getFid()));   
            json.append(",\"text\":\"" +tree.getFstep_number() + "\"");   
            json.append(",\"state\":\"open\"");
            json.append(",\"attributes\":\"1\"");
            json.append("},");  
        }  
        String str = json.toString();  
        str = str.substring(0, str.length()-1); 
        return str;  
    } 
	
	/**
	 * 工艺通过
	 * @Description
	 * @author Bruce
	 * @date 2020年2月26日下午2:59:33
	 * @param request
	 * @param wps
	 * @return
	 */
	@RequestMapping("/passReview")
	@ResponseBody
	public String passReview(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String value = request.getParameter("value");
			wpsService.passReview(fid,value);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 工艺驳回
	 * @Description
	 * @author Bruce
	 * @date 2020年2月26日下午2:59:25
	 * @param request
	 * @param wps
	 * @return
	 */
	@RequestMapping("/turnDown")
	@ResponseBody
	public String turnDown(HttpServletRequest request,@ModelAttribute Wps wps){
		JSONObject obj = new JSONObject();
		try{
			String fid = request.getParameter("fid");
			String downReason = request.getParameter("downReason");
			wps.setFid(Long.parseLong(fid));
			wps.setFback(downReason);
			wpsService.turnDown(wps);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	/**
	 * 获取工艺规程字典信息
	 * @return
	 */
	@RequestMapping("/getWpsCombobox")
	@ResponseBody
	public String getWpsCombobox(){
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		JSONObject obj = new JSONObject();
		try{
			List<Wps> list = wpsService.getWpsCombobox();
			for(Wps w:list){
				json.put("id", w.getFid());
				json.put("name", w.getFwpsnum()+":"+w.getFwps_lib_version());
				ary.add(json);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		obj.put("ary", ary);
		return obj.toString();
	}
	
	@RequestMapping("/productvalidate")
	@ResponseBody
	private String enovalidate(@RequestParam String procudt,@RequestParam String pdn){
		boolean data = true;
		int count = wpsService.getProcudtCount(pdn,procudt);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/wpsversionvalidate")
	@ResponseBody
	private String wpsversionvalidate(@RequestParam String wpsversion,@RequestParam String wln,@RequestParam String pdn,@RequestParam String pv){
		boolean data = true;
		int count = wpsService.getWpsversionCount(wln,wpsversion,pdn,pv);
		if(count>0){
			data = false;
		}
		return data + "";
	}
	
	@RequestMapping("/addVersion")
	@ResponseBody
	public String addVersion(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			JSONObject json = new JSONObject();
			String hide_id = request.getParameter("fid");
			String fproduct_drawing_no_v = request.getParameter("fproduct_drawing_no_v");
			String fproduct_name_v = request.getParameter("fproduct_name_v");
			String fproduct_version_v = request.getParameter("fproduct_version_v");
			String fwps_lib_version_v = request.getParameter("fwps_lib_version_v");
			String fwps_lib_name_v = request.getParameter("fwps_lib_name_v");
			Wps wps = new Wps();
			wps.setFproduct_drawing_no(fproduct_drawing_no_v);
			wps.setFproduct_name(fproduct_name_v);
			wps.setFproduct_version(fproduct_version_v);
			wps.setFwpsnum(fwps_lib_name_v);
			wps.setFwps_lib_version(fwps_lib_version_v);
			wpsService.addWps(wps);
			{
				json.put("fid", wps.getFid());
				json.put("fproduct_drawing_no", wps.getFproduct_drawing_no());
				json.put("fproduct_name", wps.getFproduct_name());
				json.put("fproduct_version", wps.getFproduct_version());
				json.put("fwps_lib_name", wps.getFwpsnum());
				json.put("fwps_lib_version", wps.getFwps_lib_version());
				json.put("flag", 0);
				json.put("flag_name", "自建");
				json.put("fstatus", 0);
				json.put("fback", wps.getFback());
			}
			List<Wps> employeeList = wpsService.getEmployee(hide_id);
			for(Wps ewl:employeeList) {
				String eid = String.valueOf(ewl.getFid());//eid为原先版本的工序id
				ewl.setFwpslib_id(new BigInteger(String.valueOf(wps.getFid())));
				wpsService.addEmployee(ewl);//ewl的id为新增后返回的id
				List<Wps> stepList = wpsService.getStep(eid);
				for(Wps spl:stepList) {
					String sid = String.valueOf(spl.getFid());//sid为原先版本的工步id
					spl.setFemployee_id(String.valueOf(ewl.getFid()));
					wpsService.addStep(spl);
					List<Wps> junctionList = wpsService.getJunction(sid);
					for(Wps jtl:junctionList) {
						jtl.setFstep_id(String.valueOf(spl.getFid()));
						wpsService.addJunction(jtl);
					}
					List<Wps> detailList = wpsService.getDetail(sid);
					for(Wps dil:detailList) {
						dil.setFstep_id(String.valueOf(spl.getFid()));
						wpsService.addDetail(dil);
					}
				}
			}
			obj.put("wpsId", wps.getFid());
			obj.put("objRow", json);
			obj.put("success", true);
		}catch(Exception e){
			obj.put("wpsId", "");
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
	
	@RequestMapping("/finishWork")
	@ResponseBody
	public String finishWork(HttpServletRequest request){
		JSONObject obj = new JSONObject();
		try{
			JSONObject json = new JSONObject();
			String fid = request.getParameter("fid");
			wpsService.finishWork(fid);
			obj.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success", false);
			obj.put("errorMsg", e.getMessage());
		}
		return obj.toString();
	}
}