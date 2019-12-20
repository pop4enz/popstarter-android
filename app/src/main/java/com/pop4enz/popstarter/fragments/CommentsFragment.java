package com.pop4enz.popstarter.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.adapter.CommentsAdapter;
import com.pop4enz.popstarter.model.Comment;
import com.pop4enz.popstarter.model.CommentList;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommentsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "comments";

    private int visibility;

    private CommentsAdapter commentsAdapter = new CommentsAdapter();

    private CommentList comments;

    private OnFragmentInteractionListener mListener;

    public CommentsFragment() {
    }

    public static CommentsFragment newInstance(CommentList comments) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, comments);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comments = (CommentList) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView commentsRv = Objects.requireNonNull(getView()).findViewById(R.id.commentsRv);
        Button addCommentButton = getView().findViewById(R.id.addComment);
        addCommentButton.setVisibility(visibility);
        addCommentButton.setOnClickListener(this);
        commentsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        commentsRv.setAdapter(commentsAdapter);
        if (comments != null) {
            commentsAdapter.setItems(comments.getComments());
        }
    }

    public void setComments(CommentList comments) {
        Bundle args = new Bundle();
        this.comments = comments;
        args.putSerializable(ARG_PARAM1, comments);
        this.setArguments(args);
        commentsAdapter.setItems(comments.getComments());
    }

    public void addComment(Comment comment) {
        Bundle args = new Bundle();
        this.comments.addComment(comment);
        args.putSerializable(ARG_PARAM1, comments);
        this.setArguments(args);
        commentsAdapter.addItem(comment);
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setButtonVisibility(int value) {
            visibility = value;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addComment) {
            FragmentManager fm = getFragmentManager();
            addCommentDialogFragment newFragment = new addCommentDialogFragment();
            newFragment.show(fm, null);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
