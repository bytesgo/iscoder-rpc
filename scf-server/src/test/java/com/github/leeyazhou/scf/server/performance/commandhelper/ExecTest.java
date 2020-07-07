package com.github.leeyazhou.scf.server.performance.commandhelper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.leeyazhou.scf.server.performance.Command;
import com.github.leeyazhou.scf.server.performance.CommandType;
import com.github.leeyazhou.scf.server.performance.commandhelper.Exec;

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
