package ir.parsansoft.app.ihs.center;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DialogClass {


    Context context;
    public OkListener okLis;
    public YesNoListener yesNoLis;
    public OkCancelInputListener okCancelInputLis;
    public CancelListener cancelLis;
    private TextView txtTitle;
    private TextView txtMessage;
    private EditText edtInput;
    private Dialog dialog;
    private String titleText;
    private String bodyText;
    public boolean cancelable = true;

    public DialogClass(final Context context) {
        super();
        this.context = G.currentActivity; //context;
        txtTitle = null;
        txtMessage = null;
        titleText = "";
        bodyText = "";
    }

    public void dissmiss() {

        if (!((Activity) context).isFinishing()) {
            //close dialog
            if (dialog != null)
                dialog.dismiss();
        }


        //G.toast("Dialog closed !");
    }

    public void setDialogTitle(String titleString) {
        titleText = titleString;
        if (txtTitle != null)
            G.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    txtTitle.setText(titleText);
                }
            });
    }

    public void setDialogText(String textString) {
        //        G.log("Dialog set text : " + textString);
        bodyText = textString;
        if (txtMessage != null)
            G.HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    txtMessage.setText(bodyText);
                }
            });
    }

    public interface OkListener {
        public void okClick();
    }

    public interface YesNoListener {
        public void yesClick();

        public void noClick();
    }

    public interface OkCancelInputListener {
        public boolean okClick(String input);

        public boolean cancelClick();
    }

    public interface CancelListener {
        public void cancelClick();
    }

    public void setOnOkListener(OkListener lis) {
        okLis = lis;
    }

    public void setOnYesNoListener(YesNoListener lis) {
        yesNoLis = lis;
    }

    public void setOnOkCancelInputListener(OkCancelInputListener lis) {
        okCancelInputLis = lis;
    }

    public void setCancelListener(CancelListener lis) {
        cancelLis = lis;
    }


    public void showOk() {
        showOk(titleText, bodyText);
    }

    public void showOk(String Title, String message) {
        titleText = Title;
        bodyText = message;
        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_ok, null, false);
                dialog.setCanceledOnTouchOutside(cancelable);
                dialog.setCancelable(cancelable);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
                txtTitle.setText(titleText);
                txtMessage.setText(bodyText);
                btnOk.setText(G.T.getSentence(101));
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (okLis != null)
                            okLis.okClick();
                    }
                });

                if (!((Activity) context).isFinishing()) {
                    try {
                        //show dialog
                        if (dialog != null)
                            dialog.show();
                    } catch (Exception e) {
                        G.printStackTrace(e);
                    }
                }
            }
        });
    }

    public void showYesNo() {
        showYesNo(titleText, bodyText);
    }

    public void showYesNo(String Title, String message) {
        titleText = Title;
        bodyText = message;
        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_yes_no, null, false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
                Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
                btnYes.setText(G.T.getSentence(109));
                btnNo.setText(G.T.getSentence(110));
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
                txtTitle.setText(titleText);
                txtMessage.setText(bodyText);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (yesNoLis != null) {
                            yesNoLis.yesClick();
                        }
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (yesNoLis != null) {
                            yesNoLis.noClick();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    public void showWaite() {
        showWaite(titleText, bodyText);
    }

    public void showWaite(String Title, String message) {
        titleText = Title;
        bodyText = message;
        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_waite, null, false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                ProgressBar prgWaite = (ProgressBar) dialog.findViewById(R.id.prgWaite);
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
                txtTitle.setText(titleText);
                txtMessage.setText(bodyText);
                dialog.show();
            }
        });
    }

    public void showWaitWithCancelButton(String Title, String message) {
        titleText = Title;
        bodyText = message;
        G.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.dialog_wait_with_cancel, null, false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                ProgressBar prgWaite = (ProgressBar) dialog.findViewById(R.id.prgWaite);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setText("" + G.T.getSentence(102));
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
                txtTitle.setText(titleText);
                txtMessage.setText(bodyText);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cancelLis != null)
                            cancelLis.cancelClick();
                    }
                });


                if (!((Activity) context).isFinishing()) {
                    //show dialog
                    try {
                        if (dialog != null)
                            dialog.show();
                    }
                    catch (Exception e){
                        G.printStackTrace(e);
                    }
                }
            }
        });
    }

    public void showOkCancelInput() {
        showOkCancelInput(titleText, bodyText);
    }

    public void showOkCancelInput(String Title, String message) {
        titleText = Title;
        bodyText = message;
        G.HANDLER.post(new Runnable() {

            @Override
            public void run() {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View view = G.inflater.inflate(R.layout.d_simple_edit_text, null, false);
                dialog.setCanceledOnTouchOutside(cancelable);
                dialog.setCancelable(cancelable);
                dialog.setContentView(view);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                Button btnOK = (Button) dialog.findViewById(R.id.btnPositive);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnNegative);
                btnOK.setText(G.T.getSentence(101));
                btnCancel.setText(G.T.getSentence(102));
                txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
                txtMessage = (TextView) dialog.findViewById(R.id.txtBody);
                edtInput = (EditText) dialog.findViewById(R.id.editText1);
                txtTitle.setText(titleText);
                txtMessage.setText(bodyText);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (okCancelInputLis != null) {
                            if (!okCancelInputLis.okClick(edtInput.getText().toString()))
                                dialog.dismiss();
                        } else
                            dialog.dismiss();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (okCancelInputLis != null) {
                            if (!okCancelInputLis.cancelClick())
                                dialog.dismiss();
                        } else
                            dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

}
