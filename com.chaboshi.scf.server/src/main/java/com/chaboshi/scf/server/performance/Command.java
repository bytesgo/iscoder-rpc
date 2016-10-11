package com.chaboshi.scf.server.performance;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.chaboshi.scf.server.contract.context.SCFContext;
import com.chaboshi.scf.server.performance.commandhelper.CRLF;
import com.chaboshi.scf.server.performance.commandhelper.Clear;
import com.chaboshi.scf.server.performance.commandhelper.Control;
import com.chaboshi.scf.server.performance.commandhelper.Count;
import com.chaboshi.scf.server.performance.commandhelper.Exec;
import com.chaboshi.scf.server.performance.commandhelper.Help;
import com.chaboshi.scf.server.performance.commandhelper.ICommandHelper;
import com.chaboshi.scf.server.performance.commandhelper.Illegal;
import com.chaboshi.scf.server.performance.commandhelper.JVM;
import com.chaboshi.scf.server.performance.commandhelper.Quit;
import com.chaboshi.scf.server.performance.commandhelper.Time;

/**
 * 
 * count[|second num|method methodName] * show method call times in num seconds * second : in num seconds statistics
 * once (num default 1) * method : for statistics method * example : count * example : count|second 3 * example :
 * count|second 3|method getInfo
 * 
 * time|grep abc[|group num|column -tkda] * show method execute time * grep : condition * group : method called num
 * times show statistics once * column : show column a->all t->time k->key d->description * example: time|grep getInfo *
 * example: time|grep getInfo|group 10|column -tk
 * 
 * exec|top |netstat -na * exec command (at present only allow:top or netstat) * example: exec|top
 * 
 * jvm [-gcutil time count|-class time count] *-gcutil :detection heap memory usage *-class :load class *time :time
 * milliseconds apart test again *count :detection count times *example :jvm -gcutil *example :jvm -gcutil 1000 *example
 * :jvm -gcutil 1000 5 *example :jvm -class *example :jvm -class 1000 *example :jvm -class 1000 5
 * 
 * control * use for control scf-server
 * 
 * clear * stop command
 * 
 * help * show help
 * 
 * quit * quit monitor
 * 
 * 
 */
public class Command {

  private CommandType commandType;

  private String command;

  private List<String> grep;

  private int group;

  private List<ShowColumn> columnList;

  private int second;

  private String method;

  /**
   * 
   */
  private static List<ICommandHelper> helperList = new ArrayList<ICommandHelper>();

  static {
    helperList.add(new CRLF());
    helperList.add(new Quit());
    helperList.add(new Count());
    helperList.add(new Exec());
    helperList.add(new Time());
    helperList.add(new JVM());
    helperList.add(new Clear());
    helperList.add(new Help());
    helperList.add(new Control());
    helperList.add(new Illegal());

  }

  /**
   * 
   * @param command
   * @return
   */
  public static Command create(String command) {
    Command entity = null;
    command = command.trim();
    for (ICommandHelper cc : helperList) {
      entity = cc.createCommand(command);
      if (entity != null) {
        break;
      }
    }
    if (entity == null) {
      entity = new Command();
      entity.setCommandType(CommandType.Illegal);
    }
    return entity;
  }

  /**
   * exec command
   * 
   * @return
   * @throws Exception
   */
  public void exec(MessageEvent event) throws Exception {
    for (ICommandHelper cc : helperList) {
      cc.execCommand(this, event);
    }
  }

  /**
   * 
   * @param channel
   */
  public void removeChannel(Channel channel) {
    for (ICommandHelper cc : helperList) {
      cc.removeChannel(this, channel);
    }
  }

  /**
   * 
   * @param context
   */
  public void messageReceived(SCFContext context) {
    for (ICommandHelper cc : helperList) {
      cc.messageReceived(context);
    }
  }

  /**
   * get channel count
   * 
   * @return
   */
  public int getChannelCount() {
    int count = 0;
    for (ICommandHelper cc : helperList) {
      count += cc.getChannelCount();
    }
    return count;
  }

  public CommandType getCommandType() {
    return commandType;
  }

  public void setCommandType(CommandType commandType) {
    this.commandType = commandType;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public List<ShowColumn> getColumnList() {
    return columnList;
  }

  public void setColumnList(List<ShowColumn> columnList) {
    this.columnList = columnList;
  }

  public List<String> getGrep() {
    return grep;
  }

  public void setGrep(List<String> grep) {
    this.grep = grep;
  }

  public void setGroup(int group) {
    this.group = group;
  }

  public int getGroup() {
    return group;
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}