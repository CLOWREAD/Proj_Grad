package clow.projbana;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ViewFlipper m_ViewFliper;
    LinearLayout m_ScrollView;
    BlueToothBroadcast m_BTB=new BlueToothBroadcast(this);


    Handler m_UpdateRoutineHandler = new Handler();
    Runnable m_UpdateRoutine = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                m_UpdateRoutineHandler.postDelayed(this, 200);
                String str;
                ProgressBar pb1=(ProgressBar)findViewById(R.id.SolarProgressBar);
                pb1.setProgress(255-BanaGlobal.m_ADCValues[0]);
                ProgressBar pb2=(ProgressBar)findViewById(R.id.WetnessProgressBar);
                pb2.setProgress(255-BanaGlobal.m_ADCValues[1]);
                ProgressBar pb3=(ProgressBar)findViewById(R.id.TempProgressBar);
                pb3.setProgress(255-BanaGlobal.m_ADCValues[2]);

               UpdateFlwTips();

                //tv.setText(str.toCharArray(),0,str.length());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
            TextView tv1=(TextView)findViewById(R.id.BlueToothState);
            String str;
            switch (BanaGlobal.m_Status)
            {
                case 0:
                    str="未连接";
                    tv1.setText(str);
                    break;
                case 1:
                    str="正在连接";
                    tv1.setText(str);
                    break;
                case 2:
                    str="已连接";
                    tv1.setText(str);
                    break;
                default:

            }

        }
    };

    void UpdateFlwTips()
    {
        String str="";
        TextView tv=(TextView)findViewById(R.id.Flw_Tips_TextView);
        if(255-BanaGlobal.m_ADCValues[0]>BanaGlobal.m_CurrentFlower.S_H)
        {
            str+="帮我涂一下防晒霜";
        }
        if(255-BanaGlobal.m_ADCValues[0]<BanaGlobal.m_CurrentFlower.S_L)
        {
            str+="\t好黑好害怕";
        }
        str+="\n";
        if(255-BanaGlobal.m_ADCValues[1]>BanaGlobal.m_CurrentFlower.W_H)
        {
            str+="\t淹死了淹死了";
        }
        if(255-BanaGlobal.m_ADCValues[1]<BanaGlobal.m_CurrentFlower.W_L)
        {
            str+="\t渴死了渴死了";
        }
        str+="\n";
        if(255-BanaGlobal.m_ADCValues[2]>BanaGlobal.m_CurrentFlower.T_H)
        {
            str+="\t\tIce creeeeeeeeam!";
        }
        if(255-BanaGlobal.m_ADCValues[2]<BanaGlobal.m_CurrentFlower.T_L)
        {
            str+="\t\t雪~花飘~飘~~北风啸~啸~~";
        }
        str+="\n";
    tv.setText(str.toCharArray(),0,str.length());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                        while(m_ViewFliper.getDisplayedChild()!=0)
                        {
                            m_ViewFliper.showPrevious();
                        }

                    return true;
                case R.id.navigation_dashboard:

                    while(m_ViewFliper.getDisplayedChild()!=1)
                    {
                        if(m_ViewFliper.getDisplayedChild()<1)
                        {
                            m_ViewFliper.showNext();
                        }
                        if(m_ViewFliper.getDisplayedChild()>1)
                        {
                            m_ViewFliper.showPrevious();
                        }

                    }

                    return true;
                case R.id.navigation_notifications:
                    while(m_ViewFliper.getDisplayedChild()!=2)
                    {
                        m_ViewFliper.showNext();
                    }
                    return true;
            }
            return false;
        }

    };
 void Init()
 {
     BanaGlobal.InitList();
     InitFont();
     m_ViewFliper= (ViewFlipper)findViewById(R.id.main_container);
     m_ViewFliper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
     m_ViewFliper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));



     m_ScrollView=(LinearLayout)findViewById(R.id.FlowerInfoScrollLayoutt);

     for (FlowerInfo fi:BanaGlobal.m_FlowerInfoList )
     {
         FlowerScrollItem fsi=new FlowerScrollItem(this);
         fsi.SetFlowerInfo(fi);
         m_ScrollView.addView(fsi);
     }

     m_UpdateRoutineHandler.postDelayed(m_UpdateRoutine, 200);
 }
 void InitEvent()
 {
     Switch sw=(Switch)findViewById(R.id.BluetoothSwitch);
     sw.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Switch sw=(Switch)findViewById(R.id.BluetoothSwitch);
             if(!sw.isChecked())
             {
                 //sw.setChecked(false);
                 m_BTB.LinkStop();
             }else
             {
                 //sw.setChecked(true);
                 m_BTB.InitBluetooth();
                 m_BTB.LinkStart();
             }
         }
     });

     ImageView iv=(ImageView)findViewById(R.id.Lit_ImgButton);
     iv.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             BanaGlobal.m_LitEnable+=1;
             BanaGlobal.m_LitEnable%=2;
             if( BanaGlobal.m_LitEnable==0)
             {  ImageView iv=(ImageView)findViewById(R.id.Lit_ImgButton);
                iv.setImageResource(R.mipmap.lit_un);
             }
             if( BanaGlobal.m_LitEnable==1)
             {  ImageView iv=(ImageView)findViewById(R.id.Lit_ImgButton);
                 iv.setImageResource(R.mipmap.lit_en);
             }
         }
     });


 }
 void InitFont()
 {

    TextView tv=(TextView)findViewById(R.id.Flw_Tips_TextView);
     Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/miaowu.ttf");
     tv.setTypeface(typeface);
     tv=(TextView)findViewById(R.id.textView_Lit);
     tv.setTypeface(typeface);
     tv=(TextView)findViewById(R.id.textView_Tmp);
     tv.setTypeface(typeface);
     tv=(TextView)findViewById(R.id.textView_Wet);
     tv.setTypeface(typeface);
     tv=(TextView)findViewById(R.id.BlueToothState);
     tv.setTypeface(typeface);

     Switch sw=(Switch)findViewById(R.id.BluetoothSwitch);
     sw.setTypeface(typeface);


 }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Init();
        InitEvent();
    }



}
