package com.spring.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.spring.dao.WpsMapper;
import com.spring.dto.WeldDto;
import com.spring.model.User;
import com.spring.model.Wps;
import com.spring.page.Page;
import com.spring.service.WpsService;

@Service
@Transactional
public class WpsServiceImpl implements WpsService{

	@Resource
	private WpsMapper mapper;
	public List<Wps> findAll(Page page, BigInteger parent, String str) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.findAll(parent,str);
	}

	public void save(Wps wps) {
		// TODO Auto-generated method stub
		mapper.save(wps);
	}
	
	public int getUsernameCount(String fWPSNum) {
		return mapper.getUsernameCount(fWPSNum);
	}

	public Wps findById(BigInteger fid) {
		return mapper.findById(fid);
	}

	public void update(Wps wps) {
		mapper.update(wps);
		
	}

	
	
	public void delete(BigInteger fid) {
		// TODO Auto-generated method stub
		mapper.delete(fid);
	}

	@Override
	public void give(Wps wps) {
		// TODO Auto-generated method stub
		mapper.give(wps);
	}

	@Override
	public List<Wps> gettrackcard(){
		return mapper.gettrackcard();
	}
	
	@Override
	public BigInteger findByUid(long uid) {
		// TODO Auto-generated method stub
		return mapper.findByUid(uid);
	}

	@Override
	public List<Wps> findHistory(Page page, BigInteger parent) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.findHistory(parent);
	}

	@Override
	public String findIpById(BigInteger fid) {
		// TODO Auto-generated method stub
		return mapper.findIpById(fid);
	}

	@Override
	public void deleteHistory(BigInteger fid) {
		// TODO Auto-generated method stub
		mapper.deleteHistory(fid);
	}

	@Override
	public List<Wps> findAllSpe(BigInteger machine, BigInteger chanel) {
		// TODO Auto-generated method stub
		return mapper.findAllSpe(machine, chanel);
	}
	
	@Override
	public List<Wps> getunstard(Page page, String search){
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getunstard(search);
	}

	@Override
	public List<Wps> getunstard(String search){
		return mapper.getunstard(search);
	}
	
	@Override
	public List<Wps> findSpe(BigInteger machine, String chanel) {
		// TODO Auto-generated method stub
		return mapper.findSpe(machine, chanel);
	}

	@Override
	public Wps findSpeById(BigInteger fid) {
		// TODO Auto-generated method stub
		return mapper.findSpeById(fid);
	}

	@Override
	public int findCount(BigInteger machine, String chanel) {
		// TODO Auto-generated method stub
		return mapper.findCount(machine, chanel);
	}

	@Override
	public void saveSpe(Wps wps) {
		// TODO Auto-generated method stub
		mapper.saveSpe(wps);
	}

	@Override
	public void updateSpe(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateSpe(wps);
	}

	public List<Wps> AllSpe(BigInteger machine,String ch) {
		// TODO Auto-generated method stub
		return mapper.AllSpe(machine,ch);
	}

	@Override
	public List<Wps> getWpslibList(Page page, String search) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getWpslibList(search);
	}

	@Override
	public List<Wps> getMainwpsList(Page page,BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		// TODO Auto-generated method stub
		return mapper.getMainwpsList(parent);
	}

	@Override
	public int getWpslibNameCount(String wpsName) {
		// TODO Auto-generated method stub
		return mapper.getWpslibNameCount(wpsName);
	}

	@Override
	public void saveWpslib(Wps wps) {
		// TODO Auto-generated method stub
		mapper.saveWpslib(wps);
	}

	
	
	@Override
	public void updateWpslib(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateWpslib(wps);
	}

	@Override
	public List<Wps> getWpslibStatus() {
		// TODO Auto-generated method stub
		return mapper.getWpslibStatus();
	}

	@Override
	public void deleteWpslib(BigInteger fid) {
		// TODO Auto-generated method stub
		mapper.deleteWpslib(fid);
	}

	@Override
	public void deleteMainWps(BigInteger fid) {
		// TODO Auto-generated method stub
		mapper.deleteMainWps(fid);
	}

	@Override
	public int getCountByWpslibidChanel(BigInteger wpslibid, int chanel) {
		// TODO Auto-generated method stub
		return mapper.getCountByWpslibidChanel(wpslibid, chanel);
	}

	@Override
	public void deleteWpsBelongLib(BigInteger fid) {
		// TODO Auto-generated method stub
		mapper.deleteWpsBelongLib(fid);
	}

	@Override
	public List<Wps> getSxWpsList(Page page, BigInteger parent) {
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getSxWpsList(parent);
	}

	@Override
	public boolean saveSxWps(Wps wps) {
		return mapper.saveSxWps(wps);
	}

	@Override
	public boolean editSxWps(Wps wps) {
		return mapper.editSxWps(wps);
	}

	@Override
	public List<Wps> getWpslibMachineHistoryList(Page page, String machineNum, String wpslibName, WeldDto dto) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getWpslibMachineHistoryList(machineNum, wpslibName, dto);
	}

	@Override
	public Wps getOtcDetail(String machineId, String chanel, String time) {
		// TODO Auto-generated method stub
		return mapper.getOtcDetail(machineId, chanel, time);
	}

	@Override
	public Wps getSxDetail(String machineId, String chanel, String time) {
		// TODO Auto-generated method stub
		return mapper.getSxDetail(machineId, chanel, time);
	}

	@Override
	public boolean saveSxWpsHistory(Wps wps) {
		// TODO Auto-generated method stub
		return mapper.saveSxWpsHistory(wps);
	}

	@Override
	public boolean saveOtcWpsHistory(Wps wps) {
		// TODO Auto-generated method stub
		return mapper.saveOtcWpsHistory(wps);
	}

	@Override
	public String getIdByWpslibname(String wpslibname) {
		// TODO Auto-generated method stub
		return mapper.getIdByWpslibname(wpslibname);
	}
	
	@Override
	public List<Wps> getFnsDetail(BigInteger machine, String chanel) {
		// TODO Auto-generated method stub
		return mapper.getFnsDetail(machine, chanel);
	}

	@Override
	public List<Wps> getFnsJobList(BigInteger machine) {
		// TODO Auto-generated method stub
		return mapper.getFnsJobList(machine);
	}

	@Override
	public void addJob(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addJob(wps);
	}

	@Override
	public void updateJob(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateJob(wps);
	}

	@Override
	public void deleteJob(String machine, String chanel) {
		// TODO Auto-generated method stub
		mapper.deleteJob(machine, chanel);
	}

	@Override
	public List<String> getTpsiMaterial() {
		// TODO Auto-generated method stub
		return mapper.getTpsiMaterial();
	}

	@Override
	public List<String> getTpsiGas() {
		// TODO Auto-generated method stub
		return mapper.getTpsiGas();
	}

	@Override
	public List<String> getTpsiWire() {
		// TODO Auto-generated method stub
		return mapper.getTpsiWire();
	}

	@Override
	public int getCountByWpsidAndLayerroad(String wpsid, String layer, String road) {
		// TODO Auto-generated method stub
		return mapper.getCountByWpsidAndLayerroad(wpsid, layer, road);
	}

	@Override
	public void addWpsDetail(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addWpsDetail(wps);
	}

	@Override
	public void updateWpsDetail(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateWpsDetail(wps);
	}

	@Override
	public List<Wps> getAllWpslib() {
		// TODO Auto-generated method stub
		return mapper.getAllWpslib();
	}

	@Override
	public void updateWpsDetailById(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateWpsDetailById(wps);
	}

	@Override
	public List<Wps> getWpsList(Page page, String search) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getWpsList(search);
	}
	
	@Override
	public List<Wps> gettaskview(Page page, String search) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.gettaskview(search);
	}
	
	@Override
	public List<Wps> gettaskview(String search) {
		return mapper.gettaskview(search);
	}
	
	@Override
	public List<Wps> getWpsList(String search) {
		// TODO Auto-generated method stub
		return mapper.getWpsList(search);
	}

	@Override
	public List<Wps> getEmployee(String fid) {
		// TODO Auto-generated method stub
		return mapper.getEmployee(fid);
	}

	@Override
	public List<Wps> getWeldwps(String wps_lib_id) {
		// TODO Auto-generated method stub
		return mapper.getWeldwps(wps_lib_id);
	}
	
	@Override
	public List<Wps> getstepall() {
		// TODO Auto-generated method stub
		return mapper.getstepall();
	}
	
	@Override
	public List<Wps> getEmployee1(String employ_id) {
		// TODO Auto-generated method stub
		return mapper.getEmployee1(employ_id);
	}
	
	@Override
	public List<Wps> getStep(String employeeId) {
		// TODO Auto-generated method stub
		return mapper.getStep(employeeId);
	}

	@Override
	public List<Wps> getEmployeStep(String stepId) {
		// TODO Auto-generated method stub
		return mapper.getEmployeStep(stepId);
	}
	
	@Override
	public List<Wps> getJunctionStep(String junctionId) {
		// TODO Auto-generated method stub
		return mapper.getJunctionStep(junctionId);
	}
	
	@Override
	public List<Wps> getJunction(String stepId) {
		// TODO Auto-generated method stub
		return mapper.getJunction(stepId);
	}
	
	@Override
	public List<Wps> getJunctionWeld(String junctionId) {
		// TODO Auto-generated method stub
		return mapper.getJunctionWeld(junctionId);
	}
	
	@Override
	public List<Wps> getJunctionByStepid(String stepId) {
		// TODO Auto-generated method stub
		return mapper.getJunctionByStepid(stepId);
	}

	@Override
	public List<Wps> getDetail(String stepId) {
		// TODO Auto-generated method stub
		return mapper.getDetail(stepId);
	}

	@Override
	public void addWps(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addWps(wps);
	}

	@Override
	public void updateWps(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateWps(wps);
	}

	@Override
	public void deleteWps(String fid) {
		// TODO Auto-generated method stub
		mapper.deleteWpsEmpStepSpe(fid);
		mapper.deleteWpsEmpStepJun(fid);
		mapper.deleteWpsEmpSJ(fid);
		mapper.deleteWpsEmpStep(fid);
		mapper.deleteWpsEmp(fid);
		mapper.deleteWps(fid);
	}
	
	@Override
	public void addEmployee(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addEmployee(wps);
	}

	@Override
	public void updateEmployee(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateEmployee(wps);
	}

	@Override
	public void addStep(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addStep(wps);
	}

	@Override
	public void updateStep(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateStep(wps);
	}

	@Override
	public void deleteEmployee(String fid) {
		// TODO Auto-generated method stub
		mapper.deleteEmployeeStepSpe(fid);
		mapper.deleteEmployeeStepJun(fid);
		mapper.deleteEmployeeSJ(fid);
		mapper.deleteEmployeeStep(fid);
		mapper.deleteEmployee(fid);
	}

	@Override
	public void deleteStep(String fid) {
		// TODO Auto-generated method stub
		mapper.deleteStepSpe(fid);
//		mapper.deleteStepJun(fid);
		mapper.deleteStep(fid);
	}

	@Override
	public void addJunction(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addJunction(wps);
	}

	@Override
	public void updateJunction(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateJunction(wps);
	}

	@Override
	public void deleteJunction(String fid) {
		// TODO Auto-generated method stub
		mapper.deleteJunction(fid);
	}

	@Override
	public void addDetail(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addDetail(wps);
	}

	@Override
	public void updateDetail(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateDetail(wps);
	}

	@Override
	public void deleteDetail(String fid) {
		// TODO Auto-generated method stub
		mapper.deleteDetail(fid);
	}

	@Override
	public void passReview(String fid,String value) {
		// TODO Auto-generated method stub
		mapper.passReview(fid,value);
	}

	@Override
	public void turnDown(Wps wps) {
		// TODO Auto-generated method stub
		mapper.turnDown(wps);
	}

	@Override
	public List<Wps> getWpsCombobox() {
		// TODO Auto-generated method stub
		return mapper.getWpsCombobox();
	}

	@Override
	public int getProcudtCount(String pdn, String procudt) {
		// TODO Auto-generated method stub
		return mapper.getProcudtCount(pdn, procudt);
	}

	@Override
	public int getWpsversionCount(String wln, String wpsversion, String pdn, String pv) {
		// TODO Auto-generated method stub
		return mapper.getWpsversionCount(wln, wpsversion, pdn, pv);
	}
	
	@Override
	public void addWps1(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addWps1(wps);
	}
	
	@Override
	public void addStep1(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addStep1(wps);
	}
	
	@Override
	public void addEmployee1(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addEmployee1(wps);
	}

	@Override
	public void finishWork(String fid) {
		// TODO Auto-generated method stub
		mapper.finishWork(fid);
	}

	@Override
	public List<Wps> getHistoryDatagridList(Page page, String search) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page.getPageIndex(), page.getPageSize());
		return mapper.getHistoryDatagridList(search);
	}

	@Override
	public List<Wps> getEmployeeByJun(String search) {
		// TODO Auto-generated method stub
		return mapper.getEmployeeByJun(search);
	}

	@Override
	public List<Wps> getJunctionByWpsid(String search) {
		// TODO Auto-generated method stub
		return mapper.getJunctionByWpsid(search);
	}

	@Override
	public List<Wps> getTaskParameter(String search) {
		// TODO Auto-generated method stub
		return mapper.getTaskParameter(search);
	}
	
	@Override
	public void addTaskresultRow(Wps wps) {
		// TODO Auto-generated method stub
		mapper.addTaskresultRow(wps);
	}

	@Override
	public void updateTaskresult(Wps wps) {
		// TODO Auto-generated method stub
		mapper.updateTaskresult(wps);
	}

	@Override
	public void overTaskresult(Wps wps) {
		// TODO Auto-generated method stub
		mapper.overTaskresult(wps);
	}

	@Override
	public void overCard(Wps wps) {
		// TODO Auto-generated method stub
		mapper.overCard(wps);
	}

	@Override
	public void addStepJunction(String stepId, String junctionId) {
		// TODO Auto-generated method stub
		mapper.addStepJunction(stepId, junctionId);
	}

	@Override
	public void deleteStepJunction(String stepId,String search) {
		// TODO Auto-generated method stub
		mapper.deleteStepJunction(stepId,search);
	}
}
