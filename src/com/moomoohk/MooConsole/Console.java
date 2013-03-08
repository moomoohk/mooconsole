package com.moomoohk.MooConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.moomoohk.MooCommands.Command;
import com.moomoohk.MooConsole.testCommands.HelpCommand;
import com.moomoohk.MooConsole.testCommands.SpamCommand;
import com.moomoohk.MooConsole.testCommands.TestCommand;

/**
 * @author      Meshulam Silk <moomoohk@ymail.com>
 * @version     1.0
 * @since       2013-03-08
 */
public class Console extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private StyledDocument consoleDoc;
	private SimpleAttributeSet consoleAttributeSet;
	private JTextField input;
	public static final String version="1.1"; 
	
	/**
	 * Constructor method.
	 */
	public Console()
	{
		initElements();
		setTitle("MooConsole v"+version);
		setMinimumSize(new Dimension(510, 250));
		setBackground(Color.gray.darker());
		getContentPane().setLayout(getSpringLayout());
		getContentPane().add(this.scrollPane);
		getContentPane().add(this.input);
		setConsoleTextColor(new Color(81, 148, 237));
		addText("Welcome to MooConsole v"+version+"\n");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		addWindowFocusListener(new WindowFocusListener()
		{
			public void windowLostFocus(WindowEvent arg0)
			{
			}
			public void windowGainedFocus(WindowEvent arg0)
			{
				input.requestFocus();
			}
		});
	}
	
	/**
	 * Initializes the various GUI elements.
	 */
	private void initElements()
	{
		JPanel nowrapPanel = new JPanel();
		nowrapPanel.setLayout(new BorderLayout(0, 0));
		this.scrollPane = new JScrollPane(nowrapPanel);
		this.scrollPane.setBackground(Color.black);
		this.scrollPane.setVerticalScrollBarPolicy(22);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		this.input = new JTextField(40);
		this.input.setBackground(Color.gray);
		this.input.setMaximumSize(new Dimension(400, 15));
		this.input.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				if (arg0.getKeyCode() == 10)
				{
					setConsoleTextColor(Color.white);
					Command<?> command = Command.getCommand(Command.parseCommand(input.getText()));
					if (command == null)
					{
						setConsoleTextColor(Color.red);
						addText("Command not found! ("+Command.parseCommand(input.getText())+")\n");
						input.setText("");
					}
					else
					{
						command.checkAndExecute(Command.parseParams(input.getText()));
						addText(command.getMessage() + "\n");
						input.setText("");
					}
				}
				if(arg0.getKeyCode()==27)
					input.setText("");
			}
		});

		JTextPane consoleTextPane = new JTextPane();
		consoleTextPane.setEditable(false);
		consoleTextPane.setBackground(Color.gray.darker().darker().darker());
		this.consoleDoc = consoleTextPane.getStyledDocument();
		this.consoleAttributeSet = new SimpleAttributeSet();
		this.consoleDoc.setParagraphAttributes(0, this.consoleDoc.getLength(), this.consoleAttributeSet, false);
		DefaultCaret caret = (DefaultCaret) consoleTextPane.getCaret();
		caret.setUpdatePolicy(1);
		consoleTextPane.setFont(new Font("Dialog", 0, 11));
		nowrapPanel.add(consoleTextPane, "Center");
	}
	
	/**
	 * Creates and returns a SpringLayout.
	 * @return A SpringLayout.
	 */
	private SpringLayout getSpringLayout()
	{
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, this.scrollPane, 10, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.EAST, this.scrollPane, -10, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.WEST, this.scrollPane, 10, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.NORTH, this.input, 5, SpringLayout.SOUTH, this.scrollPane);
		layout.putConstraint(SpringLayout.EAST, this.input, -7, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.WEST, this.input, 7, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, this.scrollPane, -40, SpringLayout.SOUTH, getContentPane());
		return layout;
	}
	
	/**
	 * Adds a String to the text area.
	 * @param text String to add.
	 */
	public void addText(String text)
	{
		try
		{
			final JScrollBar vbar = this.scrollPane.getVerticalScrollBar();
			boolean atBottom = vbar.getMaximum() == vbar.getValue() + vbar.getVisibleAmount();
			this.consoleDoc.insertString(this.consoleDoc.getLength(), text, this.consoleAttributeSet);
			if (atBottom)
			{
				EventQueue.invokeLater(new Runnable()
				{
					public void run()
					{
						vbar.setValue(vbar.getMaximum());
					}
				});
			}
		}
		catch (BadLocationException e)
		{
			setConsoleTextColor(Color.red);
			addText("[ERROR]: "+e.getStackTrace().toString()+"/n");
		}
	}
	
	/**
	 * Sets the font color of the text area.
	 * @param color The color to use.
	 */
	public void setConsoleTextColor(Color color)
	{
		StyleConstants.setForeground(this.consoleAttributeSet, color);
	}
	
	/**
	 * Receives an ArrayList of commands and adds them to the commands list.
	 * @param commands ArrayList of commands.
	 */
	public void loadCommands(ArrayList<Command<?>> commands)
	{
		for (Command<?> command : commands)
			Command.add(command);
	}
	
	/**
	 * A little demo I made with 3 basic commands.
	 */
	public static void runDemoTest()
	{
		Console console = new Console();
		TestCommand test = new TestCommand(console, "test", "Test command (will print out its parameters).", 0, -1);
		HelpCommand help=new HelpCommand(console, "help", "Shows help.", 0, 1);
		SpamCommand spam=new SpamCommand(console, "spam", "Spams the console.", 0, 0);
		ArrayList<Command<?>> commands = new ArrayList<Command<?>>();
		commands.add(test);
		commands.add(help);
		commands.add(spam);
		console.loadCommands(commands);
	}
	public static void main(String[] args)
	{
		runDemoTest();
	}
}
