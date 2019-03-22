package com.zm.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.context.TagComment;
import com.zm.service.entity.HouseTag;
import com.zm.service.mapper.HouseTagMapper;

import tk.mybatis.mapper.entity.Example;

@Service
public class HouseTagService {

	@Autowired
	HouseTagMapper tagMapper;

	public void addHouseTags(Long houseid, List<TagComment> list) {
		
		for(TagComment tag:list){
			addHouseTag(houseid, tag);
		}
	}

	private void addHouseTag(Long houseid, TagComment comment) {
		HouseTag tag = getTag(houseid, comment.getTagid());
		if(tag==null) {
			tag = new HouseTag();
			tag.setHouseid(houseid);
			tag.setBad(comment.getBad());
			tag.setGood(comment.getGood());
			tag.setTagid(comment.getTagid());
			tag.setTagname(comment.getTagname());
			tagMapper.insert(tag);
		}else {
			tag.setGood(tag.getGood()+comment.getGood());
			tag.setBad(tag.getBad()+comment.getBad());
			tagMapper.updateByPrimaryKey(tag);
		}
	}

	public HouseTag getTag(long houseid, int tagid) {
		Example ex = new Example(HouseTag.class);
		ex.createCriteria().andEqualTo("houseid", houseid).andEqualTo("tagid", tagid);
		HouseTag tag = tagMapper.selectOneByExample(ex);
		return tag;
	}
	
	public List<HouseTag> getHouseTags(long houseid) {
		Example ex = new Example(HouseTag.class);
		ex.createCriteria().andEqualTo("houseid", houseid);
		List<HouseTag> list = tagMapper.selectByExample(ex);
		return list;
	}

	public void clearTags(long houseid) {
		Example ex = new Example(HouseTag.class);
		ex.createCriteria().andEqualTo("houseid", houseid);
		tagMapper.deleteByExample(ex);
	}
	
}
