package com.raoul.founditt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ProductEnter extends Activity {
    ImageView priviewimage;
    String fileuri;
    String catagory;
    String location;
    String tagling;
    String retailerstr;
    String gallerystr;
    Integer DATE_DIALOG_ID=999;
    int year;
    int month;
    int day;
    TextView pxpiredate;
    TextView catagoryedittext;
    TextView locationdittext;
    TextView title_mark;
    EditText taglineedittext;
    EditText retailer;
    ImageButton back_dealimagebut;
    ImageButton locationimagebut;
    ImageButton categoryimagebut;
    private Photo photo;
    private Uri fileUri;
    private ParseFile image;
    private ProgressDialog progressDialog;
    private Date expire;
    boolean facebookflag=true;
    boolean twitterflag=true;
    int mYear;
    int mMonth;
    int mDay;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_enter);
        photo = new Photo();
        expire=new Date();
        font = Typeface.createFromAsset(getAssets(), "Questrial-Regular.ttf");
        taglineedittext=(EditText)findViewById(R.id.tagline_deal_editText);
        title_mark=(TextView)findViewById(R.id.product_enter_title_textView);
        title_mark.setTypeface(font);
        TextView tagtitle=(TextView)findViewById(R.id.tagline_title_textView);
        tagtitle.setTypeface(font);
        taglineedittext.setTypeface(font);
        retailer=(EditText)findViewById(R.id.ratailer_deal_editText);
        retailer.setTypeface(font);
        ParseUser courrent_user=ParseUser.getCurrentUser();
        //retailer.setText((String)courrent_user.get("name"));
        TextView retailertitle=(TextView)findViewById(R.id.ratailer_title_textView);
        retailertitle.setTypeface(font);
        TextView categorytitle=(TextView)findViewById(R.id.category_title_textView);
        categorytitle.setTypeface(font);
        TextView locationtitle=(TextView)findViewById(R.id.location_tltle_textView);
        locationtitle.setTypeface(font);
        TextView sharetitle=(TextView)findViewById(R.id.share_title_textView);
        sharetitle.setTypeface(font);
        priviewimage=(ImageView)findViewById(R.id.product_deal_imageView);
        back_dealimagebut=(ImageButton)findViewById(R.id.back_deal_imageButton);
        locationimagebut=(ImageButton)findViewById(R.id.location_imageButton);
        categoryimagebut=(ImageButton)findViewById(R.id.category_imageButton);
        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");
        gallerystr = datagetintent.getStringExtra("gallery");
        catagory=datagetintent.getStringExtra("cotagoryresult");
        location=datagetintent.getStringExtra("location");
        tagling=datagetintent.getStringExtra("tagline");
        retailerstr=datagetintent.getStringExtra("retailer");
        pxpiredate=(TextView)findViewById(R.id.expirer_deal_textView);
        pxpiredate.setTypeface(font);
        final TextView expiretitle=(TextView)findViewById(R.id.expiry_title_textView);
        expiretitle.setTypeface(font);
        ParseUser currentuser=ParseUser.getCurrentUser();
        currentuser.saveInBackground();
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        String formattedDate = df.format(c.getTime());
        Date date=new Date();

        int datestr=c.get(Calendar.DAY_OF_MONTH);
        int month=c.get(Calendar.MONTH)+1;
        int yearstr=c.get(Calendar.YEAR);


        String current_date = String.format("%02d/%02d/%d", datestr, month, yearstr);
        pxpiredate.setText(current_date);
        catagoryedittext=(TextView)findViewById(R.id.category_deal_textView);
        catagoryedittext.setTypeface(font);

        if(!(catagory ==null)){
            catagoryedittext.setText(catagory);
        }
        if(!(tagling ==null)){
            taglineedittext.setText(tagling);
        }
        if(!(retailerstr ==null)){
            retailer.setText(retailerstr);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        final Bitmap bitmap = BitmapFactory.decodeFile(fileuri,
                options);

        //
        locationdittext=(TextView)findViewById(R.id.location_deal_textView);
        locationdittext.setTypeface(font);
        if(!(location ==null)){
            locationdittext.setText(location);
        }
        Matrix matrix = new Matrix();
//        if(falg.equals("1"))
        matrix.postRotate(90);
//        else if(falg.equals("0"))
//        {matrix.postRotate(90);}
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap .getWidth(), bitmap .getHeight(), matrix, true);

        priviewimage.setImageBitmap(bitmap);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        final byte[] saveData = bos.toByteArray();
        ImageButton saveButton = (ImageButton) findViewById(R.id.back_deal_imageButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, CameraActivity.class);
               // intent.putExtra("serchresult",fileuri);
                startActivity(intent);

            }
        });

        categoryimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, CategoryActivity.class);
                intent.putExtra("serchresult",fileuri);
                intent.putExtra("tagline",taglineedittext.getText().toString());
                intent.putExtra("retailer",retailer.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        locationimagebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, LocationActivity.class);
                intent.putExtra("serchresult",fileuri);
                intent.putExtra("cotagoryresult",catagory);
                intent.putExtra("tagline",taglineedittext.getText().toString());
                intent.putExtra("retailer",retailer.getText().toString());
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });

        pxpiredate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(ProductEnter.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                pxpiredate.setText(String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();



            }
        });

        final ImageButton fcsharebut=(ImageButton)findViewById(R.id.facebook_share_imagebut);
        fcsharebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername());
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileuri)));
//                intent.setType("image/*");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                startActivity(Intent.createChooser(intent, null));
                facebookflag=!facebookflag;
                if(facebookflag==false){
                    fcsharebut.setImageResource(R.drawable.facebook);
                    try {
                        Intent tweetIntent = new Intent(Intent.ACTION_SEND);

                        String filename = "twitter_image.jpg";
                        File imageFile = new File(Environment.getExternalStorageDirectory(), filename);

                        tweetIntent.putExtra(Intent.EXTRA_TEXT, ParseUser.getCurrentUser().getUsername());
                        tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileuri)));
                        tweetIntent.setType("image/*");
                        PackageManager pm = ProductEnter.this.getPackageManager();
                        List<ResolveInfo> lract =pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        boolean resolved = false;
                        for (ResolveInfo ri : lract) {
                            if (ri.activityInfo.name.contains("facebook")) {
                                tweetIntent.setClassName(ri.activityInfo.packageName,
                                        ri.activityInfo.name);
                                resolved = true;
                                break;
                            }
                        }

                        startActivity(resolved ?
                                tweetIntent :
                                Intent.createChooser(tweetIntent, "Choose one"));
                    } catch (final ActivityNotFoundException e) {
                        Toast.makeText(ProductEnter.this, "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    fcsharebut.setImageResource(R.drawable.facebook_press);
                }




            }
        });


        final ImageButton twsharebut=(ImageButton)findViewById(R.id.twitershare_imagebut);
        twsharebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, ParseUser.getCurrentUser().getUsername());
