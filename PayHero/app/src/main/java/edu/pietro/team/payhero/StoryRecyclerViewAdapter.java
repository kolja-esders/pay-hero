package edu.pietro.team.payhero;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.pietro.team.payhero.FriendFeedFragment.OnFriendFeedFragmentInteractionListener;
import edu.pietro.team.payhero.social.Stories.Story;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Story} and makes a call to the
 * specified {@link OnFriendFeedFragmentInteractionListener}.
 */
public class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryRecyclerViewAdapter.ViewHolder> {

    private final List<Story> mValues;
    private final OnFriendFeedFragmentInteractionListener mListener;

    public StoryRecyclerViewAdapter(List<Story> items, OnFriendFeedFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStory = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getBuyerName() + " just bought "
                + mValues.get(position).getPurchasableName() + " from "
                + mValues.get(position).getSellerName() + ".");
        holder.mMessageView.setText(mValues.get(position).getMessage());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFriendFeedFragmentInteraction(holder.mStory);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mMessageView;
        public Story mStory;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mMessageView = (TextView) view.findViewById(R.id.message);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
