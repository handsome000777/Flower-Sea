package com.yc.net;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constans {       //����˳���
	//public static String  name = "";     //�û���
	
	public static ArrayList<GameThread> roomList0 = new ArrayList<>(); // �������socket ����  ����ʾһ�����䣩
	public static ArrayList<GameThread> roomList1 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList2 = new ArrayList<>(); // 
	public static ArrayList<GameThread> roomList3 = new ArrayList<>(); // 
	
	public static List<ArrayList<GameThread>> RoomListList =    Arrays.asList(roomList0, roomList1,roomList2,roomList3);
	//public static List RoomListList2 = new ArrayList(RoomListList);
	
//	public static volatile int messageFlag=0;            //���ƽ��� Ϊ1ʱ����    ֻ��һ���߳̽��� 
//	public static volatile int pieceFlag=0;
	public static int seatTable =0; // 
}
