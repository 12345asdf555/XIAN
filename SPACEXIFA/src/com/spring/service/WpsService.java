package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.dto.WeldDto;
import com.spring.model.User;
import com.spring.model.Wps;
import com.spring.page.Page;

public interface WpsService {
	List<Wps> findAll(Page page, BigInteger parent,String str);
	List<Wps> findAllSpe(BigInteger machine,BigInteger chanel);
	List<Wps> findSpe(BigInteger machine,String ch);
	List<Wps> findHistory(Page page, BigInteger parent);
	List<Wps> AllSpe(BigInteger machine,String ch);
	void give(Wps wps);
	BigInteger findByUid(long uid);
	void save(Wps wps);
	void update(Wps wps);
	int getUsernameCount(String fwpsnum);
	Wps findById(BigInteger fid);
	void delete(BigInteger fid);
	String findIpById(BigInteger fid);
	void deleteHistory(BigInteger fid);
	Wps findSpeById(BigInteger fid);
	int findCount(BigInteger machine, String string);
	void saveSpe(Wps wps);
	void updateSpe(Wps wps);
	List<Wps> getWpslibList(Page page, String search);
	List<Wps> getMainwpsList(Page page, BigInteger parent);
	int getWpslibNameCount(String wpsName);
	void saveWpslib(Wps wps);
	void updateWpslib(Wps wps);
	List<Wps> getWpslibStatus();
	void deleteWpslib(BigInteger fid);
	void deleteWpsBelongLib(BigInteger fid);
	void deleteMainWps(BigInteger fid);
	int getCountByWpslibidChanel(BigInteger wpslibid,int chanel);
	List<Wps> gettrackcard();
	List<Wps> getstepall();
	List<Wps> getEmployee1(String employ_id);
	/**
	 * 获取松下wps
	 * @param parent
	 * @return
	 */
	List<Wps> getSxWpsList(Page page, BigInteger parent);
	
	/**
	 * 松下焊机wps新增
	 * @param wps
	 * @return
	 */
	boolean saveSxWps(Wps wps);
	
	/**
	 * 松下焊机wps新增(历史表)
	 * @param wps
	 * @return
	 */
	boolean saveSxWpsHistory(Wps wps);
	
	/**
	 * OTC焊机wps新增(历史表)
	 * @param wps
	 * @return
	 */
	boolean saveOtcWpsHistory(Wps wps);
	
	/**
	 * 松下焊机wps修改
	 * @param wps
	 * @return
	 */
	boolean editSxWps(Wps wps);
	
	/**
	 * 工艺库与下发焊机的对应列表
	 * @param wps
	 * @return
	 */
	List<Wps> getWpslibMachineHistoryList(Page page, String machineNum, String wpslibName, WeldDto dto);
	
	/**
	 * 查询OTC参数明细
	 * @param machineId 焊机id
	 * @param chanel 通道
	 * @param machineModel 保存时间
	 * @return
	 */
	Wps getOtcDetail(String machineId, String chanel, String time);
	
	/**
	 * 查询松下参数明细
	 * @param machineId 焊机id
	 * @param chanel 通道
	 * @param time 保存时间
	 * @return
	 */
	Wps getSxDetail(String machineId, String chanel, String time);
	
	/**
	 * 根据工艺库名称获取对应的id
	 */
	String getIdByWpslibname(String wpslibname);
	
	/**
	 * 根据焊机号和job号获取参数
	 * @param machine
	 * @param chanel
	 * @return
	 */
	List<Wps> getFnsDetail(BigInteger machine, String chanel);
	/**
	 * 获取焊机的所有job号
	 * @param machine
	 * @return
	 */
	List<Wps> getFnsJobList(BigInteger machine);
	/**
	 * 新增job
	 * @param wps
	 */
	void addJob(Wps wps);
	/**
	 * 修改job
	 * @param wps
	 */
	void updateJob(Wps wps);
	/**
	 * 删除Job
	 */
	void deleteJob(String machine,String chanel);
	
	/**
	 * 获取福尼斯TPSI焊机材质
	 */
	List<String> getTpsiMaterial();
	
	/**
	 * 获取福尼斯TPSI气体
	 */
	List<String> getTpsiGas();
	
	/**
	 * 获取福尼斯TPSI丝径
	 */
	List<String> getTpsiWire();
	
