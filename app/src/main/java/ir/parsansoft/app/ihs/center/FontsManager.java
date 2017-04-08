package ir.parsansoft.app.ihs.center;
import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class FontsManager {
    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        }
        catch (NoSuchFieldException e) {
            G.printStackTrace(e);
        }
        catch (IllegalAccessException e) {
            G.printStackTrace(e);
        }
    }

    public static void changeFont() {
//        if (G.CURRENT_LANGUAGE_ID == 1 && G.CURRENT_LANGUAGE_ID == 4) {
//        			FontsManager.setDefaultFont(G.context, "DEFAULT", "fonts/new.TTF");
//        			FontsManager.setDefaultFont(G.context, "MONOSPACE","fonts/en_title.ttf");
//        			FontsManager.setDefaultFont(G.context, "SERIF", "fonts/en_list.ttf");
//        			FontsManager.setDefaultFont(G.context, "SANS_SERIF","fonts/en_button.ttf");
//        }
//        if (G.CURRENT_LANGUAGE_ID == 2 && G.CURRENT_LANGUAGE_ID == 3) {
//        	FontsOverride.setDefaultFont(G.context, "DEFAULT", "fonts/ir_matn.ttf");
//        	FontsOverride.setDefaultFont(G.context, "MONOSPACE","fonts/ir_title.ttf");
//        	FontsOverride.setDefaultFont(G.context, "SERIF", "fonts/ir_list.ttf");
//        	FontsOverride.setDefaultFont(G.context, "SANS_SERIF","fonts/ir_button.ttf");
//        }
    	
    	//FontsManager.setDefaultFont(G.context, "DEFAULT", "fonts/new.TTF");
    	
    	
    	
    	switch (G.setting.languageID) {
		case 1:
		case 4:
			FontsManager.setDefaultFont(G.context, "MONOSPACE","fonts/gothi.ttf");
			break;
		case 2:
		case 3:
			FontsManager.setDefaultFont(G.context, "MONOSPACE","fonts/sl.ttf");
			break;
		}
//    	FontsManager.setDefaultFont(G.context, "DEFAULT", "fonts/new.TTF");
//		FontsManager.setDefaultFont(G.context, "MONOSPACE","fonts/sabatica.otf");
//		FontsManager.setDefaultFont(G.context, "SERIF", "fonts/new.TTF");
//		FontsManager.setDefaultFont(G.context, "SANS_SERIF","fonts/new.TTF");
    }
}
