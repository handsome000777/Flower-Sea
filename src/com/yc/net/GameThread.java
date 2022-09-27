package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import fiveNet.Piece;

public class GameThread implements Runnable {

	boolean loop = true;
	Socket socket = null;
	private ArrayList<GameThread> list; // �ͻ����̼߳���
	ObjectOutputStream oos;
	// private ObjectInputStream ois;
	DataCenterThread dataThread;
	private boolean flag = true;// ��� �Ƿ��ж�
	int i;
	String name;
	int image;
	public GameThread(Socket socket, ObjectOutputStream oos, DataCenterThread dataThread,
			ArrayList<GameThread> roomList, String name, int image) {
		super();
		this.socket = socket;
		this.oos = oos;
		this.dataThread = dataThread;
		this.name = name;
		this.image = image;
		this.list = roomList;
		roomList.add(this); // �ѵ�ǰ�̼߳���list��
	}

	@Override
	public void run() { // ���� Ȼ�� ����
		GameThread otherSide = null;
		for (i = 0; i < list.size(); i++) {
			otherSide = list.get(i);
		}

		try {
			
			Message message = new Message();                   //�򷿼���˷����Լ�������ͷ��
			message.setRequest("������Ϣ");
			message.setName(name);
			message.setText( image +"");
			int size = list.size();
			for (i = 0; i < size; i++) {
				otherSide = list.get(i);
				if (otherSide.socket != socket) { 			//�����ַ��Լ���Ϣ
					otherSide.oos.writeObject(message);
					message.setName(otherSide.name);					//˳�㱣�������Ϣ
					message.setText(otherSide.image+"");
				}else if(size==2){										//��������Ϣ���Լ�
					try {
						Thread.sleep(590);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					otherSide.oos.writeObject(message);
				}
			}
			
			
			// ois = new ObjectInputStream(socket.getInputStream()); //����
			// oos = new ObjectOutputStream(socket.getOutputStream()); //������
			while (loop) { // �ͻ��˷�������˵���Ϣ������д�������׽�����ȥ
				// Piece piece = (Piece) ois.readObject(); //����
				Piece piece = null;
				while (loop) { // ����ѭ�� ֱ�������߳��յ�message��Ϣ ��־λ�޸�
					if (dataThread.pieceFlag == 1) {
						piece = dataThread.piece;
						dataThread.pieceFlag = 0;
						break;
					}
				}

				size = list.size();
				for (i = 0; i < size; i++) {
					otherSide = list.get(i);
					if (otherSide.socket != socket) { // ����һ���̷߳��� user�Է�
						otherSide.oos.writeObject(piece);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
