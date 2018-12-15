package com.lcb.goodnote;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by q6412 on 2018/12/6.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>
        implements  ItemTouchHelperAdapter{
    private Context mContext;
    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView notename;
        TextView notedate;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            notename = (TextView) view.findViewById(R.id.note_item_tv_title);//利用列表id作为title
            notedate = (TextView) view.findViewById(R.id.note_item_tv_time);
        }
    }
    public NoteAdapter(List<Note> noteList){
        mNoteList = noteList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item,
                parent, false);
        //添加主面板中的事件监听
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Note note = mNoteList.get(position);
                Intent intent = new Intent(mContext,NoteActivity.class);
                intent.putExtra(NoteActivity.NOTE_NAME,note.getName());
                intent.putExtra(NoteActivity.NOTE_IMAGE_ID,note.getNodeId());
                intent.putExtra(NoteActivity.ACTIVITY_ID,note.getId());
                intent.putExtra(NoteActivity.NOTE_CONTENT,note.getActivity_content());
                intent.putExtra(NoteActivity.NOTE_THEME,note.getActivity_theme());
                intent.putExtra(NoteActivity.NOTE_ADDRESS,note.getActivity_address());
                intent.putExtra(NoteActivity.NOTE_YEAR,note.getActivity_year());
                intent.putExtra(NoteActivity.NOTE_MONTH,note.getActivity_month());
                intent.putExtra(NoteActivity.NOTE_DAY,note.getActivity_day());

                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    public void onBindViewHolder(ViewHolder holder, int position){
        Note note = mNoteList.get(position);
        holder.notename.setText(note.getName());
        holder.notedate.setText(note.getDate());
    }


    public int getItemCount(){
        return mNoteList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(mNoteList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDelete(int position) {
        //移除数据
        mNoteList.remove(position);
        notifyItemRemoved(position);
    }
}
