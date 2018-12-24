package com.zm.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.entity.HouseTag;
import com.zm.service.mapper.HouseTagMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class HouseTagService {

	@Autowired
	HouseTagMapper tagMapper;

	public void addHouseTags(Long houseid, List<HouseTag> list) {
		
		for(HouseTag tag:list){
			tag.setHouseid(houseid);
		}
		tagMapper.insertList(list);
	}

	public List<HouseTag> getHouseTags(long houseid) {
		Example ex = new Example(HouseTag.class);
		ex.createCriteria().andEqualTo("houseid", houseid);
		List<HouseTag> list = tagMapper.selectByExample(ex);
		return list;
	}
	
	
}
