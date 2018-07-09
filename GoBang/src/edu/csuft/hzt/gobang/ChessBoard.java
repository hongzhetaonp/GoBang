package edu.csuft.hzt.gobang;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseEvent;
/*五子棋-棋盘类*/
public class ChessBoard extends JPanel implements MouseListener{
	public static int MARGIN=30;//边距
	public static int GRID_SPAN=35;//网格间距
	public static int ROWS=15;//棋盘行数
	public static int COLS=15;//棋盘列数
	Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//初始化每个数组元素为null
	boolean isBlack=true;//默认开始是黑棋先下
	boolean gameOver=false;//游戏是否结束
	int chessCount;//当前棋盘的棋子个数
	int xIndex,yIndex;//当前刚下棋子的索引
	Image bg;//添加背景图片
	boolean isMantoAI=false;//是否为人机模式
	
	public ChessBoard(){
		//setBackground(Color.LIGHT_GRAY);//设置背景颜色为黄色
		try {
			bg=ImageIO.read(new File("./res/qipan.jpg"));
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		addMouseListener(this);//添加事件监听器
		addMouseMotionListener(new MouseMotionListener() {//匿名内部类
			
			@Override
			public void mouseMoved(MouseEvent e) {
				int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
				int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//将鼠标单击的坐标位置转化为网格索引
				if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1)){//游戏已经结束，不能下；落在棋盘外，不能下；x，y位置已经有棋子存在，不能下
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//设置成默认形状
				}else{
					setCursor(new Cursor(Cursor.HAND_CURSOR));//设置成手型
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}
	/*绘制*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);//画棋盘
		//抗锯齿
		Graphics2D gc = (Graphics2D) g;
		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
		for(int i=0;i<=ROWS;i++){//画横线
			g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);
		}
		for(int i=0;i<=COLS;i++){//画直线
			g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN,MARGIN+ROWS*GRID_SPAN);
		}
		/*画棋子*/
		for(int i=0;i<chessCount;i++){
			int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;//网格交叉的x坐标
			int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;//网格交叉的y坐标
			g.setColor(chessList[i].getColor());//设置颜色
			g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);
			if(i==chessCount-1){
				g.setColor(Color.red);//标记最后一个棋子为红色
			    g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);
			}
		}
	}
	
 
 
	@Override
	public void mousePressed(MouseEvent e) {//鼠标按键在组件上按下时调用
		
		//人人对战
		if(isMantoAI==false) {
			if(gameOver) {//游戏已经结束，不能下
				return ;
			}
			String colorName=isBlack ? "黑棋" : "白棋";
			xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
			yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//将鼠标单击的坐标位置转化为网格索引
			if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS) {//棋子落在棋盘外，不能下
				return ;
			}
			if(findChess(xIndex,yIndex)) {//x,y位置已经有棋子存在，不能下
				return ;
			}
			
			Point ch=new Point(xIndex,yIndex,isBlack ? Color.black : Color.white);
			chessList[chessCount++]=ch;
			
			repaint();//通知系统重新绘制
		
		
		    if(isWin()){
			    String msg=String.format("恭喜，%s赢啦", colorName);
			    JOptionPane.showMessageDialog(this, msg);
			    gameOver=true;			
		    }
		    else if(chessCount==(COLS+1)*(ROWS+1))
		    {
			    String msg=String.format("棋鼓相当的对手");
			    JOptionPane.showMessageDialog(this,msg);
			    gameOver=true;
		    }
		    isBlack=!isBlack;
		}
		//人机对战
		if(isMantoAI==true)
		   {
			   manToAI(e.getX(),e.getY());
		   }
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {//鼠标按键在组件上单击(按下并释放)时调用
	}
 
	@Override
	public void mouseReleased(MouseEvent e) {////鼠标按键在组件上释放时调用
	}
 
	@Override
	public void mouseEntered(MouseEvent e) {//鼠标进入组件时调用
	}
 
	@Override
	public void mouseExited(MouseEvent e){//鼠标离开组件时调用		
	}
	
	private boolean findChess(int x,int y){
		for(Point c:chessList){
			if(c!=null&&c.getX()==x&&c.getY()==y)
				return true;
		}
		return false;
	}
	
