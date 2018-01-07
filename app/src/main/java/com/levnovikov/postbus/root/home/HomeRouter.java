package com.levnovikov.postbus.root.home;

import android.support.annotation.Nullable;

import com.levnovikov.feature_map.MapNodeHolder;
import com.levnovikov.postbus.root.home.allocating.AllocatingNodeHolder;
import com.levnovikov.postbus.root.home.di.HomeScope;
import com.levnovikov.postbus.root.home.prebooking.PrebookingNodeHolder;
import com.levnovikov.stream_state.AppState;
import com.levnovikov.system_base.Router;
import com.levnovikov.system_base.node_state.NodeState;

import javax.inject.Inject;

/**
 * Author: lev.novikov
 * Date: 14/12/17.
 */

@HomeScope
class HomeRouter extends Router {

    private final PrebookingNodeHolder prebookingBuilder;
    private final AllocatingNodeHolder allocatingBuilder;
    private final MapNodeHolder mapBuilder;
    private @Nullable AppState currentState;

    @Inject
    HomeRouter(PrebookingNodeHolder prebookingBuilder, AllocatingNodeHolder allocatingBuilder, MapNodeHolder mapBuilder) {
        this.prebookingBuilder = prebookingBuilder;
        this.allocatingBuilder = allocatingBuilder;
        this.mapBuilder = mapBuilder;
    }

    void startPrebooking() {
        detachNode(allocatingBuilder);
        attachNode(prebookingBuilder);
    }

    void startAllocating() {
        detachNode(prebookingBuilder);
        attachNode(allocatingBuilder);
    }

    void loadMap() {
        attachNode(mapBuilder);
    }

    void startTracking() {

    }

    void switchState(AppState state) {
        if (state == currentState) {
            return;
        }
        switch (state) {
            case PREBOOKING:
                startPrebooking();
                break;
            case ALLOCATING:
                startAllocating();
                break;
            case TRACKING:
                startTracking();
                break;
        }
        currentState = state;
    }

    @Override
    protected void destroyNode() {
        //root node will be destroyed with activity
    }

    @Override
    public NodeState getNodeState(NodeState nodeState) {
        if (prebookingBuilder.isActive())
            nodeState.addNodeBuilder(prebookingBuilder.getClass());
        if (allocatingBuilder.isActive())
            nodeState.addNodeBuilder(allocatingBuilder.getClass());
        if (mapBuilder.isActive())
            nodeState.addNodeBuilder(mapBuilder.getClass());
        return nodeState;
    }

    @Override
    public void setState(NodeState state) {
        if (state.contains(prebookingBuilder.getClass())) {
            attachNode(prebookingBuilder);
        }

        if (state.contains(allocatingBuilder.getClass())) {
            attachNode(allocatingBuilder);
        }

        if (state.contains(mapBuilder.getClass())) {
            attachNode(mapBuilder);
        }
    }
}
