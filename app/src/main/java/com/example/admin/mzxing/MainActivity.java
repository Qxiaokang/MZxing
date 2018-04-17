package com.example.admin.mzxing;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.mzxing.bean.Const;
import com.example.admin.mzxing.utils.ComUtil;
import com.example.admin.mzxing.utils.LogUtils;
import com.example.admin.mzxing.utils.ZXingUtils;

public class MainActivity extends Activity implements OnClickListener {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    /**
     * 显示扫描结果
     */
    private TextView mTextView;
    /**
     * 显示扫描拍的图片
     */
    private ImageView mImageView;
    /**
     * 生成二维码的按钮
     */
    private Button btCreate;
    /**
     * 生成二维码的进度
     */
    private ProgressDialog pg;
    /**
     * 保存图片
     */
    private Bitmap bitmap = null;
    private Bundle bundle;
    private Intent intent;
    private String value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.result);
        mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
        //点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
        //扫描完了之后调到该界面
        Button mButton = (Button) findViewById(R.id.button1);
        btCreate = findViewById(R.id.bt_create);
        btCreate.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        mImageView.setClickable(false);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    bundle = data.getExtras();
                    //显示扫描到的内容
                    mTextView.setText("扫描结果：\n" + bundle.getString("result"));
                    //显示
                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                    mImageView.setClickable(true);
                    value=bundle.getString("result");
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                intent = new Intent();
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
                break;
            case R.id.bt_create:
                pg = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
                pg.setTitle("提示");
                pg.setIcon(android.R.drawable.ic_dialog_alert);
                pg.setCanceledOnTouchOutside(false);
                pg.setCancelable(false);
                pg.show();
                new CreateCodeThread().start();
                break;
            case R.id.qrcode_bitmap:
                intent =new Intent(MainActivity.this,HtmlActivity.class);
                bundle=new Bundle();
                bundle.putString("value",value);
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
                mImageView.setClickable(false);
                break;
            default:
                break;
        }
    }

    Handler resultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Const.RS_SUCCESS:
                    value = (String) msg.obj;
                    if (!value.isEmpty()) {
                        mTextView.setText(Html.fromHtml(value));
                    }
                    if (bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                    mImageView.setClickable(true);
                    break;
                case Const.RS_FAILD:
                    mTextView.setText("信息为空");
                    break;
                default:
                    if (pg != null && pg.isShowing()) {
                        pg.dismiss();
                    }
                    break;
            }
            if (pg != null && pg.isShowing()) {
                pg.dismiss();
            }
        }
    };

    class CreateCodeThread extends Thread {
        @Override
        public void run() {
            super.run();
            String htmlString = ComUtil.getInstance().getAssetsString(getApplicationContext(), "Form.html");
            LogUtils.d("htmlString:" + htmlString);
            if (!htmlString.isEmpty()) {
                bitmap = ZXingUtils.createQRImage(htmlString, 300, 300);
                bitmap = ZXingUtils.addLogo(bitmap, BitmapFactory.decodeResource(getResources(), R.drawable.app_icon));
                Message message = resultHandler.obtainMessage();
                message.what = Const.RS_SUCCESS;
                message.obj = htmlString;
                resultHandler.sendMessage(message);
            } else {
                resultHandler.sendEmptyMessage(Const.RS_FAILD);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pg!=null&&pg.isShowing()){
            pg.dismiss();
        }
    }
}