//
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileuri)));
//                intent.setType("image/*");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                startActivity(Intent.createChooser(intent, null));



                twitterflag=!twitterflag;
                if(twitterflag==false){
                    twsharebut.setImageResource(R.drawable.twitter);
                    try {
                        Intent tweetIntent = new Intent(Intent.ACTION_SEND);

                        String filename = "twitter_image.jpg";
                        File imageFile = new File(Environment.getExternalStorageDirectory(), filename);

                        tweetIntent.putExtra(Intent.EXTRA_TEXT, ParseUser.getCurrentUser().getUsername());
                        tweetIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(fileuri)));
                        tweetIntent.setType("image/*");
                        PackageManager pm = ProductEnter.this.getPackageManager();
                        List<ResolveInfo> lract = pm.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
                        boolean resolved = false;
                        for (ResolveInfo ri : lract) {
                            if (ri.activityInfo.name.contains("twitter")) {
                                tweetIntent.setClassName(ri.activityInfo.packageName,
                                        ri.activityInfo.name);
                                resolved = true;
                                break;
                            }
                        }

                        startActivity(resolved ?
                                tweetIntent :
                                Intent.createChooser(tweetIntent, "Choose one"));
                    } catch (final ActivityNotFoundException e) {
                        Toast.makeText(ProductEnter.this, "You don't seem to have twitter installed on this device", Toast.LENGTH_SHORT).show();
                    }
                }

                else {

                    twsharebut.setImageResource(R.drawable.twitter_press);

                }




            }
        });


        ImageButton savebut=(ImageButton)findViewById(R.id.save_pro_imageButton);
        savebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

