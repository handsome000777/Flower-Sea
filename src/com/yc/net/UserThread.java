package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.dao.DBHelper;

import fiveNet.Piece;

class UserThread implements Runnable {
	Socket socket = null;
	ObjectOutputStream oos;
	// ObjectInputStream ois;
	boolean loop = true;
	private boolean flag = true;// ��� �Ƿ��ж�
	DataCenterThread dataThread;
	String name;
	int image=0;     //ͷ��Ϊ�ڼ�����Ƭ
	
	Integer roomNum = -1;
	int seat;

	public UserThread(Socket socket) {
		super();
		this.socket = socket;
		// this.dataThread = dataThread;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			dataThread = new DataCenterThread(ois, socket);
			dataThread.start();

			// ois = new ObjectInputStream(socket.getInputStream()); // ����
			oos = new ObjectOutputStream(socket.getOutputStream()); // ������
			while (loop) {

				Message message = null;
				while (loop) { // ����ѭ�� ֱ�������߳��յ�message��Ϣ ��־λ�޸�
					if (dataThread.messageFlag == 1) {
						message = dataThread.message;
						dataThread.messageFlag = 0;
						break;
					}

				}
				if (loop == false) {
					return;
				}
				// message = (Message) ois.readObject(); // ����
				switch (message.getRequest()) {
				case "ע��": {
					String uname = message.getName();
					String pwd = message.getPassword();
					String sql = "insert into five(cname,pwd) values( ?,?) ";
					DBHelper db = new DBHelper();

					int result = db.doUpdete(sql, uname, pwd);

					if (result > 0) {
						Message send_message = new Message();
						send_message.setName(uname);
						send_message.setRequest("ע��ɹ�");
						oos.writeObject(send_message);
					} else {
						Message send_message = new Message();
						send_message.setRequest("ע��ʧ��");
						oos.writeObject(send_message);
					}
					break;
				}
				case "��¼": {
					name = message.getName();
					String pwd = message.getPassword();
					String sql = "select *from five where cname=? and pwd=?";
					DBHelper db = new DBHelper();
					List<Map<String, Object>> list = db.select(sql, name, pwd);
					if (list != null && list.size() > 0) {
						Message send_message = new Message();
						send_message.setName(name);
						send_message.setRequest("��¼�ɹ�");
						send_message.setText( Integer.toString(Constans.seatTable)   );        //����TEXT������λ��
						oos.writeObject(send_message);
					} else {
						Message send_message = new Message();
						send_message.setRequest("��¼ʧ��");
						oos.writeObject(send_message);
					}
					break;
				}
				case "��������": {
					new Thread(new MassSendText(name, message.getText(), socket)).start();
					break;
				}
				case "˫������": {
					new Thread(new SendText(name, roomNum, message.getText(), socket)).start();
					break;
				}
				case "��������": {
					// �����߳�
					image = Integer.parseInt(message.getText() ) ;
					seat = message.getSeat();
					roomNum = (seat - 1) / 2;
					Constans.seatTable = Constans.seatTable | 0b1 << seat - 1;
					new Thread(new MassSend(seat, socket)).start();
					new Thread(new GameThread(socket, oos, dataThread, Constans.RoomListList.get(roomNum),name,image  )).start();
					break;
				}
				case "�˳�����": {
					// �����߳�
					// ɾ��GameThread
					for (int j = 0; j < Constans.RoomListList.get(roomNum).size(); j++) {
						GameThread my = Constans.RoomListList.get(roomNum).get(j);
						if (my.socket == socket) { // ɾ�Լ�
							// my.oos.writeObject(new Message(0,0,0,"�쳣�˳�"));
							Constans.RoomListList.get(roomNum).remove(my);
							my.loop = false; // �ر��߳�
							if (Constans.RoomListList.get(roomNum).size() == 1) { // ֪ͨ��һ��
								Constans.RoomListList.get(roomNum).get(0).oos.writeObject(new Piece(0, 0, 0, "�Է�����"));
							}
						}
					}
					// ֪ͨ������
					new Thread(new MassSend(seat, socket, "�˷�ת��")).start();

					Constans.seatTable = Constans.seatTable & ~0b1 << seat - 1;
					seat = 0;
					roomNum = -1;

				}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}
	}
}

class MassSend implements Runnable { // Ⱥ���߳� ����ʱ������λ
	private Socket socket = null; //
	int seat;
	String string; // ת��Ҫ�� Ĭ�Ͻ���

	public MassSend(int seat, Socket socket) {
		super();
		this.seat = seat;
		this.socket = socket;
	}

	public MassSend(int seat, Socket socket, String string) {
		super();
		this.seat = seat;
		this.socket = socket;
		this.string = string;
	}

	@Override
	public void run() { // ���� Ȼ�� ����

		UserThread otherSide = null;
		try {
			Message message = new Message();
			message.setSeat(seat);

			for (int i = 0; i < Server.userList.size(); i++) {
				otherSide = Server.userList.get(i);
				if ("�˷�ת��".equals(string)) {
					message.setRequest("�����˷�");
				} else {
					message.setRequest("��������");
					if (otherSide.socket == socket) {
						message.setRequest("�ɹ�����");
					}
				}

				otherSide.oos.writeObject(message);
				

			}
		} catch (SocketException e) { // ���շ�����
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class MassSendText implements Runnable { // Ⱥ���߳� ��������
	private Socket socket = null; //
	String text;
	String name;

	public MassSendText(String name, String text, Socket socket) {
		super();
		this.text = text;
		this.socket = socket;
		this.name = name;
	}

	@Override
	public void run() { // ���� Ȼ�� ����

		UserThread otherSide = null;
		try {
			Message message = new Message();
			message.setName(name);
			message.setRequest("���˷���");
			message.setText(text);

			for (int i = 0; i < Server.userList.size(); i++) {
				otherSide = Server.userList.get(i);
				otherSide.oos.writeObject(message);

			}
		} catch (SocketException e) { // ���շ�����
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

class SendText implements Runnable { // ת���߳� ����˫������
	private Socket socket = null; //
	String text;
	String name;
	Integer roomNum;

	public SendText(String name, Integer roomNum, String text, Socket socket) {
		super();
		this.text = text;
		this.socket = socket;
		this.name = name;
		this.roomNum = roomNum;
	}

	@Override
	public void run() { // ���� Ȼ�� ����

		GameThread otherSide = null;
		try {
			Message message = new Message();
			message.setName(name);
			message.setRequest("�Է�����");
			message.setText(text);

			for (int i = 0; i < Constans.RoomListList.get(roomNum).size(); i++) {
				otherSide = Constans.RoomListList.get(roomNum).get(i);
				otherSide.oos.writeObject(message);
			}
		} catch (SocketException e) { // ���շ�����
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
