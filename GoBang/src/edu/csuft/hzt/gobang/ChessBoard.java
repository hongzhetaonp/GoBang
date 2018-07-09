package edu.csuft.hzt.gobang;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseEvent;
/*������-������*/
public class ChessBoard extends JPanel implements MouseListener{
	public static int MARGIN=30;//�߾�
	public static int GRID_SPAN=35;//������
	public static int ROWS=15;//��������
	public static int COLS=15;//��������
	Point[] chessList=new Point[(ROWS+1)*(COLS+1)];//��ʼ��ÿ������Ԫ��Ϊnull
	boolean isBlack=true;//Ĭ�Ͽ�ʼ�Ǻ�������
	boolean gameOver=false;//��Ϸ�Ƿ����
	int chessCount;//��ǰ���̵����Ӹ���
	int xIndex,yIndex;//��ǰ�������ӵ�����
	Image bg;//��ӱ���ͼƬ
	boolean isMantoAI=false;//�Ƿ�Ϊ�˻�ģʽ
	
	public ChessBoard(){
		//setBackground(Color.LIGHT_GRAY);//���ñ�����ɫΪ��ɫ
		try {
			bg=ImageIO.read(new File("./res/qipan.jpg"));
		} catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		
		addMouseListener(this);//����¼�������
		addMouseMotionListener(new MouseMotionListener() {//�����ڲ���
			
			@Override
			public void mouseMoved(MouseEvent e) {
				int x1=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
				int y1=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//����굥��������λ��ת��Ϊ��������
				if(x1<0||x1>ROWS||y1<0||y1>COLS||gameOver||findChess(x1,y1)){//��Ϸ�Ѿ������������£����������⣬�����£�x��yλ���Ѿ������Ӵ��ڣ�������
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));//���ó�Ĭ����״
				}else{
					setCursor(new Cursor(Cursor.HAND_CURSOR));//���ó�����
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
	}
	/*����*/
	public void paintComponent(Graphics g){
		super.paintComponent(g);//������
		//�����
		Graphics2D gc = (Graphics2D) g;
		gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
		for(int i=0;i<=ROWS;i++){//������
			g.drawLine(MARGIN, MARGIN+i*GRID_SPAN, MARGIN+COLS*GRID_SPAN, MARGIN+i*GRID_SPAN);
		}
		for(int i=0;i<=COLS;i++){//��ֱ��
			g.drawLine(MARGIN+i*GRID_SPAN, MARGIN, MARGIN+i*GRID_SPAN,MARGIN+ROWS*GRID_SPAN);
		}
		/*������*/
		for(int i=0;i<chessCount;i++){
			int xPos=chessList[i].getX()*GRID_SPAN+MARGIN;//���񽻲��x����
			int yPos=chessList[i].getY()*GRID_SPAN+MARGIN;//���񽻲��y����
			g.setColor(chessList[i].getColor());//������ɫ
			g.fillOval(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);
			if(i==chessCount-1){
				g.setColor(Color.red);//������һ������Ϊ��ɫ
			    g.drawRect(xPos-Point.DIAMETER/2, yPos-Point.DIAMETER/2, Point.DIAMETER, Point.DIAMETER);
			}
		}
	}
	
 
 
	@Override
	public void mousePressed(MouseEvent e) {//��갴��������ϰ���ʱ����
		
		//���˶�ս
		if(isMantoAI==false) {
			if(gameOver) {//��Ϸ�Ѿ�������������
				return ;
			}
			String colorName=isBlack ? "����" : "����";
			xIndex=(e.getX()-MARGIN+GRID_SPAN/2)/GRID_SPAN;
			yIndex=(e.getY()-MARGIN+GRID_SPAN/2)/GRID_SPAN;//����굥��������λ��ת��Ϊ��������
			if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS) {//�������������⣬������
				return ;
			}
			if(findChess(xIndex,yIndex)) {//x,yλ���Ѿ������Ӵ��ڣ�������
				return ;
			}
			
			Point ch=new Point(xIndex,yIndex,isBlack ? Color.black : Color.white);
			chessList[chessCount++]=ch;
			
			repaint();//֪ͨϵͳ���»���
		
		
		    if(isWin()){
			    String msg=String.format("��ϲ��%sӮ��", colorName);
			    JOptionPane.showMessageDialog(this, msg);
			    gameOver=true;			
		    }
		    else if(chessCount==(COLS+1)*(ROWS+1))
		    {
			    String msg=String.format("����൱�Ķ���");
			    JOptionPane.showMessageDialog(this,msg);
			    gameOver=true;
		    }
		    isBlack=!isBlack;
		}
		//�˻���ս
		if(isMantoAI==true)
		   {
			   manToAI(e.getX(),e.getY());
		   }
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {//��갴��������ϵ���(���²��ͷ�)ʱ����
	}
 
	@Override
	public void mouseReleased(MouseEvent e) {////��갴����������ͷ�ʱ����
	}
 
	@Override
	public void mouseEntered(MouseEvent e) {//���������ʱ����
	}
 
	@Override
	public void mouseExited(MouseEvent e){//����뿪���ʱ����		
	}
	
	private boolean findChess(int x,int y){
		for(Point c:chessList){
			if(c!=null&&c.getX()==x&&c.getY()==y)
				return true;
		}
		return false;
	}
	
	/*�ж��Ƿ�Ӯ*/
	private boolean isWin(){
		int continueCount=1;//�������ӵĸ���
		for(int x=xIndex-1;x>=0;x--){//��������Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,yIndex,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex+1;x<=ROWS;x++){//��������Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,yIndex,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//�жϼ�¼�����ڵ����壬����ʾ�˷���ʤ
			return true;
		}else
			continueCount=1;
		//
		for(int y=yIndex-1;y>=0;y--){//��������Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(xIndex,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int y=yIndex+1;y<=ROWS;y++){//��������Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(xIndex,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//�жϼ�¼�����ڵ����壬����ʾ�˷���ʤ
			return true;
		}else
			continueCount=1;
		//
		for(int x=xIndex+1,y=yIndex-1;y>=0&&x<=COLS;x++,y--){//����Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex-1,y=yIndex+1;y<=ROWS&&x>=0;x--,y++){//����Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//�жϼ�¼�����ڵ����壬����ʾ�˷���ʤ
			return true;
		}else
			continueCount=1;
		//
		for(int x=xIndex-1,y=yIndex-1;y>=0&&x>=0;x--,y--){//����Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		for(int x=xIndex+1,y=yIndex+1;y<=ROWS&&x<=COLS;x++,y++){//����Ѱ��
			Color c=isBlack ? Color.black : Color.white;
			if(getChess(x,y,c)!=null){
				continueCount++;
			}else
				break;
		}
		if(continueCount>=5){//�жϼ�¼�����ڵ����壬����ʾ�˷���ʤ
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
	
	public void restartGame(){//�������
		for(int i=0;i<chessList.length;i++)
			chessList[i]=null;
		/*�ָ���Ϸ��صı���ֵ*/
		isBlack=true;
		gameOver=false;//��Ϸ�Ƿ����
		chessCount=0;//��ǰ���̵����Ӹ���
		repaint();		
	}
	//���幦��ʵ��
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
	//�˻���ս
	private void manToAI(int x, int y) {
		// TODO �Զ����ɵķ������
		 if(gameOver) return;

		   String colorName=isBlack?"����":"����";
		   
		   //�������������λ��ת������������
		   xIndex=(x-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   System.out.println(xIndex);
		   yIndex=(y-MARGIN+GRID_SPAN/2)/GRID_SPAN;
		   
		   //���������ⲻ����
		   if(xIndex<0||xIndex>ROWS||yIndex<0||yIndex>COLS)
			   return;
		   
		   //���x��yλ���Ѿ������Ӵ��ڣ�������
		   if(findChess(xIndex,yIndex))return;
		   
		   //���Խ���ʱ�Ĵ���
		   Point ch=new Point(xIndex,yIndex,isBlack?Color.black:Color.white);
		   chessList[chessCount++]=ch;
		    repaint();//֪ͨϵͳ���»���
		  
		   
		   //���ʤ���������ʾ��Ϣ�����ܼ�������
		   
		   if(isWin())
		   {
			   String msg=String.format("��ϲ��%sӮ�ˣ�", colorName);
			   JOptionPane.showMessageDialog(this, msg);
			   gameOver=true;
		   }
		   
		   isBlack=!isBlack;
		   
		   if(gameOver) return;
		   
		   
		   colorName=isBlack?"����":"����";
		   int m =(int)(Math.random()*15);
		   System.out.println(m);
		   int n =(int)(Math.random()*15);
	 
		   //���������ⲻ����
		   if(m<0||m>ROWS||n<0||n>COLS)
			   return;
		   
		   //���x��yλ���Ѿ������Ӵ��ڣ�������
		   if(findChess(m,n))return;
		   
		   //���Խ���ʱ�Ĵ���
		    ch=new Point(m,n,isBlack?Color.black:Color.white);
		   chessList[chessCount++]=ch;
		    repaint();//֪ͨϵͳ���»���
		    if(isWin())
			   {
				   String msg=String.format("��ϲ��%sӮ�ˣ�", colorName);
				   JOptionPane.showMessageDialog(this, msg);
				   gameOver=true;
			   }
		    else if(chessCount==(COLS+1)*(ROWS+1))
		    {
			    String msg=String.format("����൱�Ķ���");
			    JOptionPane.showMessageDialog(this,msg);
			    gameOver=true;
		    }
		    isBlack=!isBlack;
	}
	
	//Dimension:����
	public Dimension getPreferredSize(){
		return new Dimension(MARGIN*2+GRID_SPAN*COLS,MARGIN*2+GRID_SPAN*ROWS);
	}

}