//                String formattedDate = df.format(pxpiredate.getText().toString());
                Date expiredate = null;
                try {
                    expiredate = df.parse(pxpiredate.getText().toString());
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                Date today=new Date(System.currentTimeMillis());
                if (today.compareTo(expiredate)>0||today.compareTo(expiredate)==0){

                    Toast.makeText(ProductEnter.this,"Expired date is over,Please select other!",Toast.LENGTH_LONG).show();
                }
                else if(taglineedittext.getText().length()==0){
                    taglineedittext.setError("Please enter tagline");
                }
               else if(retailer.getText().length()==0){
                   retailer.setError("Please enter retailer");
                }

                else if (catagoryedittext.getText().length()==0){

                   catagoryedittext.setError("Please select category");

                }
                else if (locationdittext.getText().length()==0){
                   locationdittext.setError("Please select location");

                }
                else if (pxpiredate.getText().length()==0){
                   pxpiredate.setError("Please picker expire date");

                }

                else {




                    Calendar c = Calendar.getInstance();





                    image = new ParseFile("photo.jpg", saveData);
                    image.saveInBackground();
                    setProgressDialog( ProgressDialog.show(ProductEnter.this, "",
                            "uploading  data ...", true, true) );



                    // When the user clicks "Save," upload the picture to Parse
                    // Associate the picture with the current user
                    photo.setUser(ParseUser.getCurrentUser());

                    // Add the image
                    photo.setImage( image );
//                    Log.d("adfadfadsasdfadfadsfdafsßfaasswddaasaæ",ParseUser.getCurrentUser().getUsername());
                    // Add the thumbnail
                    photo.setLike("12");
                    photo.setCategory(catagory);
                    photo.setExpiry( expiredate);
                    photo.setLocation(locationdittext.getText().toString());
                    photo.setTagline(taglineedittext.getText().toString());
                    photo.setRetail(retailer.getText().toString());
                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);
                    photo.setACL(acl);
                    photo.put("expflag","false");
                    // Save the picture and return
                    photo.saveInBackground(new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            getProgressDialog().dismiss();

                            Intent intent=new Intent(ProductEnter.this,HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            if (e == null) {

                            } else {
                                try {
                                    showDialog(e.getMessage());
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                    });


                }


            }
        });




//        back_dealimagebut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ProductEnter.this,ResultimageActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_in, R.anim.right_out);
//            }
//        });
        ImageButton home_Button = (ImageButton) findViewById(R.id.home_information_imageButton);

        home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, HomeActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

            }
        });

        ImageButton carmeraButton = (ImageButton) findViewById(R.id.camera_information_imageButton);

        carmeraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, CameraActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton alertButton = (ImageButton) findViewById(R.id.alert_information_imagebutton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this,AlarmActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
        ImageButton profileButton = (ImageButton) findViewById(R.id.profile_information_imageButton);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ProductEnter.this, FavouritsActivity.class);


                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });



    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 999:
                // set date picker as current date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                return new DatePickerDialog(this, datePickerListener, year, month,day);
        }
        return null;
    }

    int ID_DATE;
    int fromYear, fromMonth, fromDay, toYear, toMonth, toDay;
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
                              int selectedDay) {
            // TODO Auto-generated method stub
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // set selected date into textview

            fromYear = year;
            fromMonth = selectedMonth;
            fromDay = selectedDay;
            pxpiredate.setText(new StringBuilder().append(year)
                    .append("-").append(month + 1).append("-").append(day)
                    .append(" "));

        }

    };

    public ProgressDialog getProgressDialog(){
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog pd){
        progressDialog = pd;
    }
    public void showDialog(String msg) throws Exception
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductEnter.this);

        builder.setMessage(msg);

//        builder.setPositiveButton("Ring", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
////                Intent callIntent = new Intent(Intent.ACTION_DIAL);// (Intent.ACTION_CALL);
////
////                callIntent.setData(Uri.parse("tel:" + phone));
////
////                startActivity(callIntent);
//
//                dialog.dismiss();
//            }
//        });

        builder.setNegativeButton("close", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        builder.show();
    }

}
