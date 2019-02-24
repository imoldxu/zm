package com.zm.service.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.alibaba.fastjson.JSONArray;


@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({JSONArray.class})
public class JSONArrayHandler extends BaseTypeHandler<JSONArray> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType)
			throws SQLException {
	     ps.setString(i, parameter.toJSONString());
	}

	@Override
	public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return  JSONArray.parseArray(rs.getString(columnName));
	}

	@Override
	public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return JSONArray.parseArray(rs.getString(columnIndex));
	}

	@Override
	public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return JSONArray.parseArray(cs.getString(columnIndex));
	}



}
