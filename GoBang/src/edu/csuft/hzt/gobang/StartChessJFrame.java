package edu.csuft.hzt.gobang;

import javax.swing.JFrame;
import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class StartChessJFrame extends JFrame {
	private ChessBoard chessBoard;//对战面板
	private Panel toolbar;//工具条面板
	private Button startButton;//设置开始按钮
	private Button backButton;//设置悔棋按钮
	private Button exitButton;//设置退出按钮
	private Button IsAIButton;//设置人机按钮
	private Button MtoMButtom;//设置人人按钮
 
	
	public StartChessJFrame(){
		setTitle("五子棋--hzt作品");//设置标题
		chessBoard=new ChessBoard();//初始化面板对象，创建和添加菜单
		MyItemListener lis=new MyItemListener();//初始化按钮事件监听器内部类
		toolbar=new Panel();//工具面板栏实例化
		startButton=new Button("重新开始");
		backButton=new Button("悔棋");
		IsAIButton=new Button("人机对战");
		MtoMButtom=new Button("人人对战");
		exitButton=new Button("退出");//五个按钮初始化
		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));//将工具面板按钮用FlowLayout布局
		toolbar.add(backButton);
		toolbar.add(startButton);
		toolbar.add(IsAIButton);
		toolbar.add(MtoMButtom);
		toolbar.add(exitButton);//将五个按钮添加到工具面板上
		startButton.addActionListener(lis);
		backButton.addActionListener(lis);
		exitButton.addActionListener(lis);
		IsAIButton.addActionListener(lis);
		MtoMButtom.addActionListener(lis);//将五个按钮事件注册监听事件
		add(toolbar,BorderLayout.SOUTH);//将工具面板布局到界面南方也就是下面
		add(chessBoard);//将面板对象添加到窗体上
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置界面关闭事件
		pack();//自适应大小
	}
	
	
	private class MyItemListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Object obj=e.getSource();//获取事件源
			if(obj==startButton){
				System.out.println("重新开始...");//重新开始
				//JFiveFrame.this内部类引用外部类
				chessBoard.restartGame();
			}
			else if(obj==exitButton){
				System.exit(0);//结束应用程序
			}
			else if(obj==backButton){
				System.out.println("悔棋...");//悔棋
				chessBoard.goback();
			}
			else if(obj==IsAIButton) {
				System.out.println("人机模式");
				chessBoard.isMantoAI=true;
				chessBoard.restartGame();
			}
			else if(obj==MtoMButtom) {
				System.out.println("人人模式");
				chessBoard.isMantoAI=false;
				chessBoard.restartGame();
			}
		}		
	}
}
