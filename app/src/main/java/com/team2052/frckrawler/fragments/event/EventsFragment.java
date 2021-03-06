package com.team2052.frckrawler.fragments.event;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team2052.frckrawler.R;
import com.team2052.frckrawler.activities.BaseActivity;
import com.team2052.frckrawler.activities.EventInfoActivity;
import com.team2052.frckrawler.adapters.ListViewAdapter;
import com.team2052.frckrawler.db.Event;
import com.team2052.frckrawler.db.Game;
import com.team2052.frckrawler.fragments.BaseFragment;
import com.team2052.frckrawler.fragments.event.dialog.ImportDataSimpleDialogFragment;
import com.team2052.frckrawler.listeners.FABButtonListener;
import com.team2052.frckrawler.listeners.ListUpdateListener;
import com.team2052.frckrawler.listitems.ListElement;
import com.team2052.frckrawler.listitems.ListItem;
import com.team2052.frckrawler.listitems.elements.EventListElement;
import com.team2052.frckrawler.tba.ConnectionChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam
 * @since 10/15/2014
 */
public class EventsFragment extends BaseFragment implements FABButtonListener, ListUpdateListener {
    private Game mGame;
    private ListView mListView;

    public static EventsFragment newInstance(Game game) {
        EventsFragment fragment = new EventsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BaseActivity.PARENT_ID, game.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.list_layout);

        mListView.setOnItemClickListener((parent, view1, position, id) -> {
            ListElement listElement = (ListElement) parent.getAdapter().getItem(position);
            Event event = mDbManager.getEventsTable().load(Long.valueOf(listElement.getKey()));
            startActivity(EventInfoActivity.newInstance(getActivity(), event));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    public void updateList() {
        new GetEventsTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.event_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_event) {
            AddEventDialogFragment.newInstance(mGame).show(getChildFragmentManager(), "addEventDialog");
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mGame = mDbManager.getGamesTable().load(getArguments().getLong(BaseActivity.PARENT_ID, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, null);
    }

    @Override
    public void onFABPressed() {
        if (ConnectionChecker.isConnectedToInternet(getActivity()))
            ImportDataSimpleDialogFragment.newInstance(mGame).show(getChildFragmentManager(), "importEvent");
        else
            Snackbar.make(getView(), getActivity().getResources().getString(R.string.not_connected_to_internet), Snackbar.LENGTH_LONG).show();
    }

    private class GetEventsTask extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... params) {
            return mDbManager.getGamesTable().getEvents(mGame);
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            ArrayList<ListItem> eventList = new ArrayList<>();
            for (Event event : events) {
                eventList.add(new EventListElement(event));
            }
            mListView.setAdapter(new ListViewAdapter(getActivity(), eventList));
        }
    }
}
