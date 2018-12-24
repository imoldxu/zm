package com.zm.service.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.entity.Condition;
import com.zm.service.entity.House;
import com.zm.service.entity.HouseTag;
import com.zm.service.entity.SimpleHouse;
import com.zm.service.feign.client.TagClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zm.service.context.ErrorCode;
import com.zm.service.context.HandleException;
import com.zm.service.mapper.HouseMapper;
import com.zm.service.utils.DateUtils;
import com.zm.service.utils.JSONUtils;
import com.zm.service.utils.RedissonUtil;

import tk.mybatis.mapper.entity.Example;

@Service
public class HouseService {

	@Autowired
	RedissonUtil redissonUtil;
	@Autowired
	HouseMapper houseMapper;
	@Autowired
	TagClient tagClient;
	

	public House issue(Integer uid, House house) {
		//检查之前是否有发布过
		House h = getMyHouse(uid);
		if(h!=null){
			throw new HandleException(ErrorCode.NORMAL_ERROR, "一个人只允许同时发布一套房源");
		}
		house.setUid(uid);
		house.setState(House.STATE_VALID);
		houseMapper.insertUseGeneratedKeys(house);
		
		//給房源設置標籤
		List<HouseTag> tagList = house.getTagList();
		String tagListStr = JSONUtils.getJsonString(tagList);
		tagClient.addHouseTags(house.getId(), tagListStr);
		
		return house;
	}

	public List<SimpleHouse> search(Condition condition, int pageIndex, int pageSize) {
		List<House> list = null;
		
		double lat = condition.getLatitude();
		double lon = condition.getLongitude();
		Integer min = condition.getMin();
		if(min == null){
			min = 0;
		}
		Integer max = condition.getMax();
		if(max == null){
			max = 100000000;
		}
		Integer type = condition.getType();
		if(type==null){
			type = 1;
		}
		Integer room = condition.getRoom();
		//TODO
		String date;
		Date indate = condition.getIndate();
		if(indate!=null){
			date = DateUtils.formatDate(indate);
		}else{
			date = "2099-12-31";
		}
		Integer tip = condition.getTip();
		if(tip==null){
			tip = 1000000000;
		}
		String tags = condition.getTags();
		
		int offset = (pageIndex-1)*pageSize;
		List<SimpleHouse> ret = null;
		if(room == null){
			ret = houseMapper.searchHouse(type, lat, lon, min, max, date, tip, offset, pageSize, );
		}else{
			ret = houseMapper.searchHouseWithRoom(type, room, lat, lon, min, max, date, tip, offset, pageSize);
		}
		return ret;
	}

	public House getDetail(Long houseid) {
	    House house = houseMapper.selectByPrimaryKey(houseid);
	    
	    ObjectMapper mapper = new ObjectMapper();
	    List<HouseTag> list = mapper.convertValue(tagClient.getHouseTags(houseid).fetchOKData(), new TypeReference<List<HouseTag>>() {});
	    house.setTagList(list);
	    
		return house;
	}

	public House getMyHouse(Integer uid) {
		Example ex = new Example(House.class);

		List<Integer> states = new ArrayList<Integer>();
		states.add(House.STATE_VALID);
		states.add(House.STATE_LOCKED);
		ex.createCriteria().andEqualTo("uid", uid).andIn("state", states);
		House h = houseMapper.selectOneByExample(ex);
		return h;
	}

	public House modify(int uid, House house) {
		if(uid != house.getUid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你没有权限操作");
		}
		
		house.setState(null);
		
		houseMapper.updateByPrimaryKeySelective(house);
		return house;
	}

	public void del(int uid, Long houseid) {
		House house = houseMapper.selectByPrimaryKey(houseid);
		if(house == null){
			throw new HandleException(ErrorCode.ARG_ERROR, "参数错误");
		}
		if(uid != house.getUid()){
			throw new HandleException(ErrorCode.DOMAIN_ERROR, "你没有权限操作");
		}
		house.setState(House.STATE_INVALID);
		houseMapper.updateByPrimaryKey(house);
	}

	public House getHouseById(Long houseid) {
		House house = houseMapper.selectByPrimaryKey(houseid);
		return house;
	}

	public void modifyState(Long houseid, int state) {
		House h = getHouseById(houseid);
		if(h.getState() == House.STATE_VALID && state == House.STATE_LOCKED){
			h.setState(state);
		}else if(h.getState() == House.STATE_LOCKED && state == House.STATE_VALID){
			h.setState(state);
		}else if(h.getState() == House.STATE_LOCKED && state == House.STATE_COMPLETE){
			h.setState(state);
		}else{
			throw new HandleException(ErrorCode.NORMAL_ERROR, "房源状态异常");
		}
		houseMapper.updateByPrimaryKey(h);
	}

	
}