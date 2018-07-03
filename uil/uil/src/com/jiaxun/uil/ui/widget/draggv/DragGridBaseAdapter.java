package com.jiaxun.uil.ui.widget.draggv;


public interface DragGridBaseAdapter {
	/**
	 * ������������
	 * @param oldPosition
	 * @param newPosition
	 */
	public void reorderItems(int oldPosition, int newPosition);
	
	
	/**
	 * ����ĳ��item����
	 * @param hidePosition
	 */
	public void setHideItem(int hidePosition);
	
	/**
	 * ɾ��ĳ��item
	 * @param removePosition
	 */
	public void removeItem(int removePosition);
	
	/**
	 * ����˵�� :
	 * @param position
	 * @author HeZhen
	 * @Date 2015-6-10
	 */
	public boolean isCanMove(int position);
	
	/**
	 * ����˵�� :����
	 * @author HeZhen
	 * @Date 2015-7-24
	 */
	public void savePos();
}
