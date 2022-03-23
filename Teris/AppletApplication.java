import javax.swing.*;

public class AppletApplication extends Tetris
{
	public static void main(String[] args)
	{
		AppletFrame frame = new AppletFrame(new Tetris());
		frame.setTitle("¶íÂÞË¹·½¿é");
		frame.setSize(250,250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}