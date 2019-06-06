package com.techfun.mvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.techfun.mvc.model.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	DataSource datasource;

	@Autowired
	private JdbcTemplate JdbcTemplate;

	public User findById(Integer id) throws Exception{

		String sql = "select * from users where id=" + id;
		List<User> users = JdbcTemplate.query(sql, new UserMapper());

		if (users.isEmpty()) {
			return null;
		} else {
			return users.get(0);//return as an object
		}
	}

	public List<User> findAll() throws Exception{

		String sql = "select * from users";
		List<User> users = JdbcTemplate.query(sql, new UserMapper());

		return users;

	}

	public void save(User user) throws Exception{
		KeyHolder keyHolder = new GeneratedKeyHolder();
		JdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO users(NAME, EMAIL, ADDRESS, PASSWORD, NEWSLETTER, FRAMEWORK, SEX, NUMBER, COUNTRY, SKILL) "
								+ "values(?,?,?,?,?,?,?,?,?,?)",
						new String[] { "id" });
				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getAddress());
				ps.setString(4, user.getPassword());
				ps.setBoolean(5, user.isNewsletter());
				try {
					ps.setString(6, convertListToDelimitedString(user.getFramework()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ps.setString(7, user.getSex());
				ps.setInt(8, user.getNumber());
				ps.setString(9, user.getCountry());
				try {
					ps.setString(10, convertListToDelimitedString(user.getSkill()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ps;
			}
		}, keyHolder);
		user.setId(keyHolder.getKey().intValue());
	}

	public void update(User user) throws Exception{

		String frameWork = convertListToDelimitedString(user.getFramework());
		String skill = convertListToDelimitedString(user.getSkill());
		JdbcTemplate.update(
				"UPDATE users SET NAME = ?, EMAIL=?, ADDRESS=?, PASSWORD=?, NEWSLETTER=?, FRAMEWORK=?, SEX=?, NUMBER=?, COUNTRY=?, SKILL=? WHERE ID=?",
				user.getName(), user.getEmail(), user.getAddress(), user.getPassword(), user.isNewsletter(), frameWork,
				user.getSex(), user.getNumber(), user.getCountry(), skill, user.getId());
	}

	public void delete(Integer id) throws Exception{
		JdbcTemplate.update("Delete from users where id = ?", id);
	}

	class UserMapper implements RowMapper<User>{
		public User mapRow(ResultSet rs, int arg1) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			try {
				user.setFramework(convertDelimitedStringToList(rs.getString("framework")));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			user.setAddress(rs.getString("address"));
			user.setCountry(rs.getString("country"));
			user.setNewsletter(rs.getBoolean("newsletter"));
			user.setNumber(rs.getInt("number"));
			user.setPassword(rs.getString("password"));
			user.setSex(rs.getString("sex"));
			try {
				user.setSkill(convertDelimitedStringToList(rs.getString("skill")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return user;
		}
	}

	private List<String> convertDelimitedStringToList(String delimitedString) throws Exception{

		List<String> result = new ArrayList<String>();

		if (!StringUtils.isEmpty(delimitedString)) {
			result = Arrays.asList(StringUtils.delimitedListToStringArray(delimitedString, ","));
		}
		return result;

	}

	private String convertListToDelimitedString(List<String> list) throws Exception{

		String result = "";
		if (list != null) {
			result = StringUtils.arrayToCommaDelimitedString(list.toArray());
		}
		return result;

	}

}