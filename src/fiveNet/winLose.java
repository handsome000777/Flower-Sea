package fiveNet;

public class winLose {
	public static boolean winLost(int[][]SaveGame ,int x ,int y ) {
		boolean flag = false;// ��Ӯ
		int count = 1;// ������
		int color = SaveGame[x][y];// ��¼������ɫ
		
//		 for (int i = 0; i < 15; i++) {
//             for (int j = 0; j < 15; j++) {
//                 System.out.print(SaveGame[i][j]);
//                 }
//             }
//         System.out.println();
		
		// �жϺ��������Ƿ�����
		//System.out.println("Flag"+flag);
		for (int i = 1; x + i < SaveGame[0].length; i++) {
			if (color == SaveGame[x + i][y]) {
				count++;
				//System.out.print("count"+count);
			} else {
				break;
			}
		}
		for (int i = 1; x - i >= 0; i++) {
			if (color == SaveGame[x - i][y]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// �ж������������Ƿ�����
		count = 1;
		for (int i = 1; y + i < SaveGame.length; i++) {
			if (color == SaveGame[x][y + i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; y - i >= 0; i++) {
			if (color == SaveGame[x][y - i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// �ж�б�������Ƿ��������������£�
		count = 1;
		for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
			if (color == SaveGame[x - i][y - i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; x + i < SaveGame[0].length && y + i < SaveGame.length; i++) {
			if (color == SaveGame[x + i][y + i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		// �ж�б�������Ƿ��������������ϣ�
		count = 1;
		for (int i = 1; x + i < SaveGame[0].length && y - i >= 0; i++) {
			if (color == SaveGame[x + i][y - i]) {
				count++;
			} else {
				break;
			}
		}
		for (int i = 1; x - i >= 0 && y + i < SaveGame.length; i++) {
			if (color == SaveGame[x - i][y + i]) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 5) {
			flag = true;
		}
		return flag;
	}
}
