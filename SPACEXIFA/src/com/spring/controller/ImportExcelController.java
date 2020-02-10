package com.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.dao.InsframeworkMapper;
import com.spring.dao.WpsMapper;
import com.spring.model.Dictionarys;
import com.spring.model.Gather;
import com.spring.model.Insframework;
import com.spring.model.MaintenanceRecord;
import com.spring.model.MyUser;
import com.spring.model.Person;
import com.spring.model.WeldedJunction;
import com.spring.model.WeldingMachine;
import com.spring.model.WeldingMaintenance;
import com.spring.model.Wps;
import com.spring.service.DictionaryService;
import com.spring.service.GatherService;
import com.spring.service.InsframeworkService;
import com.spring.service.MaintainService;
import com.spring.service.PersonService;
import com.spring.service.WeldedJunctionService;
import com.spring.service.WeldingMachineService;
import com.spring.service.WpsService;
import com.spring.util.IsnullUtil;
import com.spring.util.UploadUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * excel导入数据库
 * @author gpyf16
 *
 */

@Controller
@RequestMapping(value = "/import", produces = { "text/tjson;charset=UTF-8" })
public class ImportExcelController {
	@Autowired
	private WeldingMachineService wmm;
	@Autowired
	private MaintainService mm;
	@Autowired
	private GatherService gs;
	@Autowired
	private PersonService ps;
	@Autowired
	private DictionaryService dm;
	@Autowired
	private WeldedJunctionService wjs;
	@Autowired
	private WpsMapper wps;
	@Autowired
	private WpsService wpss;
	@Autowired
	private InsframeworkService im;
	
	IsnullUtil iutil = new IsnullUtil();
	
