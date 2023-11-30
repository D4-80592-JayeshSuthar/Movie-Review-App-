package com.sunbeam;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieDao extends Dao {
	private PreparedStatement stmtFindAll;


	protected MovieDao() throws Exception {

		String sqlFindAll = "SELECT * from Movies";
		stmtFindAll = con.prepareStatement(sqlFindAll);

	}
	
	public List<Movies> findAll() throws Exception {
		List<Movies> list = new ArrayList<Movies>();
		try (ResultSet rs = stmtFindAll.executeQuery()) {
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String release = rs.getString("release_date");
				Movies m = new Movies(id, title, release);
				list.add(m);
			}
		} // rs.close()
		return list;
	}

}
