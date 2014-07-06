package su.customfont.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import su.customfont.widget.CustomTypefaceSpan;

public class FontUtils {
    private static Map<String, Typeface> typeFaceCache = new HashMap<String, Typeface>();

    public static void setFont(View view, AttributeSet attrs, int[] attributeSet, int fontIndex, int boldFontIndex) {
        TypedArray typeArray = view.getContext().obtainStyledAttributes(attrs, attributeSet);
        String fontName = typeArray.getString(fontIndex);
        String boldFontName = typeArray.getString(boldFontIndex);

        if (fontName != null) {
            Typeface typeface = getTypeface(view.getContext(), fontName);
            Typeface boldTypeface = null;
            if (boldFontName != null) {
                boldTypeface = getTypeface(view.getContext(), boldFontName);
            }
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setTypeface(typeface);
                if (boldTypeface != null) {
                    String text = textView.getText().toString();
                    List<Pair<Integer, Integer>> boldPairList = new ArrayList<Pair<Integer, Integer>>();
                    for (int i = 1, start = -1; i < text.length(); i++) {
                        if (text.charAt(i) == '*' && text.charAt(i - 1) == '*') {
                            if (start > 0) {
                                boldPairList.add(new Pair<Integer, Integer>(start, i - 2));
                                start = -1;
                            } else {
                                start = i + 1;
                            }
                        }
                    }

                    SpannableString result = new SpannableString(text.replaceAll("\\*\\*", ""));
                    for (int i = 0; i < boldPairList.size(); i++) {
                        Pair<Integer, Integer> boldPair = boldPairList.get(i);
                        result.setSpan(new CustomTypefaceSpan(boldFontName, boldTypeface), boldPair.first - 2 - i * 4, boldPair.second - 1 - i * 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    textView.setText(result);
                }
            }
        }
        typeArray.recycle();
    }

    public static Typeface getTypeface(Context context, String name) {
        if (!typeFaceCache.containsKey(name)) {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + name);
            typeFaceCache.put(name, typeface);
        }

        return typeFaceCache.get(name);
    }

    public static void clearCache() {
        if (typeFaceCache != null) {
            typeFaceCache.clear();
        }
    }

    public static String[] getFontNames(Context context) throws IOException {
        return context.getAssets().list("fonts");
    }
}

