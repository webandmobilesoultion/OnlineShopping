package com.raoul.founditt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    public static final String TAG = CameraActivity.class.getSimpleName();
    private boolean isStartflag=true;
    private Camera mCamera;
    public String shareuri=null;
    LayoutInflater controlInflater = null;
    ImageButton buttonClick;
    ImageButton backbut;
    ImageButton folderimagebut;
    ImageButton cameraflashbut;
    ImageButton camerarotationbut;
    ImageButton backimagbutton;
    private boolean flag=true;
    // We need the phone orientation to correctly draw the overlay:
    private int mOrientation;
    private int mOrientationCompensation;
    private OrientationEventListener mOrientationEventListener;
    public Uri uriTarget;
    // Let's keep track of the display rotation and orientation also:
    private int mDisplayRotation;
    private int mDisplayOrientation;
    private  String classflag;
    private ImageButton betweenbtn;
    // Holds the Face Detection result:
    private Camera.Face[] mFaces;
    private String changeflag;

    // The surface view for the camera data
    private SurfaceView mView;

    // Draw rectangles and other fancy stuff:
    private FaceOverlayView mFaceView;
    public boolean startflag=true;
    private MediaPlayer mp;
    private static int SELECT_PICTURE = 1;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new SurfaceView(this);

        setContentView(mView);
        // Now create the OverlayView:
        mFaceView = new FaceOverlayView(this);
        addContentView(mFaceView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // Create and Start the OrientationListener:
        mOrientationEventListener = new SimpleOrientationEventListener(this);
        mOrientationEventListener.enable();
        //catuerimage();

        controlInflater = LayoutInflater.from(getBaseContext());
        View viewControl = controlInflater.inflate(R.layout.activity_camera, null);
        LayoutParams layoutParamsControl
                = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        this.addContentView(viewControl, layoutParamsControl);
        Intent datagetintent = getIntent();
        classflag= datagetintent.getStringExtra("class");
        camerarotationbut=(ImageButton)findViewById(R.id.cameratrans_imageButton);
//      backimagbutton=(ImageButton)findViewById(R.id.back_cameraimageButton);
//
//
//
//        backimagbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(CameraActivity.this,HomeActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.left_in, R.anim.right_out);
//
//            }
//        });
        camerarotationbut.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(camId==Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                    camerarotationbut.setImageResource(R.drawable.camera_rotate_front);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);


                    mCamera.setDisplayOrientation(90);
                    SurfaceHolder holder = mView.getHolder();
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    mCamera.startFaceDetection();
                    mCamera.startPreview();
                    camId=Camera.CameraInfo.CAMERA_FACING_FRONT;
                    cameraflashbut.setEnabled(false);
                }
                else
                {
                    camerarotationbut.setImageResource(R.drawable.camera_rotate);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);


                    mCamera.setDisplayOrientation(90);
                    SurfaceHolder holder = mView.getHolder();
                    try {
                        mCamera.setPreviewDisplay(holder);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    mCamera.startFaceDetection();
                    mCamera.startPreview();
                    camId=Camera.CameraInfo.CAMERA_FACING_BACK;
                    cameraflashbut.setEnabled(true);
                }


            }});
        cameraflashbut=(ImageButton)findViewById(R.id.camera_flashimageButton);
        cameraflashbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                if(!flag){
                    cameraflashbut.setImageResource(R.drawable.flssh);

                    Camera.Parameters p = mCamera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

                    mCamera.setParameters(p);

                }
                else {

                    cameraflashbut.setImageResource(R.drawable.flssh_off);

                    Camera.Parameters p = mCamera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(p);


                }


            }
        });

        buttonClick = (ImageButton) findViewById(R.id.take_photoimageButton);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (mCamera.getParameters().getFocusMode().equals(
                        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    mCamera.takePicture(myShutterCallback,
                            myPictureCallback_RAW, myPictureCallback_JPG);
                } else {
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(final boolean success, final Camera camera) {
                            mCamera.takePicture(myShutterCallback,
                                    myPictureCallback_RAW, myPictureCallback_JPG);
                        }
                    });
                }



            }
        });
        folderimagebut = (ImageButton) findViewById(R.id.folder_imageButton);
        folderimagebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);

            }
        });
