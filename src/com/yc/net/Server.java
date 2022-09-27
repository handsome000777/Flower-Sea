package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fiveNet.Piece;

public class Server {
	//static ArrayList<Socket> socketList = new ArrayList<>(); // һ��<socket����>����ͻ���
	static ArrayList<UserThread> userList = new ArrayList<>(); // һ��<�߳�����>����ͻ��˴�����߳�
	public static void main(String[] args) {
		ExecutorService es = Executors.newFixedThreadPool(8); //�����̳߳�(ִ�з���)  �̶���С���̳߳�ֻ֧�ְ˸��̣߳���������ͻ���
		try {
			ServerSocket server = new ServerSocket(10086); // ����һ������˽ӿ�server ServerSocket(10086) //����10086�˿�
			//HallThread hall = new HallThread();  //������Ϣ�߳�
			while (true) {
				// ���տͻ��˵�Socket�����û�пͻ������Ӿ�һֱ��������
				Socket socket = server.accept();                    //���ϼ�������socket  ȡ������ (ͬʱ���췴��soket)
				//socketList.add(socket);
				
				System.out.println(socket);
				// ÿ��һ���û��ʹ���һ���߳�
				
//				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//				DataCenterThread dataThread =new DataCenterThread(  ois ,socket );
//				dataThread.setDaemon(true);
//				dataThread.start();
				UserThread user = new UserThread(socket );   //�����߳�
				userList.add(user);
				// �����߳�
				es.execute(user);  //ִ���߳�

				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//����˽��շ���

}