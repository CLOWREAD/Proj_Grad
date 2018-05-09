//

#include "reg52.h" 
#include "stdio.h"              //printf???  
#include "intrins.h"
#include "SRC/oled.h"
#include "ADC.h"
#include "DS18B20.h"
typedef unsigned int uint;
void delay(uint z);
void initTimer();
int G_Value[8];
int G_CurrentPort=0;
sbit m_Lit=P0^5;

void initSer()  
{  
    TMOD=0x20;    //???????,??????1,????2 ?????????8?????          
    TH1=0xfd;     //???1??  ,??????9600 ??11.0529MHZ?  
    TL1=0xfd;  
    TR1=1;        //?????1  
  
    SM0=0;  
    SM1=1;        //10?????,(8???)?????  
    REN=1;        //????????  
    EA=1;         //????(??)  
    ES=1;         //??????  
}  


void SendInt(int i)
{

  int n;
	char str[16];
	
	sprintf(str," %d",i);
	
	for(n=0;str[n]!=0;n++)
	{
		SBUF=str[n];  
    while(!TI);  
			TI=0;
	}
	SBUF='\n';
	while(!TI);  
			TI=0;
	
}
void SendChar(char c)
{


		SBUF=c;  
    while(!TI);  
			TI=0;


}
void main()  
{  
	int tmp=0;
	char str[6];
	char port=0;
  initSer();  
	 OLED_Display_On();
	
	delay(100);
	OLED_Init();
  OLED_Clear();
	
	OLED_ShowString(0,4,"Proj Bana 1.0");
	
	while(1)
	{
	
		G_CurrentPort+=1;
		G_CurrentPort%=3;
		if(G_CurrentPort<2)
		{
			port=G_CurrentPort;
			tmp=ReadADC(port);
			if(tmp<1) tmp=1;
			if(tmp>254) tmp =254;
			G_Value[G_CurrentPort]=tmp;
		}
		if(G_CurrentPort==2)
		{
			tmp= ReadTemperature();
			tmp=tmp/10;
			
			tmp=tmp+25;
			tmp=tmp*2;
			
			tmp=255-tmp;
			if(tmp<1) tmp=1;
			if(tmp>254) tmp =254;
		G_Value[2]=tmp;
		
		//delay(1000);
		//sprintf(str,"%d",G_Value[2]);
		//OLED_ShowString(1,4,str);
		}
		delay(1000);
	}
}  
  
void ser() interrupt 4  
{  
		char a;
        if(RI)       //????,???RI?0  
    {        
				
        RI=0;  
				a=SBUF;
				
			
			if(a>='0' && a<='9')
			{
				a-='0';
				SendInt(G_Value[a]);
						
			}
			if(a=='L')
			{m_Lit=1;
			}
			if(a=='l')
			{m_Lit=0;
			}
    }  
  
    if(TI)     //????  
    {  
			TI=0;
    }      
}  

void delay(uint z)
{
	uint x,y;
	for(x=z;x>0;x--)
		for(y=110;y>0;y--);
}

