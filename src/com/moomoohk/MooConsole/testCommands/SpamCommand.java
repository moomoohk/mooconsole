
package com.moomoohk.MooConsole.testCommands;

import java.util.Random;

import com.moomoohk.MooCommands.Command;
import com.moomoohk.MooConsole.Console;

public class SpamCommand extends Command<Console>
{

	public SpamCommand(Console handler, String command, String help, int minParams, int maxParams)
	{
		super(handler, command, help, minParams, maxParams);
	}

	@Override
	public void execute(Console handler, String[] params)
	{
		this.message="";
		for(int i=0; i<20; i++)
		{
			int length=new Random().nextInt(20)+5;
			for(int j=1; j<=length; j++)
				this.message+=new Random().nextInt(10);
			this.message+="\n";
		}
	}

}

