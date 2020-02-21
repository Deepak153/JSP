package com.usrmgmt.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDao
{
	private static Connection con = null;
	private static Connection getCon()
	{
			try {
				if(con==null)
				{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					 con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","data_owner","root");
				}
			} catch (ClassNotFoundException | SQLException e){e.printStackTrace();}
		return con;
	}
	public UserDao(){}
	
	public static void insertUser(User user)
	{
		con = getCon();
		try{
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select max(id) from paper");
			int id=0;
			if(rs.next())
			{
				id=rs.getInt(1);
			}
			PreparedStatement ps = con.prepareStatement("insert into users values(?,?,?,?)");
			ps.setInt(1, id+1);
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getCountry());
			ps.executeUpdate();
		} catch (SQLException e){e.printStackTrace();}
	}

	public static User selectUser(int id)
	{
		User user = null;
			try{
				con = getCon();
				PreparedStatement ps = con.prepareStatement("select id,name,email,country from users where id =?");				
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
					while (rs.next())
					{
						String name = rs.getString("name");
						String email = rs.getString("email");
						String country = rs.getString("country");
						user = new User(id, name, email, country);
					}
			} catch (SQLException e){e.printStackTrace();}
		return user;
	}

	public static List<User> selectAllUsers()
	{

		// using try-with-resources to avoid closing resources (boiler plate code)
		con = getCon();
		List<User> users = new ArrayList<>();
		try{
			PreparedStatement ps = con.prepareStatement("select * from users");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e){e.printStackTrace();}
		return users;
	}

	public static boolean deleteUser(int id)
	{
		boolean rowDeleted=false;
		try{
			con = getCon();
			PreparedStatement statement = con.prepareStatement("delete from users where id=?");
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		} catch (SQLException e){e.printStackTrace();}
		return rowDeleted;
	}

	public static boolean updateUser(User user)
	{
		boolean rowUpdated=false;
		try{
			con = getCon();
			PreparedStatement statement = con.prepareStatement("update users set name=?,email=?,country=? where id=?");
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			rowUpdated = statement.executeUpdate() > 0;
		}catch (SQLException e){e.printStackTrace();}
		return rowUpdated;
	}
}