package edu.csuft.hzt.gobang;

import javax.swing.JFrame;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class StartChessJFrame extends JFrame {
	private ChessBoard chessBoard;//��ս���
	private Panel toolbar;//���������
	private Button startButton;//���ÿ�ʼ��ť
	private Button backButton;//���û��尴ť
	private Button exitButton;//�����˳���ť
	private Button IsAIButton;//�����˻���ť
	private Button MtoMButtom;//�������˰�ť
 
	
	public StartChessJFrame(){
		setTitle("������--hzt��Ʒ");//���ñ���
		chessBoard=new ChessBoard();//��ʼ�������󣬴�������Ӳ˵�
		MyItemListener lis=new MyItemListener();//��ʼ����ť�¼��������ڲ���
		toolbar=new Panel();//���������ʵ����
		startButton=new Button("���¿�ʼ");
		backButton=new Button("����");
		IsAIButton=new Button("�˻���ս");
		MtoMButtom=new Button("���˶�ս");
		exitButton=new Button("�˳�");//�����ť��ʼ��
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));//��������尴ť��FlowLayout����
		toolbar.add(backButton);
		toolbar.add(startButton);
		toolbar.add(IsAIButton);
		toolbar.add(MtoMButtom);
		toolbar.add(exitButton);//�������ť��ӵ����������
		startButton.addActionListener(lis);
		backButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		IsAIButton.addActionListener(lis);
		MtoMButtom.addActionListener(lis);//�������ť�¼�ע������¼�
		add(toolbar,BorderLayout.SOUTH);//��������岼�ֵ������Ϸ�Ҳ��������
		add(chessBoard);//����������ӵ�������
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ý���ر��¼�
		pack();//����Ӧ��С
	}
	
	
	private class MyItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object obj=e.getSource();//��ȡ�¼�Դ
			if(obj==startButton){
				System.out.println("���¿�ʼ...");//���¿�ʼ
				//JFiveFrame.this�ڲ��������ⲿ��
				chessBoard.restartGame();
			}
			else if(obj==exitButton){
				System.exit(0);//����Ӧ�ó���
			}
			else if(obj==backButton){
				System.out.println("����...");//����
				chessBoard.goback();
			}
			else if(obj==IsAIButton) {
				System.out.println("�˻�ģʽ");
				chessBoard.isMantoAI=true;
				chessBoard.restartGame();
			}
			else if(obj==MtoMButtom) {
				System.out.println("����ģʽ");
				chessBoard.isMantoAI=false;
				chessBoard.restartGame();
			}
		}		
	}
}
