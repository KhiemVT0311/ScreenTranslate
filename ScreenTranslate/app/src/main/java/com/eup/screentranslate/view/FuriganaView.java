package com.eup.screentranslate.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.eup.screentranslate.R;

import java.util.ArrayList;
import java.util.List;

public class FuriganaView extends AppCompatTextView {

    private static final String FURIGANA_START = "{";
    private static final String FURIGANA_MIDDLE = ";";
    private static final String FURIGANA_END = "}";
    private static final String ITALIC_START = "<i>";
    private static final String ITALIC_END = "</i>";
    private static final String BOLD_START = "<b>";
    private static final String BOLD_END = "</b>";
    private static final String UNDERLINE_START = "<u>";
    private static final String UNDERLINE_END = "</u>";
    private static final String LINE_BREAK = "<br>";
    private static final String BREAK_REGEX = "(<br>|\n)"; //remove line breaks from kanji and furigana
    private static final String TAG_REGEX = "(<i>|</i>|<b>|</b>|<u>|</u>)"; //remove tags from the text and furigana kanji combinations

    private int mTextAlignment = TEXT_ALIGNMENT_TEXT_START;
    private int mMaxLines = -1;
    private int mSideMargins = 0;

    private float mMaxLineWidth;
    private float mTextSize;
    private float mFuriganaSize;
    private float mRomajiSize;
    private float mLineSpacing;
    private float mLineSpacingWithTextNormal;

    private TextPaint mNormalPaint;
    private TextPaint mFuriganaPaint;
    private TextPaint mRomajiPaint;
    private List<Line> mLines;
    private String mText;

    private int romajiColor;
    private float wordSpacing = 4.0f;

    public FuriganaView(Context context) {
        super(context);
        initialize(context, null);
    }

    public FuriganaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public FuriganaView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TextPaint textPaint = getPaint();
        mTextSize = textPaint.getTextSize();

        mNormalPaint = new TextPaint(textPaint);
        mFuriganaPaint = new TextPaint(textPaint);
        mRomajiPaint = new TextPaint(textPaint);
        mFuriganaSize = mTextSize / 2.0f;
        mRomajiSize = mTextSize / 2.0f;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FuriganaView);

            mText = a.getString(R.styleable.FuriganaView_android_text);
            mMaxLines = a.getInt(R.styleable.FuriganaView_android_maxLines, -1);

            mTextAlignment = a.getInt(R.styleable.FuriganaView_android_textAlignment, 2);
