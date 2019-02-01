package com.github.kko7.manaflux_android.CustomElements;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CheckedTextView extends android.support.v7.widget.AppCompatCheckedTextView {
    public CheckedTextView(Context context) {
        super(context);
        setFont();
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        Typeface normal = Typeface.createFromAsset(getContext().getAssets(), "fonts/Beaufort.ttf");
        setTypeface(normal, Typeface.NORMAL);

        Typeface bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Beaufort_Bold.ttf");
        setTypeface(bold, Typeface.BOLD);
    }
}
