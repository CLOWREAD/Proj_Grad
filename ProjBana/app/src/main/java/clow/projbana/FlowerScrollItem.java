package clow.projbana;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by clow_ on 18.3.20.
 */

public class FlowerScrollItem extends LinearLayout {
    ImageView m_ImageView;
    TextView m_TextView;
    FlowerInfo m_FlowerInfo;
    Context m_Context;
    public FlowerScrollItem(Context context) {
        super(context);
        m_Context=context;
        LayoutInflater.from(context).inflate(R.layout.flower_scroll_layout, this, true);
        m_ImageView=(ImageView)findViewById(R.id.FSL_ImageView);
        m_TextView=(TextView)findViewById(R.id.FSL_Text);

        TextView tv=(TextView)findViewById(R.id.FSL_Text);
        Typeface typeface = Typeface.createFromAsset(m_Context.getAssets(), "fonts/miaowu.ttf");
        tv.setTypeface(typeface);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BanaGlobal.m_CurrentFlower=m_FlowerInfo;
                Toast toast=Toast.makeText(m_Context,"已选择"+m_FlowerInfo.Name ,Toast.LENGTH_SHORT);
                toast.show();
            }
        });


    }


    public FlowerScrollItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.flower_scroll_layout, this, true);
        m_ImageView=(ImageView)findViewById(R.id.FSL_ImageView);
        m_TextView=(TextView)findViewById(R.id.FSL_Text);

    }
    void SetText(String str)
    {
        m_TextView.setText(str.toCharArray(),0,str.length());
    }
    void SetImage(Drawable d)
    {
     m_ImageView.setImageDrawable( d);
    }
    void SetImage(int d)
    {
        m_ImageView.setImageResource(d);
    }
    void SetFlowerInfo(FlowerInfo fi)
    {
        m_FlowerInfo=fi;
        SetText(m_FlowerInfo.Name);
        SetImage(m_FlowerInfo.ImageID);
    }

}
