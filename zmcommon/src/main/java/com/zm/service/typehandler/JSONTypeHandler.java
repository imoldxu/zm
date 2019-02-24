package com.zm.service.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({JSONObject.class})
public class JSONTypeHandler extends BaseTypeHandler<JSONObject>{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.toJSONString());
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return JSON.parseObject(rs.getString(columnName));
	}

	@Override
	public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return JSON.parseObject(rs.getString(columnIndex));
	}

	@Override
	public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return JSON.parseObject(cs.getString(columnIndex));
	}

	
	
}