	/**
	 * 以工艺库id，含层号，焊道号为条件判段该工艺是否存在，大于0即存在
	 * @param wpsid 工艺库id
	 * @param layer 焊层号
	 * @param road 焊道号
	 * @return
	 */
	int getCountByWpsidAndLayerroad(String wpsid,String layer,String road);
	
	/**
	 * 新增工艺（实验室）
	 * @param wps
	 */
	void addWpsDetail(Wps wps);
	
	/**
	 * 修改工艺（实验室导入）
	 * @param wps
	 */
	void updateWpsDetail(Wps wps);
	
	/**
	 * 修改工艺（实验室网页）
	 * @param wps
	 */
	void updateWpsDetailById(Wps wps);
	
	/**
	 * 获取所有的工艺库名称
	 * @param uid
	 * @return
	 */
	List<Wps> getAllWpslib();
	/**
	 * 获取任务信息
	 * @param uid
	 * @return
	 */
	List<Wps> gettaskview(Page page, String search);
	
	/**
	 * 导出任务信息
	 * @param uid
	 * @return
	 */
	List<Wps> gettaskview(String search);
	
	/**
	 * 产品超规范
	 * @param uid
	 * @return
	 */
	List<Wps> getunstard(Page page, String search);
	
	/**
	 * 产品超规范导出
	 * @param uid
	 * @return
	 */
	List<Wps> getunstard(String search);
	
	/**
	 * 查找工艺台账
	 * @Description
	 * @author Bruce
	 * @date 2020年2月2日下午4:35:47
	 * @param page 分页
	 * @param search 查找
	 * @return
	 */
	List<Wps> getWpsList(Page page, String search);
	List<Wps> getWpsList(String search);
	
	
	/**
	 * 根据工艺台账id查询工序信息
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午4:23:54
	 * @param fid 工艺台账id
	 * @return
	 */
	List<Wps> getEmployee(String fid);
	
	/**
	 * 根据工序号查询工步
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午5:16:42
	 * @param employeeId 工序号
	 * @return
	 */
	List<Wps> getStep(String employeeId);
	
	/**
	 * 根据工步号查询焊缝号
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午5:16:53
	 * @param stepId 工步号
	 * @return
	 */
	List<Wps> getJunction(String stepId);
	
	/**
	 * 根据工序号和工步号查询参数
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午5:42:12
	 * @param employeeId 工序号
	 * @param stepId 工步号
	 * @return
	 */
	List<Wps> getDetail(String stepId);
	
	/**
	 * 新增工艺台账
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午7:38:04
	 * @param wps
	 */
	void addWps(Wps wps);
	
	/**
	 * 焊接工艺导出
	 * @Description
	 * @author BLACK
	 * @date 2020年2月5日下午7:38:04
	 * @param wps
	 */
	List<Wps> getWeldwps(String wps_lib_id);
	
	/**
	 * 更新工艺台账
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午7:38:32
	 * @param wps
	 */
	void updateWps(Wps wps);
	
	/**
	 * 删除工艺台账
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:41:14
	 * @param fid
	 */
	void deleteWps(String fid);
	
	/**
	 * 新增工序
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午5:10:00
	 * @param wps
	 */
	void addEmployee(Wps wps);
	
	/**
	 * 更新工序
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午4:07:22
	 * @param wps
	 */
	void updateEmployee(Wps wps);
	
	/**
	 * 删除工序
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午12:10:38
	 * @param fid
	 */
	void deleteEmployee(String fid);
	
	/**
	 * 新增工步
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午5:59:22
	 * @param wps
	 */
	void addStep(Wps wps);
	
	/**
	 * 更新工步
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午5:59:35
	 * @param wps
	 */
	void updateStep(Wps wps);
	
	/**
	 * 删除工步
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:38
	 * @param fid
	 */
	void deleteStep(String fid);
	
	/**
	 * 新增焊缝
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:42
	 * @param wps
	 */
	void addJunction(Wps wps);
	
	/**
	 * 更新焊缝
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:45
	 * @param wps
	 */
	void updateJunction(Wps wps);
	
	/**
	 * 删除焊缝
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:49
	 * @param fid
	 */
	void deleteJunction(String fid);
	
	/**
	 * 新增参数
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:54
	 * @param wps
	 */
	void addDetail(Wps wps);
	
	/**
	 * 更新参数
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:36:57
	 * @param wps
	 */
	void updateDetail(Wps wps);
	
