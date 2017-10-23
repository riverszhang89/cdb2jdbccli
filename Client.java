import java.sql.*;
import java.io.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws Exception {
        Connection conn = null; 
        Statement stmt = null; 
        PreparedStatement ps = null; 
        ResultSet rset = null;
        ResultSetMetaData md = null;
        int cnt = 0;
        conn = DriverManager.getConnection("jdbc:comdb2:" +args[0]);
		ArrayList<String> binds = new ArrayList<String>();
        stmt = conn.createStatement();

        if (args.length == 2 && "-".equals(args[1])) {
            /* stdin mode */
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("cdb2jdbc> ");
            String line = null;
            while ((line = br.readLine()) != null) {
				try {
					if (line.startsWith("@bind")) {
						binds.add(line);
						System.out.print("cdb2jdbc> ");
						continue;
					}

					if (line.equals("")) {
						System.out.print("cdb2jdbc> ");
						continue;
					}

					ps = conn.prepareStatement(line);
					if (binds.size() != 0) {
						for (String bind : binds) {
							String[] toks = bind.split(" ");
							if (toks == null || toks.length != 4) {
								System.err.println("Bad bind command.");
								System.out.print("cdb2jdbc> ");
								continue;
							}

							if (toks[1].equalsIgnoreCase("integer")) {
								ps.setLong(Integer.parseInt(toks[2]), Long.parseLong(toks[3]));
							} else if (toks[1].equalsIgnoreCase("real")) {
								ps.setDouble(Integer.parseInt(toks[2]), Double.parseDouble(toks[3]));
							} else if (toks[1].equalsIgnoreCase("cstring")) {
								ps.setString(Integer.parseInt(toks[2]), toks[3]);
							} else {
								System.err.format("Unsupported param type: %s\n", toks[1]);
							}
						}
						binds.clear();
					}

					rset = ps.executeQuery();
					ps.clearParameters();
					md = rset.getMetaData();
					cnt = md.getColumnCount();

					if (cnt == 0) {
						System.out.print("cdb2jdbc> ");
						continue;
					}

					while(rset.next()) {
						System.out.print("(");
						for(int i = 1; i <= cnt; ++i) {
							if (rset.getBytes(i) == null) {
								System.out.format("%s=null", md.getColumnName(i));
							} else if (md.getColumnType(i) == java.sql.Types.BLOB) {
								System.out.format("%s=x'", md.getColumnName(i));

								byte[] bytes = rset.getBytes(i);
								for (byte b : bytes)
									System.out.format("%02X", b);
								System.out.print("'");
							} else {
								System.out.format("%s=%s", md.getColumnName(i), rset.getObject(i));
							}
							if (i < cnt)
								System.out.print(", ");
						}
						System.out.print(")\n");
					}
					System.out.print("cdb2jdbc> ");
				} catch (SQLException sqle) {
					System.err.println(sqle.getMessage());
					System.out.print("cdb2jdbc> ");
				}
			}
        } else for (int j = 1; j < args.length; ++j) {
			try {
				rset = stmt.executeQuery(args[j]);
				md = rset.getMetaData();
				cnt = md.getColumnCount();

				while(rset.next()) {
					for(int i = 1; i <= cnt; ++i) {
						if (rset.getBytes(i) == null) {
							System.out.format("%s=null, ", md.getColumnName(i));
						} else if (md.getColumnType(i) == java.sql.Types.BLOB) {
							System.out.format("%s=x'", md.getColumnName(i));

							byte[] bytes = rset.getBytes(i);
							for (byte b : bytes)
								System.out.format("%02X", b);
							System.out.print("', ");
						} else {
							System.out.print(md.getColumnName(i) + "=" + rset.getObject(i) + ", ");
						}
					}
					System.out.print("\n");
				}
			} catch (SQLException sqle) {
				System.err.println(sqle.getMessage());
				System.out.print("cdb2jdbc> ");
			}
        }
    }
}
