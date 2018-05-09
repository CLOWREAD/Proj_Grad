#include "reg52.h" 
#include "DS18B20.h"
#define uchar unsigned char
#define uint unsigned int
uint DD;
sbit DQ=P0^6;
unsigned int temputer;

/*****?????*****/
void Delay_DS18B20(int num)
{
   while(num--) ;
}

/*****???DS18B20*****/
void Init_DS18B20(void)
{
   unsigned char x=0;
   DQ = 1;          //DQ??
   Delay_DS18B20(2);   //????
   DQ = 0;          //????DQ??
   Delay_DS18B20(80); //????,??480us
   DQ = 1;          //????
   Delay_DS18B20(14);
   x = DQ;            //?????,??x=0??????,x=1??????
   Delay_DS18B20(20);
}
/*****?????*****/
unsigned char ReadOneChar(void)
{
   unsigned char i=0;
   unsigned char dat = 0;
   for (i=8;i>0;i--)
   {
   DQ = 0;     // ?????
   dat>>=1;
   DQ = 1;     // ?????
   if(DQ)
   dat|=0x80;
   Delay_DS18B20(8);
   }
   return(dat);
}
/*****?????*****/
void WriteOneChar(unsigned char dat)
{
   unsigned char i=0;
   for (i=8; i>0; i--)
   {
   DQ = 0;
   DQ = dat&0x01;
   Delay_DS18B20(10);
   DQ = 1;
   dat>>=1;
   }
}
/*****????*****/
unsigned int ReadTemperature(void)
{
   unsigned char a=0;
   unsigned char b=0;
   unsigned int t=0;
   float tt=0; 
   Init_DS18B20();
// Init_DS18B20();
   WriteOneChar(0xCC); //??????????
   WriteOneChar(0x44); //??????
   Delay_DS18B20(20);
   Init_DS18B20();
   WriteOneChar(0xCC); //??????????
   WriteOneChar(0xBE); //???????
   a=ReadOneChar();      //??8?
   b=ReadOneChar();    //??8?
   t=b;
   t<<=8;
   t=t|a;
   tt=t*0.0625;
   t= tt*10+0.5;      //??10????????
   return(t);
}