//        LinearLayout layoutBackground = (LinearLayout)findViewById(R.id.background);
//        layoutBackground.setOnClickListener(new LinearLayout.OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//
//
//                mCamera.autoFocus(myAutoFocusCallback);
//                mCamera.takePicture(myShutterCallback,
//                        myPictureCallback_RAW, myPictureCallback_JPG);
//            }});

        // Showing Alert Message

    }
    public void shootSound()
    {
        AudioManager meng = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

        if (volume != 0)
        {
            if (mp== null)
                mp = MediaPlayer.create(getBaseContext(), Uri.parse("file:///system/media/audio/ui/Auto_focus.ogg"));
            if (mp != null)
                mp.start();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SurfaceHolder holder = mView.getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onPause() {
        mOrientationEventListener.disable();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mOrientationEventListener.enable();
        super.onResume();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open(camId);
        Camera.Parameters p = mCamera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//        p.setPictureSize(2448,3264);
//        p.getSupportedPictureSizes()
       // p.setPreviewSize(320,480);
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        mCamera.setParameters(p);
//        mCamera.autoFocus(myAutoFocusCallback);
        mCamera.startFaceDetection();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (Exception e) {
            Log.e(TAG, "Could not preview the image.", e);
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        // We have no surface, return immediately:
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }
        // Get the supported preview sizes:
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        // And set them:
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(parameters);
        // Now set the display orientation for the camera. Can we do this differently?
        mDisplayRotation = Util.getDisplayRotation(CameraActivity.this);
        mDisplayOrientation = Util.getDisplayOrientation(mDisplayRotation, 0);
        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }

        // Finally start the camera preview again:
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        mCamera.setFaceDetectionListener(null);
        mCamera.stopFaceDetection();
        mCamera.stopPreview();
       // mCamera.autoFocus(myAutoFocusCallback);
        mCamera.release();



    }


    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {


        }};

    ShutterCallback myShutterCallback = new ShutterCallback(){

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub
            //mCamera.autoFocus(myAutoFocusCallback);

        }};

    PictureCallback myPictureCallback_RAW = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
            // TODO Auto-generated method stub

        }};
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    PictureCallback myPictureCallback_JPG = new PictureCallback(){

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            FileOutputStream outStream = null;
            try {
                // write to local sandbox file system
                // outStream =
                // CameraDemo.this.openFileOutput(String.format("%d.jpg",
                // System.currentTimeMillis()), 0);
                // Or write to sdcard
                File dirFile = new File("/sdcard/Founditt/");
                if (!dirFile.exists())
                    dirFile.mkdir();
                File tempfile=new File(String.format(
                        "/sdcard/Founditt/%d.jpg", System.currentTimeMillis()));
                shareuri=tempfile.getPath();
                outStream = new FileOutputStream(tempfile);
                outStream.write(data);
                outStream.close();
                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            Log.d(TAG, "onPictureTaken - jpeg");
            Intent intent;
            intent = new Intent(CameraActivity.this, ResultimageActivity.class);
            String flag=Integer.toString(camId);
            intent.putExtra("changeflag",flag);
            intent.putExtra("serchresult",shareuri);
            intent.putExtra("gallery","false");
            intent.putExtra("class",classflag);

            Log.d("show",shareuri);
            startActivity(intent);

            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
            String picturePath =getPath(selectedImage);

            Log.d("show",picturePath);
//            cursor.close();
            Intent intent;
            intent = new Intent(CameraActivity.this, ResultimageActivity.class);
            intent.putExtra("serchresult",picturePath);
            intent.putExtra("gallery","true");
            intent.putExtra("class",classflag);


            startActivity(intent);



        }


    }
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
    /**
     * We need to react on OrientationEvents to rotate the screen and
     * update the views.
     */
    private class SimpleOrientationEventListener extends OrientationEventListener {

        public SimpleOrientationEventListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {

            if (orientation == ORIENTATION_UNKNOWN) return;
            mOrientation = Util.roundOrientation(orientation, mOrientation);

            int orientationCompensation = mOrientation
                    + Util.getDisplayRotation(CameraActivity.this);
            if (mOrientationCompensation != orientationCompensation) {
                mOrientationCompensation = orientationCompensation;
                mFaceView.setOrientation(mOrientationCompensation);
            }
        }
    }
}


