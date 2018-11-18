package com.github.kko7.manaflux_android.Layout;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(),"fonts/Beaufort.ttf");
        setTypeface( normal, Typeface.NORMAL );

        Typeface bold = Typeface.createFromAsset( getContext().getAssets(), "fonts/Beaufort_Bold.ttf" );
        setTypeface( bold, Typeface.BOLD );
    }
}