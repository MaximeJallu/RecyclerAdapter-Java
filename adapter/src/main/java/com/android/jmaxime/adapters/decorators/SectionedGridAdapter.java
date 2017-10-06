package com.android.jmaxime.adapters.decorators;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jmaxime.adapter.R;
import com.android.jmaxime.factory.ViewHolderFactory;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Maxime Jallu
 * @since 05/10/2017
 * <p>
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
public class SectionedGridAdapter<S> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 2312;
    ViewHolderFactory<S> mSectionHolderFactory;
    private SparseArray<SectionAdapter<S>> mSectionItems;
    private boolean mValid = true;
//    private int mSectionResourceId;
//    private int mTextResourceId;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mBaseAdapter;
//    private SparseArray<Section> mSections = new SparseArray<>();
    private RecyclerView mRecyclerView;

    public SectionedGridAdapter(Class<? extends RecyclerViewHolder<S>> viewType, RecyclerView r, RecyclerView.Adapter<RecyclerView.ViewHolder> a){
        mSectionHolderFactory = new ViewHolderFactory<>(viewType);
        mRecyclerView = r;
        mBaseAdapter = a;
    }

    public SectionedGridAdapter(RecyclerView recyclerView, RecyclerView.Adapter<RecyclerView.ViewHolder> baseAdapter) {
        this(R.layout.section_simple_title, R.id.text1, recyclerView, baseAdapter);
    }

    public SectionedGridAdapter(int sectionResourceId, int textResourceId, RecyclerView recyclerView,
            RecyclerView.Adapter<RecyclerView.ViewHolder> baseAdapter) {

//        mSectionResourceId = sectionResourceId;
//        mTextResourceId = textResourceId;
        mBaseAdapter = baseAdapter;
        mRecyclerView = recyclerView;

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

        final GridLayoutManager layoutManager = (GridLayoutManager) (mRecyclerView
                .getLayoutManager());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isSectionHeaderPosition(position)) ? layoutManager.getSpanCount() : 1;
            }
        });
    }


//    public static class SectionViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView title;
//
//        public SectionViewHolder(View view, int mTextResourceid) {
//            super(view);
//            title = view.findViewById(mTextResourceid);
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int typeView) {
        if (typeView == SECTION_TYPE) {
            return mSectionHolderFactory.createVH(parent, typeView);
//            final View view = LayoutInflater.from(parent.getContext())
//                                            .inflate(mSectionResourceId, parent, false);
//            return new SectionViewHolder(view, mTextResourceId);
        } else {
            return mBaseAdapter.onCreateViewHolder(parent, typeView - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder sectionViewHolder, int position) {
        if (isSectionHeaderPosition(position)) {
            //noinspection unchecked
            ((RecyclerViewHolder<S>) sectionViewHolder).bind(mSectionItems.get(position).getT());// title.setText(mSections.get(position).title);
        } else {
            mBaseAdapter.onBindViewHolder(sectionViewHolder, sectionedPositionToPosition(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }


    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int firstPosition, CharSequence title) {
            this.firstPosition = firstPosition;
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }


    public void addSetion(SectionAdapter<S> section){
        mSectionItems.append(section.getFirstPosition(), section);
        List<SectionAdapter<S>> s = new ArrayList<>();
        SectionAdapter<S>[] sections = new SectionAdapter[mSectionItems.size()];
        s.toArray(sections);
        for (int i = 0; i < mSectionItems.size(); i++){
            sections[i] = mSectionItems.valueAt(i);
        }
        setSections(sections);
    }

    private void setSections(SectionAdapter<S>[] sections) {
        mSectionItems.clear();

        int offset = 0; // offset positions for the headers we're adding
        for (SectionAdapter<S> section : sortSections(sections)) {
            section.setSectionedPosition(section.getFirstPosition() + offset);
            mSectionItems.append(section.getSectionedPosition(), section);
            ++offset;
        }

        notifyDataSetChanged();
    }
    private SectionAdapter<S>[] sortSections(SectionAdapter<S>[] sections){
        Arrays.sort(sections, (o, o1) -> (o.getFirstPosition() == o1.getFirstPosition())
                ? 0
                : ((o.getFirstPosition() < o1.getFirstPosition()) ? -1 : 1));
        return sections;
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
}
