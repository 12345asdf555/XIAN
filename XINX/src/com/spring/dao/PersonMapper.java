package com.spring.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.model.Person;

public interface PersonMapper {

	List<Person> findAll(@Param("parent")BigInteger parent,@Param("str")String str);
	List<Person> findLeve(int type);
	List<Person> findMethods(int id);
	List<Person> dic();
	List<Person> ins();
	void save(Person welder);
	Person findById(BigInteger fid);
	int getUsernameCount(@Param("welderno")String welderno);
	void update(Person welder);
	void delete(BigInteger fid);
	Person getIdByWelderno(@Param("welderno")String welderno);
	
	List<Person> getFreeWelder(@Param("str")String str);
	String getInsidByFid(BigInteger fid);
	List<Person> getmethod();
	void saveMethod(Person welder);
	void updateMethod(Person welder);
	void deleteMethod(BigInteger fid);
	List<Person> getlibarary1(int id);
	List<Person> getSomeWelders(@Param("str")BigInteger str,@Param("uid")BigInteger uid);
}