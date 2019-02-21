package com.example.demo4.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.example.demo4.domain.Kugc;

public interface KugcMapper {
	@Select("select kugc_cgcid, kugc_cgcname, kugc_code from gcprofile.kugc WHERE kugc_cgcid=#{id}")
	Kugc findKugcById(String id);

	@Select("select kugc_cgcid, kugc_cgcname, kugc_code from gcprofile.kugc")
	List<Kugc> findAllKugc();
}
