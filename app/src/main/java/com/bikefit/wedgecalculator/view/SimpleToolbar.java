package com.bikefit.wedgecalculator.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * A Toolbar for use in the application
 */
public class SimpleToolbar extends Toolbar {

    //region CLASS VARIABLES -----------------------------------------------------------------------
    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    public SimpleToolbar(Context context) {
        super(context);
        init(null);
    }

    public SimpleToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SimpleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
    }

    //endregion

    //region PUBLIC CLASS METHODS ------------------------------------------------------------------
    //endregion

}
