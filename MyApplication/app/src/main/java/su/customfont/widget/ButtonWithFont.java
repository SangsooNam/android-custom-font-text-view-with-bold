package su.customfont.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import su.customfont.R;
import su.customfont.util.FontUtils;

public class ButtonWithFont extends Button {

    public ButtonWithFont(Context context) {
        super(context);
    }

    public ButtonWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontUtils.setFont(this, attrs, R.styleable.ViewWithFont, R.styleable.ViewWithFont_font, R.styleable.ViewWithFont_fontBold);
    }
}