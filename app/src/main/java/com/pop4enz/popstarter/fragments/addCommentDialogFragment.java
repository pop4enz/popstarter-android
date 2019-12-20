package com.pop4enz.popstarter.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.activity.CampaignActivity;

public class addCommentDialogFragment extends DialogFragment {

    private EditText commentET;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_comment, null);
        commentET = view.findViewById(R.id.commentET);
        builder.setView(view);
        builder.setMessage(R.string.dialog_comment)
                .setPositiveButton(R.string.send, (dialog, id) -> {
                    ((CampaignActivity)getContext())
                            .addComment(commentET.getText().toString()
                                    .replaceAll("\\n", " "));
                    addCommentDialogFragment.this.getDialog().cancel();
                })
                .setNegativeButton(R.string.cancel, (dialog, id)
                        -> addCommentDialogFragment.this.getDialog().cancel());
        return builder.create();
    }
}
