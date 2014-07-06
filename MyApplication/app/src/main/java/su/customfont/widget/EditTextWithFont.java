package su.customfont.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import su.customfont.R;
import su.customfont.util.FontUtils;

public class EditTextWithFont extends EditText {

    public EditTextWithFont(Context context) {
        super(context);
    }

    public EditTextWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontUtils.setFont(this, attrs, R.styleable.ViewWithFont, R.styleable.ViewWithFont_font, R.styleable.ViewWithFont_fontBold);
    }
}