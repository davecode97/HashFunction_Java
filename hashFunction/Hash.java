package hashFunction;

import hashFunction.Connection.mySqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class Hash {

	public void hashFunction() throws SQLException {
		String option = "";
		String userName;
		String password;

		do {
			option = JOptionPane.showInputDialog("----- Login -----\nInput 1 to LogIn.\nInput 2 to SignIn.\nInput 3 to Out.");
			switch (option) {
			case "1":
				userName = JOptionPane.showInputDialog("----- Login -----\nInput your username.");
				password = JOptionPane.showInputDialog("----- Login -----\nInput your password.");
				option = checkCredentials(userName, generatePassword(userName, password), "Login") == false ? "" : "3";
				
				break;
			case "2":
				userName = JOptionPane.showInputDialog("----- SignIn -----\nInput your username.");
				password = JOptionPane.showInputDialog("----- SignIn -----\nInput your password.");
				option = checkCredentials(userName, generatePassword(userName, password), "SignIn") == false ? "" : "3";

				break;
			case "3":
				JOptionPane.showMessageDialog(null, "Out...");
				break;
			default:
				JOptionPane.showMessageDialog(null, "Invalid Option!");
				break;
			}

		} while (!option.equals("3"));
	}

	static boolean checkCredentials(String userName, String password, String sessionType) throws SQLException {
		mySqlConnection mysqlConnection = new mySqlConnection();
		Statement st = null;
		Connection con = mysqlConnection.getConnection();
		String userNameSQL = null;
		String passwordSQL = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		// To check is user & password exist
		try {
			Class.forName("com.mysql.jdbc.Driver");
			pst = (PreparedStatement) con.prepareStatement("SELECT UserName, Password FROM login WHERE UserName ='"
					+ userName + "'" + " AND " + "Password='" + password + "'");

			rs = pst.executeQuery();

			while (rs.next()) {
				userNameSQL = rs.getString("UserName");
				passwordSQL = rs.getString("Password");
			}

			if (sessionType.matches("Login")) {
				if (userName.equals(userNameSQL) && password.equals(passwordSQL)) {
					JOptionPane.showMessageDialog(null, "Login Successful!");
					JOptionPane.showMessageDialog(null, "Welcome! :) \n press enter to exit.");
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "Failed To login! \nCheck your credentials.");
					return false;
				}
			} else {
				if (!userName.equals(userNameSQL) && !password.equals(passwordSQL)) {
					String query = "insert into login values(?,?)";
					// create the mysql insert preparedstatement
					PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
					preparedStmt.setString(1, userName);
					preparedStmt.setString(2, password);
	
					// execute the preparedstatement
					preparedStmt.execute();
					con.close();
					JOptionPane.showMessageDialog(null,"SignIn Successful!");
					
					return false;
				} else {
					JOptionPane.showMessageDialog(null, "User Already exits");
					return false;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		return false;
	}

	static String generatePassword(String userName, String password) {
		int sum = 0;
		int multiply = 1;

		if (password.length() < 5) 
		{
			JOptionPane.showMessageDialog(null, "It's necessary to enter 5 or more characters for password...");
			return null;
			
		}
		else if(userName.length() < 3) 
		{
			JOptionPane.showMessageDialog(null, "It's necessary to enter 3 or more characters for User...");	
			return null;
		
		} else {
			for (int i = 0; i < password.length() / 2; i++)
				sum += (int) password.charAt(i);

			for (int j = password.length() / 2; j < password.length(); j++)
				multiply *= (int) password.charAt(j);

			return password = Integer.toString(sum).concat(Integer.toString(multiply));
		}
	}

}
