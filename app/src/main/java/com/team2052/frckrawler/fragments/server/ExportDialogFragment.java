package com.team2052.frckrawler.fragments.server;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import com.team2052.frckrawler.GlobalValues;
import com.team2052.frckrawler.activities.BaseActivity;
import com.team2052.frckrawler.database.ExportUtil;
import com.team2052.frckrawler.db.Event;
import com.team2052.frckrawler.fragments.BaseProgressDialog;

import java.io.File;
import java.io.IOException;

/**
 * @author Adam
 * @since 3/10/2015.
 */
public class ExportDialogFragment extends BaseProgressDialog {

    public static ExportDialogFragment newInstance(Event event) {
        ExportDialogFragment exportDialogFragment = new ExportDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BaseActivity.PARENT_ID, event.getId());
        exportDialogFragment.setArguments(bundle);
        return exportDialogFragment;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Event event = mDbManager.getEventsTable().load(getArguments().getLong(BaseActivity.PARENT_ID));
        new ExportToFileSystem(event).execute();
    }

    @Override
    public CharSequence getMessage() {
        return "Generating CSV";
    }

    public class ExportToFileSystem extends AsyncTask<Void, Void, File> {
        final float compileWeight;
        private final Event event;
        File file = null;

        public ExportToFileSystem(Event event) {
            this.compileWeight = getActivity().getSharedPreferences(GlobalValues.PREFS_FILE_NAME, 0).getFloat(GlobalValues.PREFS_COMPILE_WEIGHT, 1.0f);
            this.event = event;
        }

        @Override
        protected File doInBackground(Void... voids) {
            File fileSystem = Environment.getExternalStorageDirectory();

            if (event != null) {
                if (fileSystem.canWrite()) {
                    try {
                        file = File.createTempFile(
                                mDbManager.getGamesTable().load(event.getGame_id()).getName() + "_" + event.getName() + "_" + "Summary",  /* prefix */
                                ".csv",         /* suffix */
                                fileSystem      /* directory */
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file != null) {
                        return ExportUtil.exportEventDataToCSV(event, file, mDbManager, compileWeight);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            dismissAllowingStateLoss();
            if (file != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                shareIntent.setType("file/csv");
                startActivity(Intent.createChooser(shareIntent, "Share CSV with..."));
            }
        }
    }
}
