package com.spring.model;

import java.math.BigInteger;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

/**
 * 焊口
 * @author gpyf16
 *
 */
@Component
public class WeldedJunction {
	private BigInteger id;
	private BigInteger machid;
	private BigInteger counts;
	private String machine_num;
	private String weldedJunctionno;
	private String serialNo;
	private String pipelineNo;
	private String roomNo;
	private String unit;
	private String area;
	private String systems;
	private String children;
	private String externalDiameter;
	private String wallThickness;
	private int dyne;
	private String specification;
	private double maxElectricity;
	private double minElectricity;
	private double maxValtage;
	private double minValtage;
	private String material;//材质（新增字段）
	private String nextexternaldiameter;//下游外径（新增字段）
	private String startTime;
	private String endTime;
	private String creatTime;
	private String updateTime;
	private int updatecount;
	private String nextwall_thickness;
	private String next_material;
	private String electricity_unit;
	private String valtage_unit;
	private BigInteger updater;
	private BigInteger creater;
	private BigInteger insfid;
	private BigInteger iid;
	private String iname;
	private String fengineering_symbol;
	private String fweld_method;
	private String fweld_position;
	private String fbase_material_type;
	private String fweld_material_model;
	private String ftechnological_design;
	private String fwarm_up_requirement;
	private String finter_channel_temperature;
	private String fcarbon_requirement;
	private String fpost_heat_requirement;
	private String fannealed_weld;
	private String fassembly_clearance;
	private String fcarbon_depth;
	private String fcarbon_width;
	private String fpost_heat_temperature;
	private String fafter_hot_time;
	private String fwps_lib_name;
	private String fwps_lib_version;
	private BigInteger fwpslib_id;
	private String fwelder_name;
	private BigInteger fwelder_id;
	private String fstatus;
	private String ftask_no;
	private int flag;
	private String fprefix_number;
	private String fsuffix_number;
	private String fproduct_number;
	private String fback;
	private String fproduct_drawing_no;
	private String fproduct_name;
	private String fproduct_version;
	
