package fiveNet;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Stack;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import com.yc.net.Constans;
import com.yc.net.ConstansClient;
import com.yc.net.Message;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.widgets.Text;

public class BoardMain {

	protected Shell shell;
	protected Display display;

	// ���䷽��ı���
	// Client client = null;
	Piece sendPiece;
	int accept_color;
	int accept_x;
	int accept_y;

	int shoudao = 0; // 1��ʾ�߳��յ�����

	// �������
	int qx = 20, qy = 40, qw = 490, qh = 490;// ����λ�á����
	int x = 0, y = 0;// ������������
	int[][] SaveGame = new int[15][15];// ����ÿ������
	Stack<Piece> stack = new Stack<Piece>(); // ��ջ����ÿһ���µ���
	protected Canvas canvas;
	Paint paint;
	protected Label turnLabel; // ��Ϸ״̬��˭����
	Button btnNewButton_4; // ��ʼ/׼��
	Label timeLabel; // ����ʱ
	// ���Ʒ���ı���
	int state = 0; // ��Ϸ״̬ 0���� 1����̬ 2�ȴ�̬ 3��������̬
	String request; // ����������Է����� ��׼���� �����ӡ� �����,���ӡ�
	public int ready = -1; // ׼������
	int color = 0; // ��¼��Ҫ��ʲô�� ����=2������=1
	int refresh = 0; // 1ʱ˫�����ˢ�½���
	boolean back = false; // �Ƿ�ͬ�����
	volatile int time = 90; // ����ʱ
	volatile boolean loop = true; // ���������߳�
	private Text text;
	public Text text_1;
	private Composite composite_1;
	private Label lblNewLabel_1;
	private Button btnNewButton_3;
	public Label opLabel;
	public Composite composite_2;
	// ���췽�� ���򴴽���ͬʱ �����ͷ��˲����ӷ����

	public BoardMain() {
		super();
		// TODO Auto-generated constructor stub
		try {
//			Socket socket = new Socket(InetAddress.getLocalHost(), 10086); // �����˵ĵ�ַ�Ͷ˿ں�һ��
//			// TODO �������������,������������⣬�����������
//			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//			client = new Client(socket, ois, oos); // �����ͻ���

		} catch (Exception e) {
			System.out.println("�������ܾ����ӣ�");
		}

	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BoardMain window = new BoardMain();
			window.open();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();

		createContents();

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) { // �豸���þ�����
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(876, 644);
		shell.setText("\u4E94\u5B50\u68CB");
		shell.setLayout(null);
		shell.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, "/fiveNet/five.png"));
		shell.setBackgroundMode(SWT.INHERIT_FORCE); // ��ǿ���������ʹ�ø����ڵı���ɫ ʵ��͸��
