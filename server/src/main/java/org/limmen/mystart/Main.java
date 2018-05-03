package org.limmen.mystart;

import io.vertx.core.Launcher;

public class Main {

	public static void main(String[] args) {
		Launcher.main(new String[]{"run", "org.limmen.mystart.MyStart", "-conf", "../config.json"});
	}
}
