package com.lcb.goodnote;

public interface ItemTouchHelperAdapter {
    //移动item
    public void onItemMove(int fromPosition,int toPosition);
    //删除item
    public void onItemDelete(int position);

}