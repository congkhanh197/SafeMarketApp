package com.group13.safemarket.adapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by Khanh Tran-Cong on 5/3/2018.
 * Email: congkhanh197@gmail.com
 */
public class FirebaseArray implements ChildEventListener {
    public interface OnChangedListener {
        enum EventType {Added, Changed, Removed, Moved}

        void onChanged(EventType type, int index, int oldIndex);
    }

    private Query mQuery;
    private OnChangedListener mListener;
    private ArrayList<DataSnapshot> mSnapshots;

    protected FirebaseArray(Query ref) {
        mQuery = ref;
        mSnapshots = new ArrayList<>();
        mQuery.addChildEventListener(this);
    }

    protected void cleanup() {
        mQuery.removeEventListener(this);
    }

    public int getCount() {
        return mSnapshots.size();
    }

    public DataSnapshot getItem(int index) {
        return mSnapshots.get(index);
    }

    protected boolean filter(DataSnapshot dataSnapshot, String previousChildKey){
        return true;
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    // Start of ChildEventListener methods
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        if(filter(snapshot,previousChildKey)){
            int index = 0;
            if (previousChildKey != null) {
                //index = getIndexForKey(previousChildKey) + 1;
                index = mSnapshots.size();
            }
            mSnapshots.add(index, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Added, index);
        }
    }

    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        if(filter(snapshot,previousChildKey)){
            int index = getIndexForKey(snapshot.getKey());
            mSnapshots.set(index, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Changed, index);
        }

    }

    public void onChildRemoved(DataSnapshot snapshot) {
        if(filter(snapshot,"")) {
            int index = getIndexForKey(snapshot.getKey());
            mSnapshots.remove(index);
            notifyChangedListeners(OnChangedListener.EventType.Removed, index);
        }
    }

    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        if(filter(snapshot,previousChildKey)) {
            int oldIndex = getIndexForKey(snapshot.getKey());
            mSnapshots.remove(oldIndex);
            int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
            mSnapshots.add(newIndex, snapshot);
            notifyChangedListeners(OnChangedListener.EventType.Moved, newIndex, oldIndex);
        }
    }

    public void onCancelled(DatabaseError firebaseError) {
        // TODO: what do we do with this?
    }
    // End of ChildEventListener methods

    public void setOnChangedListener(OnChangedListener listener) {
        mListener = listener;
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(OnChangedListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChanged(type, index, oldIndex);
        }
    }
}


