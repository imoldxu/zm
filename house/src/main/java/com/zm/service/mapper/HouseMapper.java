package com.zm.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zm.BaseMapper;
import com.zm.service.entity.House;
import com.zm.service.entity.SimpleHouse;

public interface HouseMapper extends BaseMapper<House> {

	List<SimpleHouse> searchHouse(@Param(value = "type") int type, @Param(value = "lat") double lat,
			@Param(value = "lon") double lon, @Param(value = "min") int min, @Param(value = "max") int max,
			@Param(value = "date") String date, @Param(value = "tip") int tip, @Param(value = "offset") int offset,
			@Param(value = "pageSize") int pageSize);

	List<SimpleHouse> searchHouseWithRoom(@Param(value = "type") int type, @Param(value = "room") int room,
			@Param(value = "lat") double lat, @Param(value = "lon") double lon, @Param(value = "min") int min,
			@Param(value = "max") int max, @Param(value = "date") String date, @Param(value = "tip") int tip,
			@Param(value = "offset") int offset, @Param(value = "pageSize") int pageSize);

	List<SimpleHouse> searchHouseByTag(@Param(value = "tags") String tags, @Param(value = "tagSize") Integer tagSize,
			@Param(value = "type") int type, @Param(value = "lat") double lat, @Param(value = "lon") double lon,
			@Param(value = "min") int min, @Param(value = "max") int max, @Param(value = "date") String date,
			@Param(value = "tip") int tip, @Param(value = "offset") int offset,
			@Param(value = "pageSize") int pageSize);

	List<SimpleHouse> searchHouseWithRoomByTag(@Param(value = "tags") String tags,
			@Param(value = "tagSize") Integer tagSize, @Param(value = "type") int type, @Param(value = "room") int room,
			@Param(value = "lat") double lat, @Param(value = "lon") double lon, @Param(value = "min") int min,
			@Param(value = "max") int max, @Param(value = "date") String date, @Param(value = "tip") int tip,
			@Param(value = "offset") int offset, @Param(value = "pageSize") int pageSize);

	SimpleHouse getSimpleHouseById(@Param(value="hid") Long hid);
}
