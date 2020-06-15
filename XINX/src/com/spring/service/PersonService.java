package com.spring.service;

import java.math.BigInteger;
import java.util.List;

import com.spring.model.Person;
import com.spring.page.Page;

public interface PersonService {

	List<Person> findAll(Page page, BigInteger parent,String str);
	List<Person> findAll(BigInteger parent);
	List<Person> findAll(BigInteger parent,String str);
	List<Person> findLeve(int type);
	List<Person> findMethods(int id);
	List<Person> dic();
	List<Person> ins();
	void save(Person welder);
	Person findById(BigInteger fid);
	int getUsernameCount(String welderno);
	void update(Person welder);
	void delete(BigInteger fid);
	void saveMethod(Person welder);
	void updateMethod(Person welder);
	void deleteMethod(BigInteger fid);
	List<Person> getlibarary1(int id);
	List<Person> getSomeWelders(BigInteger str,BigInteger uid);
	/**
	 * 获取焊接方法
	 * @param typeid 类型id
	 * @return
	 */
	List<Person> getmethod();
	
	/**
	 * 获取所有焊工
	 * @return
	 */
	List<Person> getWelder();
	
	/**
	 * 根据焊工编号获取id
	 * @param welderno
	 * @return
	 */
	Person getIdByWelderno(String welderno);

	/**
	 * 获取所有空闲焊工
	 * @return
	 */
	List<Person> getFreeWelder(Page page,String str);
	
	/**
	 * 根据焊工ID获取组织机构
	 * @param fid
	 * @return
	 */
	String getInsidByFid(BigInteger fid);
}