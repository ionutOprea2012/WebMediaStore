package ra.jademy.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

import ra.jademy.domain.User;

public class UserDAO {

	private Properties importFile;
	private static UserDAO soleInstance = new UserDAO();

	private UserDAO() {
		importFile = new Properties() {
		    public synchronized Enumeration<Object> keys() {
		        return Collections.enumeration(new TreeSet<Object>(keySet()));
		    }
		};
		try {
			FileInputStream stream = new FileInputStream("user.properties");
			importFile.load(stream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open user.properties", e);
		}
	}

	public static UserDAO getInstance() {
		return soleInstance;
	}

	public User getUserByUsername(String username) {
		int i = 0;
		while (true) {
			i++;
			String dbUsername = importFile.getProperty("user[" + i + "].username");
			if (dbUsername == null) {
				return null;
			}
			if (dbUsername.equals(username)) {
				String dbPassword = importFile.getProperty("user[" + i + "].password");
				return new User(dbUsername, dbPassword);
			}
		}

	}

	public int getNextUserIndex() {
		int i = 0;
		while (true) {
			i++;
			String dbUsername = importFile.getProperty("user[" + i + "].username");
			if (dbUsername == null) {
				return i;
			}
		}
	}

	public void saveUserToDao(User user) {
		int index = getNextUserIndex();
		importFile.setProperty("user["+ index +"].username", user.getUsername());
		importFile.setProperty("user["+ index +"].password", user.getPassword());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("user.properties");
			importFile.store(fos, new Date().toString());
			fos.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot save user.properties", e);

		}

	}

}
