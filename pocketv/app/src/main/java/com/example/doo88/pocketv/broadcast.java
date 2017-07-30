package com.example.doo88.pocketv;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class broadcast extends Activity implements SurfaceHolder.Callback {

    private android.hardware.Camera cam;
    private MediaRecorder mediaRecorder;
    private SurfaceView sv;
    private SurfaceHolder sh;
    private String id, roomnumber;
    private ImageButton imgbtn;
    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broadcast);

        //방번호받기
        Intent intent = getIntent();
        roomnumber = intent.getStringExtra("roomnumber");
        Log.i("방넘버",""+roomnumber);
        recording=false;

        //방송시작 버튼
        imgbtn = (ImageButton) findViewById(R.id.imageButton);
        setting();
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recording==true) {
                    //방송종료 다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(broadcast.this);
                    builder.setTitle("방송종료")        // 제목 설정
                            .setMessage("방송을 종료할까요?")        // 메세지 설정
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                // 확인 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Toast.makeText(broadcast.this, "방송정지", Toast.LENGTH_LONG).show();
                                    mediaRecorder.stop();
                                    mediaRecorder.release();
                                    cam.lock();
                                    recording = false;
                                    finish();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                // 취소 버튼 클릭시 설정
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog dialog = builder.create();    // 알림창 객체 생성
                    dialog.show();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(broadcast.this, "방송시작", Toast.LENGTH_LONG).show();
                            try {
                                mediaRecorder = new MediaRecorder();
                                cam.unlock();
                                mediaRecorder.setCamera(cam);
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                                mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                                mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                                mediaRecorder.setOrientationHint(90);
                                mediaRecorder.setOutputFile("/sdcard/" + roomnumber + ".mp4");
                                mediaRecorder.setPreviewDisplay(sh.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording = true;
                                Log.i("레코딩",""+recording);
                                new NDK().run_ffmpeg(roomnumber);
                            } catch (final Exception ex) {
                                ex.printStackTrace();
                                mediaRecorder.release();
                                return;

                                // Log.i("---","Exception in thread");
                            }
                        }
                    });
                }


            }
        });


    }

    private void setting() {
        cam = android.hardware.Camera.open();
        cam.setDisplayOrientation(90);
        sv = (SurfaceView) findViewById(R.id.surfaceView);
        sh = sv.getHolder();
        sh.addCallback(this);
        sh.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (cam == null) {
                cam.setPreviewDisplay(holder);
                cam.startPreview();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera(cam);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void refreshCamera(android.hardware.Camera camera) {
        if (sh.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            cam.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        setCamera(camera);
        try {
            cam.setPreviewDisplay(sh);
            cam.startPreview();
        } catch (Exception e) {
        }
    }

    public void setCamera(android.hardware.Camera camera) {
        //method to set a camera instance
        cam = camera;
    }

}
