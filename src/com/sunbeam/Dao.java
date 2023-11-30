package com.sunbeam;

import java.sql.Connection;

public class Dao implements AutoCloseable {
	protected Connection con;
	
	protected Dao() throws Exception {
		con = DbUtil.getConnection();
	}
	
	@Override
	public void close() {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}