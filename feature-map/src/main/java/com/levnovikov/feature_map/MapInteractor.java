package com.levnovikov.feature_map;

import com.example.core_geo.Point;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.levnovikov.feature_map.di.MapScope;
import com.levnovikov.feature_map.map_wrapper.MapWrapper;
import com.levnovikov.system_base.Interactor;
import com.levnovikov.system_base.lifecycle.Lifecycle;
import com.levnovikov.system_base.node_state.ActivityState;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Author: lev.novikov
 * Date: 2/1/18.
 */

@MapScope
public class MapInteractor extends Interactor<MapRouter> implements OnMapReadyCallback {

    private final MapDataStream mapDataStream;
    private final Lifecycle lifecycle;

    public interface MapDataStream {
        Observable<Point> pickUpPointStream();
        Observable<Point> dropOffPointStream();
    }

    @Inject
    MapInteractor(
            MapRouter router,
            ActivityState activityState,
            MapDataStream mapDataStream,
            Lifecycle lifecycle) {
        super(router, activityState);
        this.mapDataStream = mapDataStream;
        this.lifecycle = lifecycle;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        final MapWrapper mapWrapper = new MapWrapper(googleMap);
        lifecycle.subscribeUntilDestroy(mapDataStream.dropOffPointStream()
                .subscribe(mapWrapper::setPickUp, e -> {}));

        lifecycle.subscribeUntilDestroy(mapDataStream.pickUpPointStream()
                .subscribe(mapWrapper::setDropOff, e -> {}));
    }

}
