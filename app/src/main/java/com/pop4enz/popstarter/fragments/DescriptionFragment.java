package com.pop4enz.popstarter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pop4enz.popstarter.R;

import java.util.Objects;

import io.noties.markwon.Markwon;

public class DescriptionFragment extends Fragment {

    private static final String ARG_PARAM1 = "description";

    private Markwon markwon;

    private TextView descriptionTv;

    private String description;

    private OnFragmentInteractionListener mListener;

    public DescriptionFragment() {
    }

    public static DescriptionFragment newInstance(String param1) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (description == null && getArguments() != null) {
            description = getArguments().getString(ARG_PARAM1);
        }
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        descriptionTv = Objects.requireNonNull(getView()).findViewById(R.id.descriptionTv);
        if (description != null) {
            markwon.setMarkdown(descriptionTv, description);
        } else {
            descriptionTv.setText(null);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            markwon = Markwon.builder(context).build();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void setDescription(String description) {
        markwon.setMarkdown(descriptionTv, description);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, description);
        this.setArguments(args);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
