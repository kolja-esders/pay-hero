package edu.pietro.team.payhero;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import edu.pietro.team.payhero.event.FeedFilterClicked;
import edu.pietro.team.payhero.social.Stories;
import edu.pietro.team.payhero.social.Stories.Story;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFriendFeedFragmentInteractionListener}
 * interface.
 */
public class FriendFeedFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnFriendFeedFragmentInteractionListener mListener;

    private RecyclerView.Adapter mAdapter;

    private int displayStatus = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FriendFeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FriendFeedFragment newInstance(int columnCount) {
        FriendFeedFragment fragment = new FriendFeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        Log.d("0o", "on Start");
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        Log.d("0o", "on Resume");
        super.onResume();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            RecyclerView recyclerView = (RecyclerView) this.getView().findViewById(R.id.story_list);

            Toolbar toolbar = (Toolbar) this.getView().findViewById(R.id.feed_toolbar);

            if(displayStatus == 1){
                Stories.filterPersonalStories();
                toolbar.setTitle("Own");
            } else {
                Stories.filterFriendStories();
                toolbar.setTitle("Friends");
            }

            // Set the adapter
            if (recyclerView != null && Stories.updated) {
                Context context = recyclerView.getContext();
                if (mColumnCount <= 1) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                }
                mAdapter = new StoryRecyclerViewAdapter(Stories.DISPLAYED_ITEMS, mListener);
                recyclerView.setAdapter(mAdapter);
                Stories.updated = false;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.story_list);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.feed_toolbar);
        getActivity().setActionBar(toolbar);

        if(displayStatus == 1){
            Stories.filterPersonalStories();
            toolbar.setTitle("Own");
        } else {
            Stories.filterFriendStories();
            toolbar.setTitle("Friends");
        }

        // Set the adapter
        if (recyclerView != null) {
            Context context = recyclerView.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new StoryRecyclerViewAdapter(Stories.DISPLAYED_ITEMS, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Subscribe
    public void onEvent(FeedFilterClicked event) {
        Log.d("0o", "on Event");
        Toolbar toolbar = (Toolbar) this.getView().findViewById(R.id.feed_toolbar);
        if (event.showOnlyPersonalStories) {
            this.displayStatus = 1;
            Stories.filterPersonalStories();
            toolbar.setTitle("Own");
        } else {
            this.displayStatus = 0;
            Stories.filterFriendStories();
            toolbar.setTitle("Friends");
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFriendFeedFragmentInteractionListener {
        void onFriendFeedFragmentInteraction(Story story);
    }
}
