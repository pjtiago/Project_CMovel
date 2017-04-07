package com.estg.joaoviana.project_cmovel.Messages;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estg.joaoviana.project_cmovel.R;


public class ReceiveMessagesFragment extends Fragment {

    View rootView;

    public ReceiveMessagesFragment() {
        // Required empty public constructor
    }

    public static ReceiveMessagesFragment newInstance(String param1, String param2) {
        ReceiveMessagesFragment fragment = new ReceiveMessagesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_receive_messages, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_receive_messages, null);
        }
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
