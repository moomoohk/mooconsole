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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
import com.moomoohk.MooCommands.CommandsManager;

/**
 * @author Meshulam Silk (moomoohk@ymail.com)
 * @version 1.0
 * @since 2013-03-08
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
	public static final String version = "2.0";
	private final ArrayList<String> log;
	private int lastCommandSelector;

	/**
	 * Constructor method.
	 */
	public Console()
	{
		initElements();
		setTitle("MooConsole v" + version);
		setMinimumSize(new Dimension(510, 250));
		getContentPane().setBackground(Color.gray.darker());
		getContentPane().setLayout(getSpringLayout());
		getContentPane().add(this.scrollPane);
		getContentPane().add(this.input);
		addText("Welcome to MooConsole v" + version + "\n", new Color(81, 148, 237));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.log = new ArrayList<String>();
		this.lastCommandSelector = -1;
		pack();
		setLocationRelativeTo(null);
		addWindowFocusListener(new WindowFocusListener()
		{
			@Override
			public void windowLostFocus(WindowEvent arg0)
			{
			}

			@Override
			public void windowGainedFocus(WindowEvent arg0)
			{
				Console.this.input.requestFocus();
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
		this.input.setFocusTraversalKeysEnabled(false);
		this.input.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent ke)
			{
				if (ke.getKeyCode() == 38)
				{
					if (Console.this.log.size() > 0)
					{
						if (Console.this.lastCommandSelector != Console.this.log.size() - 1)
							Console.this.lastCommandSelector++;
						if (Console.this.lastCommandSelector == Console.this.log.size())
							Console.this.lastCommandSelector = Console.this.log.size() - 1;
						Console.this.input.setText(Console.this.log.get(Console.this.lastCommandSelector));
					}
				}
				else
					if (ke.getKeyCode() == 40)
					{
						if (Console.this.lastCommandSelector != -1)
							Console.this.lastCommandSelector--;
						if (Console.this.lastCommandSelector == -1)
							Console.this.input.setText("");
						if (Console.this.log.size() > 0 && Console.this.lastCommandSelector >= 0)
							Console.this.input.setText(Console.this.log.get(Console.this.lastCommandSelector));
					}
					else
						Console.this.lastCommandSelector = -1;
				if (ke.getKeyCode() == 10)
				{
					if (Console.this.input.getText().trim().length() != 0 && Console.this.log.indexOf(Console.this.input.getText()) != 0)
						Console.this.log.add(0, Console.this.input.getText());
					Console.this.lastCommandSelector = -1;
					if (Console.this.input.getText().trim().length() == 0)
					{
						Console.this.input.setText("");
						return;
					}
					try
					{
						Command command = CommandsManager.findCommand(Console.this.input.getText());
						if (command == null)
						{
							addText("Command not found!\n", Color.red);
							Console.this.input.setText("");
						}
						else
						{
							String output = command.checkAndExecute(CommandsManager.parseParams(Console.this.input.getText()));
							if (output != null && output.trim() != "")
								addText(output + "\n", command.getOutputColor());
							Console.this.input.setText("");
						}
					}
					catch (NoClassDefFoundError e)
					{
						addText("Problem! Are you sure you have MooCommands installed? Get the latest version here: https://github.com/moomoohk/MooCommands/raw/master/Build/MooCommands.jar\n", Color.red);
						Console.this.input.setText("");
					}
				}
				if (ke.getKeyCode() == 27)
					Console.this.input.setText("");
			}

			@Override
			public void keyReleased(KeyEvent ke)
			{
				if (ke.getKeyCode() == 40 || ke.getKeyCode() == 38)
					Console.this.input.setCaretPosition(Console.this.input.getText().length());
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
	 * 
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
	 * Adds a String to the text area and colors the font white.
	 * 
	 * @param text
	 *            String to add.
	 */
	public void addText(String text)
	{
		addText(text, Color.white);
	}

	/**
	 * Adds a String to the text area.
	 * 
	 * @param text
	 *            String to add.
	 * @param color
	 *            of font to use.
	 */
	public void addText(String text, Color color)
	{
		if (color != null)
			StyleConstants.setForeground(this.consoleAttributeSet, color);
		else
			StyleConstants.setForeground(this.consoleAttributeSet, Color.white);
		try
		{
			final JScrollBar vbar = this.scrollPane.getVerticalScrollBar();
			boolean atBottom = vbar.getMaximum() == vbar.getValue() + vbar.getVisibleAmount();
			this.consoleDoc.insertString(this.consoleDoc.getLength(), text, this.consoleAttributeSet);
			if (atBottom)
				EventQueue.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						vbar.setValue(vbar.getMaximum());
					}
				});
		}
		catch (BadLocationException e)
		{
			addText("[ERROR]: " + e.getStackTrace().toString() + "/n", Color.red);
		}
	}

	/**
	 * Overrides the Eclipse console.
	 * <p>
	 * System.out.println and System.err.println Strings will be printed in the MooConsole instead of in the Eclipse console.
	 */
	public void setOutputOverride()
	{
		System.setOut(new OutputOverride(System.out, false));
		System.setErr(new OutputOverride(System.err, true));
	}

	private class OutputOverride extends PrintStream
	{
		private final boolean error;

		public OutputOverride(OutputStream str, boolean error)
		{
			super(str);
			this.error = error;
		}

		@Override
		public void write(byte[] b) throws IOException
		{
			write(new String(b).trim());
		}

		@Override
		public void write(byte[] buf, int off, int len)
		{
			write(new String(buf, off, len).trim());
		}

		private void write(String text)
		{
			if (!text.equals("") && !text.equals("\n"))
				if (!text.equals("") && !text.equals("\n"))
					if (this.error)
						addText(text + "\n", Color.red);
					else
						addText("[From Console (" + Thread.currentThread().getStackTrace()[10].getFileName().subSequence(0, Thread.currentThread().getStackTrace()[10].getFileName().indexOf(".java")) + ":" + Thread.currentThread().getStackTrace()[10].getLineNumber() + ")] " + text + "\n", Color.gray);
		}

		@Override
		public void write(int b)
		{
			write("" + b);
		}
	}

	public static void main(String[] args)
	{
		Console console = new Console();
		console.setVisible(true);
	}
}
