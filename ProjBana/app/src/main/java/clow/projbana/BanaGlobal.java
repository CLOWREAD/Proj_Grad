package clow.projbana;

import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by clow_ on 18.3.20.
 */

public class BanaGlobal {
    static BluetoothSocket m_BlueToothSocket;
    static int m_CurrentADCPort=0;
    static int []m_ADCValues=new int[8];
    static int m_Status=0;
    static int m_LitEnable=0;
    static List< FlowerInfo>  m_FlowerInfoList=new ArrayList<>();
    static FlowerInfo m_CurrentFlower=new FlowerInfo();

    static void InitList()
    {

        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(20,124,34,200,50,230,R.mipmap.flw_img00,"仙人球"));
        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(21,124,34,200,50,230,R.mipmap.flw_img01,"百合"));
        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(22,124,34,200,50,230,R.mipmap.flw_img02,"多肉"));
        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(23,124,34,200,50,230,R.mipmap.flw_img03,"含羞草"));
        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(24,124,34,200,50,230,R.mipmap.flw_img04,"彼岸花"));
        BanaGlobal.m_FlowerInfoList.add(new FlowerInfo(25,124,34,200,50,230,R.mipmap.flw_img05,"蒜苗"));

    }
}