	/*判断那方赢*/
	private boolean isWin(){
		int continueCount=1;//连续棋子的个数
		for(int x=xIndex-1;x>=0;x--){//横向向左寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,yIndex,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex+1;x<=ROWS;x++){//横向向右寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,yIndex,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//判断记录数大于等于五，即表示此方获胜
			return true;
		}else
			continueCount=1;
		//
		for(int y=yIndex-1;y>=0;y--){//纵向向上寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(xIndex,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int y=yIndex+1;y<=ROWS;y++){//纵向向下寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(xIndex,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//判断记录数大于等于五，即表示此方获胜
			return true;
		}else
			continueCount=1;
		//
		for(int x=xIndex+1,y=yIndex-1;y>=0&&x<=COLS;x++,y--){//右下寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex-1,y=yIndex+1;y<=ROWS&&x>=0;x--,y++){//左上寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//判断记录数大于等于五，即表示此方获胜
			return true;
		}else
			continueCount=1;
		//
		for(int x=xIndex-1,y=yIndex-1;y>=0&&x>=0;x--,y--){//左下寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex+1,y=yIndex+1;y<=ROWS&&x<=COLS;x++,y++){//右上寻找
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//判断记录数大于等于五，即表示此方获胜
			return true;
		}else
			continueCount=1;
		return false;		
	}
	
	private Point getChess(int xIndex,int yIndex,Color color){
		for(Point c:chessList){
			if(c!=null&&c.getX()==xIndex&&c.getY()==yIndex&&c.getColor()==color)
				return c;
		}
		return null;
	}
	
	public void restartGame(){//清除棋子
		for(int i=0;i<chessList.length;i++)
			chessList[i]=null;
		/*恢复游戏相关的变量值*/
		isBlack=true;
		gameOver=false;//游戏是否结束
		chessCount=0;//当前棋盘的棋子个数
		repaint();		
	}
	//悔棋功能实现
	public void goback(){
		if(chessCount==0)
			return ;
		chessList[chessCount-1]=null;
		chessCount--;
		if(chessCount>0){
			xIndex=chessList[chessCount-1].getX();
			yIndex=chessList[chessCount-1].getY();
		}
		isBlack=!isBlack;
		repaint();
	}
	//人机对战
	private void manToAI(int x, int y) {
		// TODO 自动生成的方法存根
		 if(gameOver) return;

		   String colorName=isBlack?"黑棋":"白棋";
		   
		   //将鼠标点击的坐标位置转换成网格索引
		   xIndex=(x-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   System.out.println(xIndex);
		   yIndex=(y-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   
		   //落在棋盘外不能下
		   if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)
			   return;
		   
		   //如果x，y位置已经有棋子存在，不能下
		   if(findChess(xIndex,yIndex))return;
		   
		   //可以进行时的处理
		   Point ch=new Point(xIndex,yIndex,isBlack?Color.black:Color.white);
		   chessList[chessCount++]=ch;
		    repaint();//通知系统重新绘制
		  
		   
		   //如果胜出则给出提示信息，不能继续下棋
		   
		   if(isWin())
		   {
			   String msg=String.format("恭喜，%s赢了！", colorName);
			   JOptionPane.showMessageDialog(this, msg);
			   gameOver=true;
		   }
		   
		   isBlack=!isBlack;
		   
		   if(gameOver) return;
		   
		   
		   colorName=isBlack?"黑棋":"白棋";
		   int m =(int)(Math.random()*15);
		   System.out.println(m);
		   int n =(int)(Math.random()*15);
	 
		   //落在棋盘外不能下
		   if(m<0||m>ROWS||n<0||n>COLS)
			   return;
		   
		   //如果x，y位置已经有棋子存在，不能下
		   if(findChess(m,n))return;
		   
		   //可以进行时的处理
		    ch=new Point(m,n,isBlack?Color.black:Color.white);
		   chessList[chessCount++]=ch;
		    repaint();//通知系统重新绘制
		    if(isWin())
			   {
				   String msg=String.format("恭喜，%s赢了！", colorName);
				   JOptionPane.showMessageDialog(this, msg);
				   gameOver=true;
			   }
		    else if(chessCount==(COLS+1)*(ROWS+1))
		    {
			    String msg=String.format("棋鼓相当的对手");
			    JOptionPane.showMessageDialog(this,msg);
			    gameOver=true;
		    }
		    isBlack=!isBlack;
	}
	
	//Dimension:矩形
	public Dimension getPreferredSize(){
		return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2+GRID_SPAN*ROWS);
	}

}
