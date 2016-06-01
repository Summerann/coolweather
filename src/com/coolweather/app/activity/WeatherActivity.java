package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.preference.PreferenceManager;

public class WeatherActivity extends Activity{
	
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)) {
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ���С�����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			//û���ؼ�����ʱ�Ͳ�ѯ��������
			showWeather();
		}
	}

	//��ѯ�ؼ���������Ӧ����������
	private void queryWeatherCode(String countyCode) {
		Log.d("1", "1");
		String address = "http://www.weather.com.cn/data/list3/city" + countyCode +".xml";
		queryFromServer(address,"countyCode");
	}
	//��ѯ�������Ŷ�Ӧ������
	private void queryWeatherInfo(String weatherCode) {
		Log.d("1", "2");
		String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode+".html";
		Log.d("1", "22");
		queryFromServer(address,"weatherCode");
	}
	//���ݴ����
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener (){
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)) {
					if(!TextUtils.isEmpty(response)) {
						//�ӷ����������н�������������
						String [] array = response.split("\\|"); 
						if(array != null && array.length ==2){
							String weatherCode = array[1];
							Log.d("������������", "2"+array[1]);
							queryWeatherInfo(weatherCode);
							Log.d("���������������", "3"+array[1]);
						}
					}
				}else if ("weatherCode".equals(type)) {
					Log.d("3","1");
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	//��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

}