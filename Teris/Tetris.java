import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.*;

public class Tetris extends JApplet implements KeyListener
{
	JButton bhu,bhd,bvu,bvd,bstart,bpause;
	JPanel p0,p1,p2,pf;
	JTextField height,speed,score,hiscore;
	Mypanel myp;

	private Image im,im2;

	public void init()
	{
		Container container=getContentPane();
		container.setLayout( new BorderLayout() );

		bhu=new JButton("增加");
		bhd=new JButton("减少");
		bvu=new JButton("增加");
		bvd=new JButton("减少");
		bstart=new JButton("开始");
		bpause=new JButton("暂停");
		bpause.setEnabled(false);
		height=new JTextField();
		speed=new JTextField();
		score=new JTextField();
		hiscore=new JTextField();
		height.setEditable(false);
		speed.setEditable(false);
		score.setEditable(false);
		hiscore.setEditable(false);
		height.setText("高度:18");
		speed.setText("速度:0");
		score.setText("得分:");
		hiscore.setText("最高分:0");

		p0=new JPanel();
		p0.setLayout(new GridLayout(1,2));
		p0.add(bhu);
		p0.add(bhd);

		p1=new JPanel();
		p1.setLayout(new GridLayout(1,2));
		p1.add(bvu);
		p1.add(bvd);

		p2=new JPanel();
		p2.setLayout(new GridLayout(1,2));
		p2.add(bstart);
		p2.add(bpause);

		pf=new JPanel();
		pf.setLayout(new GridLayout(7,1));
		pf.add(hiscore);
		pf.add(height);
		pf.add(p0);
		pf.add(speed);
		pf.add(p1);
		pf.add(score);
		pf.add(p2);

		im=getImage(getCodeBase(),"block.gif");//
		im2=getImage(getCodeBase(),"block2.gif");//
		myp=new Mypanel(im,im2,this);

		container.add(pf,BorderLayout.WEST);//

		container.add(myp);
		ActionHandler actionhandler=new ActionHandler();
		bstart.addActionListener(actionhandler);
		bpause.addActionListener(actionhandler);
		bhu.addActionListener(actionhandler);
		bhd.addActionListener(actionhandler);
		bvu.addActionListener(actionhandler);
		bvd.addActionListener(actionhandler);
		addKeyListener(this);
		setFocusable(true);
	}
	public void keyPressed(KeyEvent event)
	{
		int keyCode=event.getKeyCode();
		myp.OnkeyPressed(keyCode);
	}
	public void keyReleased(KeyEvent event)
	{
		int keyCode=event.getKeyCode();
		myp.OnkeyReleased(keyCode);
	}
	public void keyTyped(KeyEvent event)
	{
		char keyChar=event.getKeyChar();
		myp.OnkeyTyped(keyChar);
	}
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object src=event.getSource();

			if(src==bstart)
			{
				myp.setStart();
				bhu.setEnabled(false);
				bhd.setEnabled(false);
				bvu.setEnabled(false);
				bvd.setEnabled(false);
				bstart.setEnabled(false);
				bpause.setEnabled(true);
			}
			else if(src==bpause)
			{
				myp.setPause();
				bvu.setEnabled(true);
				bvd.setEnabled(true);
				bpause.setEnabled(false);
				bstart.setEnabled(true);
			}
			else if(src==bhu)
				myp.Resize(true);
			else if(src==bhd)
				myp.Resize(false);
			else if(src==bvu)
				myp.SetSpeed(true);
			else if(src==bvd)
				myp.SetSpeed(false);
		}
	}
}

class Mypanel extends JPanel
{
	private Tetris Eframe;
	private boolean w_pressed;
	public Mypanel(Image im,Image im2,Tetris eframe)
	{
		Eframe=eframe;
		img=im;
		img2=im2;
		setBackground(Color.WHITE);
		w_pressed=false;
		imgmat=new Imgmatrix();
		state=0;hiscore=0;speed=0;width=10;height=18;score=0;

		unit=50;
		dbl_v=1;
		velo=20;
		Tcount=0;
		timer=new Timer(20,new ActionHandler());
		timer.start();
	}
	public void paint(Graphics g)
	{
		int i,j;
		super.paint(g);
		setSize(10*width,10*height);
		g.drawRect(0,0,10*width,10*height);
		for(j=0;j<height;j++)
			for(i=0;i<width;i++)
			{
				if(imgmat.bitarr[j][i])
					g.drawImage(img,i*10,j*10,this);
			 	else
			 		g.drawImage(img2,i*10,j*10,this);
			}
	}

