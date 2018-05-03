package org.limmen.mystart;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class User extends BaseObject {

	private static final long serialVersionUID = -8464832961657168773L;

	private String email;

	private String password;

	public User(String email, String password) {
		this.email = email;
		this.password = encode(email, password);
		if (email != null) {
			setId(new Long(email.hashCode()));
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean check(String password) {
		return this.password.equals(encode(email, password));
	}

	private String encode(String email, String password) {
		try {
			String text = email + "/myStartSalt/" + password;
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			StringBuilder pw = new StringBuilder(64);
			for (byte b : md.digest(text.getBytes("UTF-8"))) {
				pw.append(String.format("%02x", b));
			}
			return pw.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
