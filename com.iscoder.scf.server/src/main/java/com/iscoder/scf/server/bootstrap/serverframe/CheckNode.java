package com.iscoder.scf.server.bootstrap.serverframe;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckNode extends DefaultMutableTreeNode {

  private static final long serialVersionUID = 1395199707680929698L;
  /**
   * 是否选择
   */
  protected boolean isSelected;

  public CheckNode() {
    this(null);
  }

  public CheckNode(Object userObject) {
    this(userObject, true, false);
  }

  public CheckNode(Object userObject, boolean allowsChildren, boolean isSelected) {
    super(userObject, allowsChildren);
    this.isSelected = isSelected;
  }

  /**
   * 设置子目录全选/取消
   * 
   * @param isSelected
   */
  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
    if ((children != null)) {
      Enumeration<?> enumOne = children.elements();
      while (enumOne.hasMoreElements()) {
        CheckNode node = (CheckNode) enumOne.nextElement();
        node.setSelected(isSelected);
      }
    }
  }

  /**
   * 判断当前节点是否已选择
   * 
   * @return 是否选择
   */
  public boolean isSelected() {
    return isSelected;
  }
}