	@RequestMapping("importGather")
	@ResponseBody
	public String importGather(HttpServletRequest request,HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		String path = "";
		try{
			path = u.uploadFile(request, response);
			List<Gather> list = xlsxGather(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(Gather g : list){
				g.setItemid(wmm.getInsframeworkByName(g.getItemname()));
				//编码唯一
				int count1 = gs.getGatherNoCount(g.getGatherNo(),g.getItemid());
				if(count1>0){
					/*obj.put("msg","导入失败，请检查您的设备编码是否已存在！");
					obj.put("success",false);
					return obj.toString();*/
					continue;
				}else{
					gs.addGather(g);
				}
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
			obj.put("success",false);
		}
		return obj.toString();
	}
	
	/**
	 * 导入焊机设备
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldingMachine")
	@ResponseBody
	public String importWeldingMachine(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		String path = "";
		try{
			path = u.uploadFile(request, response);
			List<WeldingMachine> list = xlsxWm(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(WeldingMachine wm : list){
				wm.setTypeId(dm.getvaluebyname(4,wm.getTypename()));
				wm.setStatusId(dm.getvaluebyname(3,wm.getStatusname()));
				wm.setMvalueid(dm.getvaluebyname(14, wm.getMvaluename()));
				wm.setModel(String.valueOf(dm.getvaluebyname(17, wm.getModel())));
				String name = wm.getInsframeworkId().getName();
				wm.getInsframeworkId().setId(wmm.getInsframeworkByName(name));
				Gather gather = wm.getGatherId();
				int count2 = 0;
				if(gather!=null){
					int count3 = gs.getGatherNoByItemCount(gather.getGatherNo(), wm.getInsframeworkId().getId()+"");
					if(count3 == 0){
						obj.put("msg","导入失败，请检查您的采集序号是否存在或是否属于该部门！");
						obj.put("success",false);
						return obj.toString();
					}
					gather.setId(gs.getGatherByNo(gather.getGatherNo()));
					wm.setGatherId(gather);
					count2 = wmm.getGatheridCount(wm.getInsframeworkId().getId(),gather.getGatherNo());
				}
				if(isInteger(wm.getEquipmentNo())){
					wm.setEquipmentNo(wm.getEquipmentNo());
				}
				wm.setGatherId(gather);
				//编码唯一
				int count1 = wmm.getEquipmentnoCount(wm.getEquipmentNo());
				if(count2>0){
					obj.put("msg","导入失败，请检查您的采集序号是否已经被占用！");
					obj.put("success",false);
					return obj.toString();
				}else if(count1>0){
					continue;
				}else{
					wmm.addWeldingMachine(wm);
				}
				List<Dictionarys> model = dm.getModelOfManu(wm.getMvalueid());
				boolean modelflag = true;
				for(int i=0;i<model.size();i++){
					if(wm.getModel().equals(model.get(i).getId().toString())){
						modelflag = false;
					}
				}
				if(modelflag){
					obj.put("msg","导入失败，请检查您的焊机型号与生产厂商是否匹配！");
					obj.put("success",false);
					return obj.toString();
				}
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
			obj.put("success",false);
		}
		return obj.toString();
	}
	
	/**
	 * 导入维修记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importMaintain")
	@ResponseBody
	public String importMaintain(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<WeldingMaintenance> wt = xlsxMaintain(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(int i=0;i<wt.size();i++){
				wt.get(i).getMaintenance().setTypeId(dm.getvaluebyname(5,wt.get(i).getMaintenance().getTypename()));
				BigInteger wmid = null;
				if(isInteger(wt.get(i).getWelding().getEquipmentNo())){
					wmid = wmm.getWeldingMachineByEno(wt.get(i).getWelding().getEquipmentNo());
				}else{
					wmid = wmm.getWeldingMachineByEno(wt.get(i).getWelding().getEquipmentNo());
				}
				wt.get(i).getWelding().setId(wmid);
				//插入数据库
				mm.addMaintian( wt.get(i),wt.get(i).getMaintenance(),wmid);
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
		}
		return obj.toString();
	}
	
	
	/**
	 * 导入焊工记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWelder")
	@ResponseBody
	public String importWelder(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<Person> we = xlsxWelder(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(Person w:we){
				if(w.getWelderno().length()>8){
					w.setWelderno(w.getWelderno().substring(0, 8));
				}else if(w.getWelderno().length()<8){
					for(int i=w.getWelderno().length();i<8;i++){
						w.setWelderno("0"+w.getWelderno());
					}
				}
				w.setLeveid(dm.getvaluebyname(8,w.getLevename()));
				w.setQuali(dm.getvaluebyname(7, w.getQualiname()));
				w.setOwner(wmm.getInsframeworkByName(w.getInsname()));
				MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				w.setCreater(new BigInteger(user.getId()+""));
				w.setUpdater(new BigInteger(user.getId()+""));
				w.setWelderno(w.getWelderno());
				String phone = w.getCellphone();
				if(iutil.isNull(phone)){
					if(!phone.matches("^1[3-8]\\d{9}$")){
						obj.put("msg","导入失败，请检查您的手机号码是否正确！");
						obj.put("success",false);
						return obj.toString();
					}
				}
				//编码唯一
				int count1 = ps.getUsernameCount(w.getWelderno());
				if(count1>0){
//					obj.put("msg","导入失败，请检查您的焊工编号是否已存在！");
//					obj.put("success",false);
//					return obj.toString();
					continue;
				}else{
					ps.save(w);
				}
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
		}
		return obj.toString();
	}
	

	/**
	 * 导入焊口记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldedJunction")
	@ResponseBody
	public String importWeldedJunction(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<WeldedJunction> we = xlsxWeldedJunction(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(WeldedJunction w:we){
				String wjno = w.getWeldedJunctionno();
				int num = wjno.length();
				if(num<=6){
					for(int i=0;i<6-num;i++){
						wjno = "0"+wjno;
					} 
				}else{
					obj.put("success",false);
					obj.put("msg","导入失败，请检查您的焊口编号长度是否符合要求！");
					return obj.toString();
				}
				w.setWeldedJunctionno(wjno);
				int count = wjs.getWeldedjunctionByNo(wjno);
				w.setInsfid(wmm.getInsframeworkByName(w.getIname()));
				MyUser user = (MyUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				w.setCreater(new BigInteger(user.getId()+""));
				w.setUpdater(new BigInteger(user.getId()+""));
				w.setWeldedJunctionno(w.getWeldedJunctionno());
				//编码唯一
				if(count>0){
//					obj.put("msg","导入失败，请检查您的焊口编号是否已存在！");
//					obj.put("success",false);
//					return obj.toString();
					continue;
				}
				wjs.addJunction(w);
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
		}
		return obj.toString();
	}
	
	/**
	 * 导入任务记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWeldTask")
	@ResponseBody
	public String importWeldTask(HttpServletRequest request,
		HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray ary = new JSONArray();
		String str = "";
		int biaozhi = 0;
		int flag=0;
		try{
			String path = u.uploadFile(request, response);
			List<WeldedJunction> we = xlsxWeldTask(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(WeldedJunction w:we){
				String wjno = w.getWeldedJunctionno();
				w.setWeldedJunctionno(wjno);
				json.put("taskNo", w.getWeldedJunctionno());
				json.put("flag", "0");
				if(w.getWeldedJunctionno()==null||"".equals(w.getWeldedJunctionno())){
					str+="任务编号不能为空;";
					biaozhi=1;
				}else{
					int count = wjs.getWeldedjunctionByNo(wjno);
					if(count>0){
//						str+="任务编号已经存在;";
//						biaozhi=1;
						json.put("flag", "1");
						flag++;
					}
				};
				if(!"".equals(w.getIname()) && w.getIname()!=null){
					Person p = ps.getIdByWelderno(w.getIname());
					if(p!=null && !"".equals(String.valueOf(p.getId())) && p.getId()!=null){
						json.put("personid", p.getId());
						json.put("personnum", w.getIname());
						json.put("itemid", p.getInsid());
					}else{
						json.put("personid", "");
						json.put("personnum", w.getIname());
						json.put("itemid", "");
						str+="焊工不存在;";
						biaozhi=1;
					}
				}else{
					json.put("personid", "");
					json.put("personnum", "");
					str+="请分配焊工;";
					biaozhi=1;
				};
				if(!"".equals(w.getRoomNo()) && w.getRoomNo()!=null){
					String wpslibid = wps.getIdByWpslibname(w.getRoomNo());
					if(!"".equals(wpslibid) && wpslibid!=null){
						json.put("wpsid", wpslibid);
						json.put("wpsname", w.getRoomNo());
					}else{
						json.put("wpsid", "");
						json.put("wpsname", w.getRoomNo());
						str+="工艺库不存在;";
						biaozhi=1;
					}
				}else{
					json.put("wpsid", "");
					json.put("wpsname", "");
					str+="请分配工艺库;";
					biaozhi=1;
				};
				if(!"".equals(w.getStartTime()) && w.getStartTime()!=null){
					json.put("start", w.getStartTime());
				}else{
					json.put("start", "");
					str+="开始时间不能为空;";
					biaozhi=1;
				};
				//json.put("end", w.getEndTime());
				json.put("fengineering_symbol", w.getFengineering_symbol());
				json.put("fweld_method", w.getFweld_method());
				json.put("fweld_position", w.getFweld_position());
				json.put("fbase_material_type", w.getFbase_material_type());
				json.put("fweld_material_model", w.getFweld_material_model());
				json.put("ftechnological_design", w.getFtechnological_design());
				json.put("fwarm_up_requirement", w.getFwarm_up_requirement());
				json.put("finter_channel_temperature", w.getFinter_channel_temperature());
				json.put("fcarbon_requirement", w.getFcarbon_requirement());
				json.put("fpost_heat_requirement", w.getFpost_heat_requirement());
				json.put("fannealed_weld", w.getFannealed_weld());
				json.put("fassembly_clearance", w.getFassembly_clearance());
				json.put("fcarbon_depth", w.getFcarbon_depth());
				json.put("fcarbon_width", w.getFcarbon_width());
				json.put("fpost_heat_temperature", w.getFpost_heat_temperature());
				json.put("fafter_hot_time", w.getFafter_hot_time());
				json.put("str", str);
				ary.add(json);
				str="";
			};
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(flag);
		obj.put("rows", ary);
		obj.put("biaozhi", biaozhi);
		return obj.toString();
	}
	
	/**
	 * 导入工艺
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/importWps")
	@ResponseBody
	public String importWps(HttpServletRequest request,
			HttpServletResponse response){
		UploadUtil u = new UploadUtil();
		JSONObject obj = new JSONObject();
		try{
			String path = u.uploadFile(request, response);
			List<Wps> we = xlsxWps(path);
			//删除已保存的excel文件
			File file  = new File(path);
			file.delete();
			for(Wps w:we){
				if("".equals(w.getFsolder_layer()) && w.getFsolder_layer()==null) {
					obj.put("msg","焊层号不能为空！");
					obj.put("success",false);
					return obj.toString();
				}else {
					w.setFsolder_layer(w.getFsolder_layer());
				}
				if("".equals(w.getFweld_bead()) && w.getFweld_bead()==null) {
					obj.put("msg","焊道号不能为空！");
					obj.put("success",false);
					return obj.toString();
				}else {
					w.setFweld_bead(w.getFweld_bead());
				}
				if("".equals(w.getFwpsnum()) && w.getFwpsnum()==null) {
					obj.put("msg","通道号不能为空！");
					obj.put("success",false);
					return obj.toString();
				}else {
					w.setFwpsnum(w.getFwpsnum());
				}
				if("".equals(w.getFweld_method()) && w.getFweld_method()==null) {
					w.setFweld_method("-");
				}else {
					w.setFweld_method(w.getFweld_method());
				}
				try {
					if("".equals(w.getDianame()) && w.getDianame()==null) {
						obj.put("msg","焊材直径不能为空！");
						obj.put("success",false);
						return obj.toString();
					}else {
						w.setFdiameter(dm.getvaluebyname(13,w.getDianame()));
					}
				} catch (Exception e) {
					obj.put("msg","焊材直径未在字典库中找到！");
					obj.put("success",false);
					return obj.toString();
				}
				try {
					if("".equals(w.getMaterialname()) && w.getMaterialname()==null) {
						obj.put("msg","焊接材料不能为空！");
						obj.put("success",false);
						return obj.toString();
					}else {
						w.setFmaterial(dm.getvaluebyname(9, w.getMaterialname()));
					}
				} catch (Exception e) {
					obj.put("msg","焊接材料未在字典库中找到！");
					obj.put("success",false);
					return obj.toString();
				}
				w.setFweld_ele((w.getFpreset_ele_top()+w.getFpreset_ele_bottom())/2);
				w.setFweld_tuny_ele(w.getFpreset_ele_top()-(w.getFpreset_ele_top()+w.getFpreset_ele_bottom())/2);
				w.setFweld_vol((w.getFpreset_vol_top()+w.getFpreset_vol_bottom())/2);
				w.setFweld_tuny_vol(w.getFpreset_vol_top()-(w.getFpreset_vol_top()+w.getFpreset_vol_bottom())/2);
				if("".equals(w.getFpower_polarity()) && w.getFpower_polarity()==null) {
					w.setFpower_polarity("-");
				}else {
					w.setFpower_polarity(w.getFpower_polarity());
				}
				if("".equals(w.getFgas_flow()) && w.getFgas_flow()==null) {
					w.setFgas_flow("-");
				}else {
					w.setFgas_flow(w.getFgas_flow());
				}
				if("".equals(w.getFweld_speed()) && w.getFweld_speed()==null) {
					w.setFweld_speed("-");
				}else {
					w.setFweld_speed(w.getFweld_speed());
				}
				try {
					if("".equals(w.getFprocessname()) && w.getFprocessname()==null) {
						obj.put("msg","脉冲不能为空！");
						obj.put("success",false);
						return obj.toString();
					}else {
						w.setFprocessid(dm.getvaluebyname(22,w.getFprocessname()));
					}
				} catch (Exception e) {
					obj.put("msg","脉冲方式未在字典库中找到！");
					obj.put("success",false);
					return obj.toString();
				}
				if(!"".equals(w.getFname()) && w.getFname()!=null){
					String wpslibid = wps.getIdByWpslibname(w.getFname());
					if("".equals(wpslibid) || wpslibid==null){
						Wps wps = new Wps();
						wps.setFwpsnum(w.getFname());
						wps.setFback("171");
						wps.setFcreater(Long.valueOf(String.valueOf(im.getUserInsframework())));
						wps.setFstatus(61);
						wpss.saveWpslib(wps);
//						obj.put("msg","导入失败，工艺库不存在！");
//						obj.put("success",false);
//						return obj.toString();
					}
					wpslibid = wps.getIdByWpslibname(w.getFname());
					w.setFwpslib_id(new BigInteger(wpslibid));
					int count1 = wpss.getCountByWpsidAndLayerroad(wpslibid, w.getFsolder_layer(), w.getFweld_bead());
					if(count1<1){
						wpss.addWpsDetail(w);
					}else{
						wpss.updateWpsDetail(w);
					}
				}else{
					obj.put("msg","导入失败，工艺库不能为空！");
					obj.put("success",false);
					return obj.toString();
				};
			};
			obj.put("success",true);
			obj.put("msg","导入成功！");
		}catch(Exception e){
			e.printStackTrace();
			obj.put("success",false);
			obj.put("msg","导入失败，请检查您的文件格式以及数据是否符合要求！");
		}
		return obj.toString();
	}
	
	/**
	 * 导入WeldingMaintenance表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldingMaintenance> xlsxMaintain(String path) throws IOException, InvalidFormatException{
		List<WeldingMaintenance> wm = new ArrayList<WeldingMaintenance>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldingMaintenance dit = new WeldingMaintenance();
			MaintenanceRecord mr = new MaintenanceRecord();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
                        double value = cell.getNumericCellValue();
                        int intValue = (int) value;
                        cellValue = value - intValue == 0 ? String.valueOf(intValue) : String.valueOf(value);
                    }
					if(k == 0){
						WeldingMachine welding = new WeldingMachine();
						welding.setEquipmentNo(cellValue);
						dit.setWelding(welding);//设备编码
						break;
					}
					else if(k == 2){
						mr.setStartTime(cellValue);//维修起始时间
						break;
					}
					else if(k == 3){
						mr.setEndTime(cellValue);//维修结束时间
						break;
	    			}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						WeldingMachine welding = new WeldingMachine();
						welding.setEquipmentNo(cellValue);
						dit.setWelding(welding);//设备编码
						break;
					}
					else if(k == 1){
						mr.setViceman(cellValue);//维修人员
						break;
					}
					else if(k == 4){
						mr.setTypename(cellValue);
						break;
 					}
					else if(k == 5){
 						mr.setDesc(cellValue);//维修说明
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			dit.setMaintenance(mr);
			wm.add(dit);
		}
		
		return wm;
	}
	
	/**
	 * 导入Wedlingmachine表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldingMachine> xlsxWm(String path) throws IOException, InvalidFormatException{
		List<WeldingMachine> wm = new ArrayList<WeldingMachine>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldingMachine dit = new WeldingMachine();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //处理数字过长时出现x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                    }
					if(k == 0){
						dit.setEquipmentNo(cellValue);//设备编码
						break;
					}
					else if(k == 2){
						dit.setJoinTime(cellValue);//入厂时间
						break;
					}
					//采集序号机设备序号只能是数字
					else if(k == 7){
						Gather g = new Gather();
						g.setGatherNo(cellValue);
						dit.setGatherId(g);//采集序号
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						dit.setEquipmentNo(cellValue);//设备编码
						break;
					}
					else if(k == 1){
						dit.setTypename(cellValue);//设备类型
						break;
					}
					else if(k == 3){
 						Insframework ins = new Insframework();
 						ins.setName(cellValue);
 						dit.setInsframeworkId(ins);//所属项目
						break;
	    			}
					else if(k == 4){
			        	dit.setStatusname(cellValue);//状态
						break;
 					}
					else if(k == 5){
 						dit.setMvaluename(cellValue);//厂家
						break;
 					}
					else if(k == 6){
						if(cellValue.equals("是")){
	 						dit.setIsnetworking(0);//是否在网
						}else{
	 						dit.setIsnetworking(1);
						}
						break;
 					}
					//采集序号机设备序号只能是数字
					else if(k == 7){
						Gather g = new Gather();
						g.setGatherNo(cellValue);
						dit.setGatherId(g);//采集序号
						break;
					}
					else if(k == 8){
						dit.setPosition(cellValue);//位置
						break;
					}
					else if(k == 9){
						dit.setIp(cellValue);//ip地址
						break;
					}
					else if(k == 10){
						dit.setModel(cellValue);//设备型号
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			wm.add(dit);
		}
		
		return wm;
	}
	
	/**
	 * 导入Welder表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Person> xlsxWelder(String path) throws IOException, InvalidFormatException{
		List<Person> welder = new ArrayList<Person>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			Person p = new Person();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //处理数字过长时出现x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                   }
					if(k == 1){
						p.setWelderno(cellValue);//焊工编号
						break;
					}
					else if(k == 2){
						p.setCellphone(cellValue);//手机
						break;
 					}
					else if(k == 4){
						p.setCardnum(cellValue);//卡号
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setName(cellValue);//姓名
						break;
					}
					else if(k == 1){
						p.setWelderno(cellValue);//焊工编号
						break;
					}
					else if(k == 2){
						p.setCellphone(cellValue);//手机
						break;
 					}
					else if(k == 3){
						p.setLevename(cellValue);//级别
						break;
 					}
					else if(k == 4){
						p.setCardnum(cellValue);//卡号
						break;
 					}
					else if(k == 5){
						p.setQualiname(cellValue);//资质
						break;
 					}
					else if(k == 6){
						p.setInsname(cellValue);//部门
						break;
 					}
					else if(k == 7){
						p.setBack(cellValue);//备注
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			welder.add(p);
		}
		
		return welder;
	}
	
	/**
	 * 导入wps表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Wps> xlsxWps(String path) throws IOException, InvalidFormatException{
		List<Wps> wps = new ArrayList<Wps>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			Wps p = new Wps();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //处理数字过长时出现x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                   }
					if(k == 0){
						p.setFsolder_layer(cellValue);//焊层号
						break;
					}
					else if(k == 1){
						p.setFweld_bead(cellValue);//焊道号
						break;
					}
					else if(k == 2){
						p.setFwpsnum(cellValue);//焊机通道号
						break;
 					}
					else if(k == 6){
						p.setFpreset_ele_bottom(Double.parseDouble(cellValue));//焊接电流下限
						break;
 					}
					else if(k == 7){
						p.setFpreset_ele_top(Double.parseDouble(cellValue));//焊接电流上限
						break;
 					}
					else if(k == 8){
						p.setFpreset_vol_bottom(Double.parseDouble(cellValue));//焊接电压下限
						break;
 					}
					else if(k == 9){
						p.setFpreset_vol_top(Double.parseDouble(cellValue));//焊接电压上限
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setFsolder_layer(cellValue);//焊层号
						break;
					}
					else if(k == 1){
						p.setFweld_bead(cellValue);//焊道号
						break;
					}
					else if(k == 2){
						p.setFwpsnum(cellValue);//焊机通道号
						break;
 					}
					else if(k == 3){
						p.setFweld_method(cellValue);//焊接方法
						break;
 					}
					else if(k == 4){
						p.setMaterialname(cellValue);//焊接材料
						break;
 					}
					else if(k == 5){
						p.setDianame(cellValue);//焊材直径
						break;
 					}
					else if(k == 6){
						p.setFpreset_ele_bottom(Double.parseDouble(cellValue));//焊接电流下限
						break;
 					}
					else if(k == 7){
						p.setFpreset_ele_top(Double.parseDouble(cellValue));//焊接电流上限
						break;
 					}
					else if(k == 8){
						p.setFpreset_vol_bottom(Double.parseDouble(cellValue));//焊接电压下限
						break;
 					}
					else if(k == 9){
						p.setFpreset_vol_top(Double.parseDouble(cellValue));//焊接电压上限
						break;
 					}
					else if(k == 10){
						p.setFpower_polarity(cellValue);//电源极性
						break;
 					}
					else if(k == 11){
						p.setFgas_flow(cellValue);//气体流量
						break;
 					}
					else if(k == 12){
						p.setFweld_speed(cellValue);//焊接速度
						break;
 					}
					else if(k == 13){
						p.setFprocessname(cellValue);//脉冲
						break;
 					}
					else if(k == 14){
						p.setFname(cellValue);//工艺库名称
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			wps.add(p);
		}
		
		return wps;
	}
	
	
	/**
	 * 导入Weldedjunction表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldedJunction> xlsxWeldedJunction(String path) throws IOException, InvalidFormatException{
		List<WeldedJunction> junction = new ArrayList<WeldedJunction>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldedJunction p = new WeldedJunction();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	String num = String.valueOf(cell.getNumericCellValue());
		            	 //处理数字过长时出现x.xxxE9
		            	BigDecimal big=new BigDecimal(cell.getNumericCellValue());
		            	//判断数字是否是小数
		            	Pattern pattern = Pattern.compile("^\\d+\\.\\d+$");
		            	Matcher isNum = pattern.matcher(big+"");
		            	if(isNum.matches()){
		            		//为小数时不进行过长处理否则小数位会自动补位，例：21.3变为21.39999999999999857891452847979962825775146484375
		            		cellValue = num;
		            	}else{
		            		cellValue = big.toString();
		            	}
//		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
//		            	 cellValue = big.toString();
                   }
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//编号
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//序列号
						break;
					}
					else if(k == 6){
						p.setDyne(Integer.parseInt(cellValue));//达因
						break;
					}
					else if(k == 8){
						p.setPipelineNo(cellValue);//管线号
						break;
					}
					else if(k == 9){
						p.setRoomNo(cellValue);//房间号
						break;
					}
					else if(k == 10){
						p.setExternalDiameter(cellValue);//上游外径
						break;
					}
					else if(k == 11){
						p.setNextexternaldiameter(cellValue);//下游外径
						break;
					}
					else if(k == 12){
						p.setWallThickness(cellValue);//上游璧厚
						break;
					}
					else if(k == 13){
						p.setNextwall_thickness(cellValue);//下游璧厚
						break;
					}
					else if(k == 16){
						p.setMaxElectricity(Double.valueOf(cellValue));//电流上限
						break;
					}
					else if(k == 17){
						p.setMinElectricity(Double.valueOf(cellValue));//电流下限
						break;
					}
					else if(k == 18){
						p.setMaxValtage(Double.valueOf(cellValue));//电压上限
						break;
					}
					else if(k == 19){
						p.setMinValtage(Double.valueOf(cellValue));//电压下限
						break;
					}
					else if(k == 22){
						p.setStartTime(cellValue);//开始时间
						break;
					}
					else if(k == 23){
						p.setEndTime(cellValue);//结束时间
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//编号
						break;
					}
					else if(k == 1){
						p.setSerialNo(cellValue);//序列号
						break;
					}
					else if(k == 2){
						p.setUnit(cellValue);//机组
						break;
					}
					else if(k == 3){
						p.setArea(cellValue);//区域
						break;
					}
					else if(k == 4){
						p.setSystems(cellValue);//系统
						break;
					}
					else if(k == 5){
						p.setChildren(cellValue);//子项
						break;
					}
					else if(k == 7){
						p.setSpecification(cellValue);//规格
						break;
					}
					else if(k == 8){
						p.setPipelineNo(cellValue);//管线号
						break;
					}
					else if(k == 9){
						p.setRoomNo(cellValue);//房间号
						break;
					}
					else if(k == 14){
						p.setMaterial(cellValue);//上游材质
						break;
					}
					else if(k == 15){
						p.setNext_material(cellValue);//下游材质
						break;
					}
					else if(k == 20){
						p.setElectricity_unit(cellValue);//电流单位
						break;
					}
					else if(k == 21){
						p.setValtage_unit(cellValue);//电压单位
						break;
					}
					else if(k == 24){
/*						Insframework insf = new Insframework();
						insf.setName(cellValue);*/
						p.setIname(cellValue);//所属部门
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			junction.add(p);
		}
		
		return junction;
	}
	
	
	/**
	 * 导入WeldTask表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<WeldedJunction> xlsxWeldTask(String path) throws IOException, InvalidFormatException{
		List<WeldedJunction> junction = new ArrayList<WeldedJunction>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			WeldedJunction p = new WeldedJunction();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	String num = String.valueOf(cell.getNumericCellValue());
		            	 //处理数字过长时出现x.xxxE9
		            	BigDecimal big=new BigDecimal(cell.getNumericCellValue());
		            	//判断数字是否是小数
		            	Pattern pattern = Pattern.compile("^\\d+\\.\\d+$");
		            	Matcher isNum = pattern.matcher(big+"");
		            	if(isNum.matches()){
		            		//为小数时不进行过长处理否则小数位会自动补位，例：21.3变为21.39999999999999857891452847979962825775146484375
		            		cellValue = num;
		            	}else{
		            		cellValue = big.toString();
		            	}
//		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
//		            	 cellValue = big.toString();
                   }
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//任务编号
						break;
					}else if(k == 1){
						p.setStartTime(cellValue);//计划开始时间
						break;
					}else if(k == 2){
						p.setFengineering_symbol(cellValue);//工程符号
						break;
					}else if(k == 3){
						p.setFweld_method(cellValue);//焊接方法
						break;
					}else if(k == 4){
						p.setFweld_position(cellValue);//焊接位置
						break;
					}else if(k == 5){
						p.setFbase_material_type(cellValue);//母材型号
						break;
					}else if(k == 6){
						p.setFweld_material_model(cellValue);//焊材型号
						break;
					}else if(k == 7){
						p.setIname(cellValue);//操作人
						break;
					}else if(k == 8){
						p.setFtechnological_design(cellValue);//工艺设计
						break;
					}else if(k == 9){
						p.setFwarm_up_requirement(cellValue);//预热要求
						break;
					}else if(k == 10){
						p.setFinter_channel_temperature(cellValue);//道间温度
						break;
					}else if(k == 11){
						p.setFcarbon_requirement(cellValue);//碳刨要求
						break;
					}else if(k == 12){
						p.setFpost_heat_requirement(cellValue);//后热要求
						break;
					}else if(k == 13){
						p.setFannealed_weld(cellValue);//退火焊道
						break;
					}else if(k == 14){
						p.setFassembly_clearance(cellValue);//装配间隙
						break;
					}else if(k == 15){
						p.setFcarbon_depth(cellValue);//碳刨深度
						break;
					}else if(k == 16){
						p.setFcarbon_width(cellValue);//碳刨宽度
						break;
					}else if(k == 17){
						p.setFpost_heat_temperature(cellValue);//后热温度
						break;
					}else if(k == 18){
						p.setFafter_hot_time(cellValue);//后热时间
						break;
					}else if(k == 19){
						p.setRoomNo(cellValue);//工艺库名称
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						p.setWeldedJunctionno(cellValue);//任务编号
						break;
					}else if(k == 1){
						p.setStartTime(cellValue);//计划开始时间
						break;
					}else if(k == 2){
						p.setFengineering_symbol(cellValue);//工程符号
						break;
					}else if(k == 3){
						p.setFweld_method(cellValue);//焊接方法
						break;
					}else if(k == 4){
						p.setFweld_position(cellValue);//焊接位置
						break;
					}else if(k == 5){
						p.setFbase_material_type(cellValue);//母材型号
						break;
					}else if(k == 6){
						p.setFweld_material_model(cellValue);//焊材型号
						break;
					}else if(k == 7){
						p.setIname(cellValue);//操作人
						break;
					}else if(k == 8){
						p.setFtechnological_design(cellValue);//工艺设计
						break;
					}else if(k == 9){
						p.setFwarm_up_requirement(cellValue);//预热要求
						break;
					}else if(k == 10){
						p.setFinter_channel_temperature(cellValue);//道间温度
						break;
					}else if(k == 11){
						p.setFcarbon_requirement(cellValue);//碳刨要求
						break;
					}else if(k == 12){
						p.setFpost_heat_requirement(cellValue);//后热要求
						break;
					}else if(k == 13){
						p.setFannealed_weld(cellValue);//退火焊道
						break;
					}else if(k == 14){
						p.setFassembly_clearance(cellValue);//装配间隙
						break;
					}else if(k == 15){
						p.setFcarbon_depth(cellValue);//碳刨深度
						break;
					}else if(k == 16){
						p.setFcarbon_width(cellValue);//碳刨宽度
						break;
					}else if(k == 17){
						p.setFpost_heat_temperature(cellValue);//后热温度
						break;
					}else if(k == 18){
						p.setFafter_hot_time(cellValue);//后热时间
						break;
					}else if(k == 19){
						p.setRoomNo(cellValue);//工艺库名称
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			junction.add(p);
		}
		
		return junction;
	}
	
	/**
	 * 导入Wedlingmachine表数据
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static List<Gather> xlsxGather(String path) throws IOException, InvalidFormatException{
		List<Gather> gather = new ArrayList<Gather>();
		InputStream stream = new FileInputStream(path);
		Workbook workbook = create(stream);
		Sheet sheet = workbook.getSheetAt(0);
		
		int rowstart = sheet.getFirstRowNum()+1;
		int rowEnd = sheet.getLastRowNum();
	    
		for(int i=rowstart;i<=rowEnd;i++){
			Row row = sheet.getRow(i);
			if(null == row){
				continue;
			}
			int cellStart = row.getFirstCellNum();
			int cellEnd = row.getLastCellNum();
			Gather dit = new Gather();
			for(int k = cellStart; k<= cellEnd;k++){
				Cell cell = row.getCell(k);
				if(null == cell){
					continue;
				}
				
				String cellValue = "";
				
				switch (cell.getCellType()){
				case HSSFCell.CELL_TYPE_NUMERIC://数字
					if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式  
		                SimpleDateFormat sdf = null;  
		                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat  
		                        .getBuiltinFormat("h:mm")) {  
		                    sdf = new SimpleDateFormat("HH:mm");  
		                } else {// 日期  
		                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                }  
		                Date date = cell.getDateCellValue();  
		                cellValue = sdf.format(date);  
		            } else if (cell.getCellStyle().getDataFormat() == 58) {  
		                // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)  
		                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                double value = cell.getNumericCellValue();  
		                Date date = org.apache.poi.ss.usermodel.DateUtil  
		                        .getJavaDate(value);  
		                cellValue = sdf.format(date);  
		            } else {
		            	 //处理数字过长时出现x.xxxE9
		            	 BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
		            	 cellValue = big.toString();
                    }
					if(k == 0){
						dit.setGatherNo(cellValue);//采集编号
						break;
					}
					else if(k == 6){
						dit.setLeavetime(cellValue);//离厂时间
						break;
					}
					break;
				case HSSFCell.CELL_TYPE_STRING://字符串
					cellValue = cell.getStringCellValue();
					if(k == 0){
						dit.setGatherNo(cellValue);//采集编号
						break;
					}
					else if(k == 1){
 						Insframework ins = new Insframework();
 						ins.setName(cellValue);
 						dit.setItemname(cellValue);//所属项目
						break;
					}
					else if(k == 2){
 						dit.setStatus(cellValue);//采集状态
						break;
					}
					else if(k == 3){
 						dit.setProtocol(cellValue);//采集通讯协议
						break;
	    			}
					else if(k == 4){
			        	dit.setIpurl(cellValue);//ip地址
						break;
 					}
					else if(k == 5){
 						dit.setMacurl(cellValue);//mac地址
						break;
 					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA: // 公式
					cellValue = String.valueOf(cell.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_BLANK: // 空值
					cellValue = "";
					break;
				case HSSFCell.CELL_TYPE_ERROR: // 故障
					cellValue = "";
					break;
				default:
					cellValue = cell.toString().trim();
					break;
				}
			}
			gather.add(dit);
		}
		
		return gather;
	}
	
	
	public static Workbook create(InputStream in) throws IOException,InvalidFormatException {
		if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("你的excel版本目前poi解析不了");
    }
	
	public static boolean isInteger(String str) {  
	     Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	     return pattern.matcher(str).matches();  
	 }
}
