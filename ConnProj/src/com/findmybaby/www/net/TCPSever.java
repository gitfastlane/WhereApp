package com.findmybaby.www.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.findmybaby.www.controller.ServerController;

public class TCPSever {

	HashMap<String, DataOutputStream> hmOut = new HashMap<>();

	public TCPSever() {
		Collections.synchronizedMap(hmOut);
		try {
			ServerSocket server = new ServerSocket(7777);
			System.out.println("서버 시작");
			
			while(true) {
				new TCPMulServerReceiver(server.accept()).start();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class TCPMulServerReceiver extends Thread {
		DataOutputStream dos;
		DataInputStream dis;
		Socket client;
		String name;

		public TCPMulServerReceiver(Socket client) {
			this.client = client;
			try {
				dos = new DataOutputStream(client.getOutputStream());
				dis = new DataInputStream(client.getInputStream());
				name = "[" + client.getInetAddress() + "]";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			String userId = null;
			try {
				System.out.println(name+" 입장");
				while (dis != null) {
					String msg = dis.readUTF();
					// msg = "parent,Login,userId,pw,phone,name"
					userId = msg.substring(msg.indexOf(",", msg.indexOf(",")+1)+1 , msg.indexOf(",", msg.indexOf(",", msg.indexOf(",")+1)+1));
					hmOut.put(userId, dos);
					System.out.println("msg="+msg);
					System.out.println("userId="+userId);
					sendToUser(userId,msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				hmOut.remove(userId);
				try {
					dos.close();
					dis.close();
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}

	void sendToUser(String userId, String msg) {
		//DAO 작업 시작
		String result = null;
		ServerController controller = new ServerController();
		result = controller.goService(msg);
		
		try {
			
			if(result!=null) {
				hmOut.get(userId).writeUTF(result);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result: "+result);
	}

	public static void main(String[] args) {
		new TCPSever();
	}
}
