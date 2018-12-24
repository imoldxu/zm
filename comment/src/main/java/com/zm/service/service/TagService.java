package com.zm.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zm.service.entity.Tag;
import com.zm.service.mapper.TagMapper;

@Service
public class TagService {

	@Autowired
	TagMapper tagMapper;

	public List<Tag> getTags() {
		List<Tag> all = tagMapper.selectAll();
		return all;
	}
}
