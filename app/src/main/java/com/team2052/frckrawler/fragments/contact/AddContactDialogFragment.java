package com.team2052.frckrawler.fragments.contact;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.team2052.frckrawler.R;
import com.team2052.frckrawler.activities.BaseActivity;
import com.team2052.frckrawler.database.DBManager;
import com.team2052.frckrawler.db.Team;
import com.team2052.frckrawler.listeners.ListUpdateListener;

/**
 * @author Adam
 */
public class AddContactDialogFragment extends DialogFragment {
    private Team mTeam;

    public static AddContactDialogFragment newInstance(Team team) {
        AddContactDialogFragment fragment = new AddContactDialogFragment();
        Bundle b = new Bundle();
        b.putLong(BaseActivity.PARENT_ID, team.getNumber());
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTeam = DBManager.getInstance(getActivity()).getTeamsTable().load(getArguments().getLong(BaseActivity.PARENT_ID, 0));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_contact, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = ((EditText) view.findViewById(R.id.nameVal)).getText().toString();
                String email = ((EditText) view.findViewById(R.id.email)).getText().toString();
                String phone = ((EditText) view.findViewById(R.id.phone)).getText().toString();
                String address = ((EditText) view.findViewById(R.id.address)).getText().toString();
                String teamRole = ((EditText) view.findViewById(R.id.teamRole)).getText().toString();
                //((FRCKrawler) getActivity().getApplication()).getDBManager().getContactDao().insert(new Contact(null, mTeam.getNumber(), name, email, address, phone, teamRole));
                ((ListUpdateListener) getParentFragment()).updateList();
            }
        });
        builder.setTitle("Add Contact");
        return builder.create();
    }
}
