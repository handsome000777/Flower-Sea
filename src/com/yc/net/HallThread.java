package com.yc.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class HallThread  implements Runnable {
	private Socket socket = null;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean flag = true;// ��� �Ƿ��ж�
	
	volatile int intoRoom =0 ;     // 
	public HallThread() {
		super();
	}
	
	@Override
	public void run() {
		try {
	          ois = new ObjectInputStream(socket.getInputStream());         //����
	          oos = new ObjectOutputStream(socket.getOutputStream());			//������
	          
	          
			 while(true){		           							//�������
				 
//				 if(Constans.roomList.size()==2) {
//					 
//				 }
//				 if(Constans.roomList.size()==2) {
//					 
//				 }
//				 if(Constans.roomList.size()==2) {
//	 
//				 }
//				 if(Constans.roomList.size()==2) {
//	 
//				 }	

				 
				 
			  Message message = (Message) ois.readObject(); //����
			  switch (message.getRequest()) {
				case "���뷿��": {
					break;
				}
			  }
			 
	       }
			}catch (IOException e) {
				// TODO: handle exception
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	

}