	/**
	 * 删除参数
	 * @Description
	 * @author Bruce
	 * @date 2020年2月7日下午3:37:01
	 * @param fid
	 */
	void deleteDetail(String fid);
	
	/**
	 * 通过审核
	 * @Description
	 * @author Bruce
	 * @date 2020年2月11日下午5:58:33
	 * @param fid
	 * @param value
	 */
	void passReview(String fid,String value);
	
	/**
	 * 驳回保存
	 * @Description
	 * @author Bruce
	 * @date 2020年2月17日下午8:15:49
	 * @param wps
	 */
	void turnDown(Wps wps);
	
	/**
	 * 获取工艺下拉框
	 * @Description
	 * @author Bruce
	 * @date 2020年2月26日下午3:04:55
	 * @return
	 */
	List<Wps> getWpsCombobox();
	
	/**
	 * 获取产品图号及其版本的count数
	 * @Description
	 * @author Bruce
	 * @date 2020年3月5日下午6:35:37
	 * @param pdn
	 * @param procudt
	 * @return
	 */
	int getProcudtCount(String pdn, String procudt);
	
	/**
	 * 获取工艺规程及其版本的count数
	 * @Description
	 * @author Bruce
	 * @date 2020年3月5日下午7:21:55
	 * @param wln
	 * @param wpsversion
	 * @param pdn
	 * @param pv
	 * @return
	 */
	int getWpsversionCount(String wln, String wpsversion, String pdn, String pv);
	
	/**
	 * 导入工序判断
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午5:10:00
	 * @param wps
	 */
	void addEmployee1(Wps wps);
	
	/**
	 * 导入工步判断
	 * @Description
	 * @author Bruce
	 * @date 2020年2月6日下午5:10:00
	 * @param wps
	 */
	void addStep1(Wps wps);
	
	/**
	 * 导入工艺台账
	 * @Description
	 * @author Bruce
	 * @date 2020年2月5日下午7:38:04
	 * @param wps
	 */
	void addWps1(Wps wps);
	
	/**
	 * 报工
	 * @Description
	 * @author Bruce
	 * @date 2020年3月27日下午6:39:56
	 * @param fid
	 */
	void finishWork(String fid);
	
	/**
	 * 获取历史记录表格
	 * @Description
	 * @author Bruce
	 * @date 2020年4月1日上午11:34:28
	 * @param page
	 * @param search
	 * @return
	 */
	List<Wps> getHistoryDatagridList(Page page, String search);
	
	/**
	 * 根据焊缝编号查询对应的工序
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日上午10:12:23
	 * @param search
	 * @return
	 */
	List<Wps> getEmployeeByJun(String search);
	
	/**
	 * 根据wps获取焊缝信息
	 * @Description
	 * @author Bruce
	 * @date 2020年7月2日下午2:24:24
	 * @param search
	 * @return
	 */
	List<Wps> getJunctionByWpsid(String search);
	
	/**
	 * 新增任务执行表记录
	 * @Description
	 * @author Bruce
	 * @date 2020年7月3日下午4:12:26
	 * @param wps
	 */
	void addTaskresultRow(Wps wps);
	
	/**
	 * 更新任务执行记录
	 * @Description
	 * @author Bruce
	 * @date 2020年7月4日下午7:53:45
	 * @param wps
	 */
	void updateTaskresult(Wps wps);
	
	/**
	 * 结束任务执行记录
	 * @Description
	 * @author Bruce
	 * @date 2020年7月4日下午7:54:06
	 * @param wps
	 */
	void overTaskresult(Wps wps);
	
	/**
	 * 结束电子任务跟踪卡
	 * @Description
	 * @author Bruce
	 * @date 2020年7月9日下午11:38:40
	 * @param wps
	 */
	void overCard(Wps wps);
	
	/**
	 * 对应工步焊缝
	 * @Description
	 * @author Bruce
	 * @date 2020年8月18日下午5:56:19
	 * @param stepId 工步id
	 * @param junctionId 焊缝id
	 */
	void addStepJunction(String stepId,String junctionId );
	
	/**
	 * 删除工步焊缝对应关系
	 * @Description
	 * @author Bruce
	 * @date 2020年8月18日下午6:29:22
	 * @param stepId
	 */
	void deleteStepJunction(String stepId);
}
