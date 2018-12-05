package com.example.administrator.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import github.vatsal.easyweather.Helper.ForecastCallback;
import github.vatsal.easyweather.Helper.TempUnitConverter;
import github.vatsal.easyweather.Helper.WeatherCallback;
import github.vatsal.easyweather.WeatherMap;
import github.vatsal.easyweather.retrofit.models.ForecastResponseModel;
import github.vatsal.easyweather.retrofit.models.Weather;
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel;

import static com.example.administrator.weather.BuildConfig.OWM_API_KEY;

public class MainActivity extends AppCompatActivity {
    public final String APP_ID = OWM_API_KEY;
    String city = "Piscataway";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadWeather(city);
    }

    private void loadWeather(String city){
        WeatherMap weatherMap = new WeatherMap(this, OWM_API_KEY);
        weatherMap.getCityWeather(city, new WeatherCallback() {
            @Override
            public void success(WeatherResponseModel response) {
                Weather weather[] = response.getWeather();
                Double temperature = TempUnitConverter.convertToCelsius(response.getMain().getTemp());
                String location = response.getName();
                String humidity= response.getMain().getHumidity();
                String pressure = response.getMain().getPressure();
                String windSpeed = response.getWind().getSpeed();
                String iconLink = weather[0].getIconLink();
            }
            @Override
            public void failure(String message) {

            }
        });

        weatherMap.getCityForecast(city, new ForecastCallback() {
            @Override
            public void success(ForecastResponseModel response) {
                //ForecastResponseModel responseModel = response;
                    Weather weather1[] = response.getList()[1].getWeather();
                    String des1= weather1[0].getDescription();
                    String Icon1=weather1[0].getIconLink();
                    Weather weather2[] = response.getList()[2].getWeather();
                    String des2= weather2[0].getDescription();
                    String Icon2=weather2[0].getIconLink();
                    Weather weather3[] = response.getList()[3].getWeather();
                    String des3= weather3[0].getDescription();
                    String Icon3=weather3[0].getIconLink();
                    Weather weather4[] = response.getList()[4].getWeather();
                    String des4= weather4[0].getDescription();
                    String Icon4=weather4[0].getIconLink();
                    Weather weather5[] = response.getList()[5].getWeather();
                    String des5= weather5[0].getDescription();
                    String Icon5=weather5[0].getIconLink();
              //  }
            }

            @Override
            public void failure(String message) {

            }
        });
    }
}
