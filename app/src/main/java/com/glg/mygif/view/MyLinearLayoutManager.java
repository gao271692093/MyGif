package com.glg.mygif.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;

/**
 * Description:
 *
 * @package: com.ekwing.intelligence.teachers.datamanager
 * @className: MyLinearLayoutManager
 * @author: gao
 * @date: 2020/10/15 10:52
 */
public class MyLinearLayoutManager extends LinearLayoutManager {
    OrientationHelper mOrientationHelper;
    public MyLinearLayoutManager(Context context) {
        super(context);
        mOrientationHelper = OrientationHelper.createOrientationHelper(this, getOrientation());
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation);
    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mOrientationHelper = OrientationHelper.createOrientationHelper(this, getOrientation());
    }

    public int findLastBottomCompletelyEmergedItemPosition() {
        final View child = findOneBorderCompleteVisibleChild(getChildCount() - 1, -1, false);
        return child == null ? -1 : getPosition(child);
    }

    public int findFirstTopLastBottomCompletelyEmergedItemPosition() {
        final View child = findOneBorderCompleteVisibleChild(0, getChildCount(), true);
        return child == null ? -1 : getPosition(child);
    }

    private View findOneBorderCompleteVisibleChild(int fromIndex, int toIndex, boolean requestTop) {
        final int start = mOrientationHelper.getStartAfterPadding();
        final int end = mOrientationHelper.getEndAfterPadding();
        final int next = toIndex > fromIndex ? 1 : -1;
        for (int i = fromIndex; i != toIndex; i+=next) {
            final View child = getChildAt(i);
            final int childStart = mOrientationHelper.getDecoratedStart(child);
            final int childEnd = mOrientationHelper.getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (requestTop) {
                    if (childStart >= start) {
                        return child;
                    }
                } else if (childEnd <= end) {
                    return child;
                }
            }
        }
        return null;
    }
}
