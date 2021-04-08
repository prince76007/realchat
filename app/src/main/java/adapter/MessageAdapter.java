package adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realchat.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final List<String> textMessage;
    private final List<Boolean> byMeOrNot;
    private final Context context;
    public MessageAdapter(ArrayList<String> textMessage, ArrayList<Boolean> byMeOrNot, Context context) {
    this.textMessage=textMessage;
    this.byMeOrNot=byMeOrNot;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view= layoutInflater.inflate(R.layout.message_layout,null);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (byMeOrNot.get(position)){
            holder.message.setGravity(Gravity.END);
        }else{
            holder.message.setGravity(Gravity.START);
        }
        holder.message.setText(textMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return textMessage.size();
    }

   static class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView message;
       public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
           message=itemView.findViewById(R.id.messageText);
        }
        //public TextView
    }
}
