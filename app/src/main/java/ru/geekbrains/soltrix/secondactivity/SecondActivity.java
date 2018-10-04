package ru.geekbrains.soltrix.secondactivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static ru.geekbrains.soltrix.secondactivity.StartSecondActivity.TEXT;

public class SecondActivity extends AppCompatActivity {

    private Handler handler;
    private TextView city;
    private TextView temp;
    private TextView sky;
    protected TextView gradus;
    private TextView detailsText;
    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Parcel parcel = null; // получить данные из Intent
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            parcel = (Parcel) Objects.requireNonNull(getIntent().getExtras()).getSerializable(TEXT);
        }

        handler = new Handler();
        temp = findViewById(R.id.temperature);
        sky = findViewById(R.id.sky);
        sky.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/weather.ttf"));
        gradus = findViewById(R.id.gradus);
        gradus.setText("\u00b0C");
        detailsText = findViewById(R.id.details);
        city = findViewById(R.id.city);
        data = findViewById(R.id.data);

        //TextView textView = (TextView) findViewById(R.id.textView);
        //EditText editText = (EditText) findViewById(R.id.editText3);
        city.setText(parcel.text); // Сохранить их в TextView

        if (!parcel.checkSky) {
            sky.setVisibility(View.GONE);
        }

        if (!parcel.checkDetails) {
            detailsText.setVisibility(View.GONE);
        }

        //data.setText(((Integer) parcel.number).toString());
        Toast.makeText(getApplicationContext(),"Second - onCreate()", Toast.LENGTH_SHORT).show();

        updateWeatherData(parcel.text);
    }

    //Обновление/загрузка погодных данных
    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = WeatherData.getJSONData(SecondActivity.this, city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(SecondActivity.this, SecondActivity.this.getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    //Обработка загруженных данных
    private void renderWeather(JSONObject json) {
        try {
            city.setText(json.getString("name").toUpperCase(Locale.US) + ", "
                    + json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsText.setText(details.getString("description").toUpperCase(Locale.US) + "\n" + getResources().getString(R.string.humidity)
                    + ": " + main.getString("humidity") + "%" + "\n" + getResources().getString(R.string.pressure)
                    + ": " + main.getString("pressure") + " hPa");
            detailsText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            detailsText.setLineSpacing(0,1.4f);

            temp.setText(String.format("%.1f", main.getDouble("temp")));

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
            data.setText(getResources().getString(R.string.last_update) + " " + updatedOn);

            setWeatherIcon(details.getInt("id"), json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.e("Weather", "One or more fields not found in the JSON data");
        }
    }

    //Подстановка нужной иконки
    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = SecondActivity.this.getString(R.string.weather_sunny);
            } else {
                icon = SecondActivity.this.getString(R.string.weather_clear_night);
            }
        } else {
            Log.d("SimpleWeather", "id " + id);
            switch (id) {
                case 2:
                    icon = SecondActivity.this.getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = SecondActivity.this.getString(R.string.weather_drizzle);
                    break;
                case 5:
                    icon = SecondActivity.this.getString(R.string.weather_rainy);
                    break;
                case 6:
                    icon = SecondActivity.this.getString(R.string.weather_snowy);
                    break;
                case 7:
                    icon = SecondActivity.this.getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = SecondActivity.this.getString(R.string.weather_cloudy);
                    break;
            }
        }
        sky.setText(icon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Second - onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Second - onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "Second - onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "Second - onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "Second - onRestart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Second - onDestroy()", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
//                TextView textView = (TextView) findViewById(R.id.data);
//                Intent intentResult = new Intent();
//                intentResult.putExtra("Data", data.getText().toString());
//                setResult(Activity.RESULT_OK, intentResult);
                finish();
                break;
            default:
                break;
        }
    }
}