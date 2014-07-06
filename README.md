## Basic font and textStyle
Android supports a easy way to show some text. That is a `TextView` widget. Also, text can be bold with a `android:textStyle` property.

```
<TextView
    android:text="@string/hello_world"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<TextView
    android:text="@string/hello_world"
    android:textStyle="bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
![image](https://cloud.githubusercontent.com/assets/4193335/3488845/7953c9f0-0506-11e4-837b-779c9ba5ffa4.png)

## Custom font
Basic font is useful, but it is very common to use a custom font for the app. The easiest way to set a custom font is to utilize `setTypeface` method.

```
textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/MontereyFLF.ttf"));
```

Even though this one is easy, I don't think that it is a good way since this makes hard to separate between a view(layout) part and a code part. Another way is to extend the original TextView widget and set a typeface based on properties. Since there are no font-related properties, we should define custom properties. I've defined this on `res/values/attrs.xml`

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="ViewWithFont">
        <attr name="font" format="string" />
        <attr name="fontBold" format="string" />
    </declare-styleable>
</resources>
```

```
public class TextViewWithFont extends TextView {

    public TextViewWithFont(Context context) {
        super(context);
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        FontUtils.setFont(this, attrs, R.styleable.ViewWithFont, R.styleable.ViewWithFont_font, R.styleable.ViewWithFont_fontBold);
    }
}
```
Now, we can set a custom font inside a layout xml file.

```
<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world"
    app:font="@string/font_normal"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world"
    app:font="@string/font_normal"
    android:textStyle="bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
![image](https://cloud.githubusercontent.com/assets/4193335/3488846/85deec36-0506-11e4-9f49-4d183e14d830.png)

You may notice that I use the `android:textStyle` property to make text bold. This can make a text bold, but that is different from an originally intended bold font by a font designer(i.e _MontereyFLF-Bold.ttf_). 

![image](https://cloud.githubusercontent.com/assets/4193335/3488890/0c65cf84-0509-11e4-9948-c1551f61892d.png)

Below `TextViewWithFont` widget uses **font_bold**(_MontereyFLF_Bold.ttf_) font.

```
<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world"
    app:font="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```        

The third row uses a bold font. As you can see, a bold font and a bold style with `android:textStyle` are different.

![image](https://cloud.githubusercontent.com/assets/4193335/3488849/8c249b04-0506-11e4-91e4-7f69c7cf4085.png)

### Combined bold text

Suppose that you want to make a text bold partially. How do you do it? 

#### HTML approach
Android TextView supports HTML based approach. In HTML, `<b>` tag is used to set partial bold text.

```
<string name="hello_world_html"><![CDATA[Hello <b>world</b>!]]></string>
```


```
<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world_html"
    app:font="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

Here is a screenshot after setting html based text.

![image](https://cloud.githubusercontent.com/assets/4193335/3488850/9090ab4c-0506-11e4-8beb-b1b151773c96.png)

Oops, this is not what you want. To utilize HTML based approach, we need one more step. Since `TextView` doesn't know this syntax, we have to convert it using a `Html.fromHtml` method.

```
public class CustomFontActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_font);
        ((TextView)findViewById(R.id.text_html)).setText(Html.fromHtml(getString(R.string.hello_world_html)));
    }
}
```

![image](https://cloud.githubusercontent.com/assets/4193335/3488851/98598d6c-0506-11e4-8bf9-abd56520db3f.png)


There are three cons. First, it is not easy to read `strings.xml` due to`![CDATA[`. Moreover, this also makes combine a layout and a code overly. Lastly, a bold text is not an originally intended bold font by a font designer(i.e _MontereyFLF-Bold.ttf_). 


#### LinearLayout approach
You may think simple workaround based on the Linear Layout. With two `TextView` widgets, it is possible to set a normal font and a bold font.

```
<LinearLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content">

    <su.customfont.widget.TextViewWithFont
        app:font="@string/font_normal"
        android:text="@string/hello"
        android:textSize="20dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

    <su.customfont.widget.TextViewWithFont
        android:layout_marginLeft="5dp"
        app:font="@string/font_bold"
        android:text="@string/world"
        android:textSize="20dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</LinearLayout>
```

The first row is based on HTML approach, and the second row is based on LinearLayout approach.

![image](https://cloud.githubusercontent.com/assets/4193335/3488852/a553db62-0506-11e4-800c-a29e7f279b9e.png)

Are we happy now? Maybe not yet. If the text is complicated(e.g. a **b** c **d**), we need to add a lot of `TextView` widgets. Moreover, we cannot support multi language since the bold part can be changed depending on the language.

#### Spannable approach

Spannable is an extended version of text. . Using API, we can specify an additional information(e.g. typeface) for a part of text. Before that, since a basic `TypefaceSpan` doesn't support a custom font, we need to extend that class and implement a custom `TypefaceSpan`. You can see the detail at `CustomTypefaceSpan` class.

```
public class CustomTypefaceSpan extends TypefaceSpan {
...
```

In addition, we need some way to mark partial texts to make it bold. HTML uses a `<b>` tags. Since android layout files are XML, it can be better to avoid angle brackets in order to mark it. I chose _markdown_ syntax, so `**` is used to do it.

```
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
```

```
<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world_md"
    app:font="@string/font_normal"
    app:fontBold="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

Third row is based on the spannable approach.

![image](https://cloud.githubusercontent.com/assets/4193335/3488853/ad696218-0506-11e4-904b-0be2264dbf79.png)

#### Beyond TextView
There are 3 `TextView` widgets:Button, EditText, TextView. Here are results with the spannable approach

```
<su.customfont.widget.TextViewWithFont
    android:text="@string/hello_world_md"
    app:font="@string/font_normal"
    app:fontBold="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<su.customfont.widget.EditTextWithFont
    android:text="@string/hello_world_md"
    app:font="@string/font_normal"
    app:fontBold="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />

<su.customfont.widget.ButtonWithFont
    android:text="@string/hello_world_md"
    app:font="@string/font_normal"
    app:fontBold="@string/font_bold"
    android:textSize="20dp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

![image](https://cloud.githubusercontent.com/assets/4193335/3488855/b6c70356-0506-11e4-8c84-1be3da47736b.png)
