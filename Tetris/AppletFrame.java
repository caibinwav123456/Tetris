import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import javax.imageio.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class AppletFrame extends JFrame
	implements AppletStub,AppletContext
{
	public AppletFrame(Applet anApplet)
	{
		tick=0;
		applet=anApplet;
		getContentPane().add(applet);
		applet.setStub(this);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			applet.init();
			super.setVisible(true);
			applet.start();
		}
		else
		{
			applet.stop();
			super.setVisible(false);
			applet.destroy();
		}
	}
	//AppletStub methods
	public boolean isActive()
	{
		return true;
	}
	public URL getDocumentBase()
	{
		return null;
	}
	public URL getCodeBase()
	{
		try
		{
			return new URL("file:///"+(System.getProperty("user.dir")).replace('\\','/')+"/");
		}
		catch(MalformedURLException e)
		{
			return null;
		}
	}
	public String getParameter(String name)
	{
		return "";
	}
	public AppletContext getAppletContext()
	{
		return this;
	}
	public void appletResize(int width,int height)
	{
	}

	//AppletContext methods
	public AudioClip getAudioClip(URL url)
	{
		return null;
	}
	public Image getImage(URL url)
	{
		try
		{
			return ImageIO.read(url);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public Applet getApplet(String name)
	{
		return applet;
	}
	public Enumeration getApplets()
	{
		return null;
	}
	public void showDocument(URL url)
	{
	}
	public void showDocument(URL url,String target)
	{
	}
	public void showStatus(String status)
	{
		//applet.showStatus(status);
	}
	public void setStream(String key,InputStream stream)
	{
	}
	public InputStream getStream(String key)
	{
		return null;
	}
	public Iterator getStreamKeys()
	{
		return null;
	}

	private Applet applet;
	private int tick;
}