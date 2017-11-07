package com.android.jmaxime.adapters.decorators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.android.jmaxime.factory.ViewHolderFactory;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @param <S> Section Type
 * @param <A> RecyclerAdapter base Type
 */
@SuppressWarnings("WeakerAccess") public class SectionedAdapter<S, A extends RecyclerView.Adapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int DEFAULT_SECTION_TYPE = 0;
    private static final int SECTION_TYPE = 2312;
    private SparseArray<ViewHolderFactory<S>> mSectionHolderFactoryList;
    private SparseArray<SectionAdapter<S>> mSectionItems = new SparseArray<>();
    @NonNull private A mBaseAdapter;
    private boolean mValid = true;

    public SectionedAdapter(@NonNull Class<? extends RecyclerViewHolder<S>> viewHolder, @NonNull A a) {
        mSectionHolderFactoryList = new SparseArray<>();
        mSectionHolderFactoryList.append(DEFAULT_SECTION_TYPE, new ViewHolderFactory.Builder<>(viewHolder).build());
        mBaseAdapter = a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_TYPE) {
            return mSectionHolderFactoryList.get(DEFAULT_SECTION_TYPE).createViewHolder(parent);
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, viewType - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            //noinspection unchecked
            ((RecyclerViewHolder<S>) sectionViewHolder).bind(mSectionItems.get(position).getT());
        } else {
            //noinspection unchecked
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSectionItems.indexOfKey(position)
                : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemCount() {
        return (mValid ? mBaseAdapter.getItemCount() + mSectionItems.size() : 0);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        init(recyclerView);
    }

    public A getBaseAdapter() {
        return mBaseAdapter;
    }

    public void addSection(int position, S item) {
        addSection(new SectionAdapter<>(position, item));
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSectionItems.size(); i++) {
            if (mSectionItems.valueAt(i).getFirstPosition() > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    /**
     * Get the real position of base adapter
     *
     * @param sectionedPosition position this adapter
     * @return position baseAdapter
     */
    private int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSectionItems.size(); i++) {
            if (mSectionItems.valueAt(i).getSectionedPosition() > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSectionItems.get(position) != null;
    }


    private void addSection(SectionAdapter<S> section) {
        mSectionItems.append(section.getFirstPosition(), section);
        List<SectionAdapter<S>> s = new ArrayList<>();
        for (int i = 0; i < mSectionItems.size(); i++) {
            s.add(mSectionItems.valueAt(i));
        }

        setSections(s);
    }

    private void setSections(List<SectionAdapter<S>> sections) {
        mSectionItems.clear();

        int offset = 0; // offset positions for the headers we're adding
        for (SectionAdapter<S> section : sortSections(sections)) {
            section.setSectionedPosition(section.getFirstPosition() + offset);
            mSectionItems.append(section.getSectionedPosition(), section);
            ++offset;
        }

        notifyDataSetChanged();
    }

    private List<SectionAdapter<S>> sortSections(List<SectionAdapter<S>> sections) {
        Collections.sort(sections, (o, o1) -> (o.getFirstPosition() == o1.getFirstPosition())
                ? 0
                : ((o.getFirstPosition() < o1.getFirstPosition()) ? -1 : 1));
        return sections;
    }

    private void init(@Nullable RecyclerView r) {
        mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                mValid = mBaseAdapter.getItemCount() > 0;
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        });

        if (r != null) {
            RecyclerView.LayoutManager layoutManager = r.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                ((GridLayoutManager) layoutManager)
                        .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                return (isSectionHeaderPosition(position)) ? ((GridLayoutManager) layoutManager)
                                        .getSpanCount() : 1;
                            }
                        });
            }
        }
    }

    private class SectionAdapter<T> {
        private T mT;
        private int firstPosition;
        private int sectionedPosition;

        public SectionAdapter(int firstPosition, T t) {
            mT = t;
            this.firstPosition = firstPosition;
        }

        public T getT() {
            return mT;
        }

        public int getFirstPosition() {
            return firstPosition;
        }

        public int getSectionedPosition() {
            return sectionedPosition;
        }

        public void setSectionedPosition(int sectionedPosition) {
            this.sectionedPosition = sectionedPosition;
        }
    }
}
