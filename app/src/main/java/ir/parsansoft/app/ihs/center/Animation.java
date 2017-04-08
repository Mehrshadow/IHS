package ir.parsansoft.app.ihs.center;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ListView;

public class Animation {

    public static enum Animation_Types {
        FADE,
        FADE_SLIDE_LEFTRIGHT_LEFT,
        FADE_SLIDE_LEFTRIGHT_RIGHT,
        ZOOM,
        ZOOMBACK
        //FADE_SLIDE_UPDOWN_UP,
        //FADE_SLIDE_UPDOWN_DOWN,
        //SLIDE_LEFTRIGHT_LEFT,
        //SLIDE_LEFTRIGHT_RIGHT,
        //SLIDE_UPDOWN_UP,
        //SLIDE_UPDOWN_DOWN
    }

    public static void doAnimation(Animation_Types animation) {

        switch (animation) {
            case FADE:
                G.currentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case FADE_SLIDE_LEFTRIGHT_LEFT:
                G.currentActivity.overridePendingTransition(R.anim.fade_slide_in_left, R.anim.fade_slide_out_right);
                break;

            case FADE_SLIDE_LEFTRIGHT_RIGHT:
                G.currentActivity.overridePendingTransition(R.anim.fade_slide_in_right, R.anim.fade_slide_out_left);
                break;
            case ZOOM:
                G.currentActivity.overridePendingTransition(R.anim.zoom_in, R.anim.zoom);
                break;
            case ZOOMBACK:
                G.currentActivity.overridePendingTransition(R.anim.zoom_out, R.anim.zoom);
                break;
        }
    }


    public static void setListAnimation(Context context, ListView listview) {
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_griditem_animation);
        listview.setLayoutAnimation(controller);
    }
    public static void setListAnimation(Context context, GridView gridview) {
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_griditem_animation);
        gridview.setLayoutAnimation(controller);
    }
}
