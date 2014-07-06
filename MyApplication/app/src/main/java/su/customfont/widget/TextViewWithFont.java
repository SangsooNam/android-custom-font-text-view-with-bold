package su.customfont.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import su.customfont.R;
import su.customfont.util.FontUtils;

public class TextViewWithFont extends TextView {

    public TextViewWithFont(Context context) {
        super(context);
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontUtils.setFont(this, attrs, R.styleable.ViewWithFont, R.styleable.ViewWithFont_font, R.styleable.ViewWithFont_fontBold);
    }
}