package com.example.minibus.ui.custom;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class SpanningLinearLayoutManager extends LinearLayoutManager {

    public SpanningLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayoutSize(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return spanLayoutSize(super.generateLayoutParams(c, attrs));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return spanLayoutSize(super.generateLayoutParams(lp));
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return super.checkLayoutParams(lp);
    }

    private RecyclerView.LayoutParams spanLayoutSize(RecyclerView.LayoutParams layoutParams){
        if(getOrientation() == RecyclerView.HORIZONTAL){
            layoutParams.width = (int) Math.round(getHorizontalSpace() / (double) getItemCount());
        } else if(getOrientation() == RecyclerView.VERTICAL){
            layoutParams.height = (int) Math.round(getVerticalSpace() /  (double) getItemCount());
        }
        return layoutParams;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
