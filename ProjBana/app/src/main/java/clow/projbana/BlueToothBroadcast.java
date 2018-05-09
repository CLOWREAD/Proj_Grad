package clow.projbana;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.*;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;
/**
 * Created by clow_ on 18.3.20.
 */

public class BlueToothBroadcast {
    private BluetoothAdapter m_Bluetooth= BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice m_BlueToothDev;
    private AnimationDrawable m_BlueTeethLinkingAni;
    private boolean m_IsShowBillboard=true;
    private IntentFilter m_BluetoothFilter;
    private Thread m_SendThread,m_ReadThread;
    Activity m_Activity;
    BlueToothBroadcast(Activity act)
    {
        m_Activity=act;
    }

    private BroadcastReceiver m_BluetoothReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Q", "reciver start");
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice=null;
                try {
                    Log.d("Q", "get dev beg");
                    scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("Q", "get dev end");
                }catch(Exception e)
                {

                }
                if(scanDevice == null || scanDevice.getName() == null) return;

                //蓝牙设备名称
                String name = scanDevice.getName();
                if(name != null && name.equals("FlowerPot"))
                {
                    Log.d("Q", "\n PotFound \n");
                    //unregisterReceiver(this);
                    m_Bluetooth.cancelDiscovery();
                    //Log.d("Q", "cancel discovery");
                    m_BlueToothDev=scanDevice;
                    ConnectThread ct=new ConnectThread(scanDevice);
                    m_SendThread=ct;
                    ct.start();
                    //取消扫描
                    //mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));                   //连接到设备。

                }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                m_Bluetooth.cancelDiscovery();
                BanaGlobal.m_Status=0;
            }
        }

    };
    void InitBluetooth()
    {
        m_BluetoothFilter = new IntentFilter();

//发现设备
        m_BluetoothFilter.addAction(BluetoothDevice.ACTION_FOUND);
//设备连接状态改变
        m_BluetoothFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//蓝牙设备状态改变
        m_BluetoothFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        m_BluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        m_Activity.registerReceiver(m_BluetoothReceiver, m_BluetoothFilter);
    }
    void LinkStop()
    {
        if(BanaGlobal.m_BlueToothSocket!=null)
        {
            try {
                BanaGlobal.m_BlueToothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void LinkStart()
    {  Log.e("Linking", "Linking");
       BanaGlobal.m_Status=1;
        if(!m_Bluetooth.isEnabled())
        {
            //弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            m_Activity.startActivity(enabler);
            //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
            //m_Bluetooth.enable();
        }
        if(BanaGlobal.m_BlueToothSocket!=null)
        {
            if(BanaGlobal.m_BlueToothSocket.isConnected())

            {
                try {
                    BanaGlobal.m_BlueToothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        boolean res =m_Bluetooth.startDiscovery();
        if(res)
        {
            //Log.e("@@@@start discovery","@@@@start discovery");
        }else
        {
            // Log.e("@@@@discovery Failed","@@@@discovery Failed");
        }


    }
}
