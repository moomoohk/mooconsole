
package com.moomoohk.MooConsole.testCommands;

import com.moomoohk.MooCommands.Command;
import com.moomoohk.MooConsole.Console;

public class TestCommand extends Command<Console>
{

	public TestCommand(Console handler, String command, String help, int minParams, int maxParams)
	{
		super(handler, command, help, minParams, maxParams);
	}

	@Override
	public void execute(Console handler, String[] params)
	{
		this.message=stringParams(params, 0);
		System.out.println(this.message);
	}
}