//		new Thread(() -> {
//			boolean flag = true;
//			while (flag) {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				Display.getDefault().asyncExec(() -> {
//					shell.setText(new Date().toGMTString());
//				});
//			}
//		}).start();
		Thread t1 = new Thread(new Listen(), "�ͻ��˼�����"); // �߳���
		t1.setDaemon(true); // ��Ϊ�ػ��߳�
		t1.start();

		Thread t2 = new Thread(() -> { // ����ʱ�߳�
			// boolean flag = true;
			while (loop) {
				if (loop == false) {
					return;
				}
				while (time <= 89) {
					if (loop == false) {
						return;
					}
					try {
						Display.getDefault().asyncExec(() -> {
							timeLabel.setText("����ʱ" + time + "");
						});
						Thread.sleep(1000);
						if (loop == false) {
							return;
						}
						time--;
						Display.getDefault().asyncExec(() -> {
							timeLabel.setText("����ʱ" + time + "");
						});
					} catch (Exception e1) {
						// e1.printStackTrace();
					}
				}
			}
		}, "Ttime");
		t2.setDaemon(true);
		t2.start();
		canvas = new Canvas(shell, SWT.NONE);
		canvas.setBounds(10, 41, 520, 543);
		Font font = new Font(shell.getDisplay(), "�����п�", 20, 50);
		canvas.setFont(font);

		timeLabel = new Label(canvas, SWT.NONE);
		timeLabel.setBounds(185, 10, 104, 24);
		timeLabel.setText("\u5012\u8BA1\u65F6\uFF1A");
		timeLabel.setFont(SWTResourceManager.getFont("�����п�", 17, SWT.BOLD));
		timeLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		composite_1 = new Composite(canvas, SWT.NONE);
		composite_1.setBounds(15, 0, 39, 39);
		composite_1.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, ConstansClient.headImg));

		lblNewLabel_1 = new Label(canvas, SWT.BORDER);
		lblNewLabel_1.setAlignment(SWT.CENTER);
		lblNewLabel_1.setText(ConstansClient.name);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(0, 0, 0));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("����", 10, SWT.BOLD));
		lblNewLabel_1.setBounds(65, 18, 49, 16);
		
		opLabel = new Label(canvas, SWT.BORDER);
		opLabel.setAlignment(SWT.CENTER);

		opLabel.setFont(SWTResourceManager.getFont("����", 10, SWT.BOLD));
		opLabel.setBounds(461, 18, 49, 16);
		
		composite_2 = new Composite(canvas, SWT.BORDER);