//            mFuriganaSize = a.getDimension(R.styleable.FuriganaView_furiganaSize, mFuriganaSize);
//            mRomajiSize = a.getDimension(R.styleable.FuriganaView_romajiSize, mRomajiSize);
            mLineSpacing = a.getDimension(R.styleable.FuriganaView_android_lineSpacingExtra, mFuriganaSize / 2.0f);
            mLineSpacingWithTextNormal = a.getDimension(R.styleable.FuriganaView_lineSpacingWithTextNormal, 0);

            romajiColor = a.getColor(R.styleable.FuriganaView_romajiColor, Color.parseColor("#727272"));
            wordSpacing = a.getDimension(R.styleable.FuriganaView_wordSpacing, 4.0f);

            float marginLeftRight = a.getDimension(R.styleable.FuriganaView_android_layout_marginLeft, 0) + a.getDimension(R.styleable.FuriganaView_android_layout_marginRight, 0);
            float marginEndStart = a.getDimension(R.styleable.FuriganaView_android_layout_marginEnd, 0) + a.getDimension(R.styleable.FuriganaView_android_layout_marginStart, 0);
            mSideMargins = (int) Math.ceil(Math.max(marginEndStart, marginLeftRight));

            a.recycle();
        }

        mFuriganaPaint.setTextSize(mFuriganaSize);
        mRomajiPaint.setTextSize(mRomajiSize);

        setOnLongClickListener(view -> {
            showPopupMenuCopy(view);
            return true;
        });
    }


    private void showPopupMenuCopy(View view) {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.popup_copy_menu, null);
        PopupWindow popup = new PopupWindow(layout, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        // Show anchored to button
        layout.findViewById(R.id.popup_copy).setOnClickListener(v -> {
            StringBuilder text = new StringBuilder();
            if(mLines != null){
                for (Line line: mLines){
                    text.append(line.getText());
                }
            }
            String label = getContext().getString(R.string.copy);
            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(label, text.toString()));
            Toast.makeText(getContext(), getContext().getString(R.string.copy_done), Toast.LENGTH_SHORT).show();
            popup.dismiss();
        });

        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(view);

    }

    public void setText(String text) {
        mText = text;
        mLines = null;
        requestLayout();
    }

    public void setTextAlignment(int textAlignment) {
        mTextAlignment = textAlignment;
        invalidate();
    }

    public void setTextSize(float size) {
        mNormalPaint.setTextSize(size);
        requestLayout();
    }

    public void setFuriganaSize(float size) {
        mFuriganaPaint.setTextSize(size);
        requestLayout();
    }

    public void setLineSpacing(float spacing) {
        mLineSpacing = spacing;
        requestLayout();
    }

    public String getText() {
        return mText;
    }

    public String getOrgText(){
        StringBuilder text = new StringBuilder();

        if(mLines != null){
            for (Line line: mLines){
                text.append(line.getText());
            }
        }
        return text.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLines != null && !mLines.isEmpty()) {
            float y = mLines.get(0).offset();
            for (int i = 0; i < mLines.size(); i++) {
                y += mLines.get(i).onDraw(canvas, y);
                if (i + 1 < mLines.size())
                    y += mLines.get(i + 1).offset();
                if (mMaxLines != -1 && i == mMaxLines - 1)
                    break;
            }
        } else
            super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY && widthSize != 0)
            width = widthSize;
        else if (widthMode == MeasureSpec.AT_MOST)
            width = measureWidth(widthSize - mSideMargins);
        else
            width = measureWidth(-1);

        mMaxLineWidth = (float) width;
        if (width > 0)
            handleText();

        int maxHeight = 0;
        if (mLines != null) {
            float totalHeight = 0.0f;
            for (Line line : mLines) {
                totalHeight += line.height();
            }
            maxHeight = (int) Math.ceil(totalHeight);
        }

        if (mMaxLines != -1 && mLines != null && mMaxLines < mLines.size()) {
            float totalHeight = 0.0f;
            for (int i = 0; i < maxHeight; i++) {
                totalHeight += mLines.get(i).height();
            }
            maxHeight = (int) Math.ceil(totalHeight);
        }

        if (heightMode == MeasureSpec.EXACTLY)
            height = heightSize;
        else if (heightMode == MeasureSpec.AT_MOST)
            height = Math.min(maxHeight, heightSize);
        else
            height = maxHeight;

        if (heightMode != MeasureSpec.UNSPECIFIED && maxHeight > heightSize)
            height |= MEASURED_STATE_TOO_SMALL;

        if (height < getMinHeight())
            height = getMinHeight();

        setMeasuredDimension(width, height);

    }

    //measures the longest line in the text
    private int measureWidth(int width) {
        if (mText == null || mText.isEmpty())
            return 0;

        String text = mText.replaceAll(TAG_REGEX, "");
        String normal = "";
        float maxLength = 0.0f;
        float length = 0.0f;

        while (!text.isEmpty()) {
            if (text.indexOf(LINE_BREAK) == 0 || text.indexOf("\n") == 0) {
                length += mNormalPaint.measureText(normal);
                maxLength = Math.max(length, maxLength);
                length = 0.0f;
                text = text.substring(1);
                normal = "";
            } else if (text.indexOf(FURIGANA_START) == 0) {
                if (!text.contains(FURIGANA_MIDDLE) || !text.contains(FURIGANA_END)) {
                    text = text.substring(1);
                    continue;
                }
                int middle = text.indexOf(FURIGANA_MIDDLE);
                int end = text.indexOf(FURIGANA_END);
                if (end < middle) {
                    text = text.substring(1);
                    continue;
                }
                float kanji = mNormalPaint.measureText(text.substring(1, middle));
                String kanaText = text.substring(middle + 1, end);
                String romaji = null;

                if (kanaText.contains(FURIGANA_MIDDLE)) {
                    middle = kanaText.indexOf(FURIGANA_MIDDLE);
                    end = kanaText.length();
                    romaji = kanaText.substring(middle + 1, end);
                    kanaText = kanaText.substring(0, middle);
                }
                float kana = mFuriganaPaint.measureText(kanaText);

                text = text.substring(text.indexOf(FURIGANA_END) + 1);
                if (romaji != null)
                    length += Math.max(Math.max(kanji, kana), mRomajiPaint.measureText(romaji));
                else
                    length += Math.max(kanji, kana);

            } else {
                normal += text.substring(0, 1);
                text = text.substring(1);
            }
        }

        length += mNormalPaint.measureText(normal);
        maxLength = Math.max(length, maxLength);

        int result = (int) Math.ceil(maxLength);
        if (width < 0)
            return result;
        return Math.min(result, width);
    }

    //breaks the text into lines shorter than the maximum length
    private void handleText() {
        if (mText == null || mText.isEmpty())
            return;

        String text = mText;
        mLines = new ArrayList<>();
        boolean isBold = false;
        boolean isItalic = false;
        boolean isUnderlined = false;
        Line line = new Line();
        NormalTextHolder normalHandler = null;

        while (!text.isEmpty()) {
            if (text.indexOf(BOLD_START) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isBold = true;
                text = text.substring(3);

            } else if (text.indexOf(BOLD_END) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isBold = false;
                text = text.substring(4);

            } else if (text.indexOf(ITALIC_START) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isItalic = true;
                text = text.substring(3);

            } else if (text.indexOf(ITALIC_END) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isItalic = false;
                text = text.substring(4);

            } else if (text.indexOf(UNDERLINE_START) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isUnderlined = true;
                text = text.substring(3);

            } else if (text.indexOf(UNDERLINE_END) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                isUnderlined = false;
                text = text.substring(4);

            } else if (text.indexOf(LINE_BREAK) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                text = text.substring(4);
                mLines.add(line);
                line = new Line();

            } else if (text.indexOf("\n") == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                text = text.substring(1);
                mLines.add(line);
                line = new Line();

            } else if (text.indexOf(FURIGANA_START) == 0) {
                if (normalHandler != null)
                    line.add(normalHandler.endText());
                if (!text.contains(FURIGANA_MIDDLE) || !text.contains(FURIGANA_END)) {
                    text = text.substring(1);
                    continue;
                }
                int middle = text.indexOf(FURIGANA_MIDDLE);
                int end = text.indexOf(FURIGANA_END);
                if (end < middle) {
                    text = text.substring(1);
                    continue;
                }

                String kanji = text.substring(1, middle).replaceAll(BREAK_REGEX, "").replaceAll(TAG_REGEX, ""); //remove all tags and line breaks
                String kana = text.substring(middle + 1, end).replaceAll(BREAK_REGEX, "").replaceAll(TAG_REGEX, "");
                String romaji = null;

                if (kana.contains(FURIGANA_MIDDLE)) {
                    middle = kana.indexOf(FURIGANA_MIDDLE);
                    end = kana.length();
                    romaji = kana.substring(middle + 1, end);
                    kana = kana.substring(0, middle);
                }
                text = text.substring(text.indexOf(FURIGANA_END) + 1);

                PairedText pair = new PairedText(kanji, kana, romaji, isBold, isItalic, isUnderlined);
                if ((pair.width() + line.width()) > mMaxLineWidth) {
                    if (!line.isEmpty())
                        mLines.add(line);
                    line = new Line();
                }
                line.add(pair);

            } else {
                if (normalHandler == null)
                    normalHandler = new NormalTextHolder();

                if (normalHandler.test(text.substring(0, 1), line)) {
                    line.add(normalHandler.endText());
                    mLines.add(line);
                    line = new Line();
                }

                normalHandler.expand(text.substring(0, 1), isBold, isItalic, isUnderlined);
                text = text.substring(1);
            }
        }
        if (normalHandler != null)
            line.add(normalHandler.endText());

        if (!line.isEmpty())
            mLines.add(line);
    }

    //sets how much space should be in the start of the line
    private float handleNewline(float width) {
        float remainder = mMaxLineWidth - width;
        if (remainder > 0)
            switch (mTextAlignment) {
                case TEXT_ALIGNMENT_CENTER:
                    return remainder / 2.0f;
                case TEXT_ALIGNMENT_TEXT_END:
                    return remainder;
            }
        return 0.0f;
    }

    private class NormalTextHolder {
        private String normal;
        private boolean bold, italic, underlined;

        NormalTextHolder() {
            normal = "";
            bold = false;
            italic = false;
            underlined = false;
        }

        boolean test(String test, Line line) {
            float width = mNormalPaint.measureText(normal + test) + line.width();
            if (test.equals("。")) //reduces lines with just a period
                width -= mTextSize * 0.7f;
            return width > mMaxLineWidth;
        }

        void expand(String text, boolean isBold, boolean isItalic, boolean isUnderlined) {
            normal += text;
            bold = isBold;
            italic = isItalic;
            underlined = isUnderlined;
        }

        PairedText endText() {
            PairedText pair = new PairedText(normal, null, null, bold, italic, underlined);
            normal = "";
            return pair;
        }
    }

    private class Line {
        private float width;
        private final List<PairedText> pairs;
        private boolean isFurigana = false, isRomaji = false;

        Line() {
            width = 0.0f;
            pairs = new ArrayList<>();
        }

        boolean isEmpty() {
            return pairs.isEmpty();
        }

        float width() {
            return width;
        }

        void add(PairedText pairText) {
            pairs.add(pairText);
            width += pairText.width();
            if (!isFurigana)
                isFurigana = pairText.furiganaText != null;
            if (!isRomaji)
                isRomaji = pairText.romajiText != null;
        }

        float onDraw(Canvas canvas, float y) {
            float x = handleNewline(width);
            for (int i = 0; i < pairs.size(); i++) {
                pairs.get(i).onDraw(canvas, x, y);
                x += pairs.get(i).width();
            }
            if (isRomaji)
                return mRomajiSize + mLineSpacingWithTextNormal;
            return 0;
        }

        float offset() {// tai vi lay normal lam chuan (y theo normal)
            if (isFurigana)
                return mTextSize + mFuriganaSize + mLineSpacing + mLineSpacingWithTextNormal / 2;
            return mTextSize + mLineSpacing;
        }

        float height() {
            float height = mTextSize + mRomajiSize + mFuriganaSize + mLineSpacing + mLineSpacingWithTextNormal + mLineSpacingWithTextNormal / 2;

            if (isFurigana) {
                if(!isRomaji)
                    height = mTextSize + mFuriganaSize + mLineSpacing + mLineSpacingWithTextNormal / 2;
            } else {
                if (isRomaji) {
                    height = mTextSize + mRomajiSize + mLineSpacing + mLineSpacingWithTextNormal;
                } else {
                    height = mTextSize + mLineSpacing;
                }
            }
            return height;
        }

        boolean isFurigana() {
            return isFurigana;
        }

        String getText(){
            StringBuilder text = new StringBuilder();
            for (PairedText pairedText: pairs){
                text.append(pairedText.normalText);
            }
            return text.toString();
        }
    }

    private class PairedText {
        private final String normalText;
        private String furiganaText;
        private String romajiText;
        private float width, normalWidth, furiganaWidth, romajiWidth, offset, _offset, x, y;
        private TextPaint normalPaint, furiganaPaint, romajiPaint;


        PairedText(String normal, String furigana, String romaji, boolean isBold, boolean isItalic, boolean isUnderlined) {
            normalText = normal;
            if (!TextUtils.isEmpty(furigana))
                furiganaText = furigana;
            if (!TextUtils.isEmpty(romaji))
                romajiText = romaji;

            setPaint(isBold, isItalic, isUnderlined);
        }

        //set paint and calculate spacing between characters
        private void setPaint(boolean bold, boolean italic, boolean underlined) {
            normalPaint = new TextPaint(mNormalPaint);
            normalPaint.setFakeBoldText(bold);
            normalPaint.setUnderlineText(underlined);
            if (italic)
                normalPaint.setTextSkewX(-0.35f);

            normalWidth = normalPaint.measureText(normalText);

            if (furiganaText != null) {
                furiganaPaint = new TextPaint(mFuriganaPaint);
                furiganaPaint.setFakeBoldText(bold);
                if (italic)
                    furiganaPaint.setTextSkewX(-0.35f);

                furiganaWidth = furiganaPaint.measureText(furiganaText);

                if (romajiText != null) {
                    romajiPaint = new TextPaint(mRomajiPaint);
                    romajiPaint.setFakeBoldText(bold);
                    if (italic)
                        romajiPaint.setTextSkewX(-0.35f);

                    romajiWidth = romajiPaint.measureText(romajiText);

                    float max = Math.max(Math.max(normalWidth, furiganaWidth), romajiWidth);

                    if (max == normalWidth) {
                        offset = (max - furiganaWidth) / (furiganaText.length() + 1); // furigana
                        _offset = (max - romajiWidth) / 2; // romaji
                    } else if (max == romajiWidth) {
                        offset = (max - furiganaWidth) / (furiganaText.length() + 1); // furigana
                        _offset = (max - normalWidth) / (normalText.length() + 1); // normal
                    } else {
                        offset = (max - romajiWidth) / 2; // romaji
                        _offset = (max - normalWidth) / (normalText.length() + 1); // normal
                    }

                    width = max;
                } else {
                    if (normalWidth < furiganaWidth) { // offset cua normal
                        offset = (furiganaWidth - normalWidth) / (normalText.length() + 1);
                    } else {
                        offset = (normalWidth - furiganaWidth) / (furiganaText.length() + 1);
                    }

                    width = Math.max(normalWidth, furiganaWidth);
                }
            } else if (romajiText != null) {
                romajiPaint = new TextPaint(mRomajiPaint);
                romajiPaint.setFakeBoldText(bold);
                if (italic)
                    romajiPaint.setTextSkewX(-0.35f);

                romajiWidth = romajiPaint.measureText(romajiText);

                if (normalWidth < romajiWidth) { // offset cua normal
                    offset = (romajiWidth - normalWidth) / (normalText.length() + 1);
                } else {
                    offset = (normalWidth - romajiWidth) / 2;
                }

                width = Math.max(normalWidth, romajiWidth);
            } else {
                width = normalWidth;
            }
        }

        float width() {
            if (normalText != null && normalText.length() > 1)
                return width + wordSpacing;
            return width + wordSpacing / 2;
        }

        void onDraw(Canvas canvas, float x, float y) {
            y = y - mLineSpacing;
            this.x = x;
            this.y = y;
            if (furiganaText == null) {
                normalPaint.setColor(getCurrentTextColor());
                if (romajiText == null) { // normal
                    canvas.drawText(normalText, 0, normalText.length(), x, y, normalPaint);
                } else { // romaji & normal
                    romajiPaint.setColor(romajiColor);

                    if (normalWidth < romajiWidth) {
                        float offsetX = x + offset;
                        for (int i = 0; i < normalText.length(); i++) {
                            canvas.drawText(normalText, i, i + 1, offsetX, y, normalPaint);
                            offsetX += normalPaint.measureText(normalText.substring(i, i + 1)) + offset;
                        }

                        canvas.drawText(romajiText, 0, romajiText.length(), x, y + mRomajiSize + mLineSpacingWithTextNormal, romajiPaint);
                    } else {
                        canvas.drawText(romajiText, 0, romajiText.length(), x + offset, y + mRomajiSize + mLineSpacingWithTextNormal, romajiPaint);

                        canvas.drawText(normalText, 0, normalText.length(), x, y, normalPaint);
                    }
                }
            } else {
                normalPaint.setColor(getCurrentTextColor());
                furiganaPaint.setColor(getCurrentTextColor());

                if (romajiText == null) {
                    //draw kanji and kana and apply spacing
                    float offsetX = x + offset;
                    if (normalWidth < furiganaWidth) {
                        for (int i = 0; i < normalText.length(); i++) {
                            canvas.drawText(normalText, i, i + 1, offsetX, y, normalPaint);
                            offsetX += normalPaint.measureText(normalText.substring(i, i + 1)) + offset;
                        }

                        canvas.drawText(furiganaText, 0, furiganaText.length(), x, y - mTextSize - mLineSpacingWithTextNormal / 2, furiganaPaint);
                    } else {
                        for (int i = 0; i < furiganaText.length(); i++) {
                            canvas.drawText(furiganaText, i, i + 1, offsetX, y - mTextSize - mLineSpacingWithTextNormal / 2, furiganaPaint);
                            offsetX += furiganaPaint.measureText(furiganaText.substring(i, i + 1)) + offset;
                        }

                        canvas.drawText(normalText, 0, normalText.length(), x, y, normalPaint);
                    }
                } else { // romaji && normal && furigana
                    romajiPaint.setColor(romajiColor);
                    if (width == normalWidth) {
//                        offset = (max - furiganaWidth) / (furiganaText.length() + 1); // furigana
//                        _offset = (max - romajiWidth) / 2; // romaji

                        canvas.drawText(romajiText, 0, romajiText.length(), x + _offset, y + mRomajiSize + mLineSpacingWithTextNormal, romajiPaint);


                        float offsetX = x + offset;
                        for (int i = 0; i < furiganaText.length(); i++) {
                            canvas.drawText(furiganaText, i, i + 1, offsetX, y - mTextSize - mLineSpacingWithTextNormal / 2, furiganaPaint);
                            offsetX += furiganaPaint.measureText(furiganaText.substring(i, i + 1)) + offset;
                        }

                        canvas.drawText(normalText, 0, normalText.length(), x, y, normalPaint);

                    } else if (width == romajiWidth) {
//                        offset = (max - furiganaWidth) / (furiganaText.length() + 1); // furigana
//                        _offset = (max - normalWidth) / (normalText.length() + 1); // normal

                        float offsetX = x + _offset;
                        for (int i = 0; i < normalText.length(); i++) {
                            canvas.drawText(normalText, i, i + 1, offsetX, y, normalPaint);
                            offsetX += normalPaint.measureText(normalText.substring(i, i + 1)) + _offset;
                        }

                        offsetX = x + offset;
                        for (int i = 0; i < furiganaText.length(); i++) {
                            canvas.drawText(furiganaText, i, i + 1, offsetX, y - mTextSize - mLineSpacingWithTextNormal / 2, furiganaPaint);
                            offsetX += furiganaPaint.measureText(furiganaText.substring(i, i + 1)) + offset;
                        }

                        canvas.drawText(romajiText, 0, romajiText.length(), x, y + mRomajiSize + mLineSpacingWithTextNormal, romajiPaint);

                    } else {
//                        offset = (max - romajiWidth) / 2; // romaji
//                        _offset = (max - normalWidth) / (normalText.length() + 1); // normal

                        canvas.drawText(romajiText, 0, romajiText.length(), x + offset, y + mRomajiSize + mLineSpacingWithTextNormal, romajiPaint);


                        float offsetX = x + _offset;
                        for (int i = 0; i < normalText.length(); i++) {
                            canvas.drawText(normalText, i, i + 1, offsetX, y, normalPaint);
                            offsetX += normalPaint.measureText(normalText.substring(i, i + 1)) + _offset;
                        }

                        canvas.drawText(furiganaText, 0, furiganaText.length(), x, y - mTextSize - mLineSpacingWithTextNormal / 2, furiganaPaint);
                    }
                }
            }
        }
    }
}

