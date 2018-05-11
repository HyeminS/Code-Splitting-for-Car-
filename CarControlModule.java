package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CarControlModule {
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket client = null;

		try {
			server = new ServerSocket(7880);
			while (true) {
				client = server.accept();
				System.out.println("User App Connect Acceppted");
				process(client);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void process(Socket socket) throws IOException {
		BufferedReader br = null;
		String command = null;
		PrintStream ps = null;

		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			ps = new PrintStream(socket.getOutputStream());

			command = br.readLine();
			command = command.toLowerCase();

			if (command.equals("open")) {
				System.out.println("Open Message Received");
				processOpen(br, ps);
			}
			ps.println("Open Message Received!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				System.out.println("Buffered Reader Close");
				br.close();
			}
			if (socket != null) {
				System.out.println("User Socket Close\n");
				socket.close();
			}
		}

	}

	private static String processOpen(BufferedReader br, PrintStream ps) throws IOException {
		boolean b = false;

		b = authenticateUser(br, ps);
		if (!b) {
			return "auth failed";
		}

		b = verifyOpen();
		if (!b) {
			return "Open failed";
		}
		
		b = execCore(br, ps);
		if(!b)
		{
			return "Get CoreCode failed";
		}
		
		powerONInfotainment();

		return "OK";
	}

	private static boolean authenticateUser(BufferedReader br, PrintStream ps) throws IOException {
		int code = 0;
		String ret = null;
		int retcode = 0;
		
		code = 10;
		ps.println(String.valueOf(code));
		ret = br.readLine();
		
		retcode = Integer.parseInt(ret);
		if(retcode != (code + 1))
		{
			return false;
		}
		return true;
	}

	private static boolean verifyOpen() {
		return true;
	}
	
	private static boolean execCore(BufferedReader br, PrintStream ps) throws IOException
	{
		String ret = null;
		
		ps.println("Code Requesting");
		ret = br.readLine();
		System.out.println(ret);
		ret = ret.toLowerCase();
		System.out.println(ret);
		if(!ret.equals("core code"))
		{
			return false;
		}
		System.out.println("CoreCode Received\n");
		System.out.println("Code Reconstruction...\n");

		try {
		Runtime.getRuntime().exec("/Users/kyungrikim/Desktop/a.out");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	private static void powerONInfotainment() throws IOException {
		Socket infotainment = null;
		PrintStream ps = null;

		String ip = "192.168.137.220";
		int port = 8888;

		try {
			infotainment = new Socket(ip, port);
			System.out.println("Infotainment Connect Requested");
			ps = new PrintStream(infotainment.getOutputStream());
			System.out.println("Send PowerON Message To INFO");
			ps.println("PowerON");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				System.out.println("INFO PrintStream Close");
				ps.close();
			}
			if (infotainment != null) {
				System.out.println("INFO Socket Close");
				infotainment.close();
			}
		}
	}
}
