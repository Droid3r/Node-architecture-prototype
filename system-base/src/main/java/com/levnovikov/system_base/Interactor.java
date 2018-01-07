package com.levnovikov.system_base;

import android.os.Parcelable;

import com.levnovikov.system_base.node_state.ActivityState;
import com.levnovikov.system_base.node_state.NodeState;

public abstract class Interactor<R extends Router> {

    protected R router;
    protected ActivityState activityState;

    public Interactor(R router, ActivityState activityState) {
        this.router = router;
        this.activityState = activityState;
    }

    public void onGetActive() {
        final NodeState state = getNodeState();
        if (state != null) {
            router.setState(state);
        }
    }

    protected NodeState getNodeState() {
        return activityState.findNodeState(router.getClass());
    }

    protected Parcelable getNodeStateData() {
        final NodeState state = getNodeState();
        return state != null ? state.data() : null;
    }

    protected boolean hasSavedState() {
        return activityState.findNodeState(router.getClass()) != null;
    }
}
