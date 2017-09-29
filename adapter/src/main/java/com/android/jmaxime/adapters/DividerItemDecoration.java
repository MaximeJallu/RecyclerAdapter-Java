package com.android.jmaxime.adapters;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String INVALID = "invalid orientation";
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int LIST_HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int LIST_VERTICAL = LinearLayoutManager.VERTICAL;
    public static final int GRID_STROKE = 3;
    public static final int GRID_FILL = 4;
    private static Drawable mDivider;
    private int mOrientation;

    /**
     * Custom constructor
     *
     * @param context
     * @param orientation
     */
    public DividerItemDecoration(final Context context, final int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        if (mDivider == null){
            mDivider = a.getDrawable(0);
        }
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * Set the orientation of the underlying grid / list
     *
     * @param orientation must be
     * {@link com.android.jmaxime.adapters.DividerItemDecoration#LIST_HORIZONTAL}
     * {@link com.android.jmaxime.adapters.DividerItemDecoration#LIST_VERTICAL}
     * {@link com.android.jmaxime.adapters.DividerItemDecoration#GRID_STROKE}
     * {@link com.android.jmaxime.adapters.DividerItemDecoration#GRID_FILL}
     */
    public void setOrientation(final int orientation) {
        if (orientation != LIST_HORIZONTAL &&
                orientation != LIST_VERTICAL &&
                orientation != GRID_STROKE &&
                orientation != GRID_FILL) {
            throw new IllegalArgumentException(INVALID);
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDivider != null) {
            switch (mOrientation) {
                case LIST_VERTICAL:
                    drawVertical(c, parent);
                    break;
                case LIST_HORIZONTAL:
                    drawHorizontal(c, parent);
                    break;
                case GRID_FILL:
                    drawGridFill(c, parent);
                    break;
                case GRID_STROKE:
                    drawGridStroke(c, parent);
                    break;
                default:
                    throw new IllegalArgumentException(INVALID);
            }
        }
    }

    /**
     * Draw vertical divider in parent
     *
     * @param c      canvas
     * @param parent recycler view
     */
    public void drawVertical(final Canvas c, final RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * Draw horizontal divider in parent
     *
     * @param c      Canvas
     * @param parent Recycler view
     */
    public void drawHorizontal(final Canvas c, final RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * Remove both horizontal and vertical divider in parent
     *
     * @param c      Canvas
     * @param parent Recycler view
     */
    public void drawGridStroke(final Canvas c, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, left + mDivider.getIntrinsicHeight(), bottom);
            mDivider.draw(c);
            mDivider.setBounds(right - mDivider.getIntrinsicHeight(), top, right, bottom);
            mDivider.draw(c);
            mDivider.setBounds(left, top, right, top + mDivider.getIntrinsicHeight());
            mDivider.draw(c);
            mDivider.setBounds(left, bottom - mDivider.getIntrinsicHeight(), right, bottom);
            mDivider.draw(c);
            mDivider.draw(c);
        }
    }

    /**
     * Draw both horizontal and vertical divider in parent
     *
     * @param c      Canvas
     * @param parent Recler view
     */
    public void drawGridFill(final Canvas c, final RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDivider != null) {
            switch (mOrientation) {
                case LIST_VERTICAL:
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                    break;
                case LIST_HORIZONTAL:
                    outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
                    break;
                case GRID_FILL:
                    outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth());
                    break;
                case GRID_STROKE:
                    outRect.set(0, 0, 0, 0);
                    break;
                default:
                    throw new IllegalArgumentException(INVALID);
            }
        }
    }
}

