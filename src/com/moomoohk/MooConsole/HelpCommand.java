package com.moomoohk.MooConsole;

import java.awt.Color;

import com.moomoohk.MooCommands.Command;
import com.moomoohk.MooCommands.CommandsManager;

public class HelpCommand extends Command
{

	public HelpCommand()
	{
		super();
	}

	public boolean check(String[] params)
	{
		if (params.length > 0 && CommandsManager.findCommand(params[0]) == null)
		{
			this.outputMessage = "Command not found!";
			this.outputColor = Color.red;
			return false;
		}
		return true;
	}

	@Override
	public void execute(String[] params)
	{
		if (params.length == 0)
		{
			this.outputMessage = "---";
			for (Command c : CommandsManager.getAllCommands())
				this.outputMessage += "\n" + c.toString() + "\n---";
			return;
		}
		this.outputMessage = CommandsManager.findCommand(params[0]).toString();
	}

	@Override
	public String getCommand()
	{
		return "help";
	}

	@Override
	public String getHelpMessage()
	{
		return "Shows help";
	}

	@Override
	public String getUsage()
	{
		return "help [command]";
	}

	@Override
	public int getMaxParams()
	{
		return 1;
	}

	@Override
	public int getMinParams()
	{
		return 0;
	}
}