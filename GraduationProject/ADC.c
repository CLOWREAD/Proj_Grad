#include "ADC.h"
#include "reg52.h" 
#include "stdio.h"
#include "intrins.h"
//#include "SRC/oled.h"

sbit adcclock=P1^0;
sbit adcstart=P1^1;
sbit adceoc=P1^2;
sbit adcoe=P1^3;
sbit adcale=P1^4;
sbit adcaddra=P1^5;
sbit adcaddrb=P1^6;
sbit adcaddrc=P1^7;

void clockdelay(int z)
{
	int x,y;
	for(x=z;x>0;x--)
		for(y=110;y>0;y--)
	{
		adcclock=0;
		_nop_();
		//_nop_();
		adcclock=1;
	}
	
}
int ReadADC(char port)
{
	unsigned char value;
	int v;
	char str[8];
	//OLED_ShowString(0,4,"START");
		
	adcaddra=(port<<7)>>7;
	adcaddrb=(port<<6)>>7;
	adcaddrc=(port<<5)>>7;
	adcale=0;
	_nop_();
	_nop_();
	_nop_();
	adcale=1;
	
	
	adcoe=0;
	_nop_();
	_nop_();
	_nop_();
	adcoe=1;
	_nop_();
	_nop_();
	adcstart=1;
	clockdelay(2);
	adcstart=0;
	_nop_();
	while(adceoc==0)
	{
		clockdelay(2);
	}
	//OLED_ShowString(0,4,"FINISH");	
	
	clockdelay(5);
	value=P2;
	

	return value;
	
}