	public BigInteger getInsfid() {
		return insfid;
	}
	public void setInsfid(BigInteger insfid) {
		this.insfid = insfid;
	}
	public BigInteger getMachid() {
		return machid;
	}
	public void setMachid(BigInteger machid) {
		this.machid = machid;
	}
	public BigInteger getCounts() {
		return counts;
	}
	public void setCounts(BigInteger counts) {
		this.counts = counts;
	}
	public BigInteger getUpdater() {
		return updater;
	}
	public void setUpdater(BigInteger updater) {
		this.updater = updater;
	}
	public BigInteger getCreater() {
		return creater;
	}
	public void setCreater(BigInteger creater) {
		this.creater = creater;
	}
	public String getElectricity_unit() {
		return electricity_unit;
	}
	public void setElectricity_unit(String electricity_unit) {
		this.electricity_unit = electricity_unit;
	}
	public String getValtage_unit() {
		return valtage_unit;
	}
	public void setValtage_unit(String valtage_unit) {
		this.valtage_unit = valtage_unit;
	}
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getMachine_num() {
		return machine_num;
	}
	public void setMachine_num(String machine_num) {
		this.machine_num = machine_num;
	}
	public String getWeldedJunctionno() {
		return weldedJunctionno;
	}
	public void setWeldedJunctionno(String weldedJunctionno) {
		this.weldedJunctionno = weldedJunctionno;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getPipelineNo() {
		return pipelineNo;
	}
	public void setPipelineNo(String pipelineNo) {
		this.pipelineNo = pipelineNo;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSystems() {
		return systems;
	}
	public void setSystems(String systems) {
		this.systems = systems;
	}
	public String getChildren() {
		return children;
	}
	public void setChildren(String children) {
		this.children = children;
	}
	public String getExternalDiameter() {
		return externalDiameter;
	}
	public void setExternalDiameter(String externalDiameter) {
		this.externalDiameter = externalDiameter;
	}
	public String getWallThickness() {
		return wallThickness;
	}
	public void setWallThickness(String wallThickness) {
		this.wallThickness = wallThickness;
	}
	public int getDyne() {
		return dyne;
	}
	public void setDyne(int dyne) {
		this.dyne = dyne;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public double getMaxElectricity() {
		return maxElectricity;
	}
	public void setMaxElectricity(double maxElectricity) {
		this.maxElectricity = maxElectricity;
	}
	public double getMinElectricity() {
		return minElectricity;
	}
	public void setMinElectricity(double minElectricity) {
		this.minElectricity = minElectricity;
	}
	public double getMaxValtage() {
		return maxValtage;
	}
	public void setMaxValtage(double maxValtage) {
		this.maxValtage = maxValtage;
	}
	public double getMinValtage() {
		return minValtage;
	}
	public void setMinValtage(double minValtage) {
		this.minValtage = minValtage;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getNextexternaldiameter() {
		return nextexternaldiameter;
	}
	public void setNextexternaldiameter(String nextexternaldiameter) {
		this.nextexternaldiameter = nextexternaldiameter;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public int getUpdatecount() {
		return updatecount;
	}
	public void setUpdatecount(int updatecount) {
		this.updatecount = updatecount;
	}
	public String getNextwall_thickness() {
		return nextwall_thickness;
	}
	public void setNextwall_thickness(String nextwall_thickness) {
		this.nextwall_thickness = nextwall_thickness;
	}
	public String getNext_material() {
		return next_material;
	}
	public void setNext_material(String next_material) {
		this.next_material = next_material;
	}
	public BigInteger getIid() {
		return iid;
	}
	public void setIid(BigInteger iid) {
		this.iid = iid;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getFengineering_symbol() {
		return fengineering_symbol;
	}
	public void setFengineering_symbol(String fengineering_symbol) {
		this.fengineering_symbol = fengineering_symbol;
	}
	public String getFweld_method() {
		return fweld_method;
	}
	public void setFweld_method(String fweld_method) {
		this.fweld_method = fweld_method;
	}
	public String getFweld_position() {
		return fweld_position;
	}
	public void setFweld_position(String fweld_position) {
		this.fweld_position = fweld_position;
	}
	public String getFbase_material_type() {
		return fbase_material_type;
	}
	public void setFbase_material_type(String fbase_material_type) {
		this.fbase_material_type = fbase_material_type;
	}
	public String getFweld_material_model() {
		return fweld_material_model;
	}
	public void setFweld_material_model(String fweld_material_model) {
		this.fweld_material_model = fweld_material_model;
	}
	public String getFtechnological_design() {
		return ftechnological_design;
	}
	public void setFtechnological_design(String ftechnological_design) {
		this.ftechnological_design = ftechnological_design;
	}
	public String getFwarm_up_requirement() {
		return fwarm_up_requirement;
	}
	public void setFwarm_up_requirement(String fwarm_up_requirement) {
		this.fwarm_up_requirement = fwarm_up_requirement;
	}
	public String getFinter_channel_temperature() {
		return finter_channel_temperature;
	}
	public void setFinter_channel_temperature(String finter_channel_temperature) {
		this.finter_channel_temperature = finter_channel_temperature;
	}
	public String getFcarbon_requirement() {
		return fcarbon_requirement;
	}
	public void setFcarbon_requirement(String fcarbon_requirement) {
		this.fcarbon_requirement = fcarbon_requirement;
	}
	public String getFpost_heat_requirement() {
		return fpost_heat_requirement;
	}
	public void setFpost_heat_requirement(String fpost_heat_requirement) {
		this.fpost_heat_requirement = fpost_heat_requirement;
	}
	public String getFannealed_weld() {
		return fannealed_weld;
	}
	public void setFannealed_weld(String fannealed_weld) {
		this.fannealed_weld = fannealed_weld;
	}
	public String getFassembly_clearance() {
		return fassembly_clearance;
	}
	public void setFassembly_clearance(String fassembly_clearance) {
		this.fassembly_clearance = fassembly_clearance;
	}
	public String getFcarbon_depth() {
		return fcarbon_depth;
	}
	public void setFcarbon_depth(String fcarbon_depth) {
		this.fcarbon_depth = fcarbon_depth;
	}
	public String getFcarbon_width() {
		return fcarbon_width;
	}
	public void setFcarbon_width(String fcarbon_width) {
		this.fcarbon_width = fcarbon_width;
	}
	public String getFpost_heat_temperature() {
		return fpost_heat_temperature;
	}
	public void setFpost_heat_temperature(String fpost_heat_temperature) {
		this.fpost_heat_temperature = fpost_heat_temperature;
	}
	public String getFafter_hot_time() {
		return fafter_hot_time;
	}
	public void setFafter_hot_time(String fafter_hot_time) {
		this.fafter_hot_time = fafter_hot_time;
	}
	public String getFwps_lib_name() {
		return fwps_lib_name;
	}
	public void setFwps_lib_name(String fwps_lib_name) {
		this.fwps_lib_name = fwps_lib_name;
	}
	public BigInteger getFwpslib_id() {
		return fwpslib_id;
	}
	public void setFwpslib_id(BigInteger fwpslib_id) {
		this.fwpslib_id = fwpslib_id;
	}
	public BigInteger getFwelder_id() {
		return fwelder_id;
	}
	public void setFwelder_id(BigInteger fwelder_id) {
		this.fwelder_id = fwelder_id;
	}
	public String getFwelder_name() {
		return fwelder_name;
	}
	public void setFwelder_name(String fwelder_name) {
		this.fwelder_name = fwelder_name;
	}
	public String getFstatus() {
		return fstatus;
	}
	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	public String getFtask_no() {
		return ftask_no;
	}
	public void setFtask_no(String ftask_no) {
		this.ftask_no = ftask_no;
	}
	public String getFproduct_number() {
		return fproduct_number;
	}
	public void setFproduct_number(String fproduct_number) {
		this.fproduct_number = fproduct_number;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFwps_lib_version() {
		return fwps_lib_version;
	}
	public void setFwps_lib_version(String fwps_lib_version) {
		this.fwps_lib_version = fwps_lib_version;
	}
	public String getFprefix_number() {
		return fprefix_number;
	}
	public void setFprefix_number(String fprefix_number) {
		this.fprefix_number = fprefix_number;
	}
	public String getFback() {
		return fback;
	}
	public void setFback(String fback) {
		this.fback = fback;
	}
	public String getFproduct_drawing_no() {
		return fproduct_drawing_no;
	}
	public void setFproduct_drawing_no(String fproduct_drawing_no) {
		this.fproduct_drawing_no = fproduct_drawing_no;
	}
	public String getFproduct_name() {
		return fproduct_name;
	}
	public void setFproduct_name(String fproduct_name) {
		this.fproduct_name = fproduct_name;
	}
	public String getFproduct_version() {
		return fproduct_version;
	}
	public void setFproduct_version(String fproduct_version) {
		this.fproduct_version = fproduct_version;
	}
	public String getFsuffix_number() {
		return fsuffix_number;
	}
	public void setFsuffix_number(String fsuffix_number) {
		this.fsuffix_number = fsuffix_number;
	}
	
}
