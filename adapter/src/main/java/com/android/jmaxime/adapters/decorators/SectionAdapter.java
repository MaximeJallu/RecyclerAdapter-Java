package com.android.jmaxime.adapters.decorators;

/**
 * @author Maxime Jallu
 * @since 05/10/2017
 * <p>
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
public class SectionAdapter<T> {
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

    public void setSectionedPosition(int sectionedPosition) {
        this.sectionedPosition = sectionedPosition;
    }

    public int getFirstPosition() {
        return firstPosition;
    }

    public int getSectionedPosition() {
        return sectionedPosition;
    }
}
