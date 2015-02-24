package ba.bitcamp.benjamin.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import ba.bitcamp.loger.Logger;

public class Connection implements Runnable {

	private Socket client;

	public Connection(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {

		BufferedReader read = null;
		PrintStream write = null;
		try {
			read = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			write = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			Logger.log("error", e.getMessage());
			closeClient();
			return;
		}

		String line = null;
		String tempLine = null;
		try {
			while ((tempLine = read.readLine()) != null) {
				if (tempLine.contains("GET") || tempLine.contains("POST")) {
					line = tempLine;
					break;
				}
				System.out.println(tempLine);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dobili: " + line);
		if (!line.contains("GET") && !line.contains("POST")) {
			Logger.log("warning", "Was not GET request");
			Response.error(write, "Invalid request");
			closeClient();
			return;
		}
		String fileName = getFileName(line);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			Response.error(write, "This is not the page you are looking for");
			Logger.log("warning",
					"Client requested missing file " + e.getMessage());
			closeClient();
			return;
		}
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(
				fis));
		String fileLine = "";
		StringBuilder sb = new StringBuilder();
		try {
			while ((fileLine = fileReader.readLine()) != null) {
				sb.append(fileLine);
			}
		} catch (IOException e) {
			Logger.log("error", e.getMessage());
			Response.serverError(write,
					"A well trained group of monkeys is trying to fix the problem");
			closeClient();
			return;
		}
		Response.ok(write, sb.toString());
		closeClient();

	}

	private void closeClient() {
		try {
			client.close();
		} catch (IOException e) {
			Logger.log("warning", e.getMessage());
		}
	}

	private String getFileName(String request) {
		String[] parts = request.split(" ");
		String fileName = null;
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].equals("GET")) {
				fileName = parts[i + 1];
				break;
			}
		}
		String basePath = "." + File.separator + "html" + File.separator;
		if (fileName == null || fileName.equals("/"))
			return basePath + "index.html";

		if (!fileName.contains(".")) {
			fileName += ".html";
		}
		return basePath + fileName;
	}

}
