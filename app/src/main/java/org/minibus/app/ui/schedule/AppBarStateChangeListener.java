package org.minibus.app.ui.schedule;

import com.google.android.material.appbar.AppBarLayout;

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private State state = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (state != State.EXPANDED) {
                onAppBarStateChanged(appBarLayout, State.EXPANDED);
            }
            state = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED) {
                onAppBarStateChanged(appBarLayout, State.COLLAPSED);
            }
            state = State.COLLAPSED;
        } else {
            if (state != State.IDLE) {
                onAppBarStateChanged(appBarLayout, State.IDLE);
            }
            state = State.IDLE;
        }
    }

    public abstract void onAppBarStateChanged(AppBarLayout appBarLayout, State state);

    public enum State {
        EXPANDED ("EXPANDED"),
        COLLAPSED ("COLLAPSED"),
        IDLE ("IDLE");

        private final String state;

        State(String state) {
            this.state = state;
        }

        public String toString() {
            return this.state;
        }
    }
}
