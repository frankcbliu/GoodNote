package com.lcb.goodnote;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by q6412 on 2018/12/6.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private Context mContext;
    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView notename;
        TextView notedate;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            notename = (TextView) view.findViewById(R.id.note_title);//利用列表id作为title
            notedate = (TextView) view.findViewById(R.id.note_time);
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
                intent.putExtra(NoteActivity.NOTE_NAME, note.getName());
                intent.putExtra(NoteActivity.NOTE_IMAGE_ID,note.getNodeId());
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
}
