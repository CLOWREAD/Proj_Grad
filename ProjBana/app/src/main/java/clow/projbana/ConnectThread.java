package clow.projbana;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by clow_ on 18.3.20.
 */

public class ConnectThread extends Thread
{
    BluetoothDevice m_Device;
    BluetoothSocket m_Socket;
    ReadingThread m_RT;
    SendingThread m_ST;
    ConnectThread(BluetoothDevice device)
    {
        m_Device = device;

        BluetoothSocket temp = null;
        m_Device = device;
        try {
            //Log.d("Q", "create socket");
            temp = m_Device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException e) {
            //Log.d("Q", "create socket FAIL !!");
            e.printStackTrace();
        }
        m_Socket = temp;
        BanaGlobal.m_BlueToothSocket=m_Socket;
    }


    public void run() {
        super.run();
        try {
            m_Socket.connect();
            BanaGlobal.m_Status=2;
            m_RT=new ReadingThread(m_Socket);
            m_RT.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (m_Socket.isConnected())
        {
            manageConnectedSocket(m_Socket);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e("EXIT","EXIT");
        BanaGlobal.m_Status=0;
    }
    public void manageConnectedSocket(BluetoothSocket socket) {
        //String result;
        try {
            BanaGlobal.m_CurrentADCPort += 1;
            BanaGlobal.m_CurrentADCPort %= 4;
            OutputStream os = socket.getOutputStream();
            os.write(String.valueOf(BanaGlobal.m_CurrentADCPort).getBytes());
            //Log.d("QQQQ", "Write" + String.valueOf(BanaGlobal.m_CurrentADCPort));
            os.flush();

            if(BanaGlobal.m_LitEnable==0)
            {
                os.write(("l").getBytes());
                os.flush();
            }else
            {
                os.write(("L").getBytes());
                os.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
 class ReadingThread extends Thread{
    BluetoothSocket mSocket;
    ReadingThread(BluetoothSocket socket)
    {
        mSocket=socket;
    }

    @Override
    public void run() {
        super.run();
        while (mSocket.isConnected())
        {
            try {

                InputStream is = mSocket.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String recstr =br.readLine();
                recstr=recstr.trim();
                int tempvalue=Integer.parseInt(recstr);
                if(tempvalue>0 && tempvalue<255)
                {
                    BanaGlobal.m_ADCValues[BanaGlobal.m_CurrentADCPort]=tempvalue;
                    // Log.d("QQQQ", ""+ BanaGlobal.m_ADCValues[BanaGlobal.m_CurrentADCPort]);

                }


            } catch (Exception e) {
              //  e.printStackTrace();
            }
        }
        Log.e("EXIT","EXIT");
        BanaGlobal.m_Status=0;
    }
}

class SendingThread extends Thread{
    BluetoothSocket mSocket;
    SendingThread(BluetoothSocket socket)
    {
        mSocket=socket;
    }

    @Override
    public void run() {
        super.run();
        while (mSocket.isConnected())
        {
            manageConnectedSocket(mSocket);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e("EXIT","EXIT");
    }
    public void manageConnectedSocket(BluetoothSocket socket){
        //String result;
        try {
            BanaGlobal.m_CurrentADCPort+=1;
            BanaGlobal.m_CurrentADCPort%=4;
            OutputStream os= socket.getOutputStream();
            os.write(String.valueOf(BanaGlobal.m_CurrentADCPort).getBytes());
            Log.d("QQQQ","Write"+ String.valueOf(BanaGlobal.m_CurrentADCPort));
            os.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
