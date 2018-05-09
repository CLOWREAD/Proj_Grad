package clow.projbana;

/**
 * Created by clow_ on 18.3.25.
 */

class FlowerInfo
{
    int T_L,T_H,W_L,W_H,S_L,S_H;
    int ImageID;
    String Name;

    public FlowerInfo() {
    }

    public FlowerInfo(int t_L, int t_H, int w_L, int w_H, int s_L, int s_H, int imageID, String name) {
        T_L = t_L;
        T_H = t_H;
        W_L = w_L;
        W_H = w_H;
        S_L = s_L;
        S_H = s_H;
        ImageID = imageID;
        Name = name;
    }
}