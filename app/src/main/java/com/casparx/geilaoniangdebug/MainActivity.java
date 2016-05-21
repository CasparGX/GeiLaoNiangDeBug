package com.casparx.geilaoniangdebug;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Button;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.btn_choose_pic)
    Button btnChoosePic;
    @Bind(R.id.btn_choose_time)
    Button btnChooseTime;
    @Bind(R.id.btn_choose_date)
    Button btnChooseDate;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.img_photo)
    ImageView imgPhoto;

    private Bitmap photo;
    private PopupWindow popupWindowWait;
    private Bitmap BITMAP;

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param bm
     * @return
     */
    public static Bitmap readBitMap(Bitmap bm) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

    }

    /* onClick */
    @OnClick(R.id.btn_choose_pic)
    void onClickBtnChoosePic() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    @OnClick(R.id.btn_choose_date)
    void onClickBtnChooseDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.btn_choose_time)
    void onClickBtnChooseTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @OnClick(R.id.btn_confirm)
    void onClickBtnConfirm(View view) {
        Bitmap bitmap = drawTextToBitmap(this, tvDate.getText().toString() + "     ", tvTime.getText().toString(), " 岳阳");
        imgPhoto.setImageBitmap(readBitMap(bitmap));
        BITMAP = bitmap;
        saveBitmap(bitmap);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        view.getFirstDayOfWeek();
        String date = null;
        String month = null;
        String day = null;
        monthOfYear += 1;
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        } else {
            month = monthOfYear + "";
        }
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = dayOfMonth + "";
        }
        Log.i("month", month);
        date = year + "-" + month + "-" + day + " " + getWeek(year, monthOfYear - 1, dayOfMonth);
        tvDate.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = null;
        String minuteString = "";
        String hourString = "";
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = minute+"";
        }
        if (hourOfDay<10){
            hourString = "0"+hourOfDay;
        } else {
            hourString = hourOfDay +"";
        }
        time = hourString + ":" + minuteString;
        Log.i("time",time);
        tvTime.setText(time);
    }

    private String getWeek(int year, int monthOfYear, int dayOfMonth) {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//也可将此值当参数传进来
        Date curDate = new Date();
        curDate.setYear(year);
        curDate.setMonth(monthOfYear);
        curDate.setDate(dayOfMonth);
        String pTime = format.format(curDate);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 2:
                Week += "天";
                break;
            case 3:
                Week += "一";
                break;
            case 4:
                Week += "二";
                break;
            case 5:
                Week += "三";
                break;
            case 6:
                Week += "四";
                break;
            case 7:
                Week += "五";
                break;
            case 1:
                Week += "六";
                break;
            default:
                break;
        }
        Log.i("getWeek", c.get(Calendar.DAY_OF_WEEK) + " " + Week);
        return Week;
    }

    /**
     * 添加文字到图片，类似水印文字。
     *
     * @param gContext
     * @param date
     * @return
     */
    public Bitmap drawTextToBitmap(Context gContext, String date, String time, String local) {
        Resources resources = gContext.getResources();
        Bitmap bitmap = this.photo;
        float scale = resources.getDisplayMetrics().density;
        //scale = bitmap.getDensity();
        float fontSize = bitmap.getWidth()/1080;
        if (fontSize<1){
            fontSize = 1;
        }

        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint datePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint icPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint localPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        datePaint.setColor(Color.rgb(255, 255, 255));
        timePaint.setColor(Color.rgb(255, 255, 255));
        localPaint.setColor(Color.rgb(255, 255, 255));
        // text size in pixels
        datePaint.setTextSize((int) (14 *fontSize * 3));
        icPaint.setTextSize((int) (14 *fontSize * 3));
        localPaint.setTextSize((int) (14 *fontSize * 3));
        timePaint.setTextSize((int) (14 *fontSize * 10));
        // text shadow
        //paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect dateRect = new Rect();
        Rect timeRect = new Rect();
        Rect icRect = new Rect();
        Rect localRect = new Rect();
        datePaint.getTextBounds(date, 0, date.length(), dateRect);
        timePaint.getTextBounds(time, 0, time.length(), timeRect);
        icPaint.getTextBounds("0", 0, "0".length(), icRect);
        localPaint.getTextBounds(local, 0, local.length(), localRect);


        int x1 = (bitmap.getWidth() - dateRect.width() - icRect.width() - localRect.width()) / 2;
        int y1 = (bitmap.getHeight() - 2 * dateRect.height());

        int x2 = (bitmap.getWidth() - timeRect.width()) / 2;
        int y2 = (y1 - 2 * dateRect.height());

        int x3 = x1 + dateRect.width();
        int y3 = y1 - (localRect.height() * 4 / 3);

        int x4 = x3 + icRect.width();
        int y4 = y1;

        canvas.drawText(date, x1, y1, datePaint);
        canvas.drawText(time, x2, y2, timePaint);

        Bitmap icBitmap = BitmapFactory.decodeResource(resources, R.drawable.dingwei);
        int w = icBitmap.getWidth();
        int h = icBitmap.getHeight();
        float scaleWidth = ((float) localRect.height()) / w;
        float scaleHeight = ((float) localRect.height() * 3 / 2) / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        icBitmap = Bitmap.createBitmap(icBitmap, 0, 0, w, h, matrix, true);
        //icBitmap.setHeight(localRect.height());
        //icBitmap.setWidth(localRect.width());
        canvas.drawBitmap(icBitmap, x3, y3, null);
        canvas.drawText(local, x4, y4, localPaint);

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            System.out.println("requestCode" + requestCode);
            if (requestCode == 2) {
                Uri uri = data.getData();
                System.out.println(uri.getPath());

                ContentResolver cr = this.getContentResolver();

                try {
                    photo = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //String picturePath = getImageFilePath(uri);
                //图片压缩工具类
                //CompImageUtil compImage = new CompImageUtil();
                //photo = compImage.getimage(picturePath, 1500f, 1500f, 500);
                imgPhoto.setImageBitmap(readBitMap(photo));

            }
        }

    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bitmap) {
        String photoDir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Camera/";
        File f = new File(photoDir + DateFormat.format("yyyy-MM-dd kk.mm.ss", System.currentTimeMillis()).toString() + ".jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(MainActivity.this, new String[]{f + ""}, null, null);
            Snackbar.make(btnConfirm, "图片生成成功，到相册查看", Snackbar.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(btnConfirm, "图片生成失败", Snackbar.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
                saveBitmap(BITMAP);
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPopupWindowWait(View view) {
        //自定义布局
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popupwindow_wait, null);
        popupWindowWait = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindowWait.setTouchable(true);

        popupWindowWait.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindowWait.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_default_black));

        // 设置好参数之后再show
        popupWindowWait.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private String getImageFilePath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}
