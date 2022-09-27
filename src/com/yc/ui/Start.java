package com.yc.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.yc.util.SwtUtils;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

public class Start {
	protected Display display;
	protected Shell shell;

	static StackLayout s1;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Start window = new Start();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
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
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		
		shell.setImage(SWTResourceManager.getImage(Start.class, "/images/bj-1.webp.jpg"));
		shell.setBackgroundImage(SWTResourceManager.getImage(Start.class, "/images/bbbggg.png"));
		shell.setBackgroundMode(SWT.INHERIT_FORCE); // ��ǿ���������ʹ�ø����ڵı���ɫ ʵ��͸��
		shell.setSize(495, 606);
		shell.setText("������");
		SwtUtils.centerShell(display, shell); //����
		s1=new StackLayout()  ;
		shell.setLayout(s1);  //��ǰ��������Ϊ ��ջStackʽ����
		//����Ҫ�л������
		BeginUi bu  = new BeginUi(shell , SWT.None) ;

		//ָ����ǰ���Ϊ
		s1.topControl=bu;
		
		Label lblNewLabel = new Label(bu, SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(51, 51, 51));
		lblNewLabel.setText("\u4E94\r\n\u5B50\r\n\u68CB");
		lblNewLabel.setFont(SWTResourceManager.getFont("�����п�", 32, SWT.NORMAL));
		lblNewLabel.setBounds(411, 86, 50, 163);

	}

}
