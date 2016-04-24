package com.casparx.geilaoniangdebug;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /* onClick */
    @OnClick(R.id.btn_choose_pic)
    void onClickBtnChoosePic() {

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
    void onClickBtnConfirm() {
        Bitmap bitmap = drawTextToBitmap(this, R.drawable.test_img, "1234567890");
        imgPhoto.setImageBitmap(bitmap);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        view.getFirstDayOfWeek();
        String date = null;
        date = year + "-" + monthOfYear + "-" + dayOfMonth + " " + getWeek(year, monthOfYear, dayOfMonth);
        tvDate.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = null;
        time = hourOfDay + ":" + minute;
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
     * @param gResId
     * @param gText
     * @return
     */
    public static Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (14 * scale * 5));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        //      int x = (bitmap.getWidth() - bounds.width()) / 2;
        //      int y = (bitmap.getHeight() + bounds.height()) / 2;
        //draw  text  to the bottom
        int x = (bitmap.getWidth() - bounds.width()) / 10 * 9;
        int y = (bitmap.getHeight() + bounds.height()) / 10 * 9;
        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

}
