package com.yc.ui;

import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class BeginUi extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BeginUi(Composite parent, int style) {
		super(parent, style);
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.setBounds(191, 208, 121, 34);
		btnNewButton.setText("�˻���ս");
		
		Button btnNewButton_1 = new Button(this, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//��������
				
				

				
//				Thread t1 = new Thread(new Listen(), "�ͻ��˼�����"); // �߳���
//				t1.setDaemon(true);   //��Ϊ�ػ��߳�
//				t1.start();
				
				
				LoginUi lu = null;
				try {
					lu = new LoginUi(getShell() , SWT.None);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
				Start.s1.topControl=lu;
				getShell().layout();
			}
		});
		btnNewButton_1.setText("������ս");
		btnNewButton_1.setBounds(191, 275, 121, 34);
		
		Button btnNewButton_2 = new Button(this, SWT.NONE);
		btnNewButton_2.setText("����");
		btnNewButton_2.setBounds(191, 338, 121, 34);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
