package ir.parsansoft.app.ihs.center;

import android.media.AudioManager;
import android.media.SoundPool;

public class PlaySound {
    static int       hover;
    static SoundPool sp;

    public PlaySound() {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        hover = sp.load(G.context, R.raw.hover_sound, 1);
    }
    public static void Hover() {
        sp.play(hover, 1, 1, 0, 0, 1);
    }

}