	public void OnkeyPressed(int keyCode)
	{
		if(state!=1)
			return;
		if(keyCode==KeyEvent.VK_W&&!w_pressed)
		{
			imgmat.Chgdir();
			w_pressed=true;
		}
		else if(keyCode==KeyEvent.VK_S)
			dbl_v=10;
	}
	public void OnkeyReleased(int keyCode)
	{
		if(state!=1)
			return;
		if(keyCode==KeyEvent.VK_W)
			w_pressed=false;
		else if(keyCode==KeyEvent.VK_S)
			dbl_v=1;
	}
	public void OnkeyTyped(char keyChar)
	{
		if(state!=1)
			return;

		if(keyChar=='a')
			imgmat.Move(1);
		else if(keyChar=='d')
			imgmat.Move(2);

		repaint();
	}

	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(state!=1)
				return;
			Tcount++;
			if(Tcount>=velo/dbl_v)
			{
				Tcount=0;
				if(!imgmat.Move(3))
				{
					state=0;
					Eframe.bpause.setEnabled(false);
					Eframe.bstart.setEnabled(true);
					Eframe.bhu.setEnabled(true);
					Eframe.bhd.setEnabled(true);
					Eframe.bvu.setEnabled(true);
					Eframe.bvd.setEnabled(true);
					if(score>hiscore)
					{
						hiscore=score;
						Eframe.hiscore.setText("最高分:"+hiscore);
					}
				}

				repaint();
				score+=imgmat.tick*unit;
				imgmat.tick=0;
				Eframe.score.setText("得分:"+score);
			}
		}
	}
	public void setStart()
	{
		if(state==1)
			return;
		else if(state==0)
		{
			imgmat.start();
			state=1;
			score=0;
			Eframe.score.setText("得分:0");
		}
		else if(state==2)
			state=1;
		dbl_v=1;
	}
	public void setPause()
	{
		if(state==1)
			state=2;
	}
	void Resize(boolean bExpd)
	{
		if(bExpd)
		{
			height++;
			if(height>25)
				height=25;
		}
		else
		{
			height--;
			if(height<5)
				height=5;
		}
		imgmat.SetDimension(width,height);
		unit=50+10*speed+5*(18-height);
		repaint();
		Eframe.height.setText("高度:"+height);
	}
	void SetSpeed(boolean bInc)
	{
		if(bInc)
		{
			speed++;
			if(speed>9)
				speed=9;
		}
		else
		{
			speed--;
			if(speed<0)
				speed=0;
		}
		velo=20-speed;
		unit=50+10*speed+5*(18-height);
		Eframe.speed.setText("速度:"+speed);
	}
	public int GetState()
	{
		return state;
	}
	private Image img;
	private Image img2;
	private int state,hiscore,speed,width,height,score;
	private int unit;
	private Imgmatrix imgmat;
	private int dbl_v;
	private int velo;
	private Timer timer;
	private int Tcount;
}

