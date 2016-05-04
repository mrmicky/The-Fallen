package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Gui extends JFrame {

	public JFrame jframe = new JFrame();
	public JPanel jpanel = new JPanel();
	public JPanel jpanel2 = new JPanel();

	public JTextArea jta = new JTextArea();
	public JTextArea jta2 = new JTextArea();
	public JTextArea jta3 = new JTextArea();
	public JTextField jtf = new JTextField();

	public JMenuBar menuBar = new JMenuBar();
	public JMenu menuFile = new JMenu("File");
	public JMenu menuHelp = new JMenu("Help");

	public JMenuItem menuSave = new JMenuItem("Save Profile", KeyEvent.VK_T);
	public JMenuItem menuLoad = new JMenuItem("Load Profile", KeyEvent.VK_T);
	public JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_T);
	public JMenuItem menuAbout = new JMenuItem("About", KeyEvent.VK_T);

	// Resources folder created (discovered better way to load images into runnable JAR
	String playerPath = "resources/player.png";
	// Makes new ImageIcon and passes the .png into it using some fancy loading
	ImageIcon imagePlayer = new ImageIcon(getClass().getClassLoader().getResource(playerPath));
	Image image = imagePlayer.getImage();
	Image newimg = image.getScaledInstance(202, 214,  Image.SCALE_SMOOTH);
	ImageIcon imageIcon = new ImageIcon(newimg); 	
	// Add Image to JLabel
	public JLabel playerLabel = new JLabel(imageIcon);
	String npcPath = "resources/npc1.png";
	ImageIcon imageNpc = new ImageIcon(getClass().getClassLoader().getResource(npcPath));	
	Image image2 = imageNpc.getImage();
	Image newimg2 = image2.getScaledInstance(202, 214,  Image.SCALE_SMOOTH);
	ImageIcon imageIcon2 = new ImageIcon(newimg2); 	
	public JLabel npcLabel = new JLabel(imageIcon2);

	public Gui() throws IOException {
		jframe.setLayout(new BorderLayout());
		jpanel.setLayout(new BorderLayout());
		jpanel2.setLayout(new GridLayout(4, 1));
		jta.setLayout(new GridLayout(2,1));
		
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);
		jframe.setAlwaysOnTop(false);

		// Gets screen size
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xs = (int) (tk.getScreenSize().getWidth() * (85.0f / 100.0f));
		int ys = (int) (tk.getScreenSize().getHeight() * (85.0f / 100.0f));

		// Sets size of JFame based on what size TK received
		jframe.setSize(xs, ys);
		
		jframe.add(jpanel);
		jframe.add(jpanel2, BorderLayout.EAST);
		jpanel.add(jta);
		jpanel.add(jtf, BorderLayout.SOUTH);

		playerLabel.setPreferredSize(new Dimension(25, 25));
		npcLabel.setPreferredSize(new Dimension(25, 25));

		jpanel2.add(jta2);
		jpanel2.add(playerLabel);
		playerLabel.setVisible(false);
		jpanel2.add(jta3);
		jpanel2.add(npcLabel);
		npcLabel.setVisible(false);
		
		jta2.setPreferredSize(new Dimension(xs / 8, ys / 2));
		jta3.setPreferredSize(new Dimension(xs / 8, ys / 2));
		jtf.setPreferredSize(new Dimension((int) (xs), ys / 12));
        jta.setEditable(false);
        jta2.setEditable(false);
        jta3.setEditable(false);

		jta.setForeground(Color.LIGHT_GRAY);
		jtf.setForeground(Color.LIGHT_GRAY);
		jta.setBackground(Color.DARK_GRAY);
		jtf.setBackground(Color.DARK_GRAY);
		jta2.setBackground(Color.DARK_GRAY);
		jta2.setForeground(Color.LIGHT_GRAY);
		jta3.setBackground(Color.DARK_GRAY);
		jta3.setForeground(Color.LIGHT_GRAY);
		jpanel2.setBackground(Color.DARK_GRAY);

		Font font = jta.getFont();
		jta.setFont(new Font("Calibri", Font.PLAIN, 18));
		jtf.setFont(new Font("Calibri", Font.PLAIN, 18));
		jta2.setFont(new Font("Calibri", Font.PLAIN, 14));
		jta3.setFont(new Font("Calibri", Font.PLAIN, 14));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		
		Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
		jta2.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		jtf.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		jta2.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		jta3.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY));
		jpanel2.setBorder(border);
	
		// Adds "items" such as File / Help to menuBar
		menuFile.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menuFile);
		menuHelp.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menuHelp);
		menuFile.add(menuSave);
		menuFile.add(menuLoad);
		menuFile.add(menuExit);
		menuHelp.add(menuAbout);

		// Adds menuBar to the jframe
		jframe.getContentPane().add(menuBar, BorderLayout.PAGE_START);	    
	    
	}
	
	/**
	 * ALL METHODS RELATED TO GUI RETURN THE OBJECTS THEY'RE.
	 * @return
	 */
	public JMenu getMenuHelp() {
		return menuHelp;
	}

	public JMenuItem getMenuSave() {
		return menuSave;
	}

	public JMenuItem getMenuLoad() {
		return menuLoad;
	}

	public JMenuItem getMenuExit() {
		return menuExit;
	}

	public JMenuItem getMenuAbout() {
		return menuAbout;
	}

	
	public String getNpcPath() {
		return npcPath;
	}
	
	public JLabel getNpcLabel() {
		return npcLabel;
	}

	public JLabel getPlayerLabel() {
		return playerLabel;
	}
	
	public void setPlayerPath(String playerPath) {
		this.playerPath = playerPath;
	}
	
	public void setNpcPath(String npcPath) {
		this.npcPath = npcPath;
	}
	
	public static void main(String arg[]) throws IOException {
		Gui UI = new Gui();
	}
	
	
}