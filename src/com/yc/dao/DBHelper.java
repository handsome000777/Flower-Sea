package com.yc.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper {

	/*
	 * static { try { System.out.println("��������");
	 * Class.forName(DbProperties.getInstance().getProperty("driverClassName"));
	 * 
	 * } catch (ClassNotFoundException e) { // TODO: handle exception
	 * e.printStackTrace(); } }
	 */

	public Connection getConnection() {
		DbProperties p = DbProperties.getInstance();
		Connection con = null;
		try {
			con = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
					p.getProperty("password"));
			// ��һ�����ط�����������ͬ����������ķ����Ļ�����db.properties��Ҫ�޸�u.s.enname ->userl l
			// con=DriverManager.getConnection( p.getProperty( "url")��p );
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	public int doUpdete(String sql, Object... values) {
		int result = 0;
		try (Connection con = getConnection(); 
			PreparedStatement pstm = con.prepareStatement(sql);
				) {
			setParams(pstm, values);
			result = pstm.executeUpdate();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

	private void setParams(PreparedStatement pstmt, Object... values) throws SQLException {
		// ѭ�������б�values����pstm̫�е�sgl�еĨD?���ò���ֵ
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				// ���еĲ���������object����
				pstmt.setObject(i + 1, values[i]);
			}
		}
	}

	public double selectAggreation(String sql, Object... values) {       //�ۺϲ�ѯ �����
		double result = 0;
		try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); // ԤԤ����
		) {
			// ���ò���
			setParams(pstmt, values);// ��ѯ
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getDouble(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public List<Map<String, Object>> select(String sql, Object... values) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); // ԤԤ����
		) {
			// ���ò���
			setParams(pstmt, values);
			// ��ѯ
			ResultSet rs = pstmt.executeQuery();
			// ����̫s��ȡ�������Ԫ���ݣ�ȡ���е��ֶ���,����Map�ļ�
			ResultSetMetaData rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount(); // ��������ܵ�����//�������
			List<String> columnName = new ArrayList<String>();
			for (int i = 0; i < cc; i++) {
				columnName.add(rsmd.getColumnName(i + 1));
			}
			// ѭ�������,ȡ��ÿһ��while (rs.next()) i
			// ѭ�����е��У�һ����һ���е�ȡֵ���浽Map��
			while (rs.next()) {
			Map<String, Object> map = new HashMap<String, Object>();// ѭ�����е���.
			for (int i = 0; i < columnName.size(); i++) {
				String cn = columnName.get(i); // ȡ����
				Object value = rs.getObject(cn); // ��������ȡֵ//�浽map��
				map.put(cn, value);
			}
			list.add(map);
		  }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public Map<String, String> batchInsert(String path) {
		Map<String, String> map = new HashMap<String, String>();


		List<String> list = new ArrayList<String>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))) {
			String s;
			while ((s = reader.readLine()) != null) {
				list.add(s);
			}
		} catch (Exception ex) {
			// TODO: handle exceptionprivate
			ex.printStackTrace();
		}

		map.put("�����������", list.size() + "");

		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		String sql = "insert into students values(?,?,?)";// seq_students.nextval
		Connection con = null;
		PreparedStatement pstmt = null;
		String s = null;
		String[] ss = null;
		int total = 0;
		try {// ��Դ�Զ��ر�
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "a");
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql);

			for (int i = 0; i < list.size(); i++) {
				s = list.get(i);
				ss = s.split("\t");

				pstmt.setString(1, ss[0]);
				pstmt.setString(2, ss[1]);
				pstmt.setString(3, ss[2]);
				// ������
				pstmt.addBatch();
				if ((i + 1) % 1000 == 0) {
					int[] result = pstmt.executeBatch();
					total += sum(result);
					con.commit();
					pstmt.clearBatch();
				}
			}
			int[] result = pstmt.executeBatch();
			total += sum(result);
			con.commit();
			pstmt.clearBatch();

			map.put("�ɹ�����������", total + "");
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			if (con != null) {
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	private int sum(int[] result) {
		int total = 0;
		if (result == null || result.length <= 0) {
			return 0;
		}
		for (int i = 0; i < result.length; i++) {
			total += 1;
		}
		return total;
	}

}
