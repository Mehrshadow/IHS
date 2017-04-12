package ir.parsansoft.app.ihs.center;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jahanbin on 1/16/2017.
 */

public class Weather {

//    private boolean isRunning = false;
    private static Double temperature = 0.0;
    private static int weatherCode = 0;
//    private Thread wt;

    public void start() {
//        if ( !isRunning) {
        G.log("ir.parsansoft.app.ihs.center.Weather Started...");
        /*********************** Jahanbin *********************/
//            doJob();
        getConditionData();

        /*********************** Jahanbin *********************/
//        }
    }

//    public void stop() {
//        if (!isRunning) {
//            G.log("ir.parsansoft.app.ihs.center.Weather Stop...");
//            wt.stop();
//        }
//    }

//    private void doJob() {
//        wt = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isRunning = true;
//                while (isRunning) {
//
//                    ModuleWebservice mw = new ModuleWebservice();
//                    mw.url(G.URL_Weather_Webservice);
//                    mw.addParameter("rt", "GetWeather");
//                    mw.addParameter("CustomerID", "" + G.setting.customerID);
//                    mw.addParameter("ExKey", "" + G.setting.exKey);
//                    mw.addParameter("lat", "" + G.setting.gPSLat);
//                    mw.addParameter("lon", "" + G.setting.gPSLon);
//                    G.log("GPS: " + G.setting.gPSLat + " , " + G.setting.gPSLon);
//                    mw.enableCache(false);
//                    mw.socketTimeout(15000);
//                    //mw.cacheExpireTime(15 * 60 * 1000);
//                    mw.cacheDir(G.DIR_CACHE);
//                    mw.setListener(new ModuleWebservice.WebServiceListener() {
//
//                        @Override
//                        public void onFail(int statusCode) {
//
//                        }
//
//                        @Override
//                        public void onDataReceive(String data, boolean cached) {
//                            G.log("weather data is :\n888888888888888888888888888888888888888\n" + data);
//                            try {
//                                JSONArray ja = new JSONArray(data);
//                                JSONObject jo = ja.getJSONObject(0);
//                                weatherCode = jo.getInt("sensorNodeId");
//                                temperature = (int) jo.getDouble("temp");
//                                G.log("weatherCode:" + weatherCode + "  temperature:" + temperature);
//                                G.ui.runOnWeatherChanged(weatherCode, temperature);
//                                G.scenarioBP.runByWeather(weatherCode);
//                                G.scenarioBP.runByTemperature(temperature);
//                            } catch (Exception e) {
//                                G.printStackTrace(e);
//                            }
//                        }
//                    });
//                    mw.readFromNet();
//
//                    try {
//                        if (weatherCode == 0)
//                            Thread.sleep(10 * 60 * 1000);
//                        else
//                            Thread.sleep(10 * 60 * 1000);
//                    } catch (InterruptedException e) {
//                        G.log("Error ir.parsansoft.app.ihs.center.Weather BP:" + e.getMessage());
//                        G.printStackTrace(e);
//                    }
//
//                }
//            }
//        });
//        wt.start();
//    }

    public void refreshUI() {
        if (G.ui != null)
            G.ui.runOnWeatherChanged(weatherCode, temperature.intValue());
    }

    public int getCurrentTemperature() {
        return temperature.byteValue();
    }

    public int getCurrentWeatherCode() {
        return weatherCode;
    }

    /***************************
     * Jahanbin
     ***************************/
    public class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {

                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "FAILED";
            }
        }
    }

    /**
     * <p> getting weather and temperature using accuweather service</p>
     */
    public ArrayList<Object> getConditionData() {

        String coordinate = G.setting.gPSLat + "," + G.setting.gPSLon;
        String apiKey = G.setting.weatherAPIKey;
        String geoData = null;

        if (apiKey == null)
            return null;

        if (apiKey.equals("")) return null;

        try {
            geoData = new GetWeatherTask().execute("http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?q=" + coordinate + "&apikey=" + apiKey).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (geoData.equals("FAILED")) return null;

        String key = getKey(geoData);
        String city = getCity(geoData);
        String weatherJson = getWeatherJson(key, apiKey);
        return getConditionData(city, weatherJson);

    }

    private String getKey(String jsonString) {
        String key = null;
        try {
            key = new JSONObject(jsonString).getString("Key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return key;
    }

    private String getCity(String jsonString) {
        String city = null;
        try {
            city = new JSONObject(jsonString).getString("LocalizedName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }

    private String getWeatherJson(String key, String apiKey) {
        if (key == null | apiKey == null)
            return null;

        String weatherJson = null;
        try {
            GetWeatherTask weatherTask = new GetWeatherTask();
            weatherJson = weatherTask.execute("http://dataservice.accuweather.com/currentconditions/v1/" + key + ".json?language=en&apikey=" + apiKey).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return weatherJson;
    }

    private ArrayList<Object> getConditionData(String city, String weatherJson) {
        if (weatherJson == null)
            return null;

        JSONArray weatherArray;
        ArrayList<Object> conditionData = new ArrayList<>();
        try {
            weatherArray = new JSONArray(weatherJson);

            for (int i = 0; i < weatherArray.length(); i++) {

                String weather = weatherArray.getJSONObject(i).getString("WeatherText");
                weatherCode = weatherArray.getJSONObject(i).getInt("WeatherIcon");
                String temperatureText = weatherArray.getJSONObject(i).getString("Temperature");
                JSONObject temperatureObject = new JSONObject(temperatureText);
                temperature = Double.parseDouble(new JSONObject(temperatureObject.getString("Metric")).getString("Value"));
                G.log("weather: " + weather + "  temperature: " + temperature);

                G.ui.runOnWeatherChanged(weatherCode, temperature.intValue());
                G.scenarioBP.runByWeather(weatherCode);
                G.scenarioBP.runByTemperature(temperature.intValue());

                conditionData.add(city);
                conditionData.add(weather);
                conditionData.add(temperature.intValue());
                conditionData.add(weatherCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conditionData;
    }
    /*************************** Jahanbin ***************************************/
}
