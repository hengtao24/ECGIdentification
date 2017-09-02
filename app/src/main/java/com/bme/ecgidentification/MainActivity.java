package com.bme.ecgidentification;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // view
    Toolbar toolbar;
//    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    SurfaceView surface;
    SurfaceHolder holder;
    TextView tv_heartRate;
    Button store;
    Button draw;
    Button clear;
    Button start;

    // canvas
    private int WIDTH;
    private int HEIGHT;
    private Paint paint = null;

    // var
    private String buffer = "";
    private int HeartRate;
    private int spanW = WIDTH / 14;
    private int spanH = HEIGHT / 9;
    private int cx = spanW + 10; // 实时x的坐标

    // socket
    private static ServerSocket serverSocket = null;

    // boolean
    private boolean isStore = false;
    private boolean isPaint = true;
    private boolean isDraw = false;

    // store
    private int[] storeData;

    // msg
    private static final int DRAW_MESSAGE = 0x01;

    // context
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_heartRate = (TextView) findViewById(R.id.tv_heartrate);
        draw = (Button) findViewById(R.id.bt_draw);
        store = (Button) findViewById(R.id.bt_store);
        start = (Button) findViewById(R.id.bt_start);
        clear = (Button) findViewById(R.id.bt_clear);
        surface = (SurfaceView) findViewById(R.id.show);

        mContext = this;
        initCanvas();
        startConnect();
        initCallback();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
       // drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initCanvas(){

        WIDTH = DisplayUtil.getScreenWidth(this);
        HEIGHT = DisplayUtil.getScreenHeight(this)-200;

        spanW = WIDTH / 14;
        spanH = HEIGHT / 9;

        holder = surface.getHolder();
        holder.setFixedSize(WIDTH + 50, HEIGHT + 50); // 设置画布大小，要比实际的绘图位置大一点

        paint = new Paint();
        paint.setColor(Color.BLACK); // 画波形的颜色是绿色的，区别于坐标轴黑色
        paint.setStrokeWidth(3);

    }

    private void startConnect(){
        new Thread() {
            public void run() {
                Bundle bundle = new Bundle();
                bundle.clear();
                OutputStream output;
                String str = "通信成功";
                try {
                    serverSocket = new ServerSocket(5001);
                    while (true) {
                        Message msg = new Message();
                        msg.what = DRAW_MESSAGE;
                        try{
                            Thread.sleep(1);
                        } //让线程睡眠1秒再启动
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                        try {
                            Socket socket = serverSocket.accept();
                            buffer = "";
                            BufferedReader bff = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String line = null;
                            while ((line = bff.readLine()) != null) {
                                buffer = line + buffer;
                            }
                            int []temp = CharacterUtil.String2Int(buffer);
                            if(isStore){
                                storeData = CharacterUtil.String2Int(buffer);
                            }
                            Draw(temp);
                            mHandler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    MESLog.logE("thread","catch");
                    e1.printStackTrace();
                }
            };
        }.start();

    }

    private void initCallback(){

        start.setOnClickListener(new MyButtonStopListener());

        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                drawBack(holder);
                // 如果没有这句话，会使得在开始运行程序，整个屏幕没有白色的画布出现
                // 直到按下按键，因为在按键中有对drawBack(SurfaceHolder holder)的调用
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isStore) {
                    isStore = true;
                    store.setText("  reshow ");
                } else {
                    cx = spanW + 10;
                    isStore = false;
                    isPaint = false;
                    drawBack(holder);
                    store.setText("  store  ");
                    Draw(storeData);
                }
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!isDraw) {
                    isDraw = true;
                    isPaint = true;
                    start.setText("  stop   ");
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isDraw = false;
                isPaint = false;
                isStore = false;
                cx = spanW + 10;
                start.setText("  stop  ");
                drawBack(holder);
            }
        });
    }


    public void Draw(int[] data) {
        // TODO Auto-generated method stub
        int p = 0;
        HeartRate = data[0];
        if (cx == spanW + 10) {
            drawBack(holder);
        }
        if (isPaint) {
            for (; p< data.length - 1;	p++) {
                int a, b;
                if (data[p] >= data[p + 1]) {
                    a = data[p];
                    b = data[p + 1];
                } else {
                    a = data[p+ 1];
                    b = data[p];
                }
                Canvas canvas = holder.lockCanvas(new Rect(cx, 358 - 4*a, cx + 1,
                        362 -  4*b));
                // y = cy
                // - (int) (50 * Math.sin((cx - 10 - spanW) * 2
                // * Math.PI / 150));
                paint.setColor(Color.BLACK);// 设置波形颜色
                canvas.drawLine(cx, 360 - 4*data[p], cx + 1, 360 - 4* data[p + 1],
                        paint);
                holder.unlockCanvasAndPost(canvas); // 解锁画布
                cx = cx + 1;

                if (cx >= WIDTH) {
                    cx = spanW + 10; // 如果画满则从头开始画
                    drawBack(holder); // 画满之后，清除原来的图像，从新开始
                }
            }
        }
    }

    // 设置画布背景色，设置XY轴的位置
    public void drawBack(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas(); // 锁定画布
        // 绘制白色背景
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStrokeWidth(2);
        // 绘制坐标轴
        for (int i = 0; i <= 8; i++) {
            if (i == 4) {
                xuxian(canvas, p, 15 + spanW, 10 + i * spanH, 15 + 14 * spanW + 3, 10 + i * spanH);
            }
            xuxian(canvas, p, 10 + spanW, 10 + i * spanH, 10 + 14 * spanW + 3, 10 + i * spanH);
        }
        for (int i = 1; i <= 14; i++) {
            if (i == 1) {
                xuxian(canvas, p, 10 + i * spanW, 5, 10 + i * spanW, 5 + 8 * spanH + 3);
            }
            xuxian(canvas, p, 10 + i * spanW, 10, 10 + i * spanW, 10 + 8 * spanH + 3);
        }
        p.setTextSize(22);
        for (int i = 0; i <= 8; i++) {
            canvas.drawText((4 - i) + "", 10 + spanW - 30, 10 + i * spanH + 9, p);
        }
        holder.unlockCanvasAndPost(canvas); // 结束锁定 显示在屏幕上
        holder.lockCanvas(new Rect(0, 0, 0, 0)); // 锁定局部区域，其余地方不做改变
        holder.unlockCanvasAndPost(canvas);
    }

    private void xuxian(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            for (int i = y1 + 5; i < y2; i += 10) {
                canvas.drawLine(x1, y1, x1, i, paint);
                y1 = i + 5;
            }
        }
        if (y1 == y2) {
            for (int i = x1 + 5; i < x2; i += 10) {
                canvas.drawLine(x1, y1, i, y1, paint);
                x1 = i + 5;
            }
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case DRAW_MESSAGE:
                    Bundle bundle = msg.getData();
                    tv_heartRate.setText("HeartRate:" + HeartRate);
                    break;
            }
        }
    };

    class MyButtonStopListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (isPaint) {
                isPaint = false;
                start.setText(getString(R.string.start));
            } else {
                isPaint = true;
                start.setText(getString(R.string.stop));
            }
        }
    }

}
