package ir.parsansoft.app.ihs.center.components;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoScrollableTextView extends TextView {

	  public AutoScrollableTextView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	    setEllipsize(TruncateAt.MARQUEE);
	    setMarqueeRepeatLimit(-1);
	    setSingleLine();
	    setHorizontallyScrolling(true);
	    }

	    public AutoScrollableTextView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    setEllipsize(TruncateAt.MARQUEE);
	    setMarqueeRepeatLimit(-1);
	    setSingleLine();
	    setHorizontallyScrolling(true);
	    }

	    public AutoScrollableTextView(Context context) {
	        super(context);   
	    setEllipsize(TruncateAt.MARQUEE);
	    setMarqueeRepeatLimit(-1);
	    setSingleLine();
	    setHorizontallyScrolling(true);

	    }
	    @Override
	    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
	        if(focused)
	            super.onFocusChanged(focused, direction, previouslyFocusedRect);
	    }

	    @Override
	    public void onWindowFocusChanged(boolean focused) {
	        if(focused)
	            super.onWindowFocusChanged(focused);
	    }


	    @Override
	    public boolean isFocused() {
	        return true;
	    }
	}
