package com.team2052.frckrawler.fragments.scout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.team2052.frckrawler.R;
import com.team2052.frckrawler.activities.MatchListActivity;
import com.team2052.frckrawler.background.scout.PopulateMatchMetricsTask;
import com.team2052.frckrawler.background.scout.PopulateMatchScoutTask;
import com.team2052.frckrawler.background.scout.SaveMatchMetricsTask;
import com.team2052.frckrawler.database.MetricValue;
import com.team2052.frckrawler.db.Event;
import com.team2052.frckrawler.db.Robot;
import com.team2052.frckrawler.fragments.BaseFragment;
import com.team2052.frckrawler.views.metric.MetricWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Adam
 */
public class ScoutMatchFragment extends BaseFragment implements View.OnClickListener {
    public static final int MATCH_GAME_TYPE = 0;
    public static final int MATCH_PRACTICE_TYPE = 1;
    public static final String MATCH_TYPE = "MATCH_TYPE";
    public static final String EVENT_ID = "EVENT_ID";

    public static final String LOG_TAG = "ScoutMatchFragment";

    @Bind(R.id.match_number_input)
    public TextInputLayout mMatchNumberInput;

    @Bind(R.id.metric_widget_list)
    public LinearLayout mMetricList;

    @Bind(R.id.comments)
    public TextInputLayout mComments;

    @Bind(R.id.robot)
    public Spinner mRobotAutoComplete;

    @Bind(R.id.button_save)
    public FloatingActionButton mSaveButton;

    public Robot selectedRobot;
    public List<String> mRobotNames;
    private List<Robot> mRobots;
    private Event mEvent;
    private PopulateMatchScoutTask mPopulateTask;
    private PopulateMatchMetricsTask mPopulateMatchTask;
    private SaveMatchMetricsTask mSaveTask;

    private int mType;
    private List<MetricWidget> mWidgets;

    public static ScoutMatchFragment newInstance(Event event, int type) {
        ScoutMatchFragment scoutMatchFragment = new ScoutMatchFragment();
        Bundle args = new Bundle();
        args.putInt(MATCH_TYPE, type);
        args.putLong(EVENT_ID, event.getId());
        scoutMatchFragment.setArguments(args);
        return scoutMatchFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        mEvent = mDbManager.getEventsTable().load(getArguments().getLong(EVENT_ID));
        mType = getArguments().getInt(MATCH_TYPE, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scouting_match, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mSaveButton.setOnClickListener(this);
        mMatchNumberInput.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int value = Integer.parseInt(s.toString());
                    if (value == 0) {
                        mMatchNumberInput.setErrorEnabled(true);
                        mMatchNumberInput.setError("You must be an engineer!");
                    } else if (value == 2052) {
                        mMatchNumberInput.setErrorEnabled(true);
                        mMatchNumberInput.setError("That's a good tem");
                    } else if (value > 9000) {
                        mMatchNumberInput.setErrorEnabled(true);
                        mMatchNumberInput.setError("It's over 9000!");
                    } else {
                        mMatchNumberInput.setError("");
                        mMatchNumberInput.setErrorEnabled(false);
                    }
                    updateMetricValues();
                } catch (NumberFormatException e) {
                    mMatchNumberInput.setErrorEnabled(true);
                    mMatchNumberInput.setError("Invalid Number");
                }

            }
        });

        mComments.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isCommentValid()) {
                    mComments.setError("You had one job");
                } else {

                    mComments.setError("");
                    mComments.setErrorEnabled(false);
                }
            }
        });
        mRobotAutoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!(mRobots.size() < position))
                    selectedRobot = mRobots.get(position);
                updateMetricValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/*NOOP*/}
        });
        mPopulateTask = new PopulateMatchScoutTask(this, mEvent);
        mPopulateTask.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.scout, menu);
        if (mType == MATCH_PRACTICE_TYPE) {
            menu.removeItem(R.id.action_view_match);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_view_match) {
            startActivity(MatchListActivity.newInstance(getActivity(), mEvent));
        }
        return false;
    }

    private boolean isMatchNumberValid() {
        try {
            Integer.parseInt(mMatchNumberInput.getEditText().getText().toString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<MetricValue> getMetricValues() {
        List<MetricValue> list = new ArrayList<>();
        for (int i = 0; i < mMetricList.getChildCount(); i++) {
            list.add(((MetricWidget) mMetricList.getChildAt(i)).getValue());
        }
        return list;
    }

    private boolean isCommentValid() {
        String comment = getComment().toLowerCase();
        //Some people.... some people
        return !(comment.contains("no comment") || comment.contains("nocomment"));
    }

    private String getComment() {
        return mComments.getEditText().getText().toString();
    }

    public void setComment(String comment) {
        mComments.getEditText().setText(comment);
    }

    public void updateMetricValues() {
        if (getSelectedRobot() != null) {
            mPopulateMatchTask = new PopulateMatchMetricsTask(
                    this,
                    mEvent,
                    getSelectedRobot(),
                    getMatchNumber(),
                    mType);
            mPopulateMatchTask.execute();
        }
    }

    private int getMatchNumber() {
        try {
            return Integer.parseInt(mMatchNumberInput.getEditText().getText().toString());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Nullable
    public Robot getSelectedRobot() {
        return selectedRobot;
    }

    public void setWidgets(List<MetricWidget> widgets) {
        mWidgets = widgets;
        for (MetricWidget metricWidget : widgets) {
            mMetricList.addView(metricWidget);
        }
    }

    public List<Robot> getRobots() {
        return mRobots;
    }

    public void setRobots(List<Robot> mRobots) {
        this.mRobots = mRobots;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save) {
            if (mEvent != null && getSelectedRobot() != null && isMatchNumberValid()) {
                mSaveTask = new SaveMatchMetricsTask(
                        getActivity(),
                        this,
                        mEvent,
                        getSelectedRobot(),
                        /*TODO: USER*/ null,
                        getMatchNumber(),
                        mType,
                        getMetricValues(),
                        getComment());
                mSaveTask.execute();
            } else if (getSelectedRobot() == null) {
                Snackbar.make(getView(), "Please select a robot", Snackbar.LENGTH_SHORT).show();
            } else if (!isMatchNumberValid()) {
                Snackbar.make(getView(), "Match Number is Invalid", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(getView(), getActivity().getString(R.string.something_seems_wrong), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