//		composite_2.setBackgroundImage(SWTResourceManager.getImage(BoardMain.class, ""));
//		composite_2.setBackgroundImage(null);
		composite_2.setBounds(411, 0, 39, 39);
		
		Label lblNewLabel_2_1 = new Label(canvas, SWT.NONE);
		lblNewLabel_2_1.setAlignment(SWT.CENTER);
		lblNewLabel_2_1.setText("\u5BF9\u624B\uFF1A");
		lblNewLabel_2_1.setFont(SWTResourceManager.getFont("΢���ź�", 10, SWT.BOLD));
		lblNewLabel_2_1.setBounds(338, 15, 49, 19);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(523, 0, 330, 584);
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setLocation(33, 45);
		lblNewLabel.setSize(232, 84);
		lblNewLabel.setFont(SWTResourceManager.getFont("�����п�", 55, SWT.BOLD));
		lblNewLabel.setText("\u4E94\u5B50\u68CB");
		Button btnNewButton_1 = new Button(composite, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 1) {
					state = 3;
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "����"));
						turnLabel.setText("�ѷ��ͻ�������,��ȴ�...");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton_1.setLocation(94, 188);
		btnNewButton_1.setSize(105, 37);
		btnNewButton_1.setText("\u6094\u68CB");
		btnNewButton_1.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		Button btnNewButton_2 = new Button(composite, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 1) {
					state = 3;
					if (MessageDialog.openConfirm(shell, "����", "���Ͷ��?")) {
						try {
							ConstansClient.client.send(new Piece(0, 0, 0, "����"));
							turnLabel.setText("���䣡");
							btnNewButton_4.setVisible(true);
							time = 91;
							state = 0;
							ready = 0;
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}

				}

			}
		});
		btnNewButton_2.setLocation(94, 241);
		btnNewButton_2.setSize(105, 37);
		btnNewButton_2.setText("\u8BA4\u8F93");
		btnNewButton_2.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		turnLabel = new Label(composite, SWT.BORDER | SWT.CENTER);
		turnLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_RED));
		turnLabel.setLocation(33, 345);
		turnLabel.setSize(242, 30);
		turnLabel.setFont(SWTResourceManager.getFont("��������", 17, SWT.NORMAL));
		turnLabel.setText("\u25CF\u7B49\u5F85\u5F00\u59CB");

		Label colorLabel = new Label(composite, SWT.BORDER);
		colorLabel.setFont(SWTResourceManager.getFont("������κ", 21, SWT.BOLD));
		colorLabel.setBounds(119, 296, 58, 30);

		colorLabel.setText("�ڷ�");
		colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
		colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_FOREGROUND));

		btnNewButton_4 = new Button(composite, SWT.NONE);
		btnNewButton_4.setFont(SWTResourceManager.getFont("Microsoft Sans Serif", 16, SWT.NORMAL));
		btnNewButton_4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (state == 0) {
					for (int i = 0; i < 15; i++) {
						for (int j = 0; j < 15; j++) {
							SaveGame[i][j] = 0;
						}
					}
					canvas.redraw(); // ׼��ǰ���������̽���
				}
				if(ready==-1) {
					MessageDialog.openInformation(shell, "�ȴ�����", "����������׼����");
				}
				if (state == 0 && ready == 0) { // ��׼��
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "׼��"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ready = 1;
					colorLabel.setText("�ڷ�");
					colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
					colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_FOREGROUND));
					turnLabel.setText("��׼��,�ȴ�����׼��...");
					btnNewButton_4.setVisible(false);

				} else if (state == 0 && ready == 1) { // ��׼��
					try {
						ConstansClient.client.send(new Piece(0, 0, 0, "׼��"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ready = 2;
					state = 2; // ��׼���ĸı�Ϊ�ȴ�̬
					colorLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
					colorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
					colorLabel.setText("�׷�");
					turnLabel.setText("�ȴ���������...");
					btnNewButton_4.setVisible(false);
				}
			}
		});
		btnNewButton_4.setBounds(94, 135, 105, 37);
		btnNewButton_4.setText("\u5F00\u59CB/\u51C6\u5907");

		text = new Text(composite, SWT.BORDER);
		text.setBounds(31, 532, 208, 30);

		text_1 = new Text(composite, SWT.BORDER | SWT.V_SCROLL);
		text_1.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.BOLD));
		text_1.setBounds(33, 414, 263, 92);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!"".equals(text.getText()) && text.getText() != null) {
					Message message = new Message();
					message.setRequest("˫������");
					message.setText(text.getText());
					;
					try {
						ConstansClient.client.send(message);
						text.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(245, 532, 51, 30);
		btnNewButton.setText("\u53D1\u9001");

		btnNewButton_3 = new Button(composite, SWT.NONE);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loop=false;
				Message message = new Message();
				message.setRequest("�˳�����");
				//message.setSeat(1);
				try {
					ConstansClient.client.send(message);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				ConstansClient.seat=0;
				shell.dispose();
			}
		});
		btnNewButton_3.setText("<-\u9000\u51FA");
		btnNewButton_3.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 9, SWT.NORMAL));
		btnNewButton_3.setBounds(258, 14, 55, 25);

		// ������Ļ�ͼ����
		paint = new Paint(qx, qy, qw, qh);
		// �滭�����¼� ����������ִ��ȫ������ ֻҪ������ָı�(�¼�) �� ������������ ������Ӧ����(����)
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent arg0) {
				// �������̸���
				paint.board(arg0);
				switch (state) {
				case 0: {
					break;
				}
				case 1: { // ����̬�����Լ�����
					if (color == 1) {
						paint.blackPiece(arg0, x, y);
					} else if (color == 2) {
						paint.whitePiece(arg0, x, y);
					}
					break;
				}
				case 2: { // �ȴ�̬���ƶ��ֵ���
					if (accept_color == 1) {
						paint.blackPiece(arg0, accept_x, accept_y);
					} else if (accept_color == 2) {
						paint.whitePiece(arg0, accept_x, accept_y);
					}
					break;

				}
				}
				if (refresh == 1) {
					paint.allPiece(arg0, SaveGame);
				}

			}
		});
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				// �ж��Ƿ��ѿ�ʼ��Ϸ
				if (state == 0 && ready == 0) {
					MessageDialog.openInformation(shell, "δ׼��", "׼���˲����棡");
				} else if (state == 0 && ready == 1) {
					MessageDialog.openInformation(shell, "׼����", "����û׼����");
				}
				if (state == 1) { // ��ǰΪ����״̬
					// ��ȡ�����λ��
					x = e.x;
					y = e.y;
					if (!(x > qx && x < qx + qw && y > qy && y < qy + qh)) {
						return;
					}
					// ������λ������ĵ�
					if ((x - qx) % 35 > 17) {
						x = (x - qx) / 35 + 1;
					} else {
						x = (x - qx) / 35;
					}
					if ((y - qy) % 35 > 17) {
						y = (y - qy) / 35 + 1;
					} else {
						y = (y - qy) / 35;
					}
					if (!(SaveGame[x][y] == 0)) {
						return;
					}

					SaveGame = callPaintPiece(color, x, y, SaveGame, canvas); // ����׼����xy ͬʱ�޸���������

					// ����ʤ���Ի���
					boolean wl = winLose.winLost(SaveGame, x, y);
					if (wl) {
						try {
							request = "�������"; // �ж��Ƿ�Ӯ
							sendPiece = new Piece(color, x, y, request);
							ConstansClient.client.send(sendPiece);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						MessageDialog.openInformation(shell, "��Ϸʤ����", "ţƤ����Ӯ�ˣ�"); // ������Ҳ���ػ����
						Display.getDefault().asyncExec(() -> {
							state = 0;
							time = 91;
							turnLabel.setText("Ӯ��");
							btnNewButton_4.setVisible(true);
						});
						ready = 0;

					} else {
						try {
							request = "����";
							sendPiece = new Piece(color, x, y, request);
							ConstansClient.client.send(sendPiece);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						Display.getDefault().asyncExec(() -> {
							state = 2; // �����Ӻ� �ĳɵȴ�̬
							time = 91;
							turnLabel.setText("�ȴ���������...");
						});

					}
				}

			}

			@Override
			public void mouseDoubleClick(MouseEvent e) { // ˫�������������� �ָ���ҳ��
				refresh = 1;
				canvas.redraw();
				Display.getDefault().asyncExec(() -> {
					refresh = 0;
				});
			}
		});

	}

	private int[][] callPaintPiece(int color, int x, int y, int[][] SaveGame, Canvas canvas) { // ��׼λ���µ� x y
		int sx = x * 35 + qx; // ʵ�ʻ���λ��
		int sy = y * 35 + qy;
		if (color == 1 && SaveGame[x][y] == 0) { // ���ƺ���
			Display.getDefault().asyncExec(() -> {
				canvas.redraw(sx - 15, sy - 15, 30, 30, false); // ��� ���� ���ǵ��� û�д���
			});
			SaveGame[x][y] = 1;
			stack.push(new Piece(1, x, y, null));
		} else if (color == 2 && SaveGame[x][y] == 0) { // ���ư���
			Display.getDefault().asyncExec(() -> {
				canvas.redraw(sx - 15, sy - 15, 30, 30, false); // ��� ���� ���ǵ��� û�д���
			});
			SaveGame[x][y] = 2;
			stack.push(new Piece(2, x, y, null));
		}
		return SaveGame;
	}

	class Listen implements Runnable { // ѭ����������˵���Ϣ
		//private boolean flag = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (loop) {
				if (ConstansClient.client != null) {
					try {
						// Piece accept_piece = (Piece) ConstansClient.client.accept();
						Piece accept_piece = null;
						while (loop) { // ����ѭ�� ֱ�������߳��յ�message��Ϣ ��־λ�޸�
							if (ConstansClient.pieceFlag == 1) {
								accept_piece = ConstansClient.client.piece;
								ConstansClient.pieceFlag = 0;
								break;
							}
							if(loop==false) {
								return;
							}
						}

						String accept_Request = accept_piece.getRequest();
						accept_color = accept_piece.getColor();
						accept_x = accept_piece.getX();
						accept_y = accept_piece.getY();
						switch (accept_Request) {
						case "׼��": {
							if (state == 0 && ready == 1) { // �Լ���׼�� �յ��Է�׼��
								ready = 2;
								state = 1; // ��״̬Ϊ����̬
								color = 1; // ��������
								time = 89;
								Display.getDefault().asyncExec(() -> {
									turnLabel.setText("������...");
								});
							}
							if (state == 0 && ready == 0) { // �Լ�û׼�� �յ��Է�׼��
								ready = 1;
								color = 2; // �������
								Display.getDefault().asyncExec(() -> {
									turnLabel.setText("������׼��,��׼��...");
								});
							}
							break;
						}

						case "����": {

							SaveGame = callPaintPiece(accept_color, accept_x, accept_y, SaveGame, canvas);

							Display.getDefault().asyncExec(() -> {
								state = 1; // �����Ӻ��Ϊ����̬
								time = 89;
								turnLabel.setText("������...");
							});

							break;
						}
						case "����": {
							// int temp = state;
							state = 3; // �յ����� ������������̬
							Display.getDefault().syncExec(new Runnable() { // ͬ�� ��Ҫ�ȴ� back ���ؽ��
								@Override
								public void run() {
									back = MessageDialog.openConfirm(shell, "�Է��������", "�Ƿ�ͬ�����?");
								}
							});
							if (back) { // ͬ��
								Piece back_piece = stack.pop();
								Piece back_piece2 = stack.pop();
								SaveGame[back_piece.getX()][back_piece.getY()] = 0;
								SaveGame[back_piece2.getX()][back_piece2.getY()] = 0;
								refresh = 1;
								Display.getDefault().asyncExec(() -> {
									canvas.redraw();
								});
								Display.getDefault().asyncExec(() -> {
									refresh = 0;
									state = 2;
									try {
										ConstansClient.client.send(new Piece(back_piece2.getColor(), 0, 0, "�������")); // �����
																														// ����ɫ��ͬ��
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});

							} else { // ��ͬ��
								state = 2;
								ConstansClient.client.send(new Piece(0, 0, 0, "�������")); // 0��ʾ�ܾ�
							}

							break;
						}
						case "�������": {
							if (accept_color == 0) { // 0��ʾ��ͬ��
								Display.getDefault().asyncExec(() -> {
									MessageDialog.openInformation(shell, "����ʧ��", "�Է��ܾ������Ļ��壡");
									state = 1;
									turnLabel.setText("������...");
								});
							} else {
								Display.getDefault().asyncExec(() -> {
									MessageDialog.openInformation(shell, "����ɹ�", "�Է�ͬ�������Ļ��壡");
								});
								Piece back_piece = stack.pop();
								Piece back_piece2 = stack.pop();
								SaveGame[back_piece.getX()][back_piece.getY()] = 0;
								SaveGame[back_piece2.getX()][back_piece2.getY()] = 0;
								refresh = 1;
								Display.getDefault().asyncExec(() -> {
									canvas.redraw();
								});
								Display.getDefault().asyncExec(() -> {
									refresh = 0;
									state = 1;
									turnLabel.setText("������...");
								});
							}
							break;
						}
						case "����": {
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "��Ϸʤ����", "�Է��򲻹�Ͷ���ˣ�");
							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								turnLabel.setText("Ӯ����");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}
						case "�������": {
							SaveGame = callPaintPiece(accept_color, accept_x, accept_y, SaveGame, canvas);
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "��Ϸʧ�ܣ�", "���ˣ�");

							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								turnLabel.setText("�䣡");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}
						case "�Է�����": {
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openInformation(shell, "��Ϸʤ����", "�Է������ˣ�");
							});
							Display.getDefault().asyncExec(() -> {
								state = 0;
								time = 91;
								ready=-1;
								opLabel.setText("");
								composite_2.setBackgroundImage(null);
								turnLabel.setText("�Է������ˣ�");
								btnNewButton_4.setVisible(true);
							});
							ready = 0;
							break;
						}

						}
					} catch (Exception e) {
						// client = null;
					}
				} else {
					// flag = false;
				}
			}

		}
	}
}
