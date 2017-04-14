package com.estg.joaoviana.project_cmovel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estg.joaoviana.project_cmovel.Messages.Message;
import com.estg.joaoviana.project_cmovel.R;

import java.util.ArrayList;

/**
 * Created by PJ on 12/04/2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<Message>{
    public CustomArrayAdapter(Context context, ArrayList<Message> mensagens) {
        super(context,0,mensagens);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Message m = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_linha,parent,false);
        }
        ((TextView) convertView.findViewById(R.id.author)).setText(m.getName_other());
        ((TextView) convertView.findViewById(R.id.title)).setText(m.getTitle());
        ((TextView) convertView.findViewById(R.id.body)).setText(m.getBody());
        return convertView;
    }
}