class Imgmatrix
{
	public Imgmatrix()
	{
		SetDimension(10,18);
		blkdata=new int[7][][][];
		blkdata[0]=new int[][][]{{{-1,-1},{-1,0},{0,-1},{0,0}},{{-1,-1},{-1,0},{0,-1},{0,0}},{{-1,-1},{-1,0},{0,-1},{0,0}},{{-1,-1},{-1,0},{0,-1},{0,0}}};//tian
		blkdata[1]=new int[][][]{{{-1,-2},{-1,-1},{0,-1},{0,0}},{{-1,0},{0,0},{0,-1},{1,-1}},{{-1,-2},{-1,-1},{0,-1},{0,0}},{{-1,0},{0,0},{0,-1},{1,-1}}};//s
		blkdata[2]=new int[][][]{{{0,-2},{0,-1},{-1,-1},{-1,0}},{{-1,-1},{0,-1},{0,0},{1,0}},{{0,-2},{0,-1},{-1,-1},{-1,0}},{{-1,-1},{0,-1},{0,0},{1,0}}};//z
		blkdata[3]=new int[][][]{{{-1,-2},{-1,-1},{-1,0},{0,0}},{{-1,0},{-1,-1},{0,-1},{1,-1}},{{-1,-2},{0,-2},{0,-1},{0,0}},{{-1,0},{0,0},{1,0},{1,-1}}};//L
		blkdata[4]=new int[][][]{{{-1,0},{0,0},{0,-1},{0,-2}},{{-1,-1},{-1,0},{0,0},{1,0}},{{0,-2},{-1,-2},{-1,-1},{-1,0}},{{-1,-1},{0,-1},{1,-1},{1,0}}};//J
		blkdata[5]=new int[][][]{{{-1,-1},{0,-1},{0,0},{1,-1}},{{-1,-1},{0,-2},{0,-1},{0,0}},{{-1,0},{0,-1},{0,0},{1,0}},{{0,-2},{0,-1},{0,0},{1,-1}}};//T
		blkdata[6]=new int[][][]{{{0,-3},{0,-2},{0,-1},{0,0}},{{-1,0},{0,0},{1,0},{2,0}},{{0,-3},{0,-2},{0,-1},{0,0}},{{-1,0},{0,0},{1,0},{2,0}}};//I
		blkshft=new int[7][][];
		blkshft[0]=new int[][]{{0},{0},{0},{0}};
		blkshft[1]=new int[][]{{0},{0,1},{0},{0,1}};
		blkshft[2]=new int[][]{{0,1},{0,-1,-2},{0,1},{0,-1,-2}};
		blkshft[3]=new int[][]{{0},{0},{0,-1},{0}};
		blkshft[4]=new int[][]{{0,1,2},{0,-1},{0},{0,-1,-2}};
		blkshft[5]=new int[][]{{0,1},{0},{0,-1,1},{0}};
		blkshft[6]=new int[][]{{0},{0,-1,1,-2},{0},{0,-1,1,-2}};
		blkyrange=new int[7][][];
		blkyrange[0]=new int[][]{{-1,0},{-1,0},{-1,0},{-1,0}};
		blkyrange[1]=new int[][]{{-2,-1,0},{-1,0},{-2,-1,0},{-1,0}};
		blkyrange[2]=new int[][]{{-2,-1,0},{-1,0},{-2,-1,0},{-1,0}};
		blkyrange[3]=new int[][]{{-2,-1,0},{-1,0},{-2,-1,0},{-1,0}};
		blkyrange[4]=new int[][]{{-2,-1,0},{-1,0},{-2,-1,0},{-1,0}};
		blkyrange[5]=new int[][]{{-1,0},{-2,-1,0},{-1,0},{-2,-1,0}};
		blkyrange[6]=new int[][]{{-3,-2,-1,0},{0},{-3,-2,-1,0},{0}};
	}
	public void Chgdir()
	{
		int i;
		int dir=(cdir+1)%4;
		for(i=0;i<blkshft[btype][dir].length;i++)
		{
			if(Matched(btype,cx+blkshft[btype][dir][i],cy,dir))
			{
				cdir=dir;
				cx+=blkshft[btype][dir][i];
				break;
			}
		}
	}
	public boolean Move(int lorod)
	{
		int tempx=cx,tempy=cy,i;
		if(lorod==1)
			tempx--;
		if(lorod==2)
			tempx++;
		if(lorod==3)
			tempy++;
		if(Matched(btype,tempx,tempy,cdir))
		{
			cx=tempx,cy=tempy;
			newput=false;
			return true;
		}
		else if(lorod==3)
		{
			if(newput)
				return false;
			Begindelete();
			for(i=0;i<blkyrange[btype][cdir].length;i++)
			{
				if(Deleteln(cy+blkyrange[btype][cdir][i]))
					tick++;
			}
			Enddelete();
			DropNew();
			return true;
		}
		else
		{
			newput=false;
			return true;
		}
	}
	public boolean Matched(int blktype,int baseptx,int basepty,int direction)
	{
		int i,j;
		int tempx,tempy;
		for(i=0;i<blkdata[blktype][direction].length;i++)
		{
			tempx=baseptx+blkdata[blktype][direction][i][0];
			tempy=basepty+blkdata[blktype][direction][i][1];
			if(tempx<0||tempx>=pwidth||tempy<0||tempy>=pheight||basearr[tempy][tempx])
				break;
		}
		if(i!=blkdata[blktype][direction].length)
			return false;
		else
		{
			refresh();
			for(i=0;i<blkdata[blktype][direction].length;i++)
			{
				tempx=baseptx+blkdata[blktype][direction][i][0];
				tempy=basepty+blkdata[blktype][direction][i][1];
				bitarr[tempy][tempx]=true;
			}
			return true;
		}
	}
	public void Begindelete()
	{
		int i,tempx,tempy;

		for(i=0;i<blkdata[btype][cdir].length;i++)
		{
			tempx=cx+blkdata[btype][cdir][i][0];
			tempy=cy+blkdata[btype][cdir][i][1];
			basearr[tempy][tempx]=true;
		}
	}
	public void Enddelete()
	{
		refresh();
	}
	public boolean Deleteln(int nline)//this method should be called by an ascending order of nline
	{
		int i;
		for(i=0;i<pwidth;i++)
		{
			if(!basearr[nline][i])break;
		}
		if(i<pwidth)
			return false;
		else
		{
			boolean temp[];
			temp=basearr[nline];
			for(i=nline;i>0;i--)
			{
				basearr[i]=basearr[i-1];
			}
			basearr[0]=temp;
			for(i=0;i<pwidth;i++)
			{
				basearr[0][i]=false;
			}
			return true;
		}
	}
	public void SetDimension(int width,int height)
	{
		pwidth=width;
		pheight=height;
		bitarr=new boolean[height][width];
		basearr=new boolean[height][width];
		tick=0;newput=false;
	}
	public void start()
	{
		SetDimension(pwidth,pheight);
		DropNew();
	}
	public boolean bitarr[][];
	private boolean basearr[][];
	public int tick;
	private int blkdata[][][][];//type,dir,blk,xy
	private int blkshft[][][];
	private int blkyrange[][][];
	private int cx,cy,pwidth,pheight;
	private int cdir,btype;
	private boolean newput;
	private void refresh()
	{
		int i;
		for(i=0;i<pheight;i++)
		{
			System.arraycopy(basearr[i],0,bitarr[i],0,pwidth);
		}
	}
	private int bGenerate()
	{
		return (int)(Math.random()*7);
	}
	private void DropNew()
	{
		cx=5,cy=3,cdir=0;
		btype=bGenerate();
		newput=true;
	}
}