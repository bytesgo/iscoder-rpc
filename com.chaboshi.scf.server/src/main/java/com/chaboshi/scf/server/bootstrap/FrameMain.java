package com.chaboshi.scf.server.bootstrap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.chaboshi.scf.server.bootstrap.serverframe.ButtonFrame;
import com.chaboshi.scf.server.bootstrap.serverframe.MenuBar;
import com.chaboshi.scf.server.bootstrap.serverframe.TreeFrame;
import com.chaboshi.scf.server.contract.log.ILog;
import com.chaboshi.scf.server.contract.log.LogFactory;

/**
 * main window
 * 
 * @author haoxb
 */
public class FrameMain extends JFrame {
  private static final long serialVersionUID = 563726381249120291L;
  private static ILog logger = null;

  public FrameMain(String title) {
    super(title);
  }

  public static void main(String[] args) {
    FrameMain frameMain = new FrameMain("授权文件生成器");
    frameMain.setSize(450, 550);
    frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /** load log4j */
    logger = LogFactory.getLogger(FrameMain.class);
    /** 获得显示器大小对象 */
    Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
    /** 获得窗口大小对象 */
    Dimension frameSize = frameMain.getSize();
    if (frameSize.width > displaySize.width) {
      frameSize.width = displaySize.width;
    }
    if (frameSize.height > displaySize.height) {
      frameSize.height = displaySize.height;
    }
    /** 设置窗口居中显示器显示 */
    frameMain.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);
    /** Build main MenuBar */
    logger.info("-----------------build Menu Start------------------");
    frameMain.setJMenuBar(new MenuBar().bulidMenuBar(frameMain));
    logger.info("-----------------build Menu End--------------------");
    frameMain.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frameMain.setVisible(true);
  }

  public void createGUI() {
    /** tree button */
    JPanel topPanel = new JPanel(new BorderLayout());
    JTextArea keyjta = new JTextArea(10, 10);

    topPanel.add(new JScrollPane(TreeFrame.create().buildTree()), BorderLayout.CENTER);
    topPanel.add(new ButtonFrame(keyjta), BorderLayout.SOUTH);
    topPanel.setName("top");

    /** key生成器 */
    JPanel downPanel = new JPanel(new BorderLayout());
    downPanel.add(new JScrollPane(keyjta), BorderLayout.CENTER);
    downPanel.setName("down");

    this.getContentPane().add(topPanel, BorderLayout.CENTER);
    this.getContentPane().add(downPanel, BorderLayout.SOUTH);
    this.getContentPane().validate();
  }
}
