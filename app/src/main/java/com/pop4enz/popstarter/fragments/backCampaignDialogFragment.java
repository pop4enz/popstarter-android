package com.pop4enz.popstarter.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.pop4enz.popstarter.R;
import com.pop4enz.popstarter.activity.CampaignActivity;

public class backCampaignDialogFragment extends DialogFragment {

    private EditText backValueET;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_back_campaign, null);
        backValueET = view.findViewById(R.id.backValueET);
        builder.setView(view);
        builder.setMessage(R.string.dialog_back_campaign)
                .setPositiveButton(R.string.support, (dialog, id) -> {
                    ((CampaignActivity)getContext())
                            .supportCampaign(backValueET.getText().toString());
                    backCampaignDialogFragment.this.getDialog().cancel();
                })
                .setNegativeButton(R.string.cancel, (dialog, id)
                        -> backCampaignDialogFragment.this.getDialog().cancel());
        return builder.create();
    }

}
