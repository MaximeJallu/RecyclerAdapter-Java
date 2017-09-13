package com.android.jmaxime.annotations

import android.support.annotation.LayoutRes

/**
 * @author Maxime Jallu
 * @since 09/06/2017
 * Use this Class for : <br></br>
 * ... {DOCUMENTATION}
 */
//@Scope
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class BindLayoutRes(
        /**
         * View ID to which the field will be bound.
         */
        @LayoutRes val value: Int
)
