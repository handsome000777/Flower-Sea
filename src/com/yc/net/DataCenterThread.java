package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import fiveNet.Piece;

public class DataCenterThread extends Thread {
	ObjectInputStream ois;
	Socket socket;
	boolean loop =true;
	Object obj = null;
	Message message = null;
	Piece piece = null;
	Integer flag = 0;
	public volatile int messageFlag = 0; // ���ƽ��� Ϊ1ʱ���� ֻ��һ���߳̽���
	public volatile int pieceFlag = 0;

	public Message getMessage() {
		return message;
	}

	public Piece getPiece() {
		return piece;
	}

	public DataCenterThread(ObjectInputStream ois, Socket socket) {
		super();
		this.ois = ois;
		this.socket = socket;
	}

	@Override
	public void run() {

		while (loop) { // ����ѭ�� ֱ����־λ�޸� �ӽ����޸ģ�
			try {
				flag = 0;
				obj = ServerAccept(ois);
				if (flag == 1) {
					message = (Message) obj;
					messageFlag = 1;
				} else if (flag == 2) {
					piece = (Piece) obj;
					pieceFlag = 1;
				}

			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object ServerAccept(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Message message = null;
		Piece piece = null;
		try {
			Object obj = ois.readObject();
			if (obj instanceof Message) {
				message = (Message) obj;
				flag = 1;
				return message;

			}
			if (obj instanceof Piece) {
				piece = (Piece) obj;
				flag = 2;
				return piece;
			}
		} catch (SocketException e) {   //�û��Ͽ����� ��ɾ����Ӧ�ķ���˴����߳�
			for(int i =0; i<Constans.RoomListList.size(); i++) {         //ɾ��GameThread
				for(int j = 0 ; j <Constans.RoomListList.get(i).size() ;j++) {
					GameThread my = Constans.RoomListList.get(i).get(j);
					if (my.socket == socket) { // ɾ�Լ�
						System.out.println("�����û���Ϸ�ķ����߳�  �����СΪ " + Constans.RoomListList.get(i).size());
						//my.oos.writeObject(new Message(0,0,0,"�쳣�˳�"));
						Constans.RoomListList.get(i).remove(my);
						my.loop=false;
						System.out.println("�Ѿ�ɾ����һ�������û���Ϸ�ķ����߳�  �����СΪ " + Constans.RoomListList.get(i).size());
						if(Constans.RoomListList.get(i).size()==1) {
							Constans.RoomListList.get(i).get(0).oos.writeObject(new Piece(0,0,0,"�Է�����"));
						}
					}
				}
			}
			
			for (int i = 0; i < Server.userList.size(); i++) {           //ɾ��UserThread
				UserThread my = Server.userList.get(i);
				if (my.socket == socket) { // ɾ�Լ�
					Server.userList.remove(my);
					my.loop=false;
					System.out.println("�Ѿ�ɾ����һ�������û��ķ����߳�  userlist��СΪ " + Server.userList.size());
				}
			}
			//e.printStackTrace();
			loop=false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
}
