package com.chaboshi.scf.server.performance.commandhelper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.chaboshi.scf.server.performance.Command;
import com.chaboshi.scf.server.performance.CommandType;

public class ExecTest {

  @Test
  public void testCreateCommand() {
    Exec exec = new Exec();
    Command command1 = exec.createCommand("exec|netstat -na");
    assertEquals(CommandType.Exec, command1.getCommandType());
    assertEquals("netstat -na", command1.getCommand());

    Command command2 = exec.createCommand("exec|killall java");
    assertEquals(CommandType.Illegal, command2.getCommandType());
  }

}
