package com.raoul.founditt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.media.ExifInterface;
import com.edmodo.cropper.CropImageView;
import com.soundcloud.android.crop.Crop;

public class ResultimageActivity extends Activity {
    private String fileuri;
    private String galleryplag;
    private ImageView priviewimage;
    private Uri outputUri;
    private String falg;
    private String classflag;
    Bitmap rotatedBitmap;
    Bitmap smallbitmap;
    Bitmap galllerybitamp;
    byte[] saveData;
    Bitmap capturebitmap;
    ImageView resulimageveiw;
    Matrix matrix;
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultimage);
       // priviewimage=(ImageView)findViewById(R.id.result_imageView);
        Intent datagetintent = getIntent();
        fileuri = datagetintent.getStringExtra("serchresult");
        galleryplag=datagetintent.getStringExtra("gallery");
        classflag=datagetintent.getStringExtra("class");
        falg = datagetintent.getStringExtra("changeflag");
        resulimageveiw=(ImageView)findViewById(R.id.result_imageView);
        matrix=new Matrix();
//        final CropImageView cropImageView = (CropImageView) findViewById(R.id.result_imageView);
//        cropImageView.setFixedAspectRatio(true);
//      //  cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
//        cropImageView.setGuidelines(1);
//        cropImageView.setAspectRatio(480,350);

        BitmapFactory.Options options = new BitmapFactory.Options();

        final Bitmap bitmap = BitmapFactory.decodeFile(fileuri,
                options);

        //bitmap.createScaledBitmap(bitmap, 100, 50, true);

        if(galleryplag.equals("true")){
            galllerybitamp=getSmallBitmap(fileuri,480,640,100,0);
            File pictureFile=new File(fileuri);
            if(pictureFile.exists()){
                pictureFile.delete();
                pictureFile=new File(fileuri);
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                galllerybitamp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
           // cropImageView.setImageBitmap(galllerybitamp);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            saveData = bos.toByteArray();

        }
        else {

            if(falg.equals("1"))
            {
//                rotatedBitmap=getResizedBitmap(bitmap,480,640,-90);
//                rotatedBitmap=getSmallBitmap(fileuri,240,320,100,-90);
                matrix.postRotate(-90);
                }
            else if(falg.equals("0"))
            {
//                rotatedBitmap=getSmallBitmap(fileuri,240,320,100,90);
//                rotatedBitmap=getResizedBitmap(bitmap,480,640,90);
                matrix.postRotate(90);
            }
            int h=0;
            int w=0;
//            if(bitmap.getWidth()>480&&bitmap.getHeight()>640){
//
//                 rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, 640, 480, matrix, true);
//            }
//            if(bitmap.getWidth()>480&&bitmap.getHeight()<640){
//
//                 rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, 640, bitmap.getHeight(), matrix, true);
//            }
//
//            if(bitmap.getWidth()<480&&bitmap.getHeight()>640){
//
//                 rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), 480, matrix, true);
//            }
//            if(bitmap.getWidth()<=480&&bitmap.getHeight()<=640){
//
//                 rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            }

            rotatedBitmap=Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

//            rotatedBitmap = Bitmap.createBitmap(capturebitmap , 0, 0, 120,160 , matrix, true);
            //Log.d("asdfasdfadfasdf", String.valueOf(bitmap.getWidth()+" ; "+bitmap.getHeight()));





            //cropImageView.setImageBitmap(rotatedBitmap);
            File pictureFile=new File(fileuri);
            if(pictureFile.exists()){
                pictureFile.delete();
                pictureFile=new File(fileuri);
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }
        startCropActivity();
        String pictureFilename=fileuri+".jpg";



        ImageButton saveButton = (ImageButton) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              //Bitmap  croppedImage = cropImageView.getCroppedImage();
//                File pictureFile=new File(fileuri);
//                if(pictureFile.exists()){
//                    pictureFile.delete();
//                    pictureFile=new File(fileuri);
//                }
//
//                try {
//                    FileOutputStream fos = new FileOutputStream(pictureFile);
//                   // croppedImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    Log.d("TAG", "File not found: " + e.getMessage());
//                } catch (IOException e) {
//                    Log.d("TAG", "Error accessing file: " + e.getMessage());
//                }
                String catagory=null;
                if(!(classflag ==null)){
//                    ParseFile image;
//                    ParseUser user=ParseUser.getCurrentUser();
//                    if(!(saveData ==null)){
//                    image = new ParseFile("photo.jpg", saveData);
//                    try {
//                        image.save();
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    user.put("userphoto",image);
//                        try {
//                            user.save();
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
////               else {
////                   user.put("userphoto",userphoto);
////              }
                    Intent intent;
                    intent = new Intent(ResultimageActivity.this, FavouritsActivity.class);
                   intent.putExtra("serchresult",fileuri);
                    startActivity(intent);
                }
                else {
                    Intent intent;
                    intent = new Intent(ResultimageActivity.this, ProductEnter.class);
                    intent.putExtra("serchresult",fileuri);
                    intent.putExtra("cotagoryresult",catagory);

                    startActivity(intent);
                }


            }
        });

//        ImageButton backButton = (ImageButton) findViewById(R.id.back_resultimageButton);
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent;
//                intent = new Intent(ResultimageActivity.this, HomeActivity.class);
//                startActivity(intent);
//
//            }
//        });

        ImageButton cancleButton = (ImageButton) findViewById(R.id.cancle_button);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                intent = new Intent(ResultimageActivity.this, CameraActivity.class);
                intent.putExtra("serchresult",fileuri);

                startActivity(intent);

            }
        });
    }
    private void startCropActivity() {
        outputUri = Uri.fromFile(new File(fileuri));
        new Crop(outputUri).output(outputUri).asSquare().start(this);
    }

    public void cropimage(){

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(Uri.parse(fileuri), "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            startactivity();
//            doSomethingWithCroppedImage(fileuri);
        }
    }
    public void doSomethingWithCroppedImage(String displayurl){


        BitmapFactory.Options options = new BitmapFactory.Options();

        final Bitmap bitmap = BitmapFactory.decodeFile(fileuri,
                options);
        resulimageveiw.setImageBitmap(bitmap);




    }

    public  void  startactivity(){

        smallbitmap=getSmallBitmap(fileuri,360,480,100,0);
        File pictureFile=new File(fileuri);
        if(pictureFile.exists()){
            pictureFile.delete();
            pictureFile=new File(fileuri);
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            smallbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }


        String catagory=null;
        if(!(classflag ==null)){

            Intent intent;
            intent = new Intent(ResultimageActivity.this, FavouritsActivity.class);
            intent.putExtra("serchresult",fileuri);
            startActivity(intent);
        }
        else {
            Intent intent;
            intent = new Intent(ResultimageActivity.this, ProductEnter.class);
            intent.putExtra("serchresult",fileuri);
            intent.putExtra("cotagoryresult",catagory);

            startActivity(intent);
        }

    }
    public static Bitmap getSmallBitmap(String filePath, int width, int height, int quality,int degree) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);
        // options.inSampleSize = 4;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }
//        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;

    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth,int degree) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(degree);

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


}
