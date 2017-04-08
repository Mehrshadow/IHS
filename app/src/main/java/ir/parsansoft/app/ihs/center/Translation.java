package ir.parsansoft.app.ihs.center;

import android.util.SparseArray;

public class Translation {
    public SparseArray<String> sentences;

    public Translation() {
        sentences = new SparseArray<String>();
        changeLanguage(G.setting.languageID);
    }

    public void changeLanguage(int languageID) {

        String stringArray[] = null;
        switch (languageID) {
            case 1:// en
                stringArray = G.context.getResources().getStringArray(R.array.translation_En);
                break;
            case 2:// fa
                stringArray = G.context.getResources().getStringArray(R.array.translation_Fa);
                break;
            case 3:// ar
                stringArray = G.context.getResources().getStringArray(R.array.translation_Ar);
                break;
            case 4:// tu
                stringArray = G.context.getResources().getStringArray(R.array.translation_Tr);
                break;
            default:
                break;
        }

        if (stringArray != null) {
            sentences.clear();
            for (int i = 0; i < stringArray.length; i++) {
                try {
                    String[] tmp = stringArray[i].split("\\|");
                    sentences.put(Integer.parseInt(tmp[0]), tmp[1]);
                } catch (Exception e) {
                    G.printStackTrace(e);
                }
            }
        }
    }

    public String getSentence(int id) {
        String ans;
        try {
            ans = sentences.get(id);
            if (ans == null)
                return "NS:" + id;
            else
                return ans;
        } catch (Exception e) {
            G.log("Sentence " + id + " not found in language :" + G.setting.languageID);
            return "NS:" + id;
        }
    }
}
