package michael.quon.n01565129.mq;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Qu2onFragment extends Fragment {

    public Qu2onFragment() {
        // Required empty public constructor
    }

    private TextView studentIDTV, longitudeTV, latitudeTV, countryTV, humidityTV, cityTV, descriptionTV, temperatureTV;
    private Spinner citySpinner;
    private RadioGroup tempRadioGroup;
    private SharedPreferences sharedPrefs;
    private String[] citiesArray;
    private String selectedCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_qu2on, container, false);

        studentIDTV = view.findViewById(R.id.Mic_idTV);
        longitudeTV = view.findViewById(R.id.Mic_Longitude);
        latitudeTV = view.findViewById(R.id.Mic_Latitude);
        countryTV = view.findViewById(R.id.Mic_Country);
        humidityTV = view.findViewById(R.id.Mic_Humidity);
        cityTV = view.findViewById(R.id.Mic_City);
        descriptionTV = view.findViewById(R.id.Mic_Desc);
        temperatureTV = view.findViewById(R.id.Mic_tempTV);
        citySpinner = view.findViewById(R.id.Mic_cityspinner);
        tempRadioGroup = view.findViewById(R.id.Mic_tempRadioGroup);

        citiesArray = getResources().getStringArray(R.array.cities);

        sharedPrefs = requireActivity().getPreferences(requireContext().MODE_PRIVATE);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, citiesArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(spinnerAdapter);

        // Preselect the Celsius radio button
        tempRadioGroup.check(R.id.Mic_Celsius_radioButton);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = citiesArray[position];
                fetchWeatherData(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tempRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String temperatureUnit;
            if (checkedId == R.id.Mic_Celsius_radioButton) {
                temperatureUnit = getString(R.string.unit_metric);
            } else {
                temperatureUnit = getString(R.string.unit_imperial);
            }
            sharedPrefs.edit().putString(getString(R.string.temp_unit_key), temperatureUnit).apply();
            // Call method to update temperature display here
            fetchWeatherData(selectedCity); // Update temperature based on selected city and unit
        });

        return view;
    }

    private void fetchWeatherData(String city) {
        new Thread(() -> {
            try {
                // Construct API URL
                String apiKey = getString(R.string.api_key);
                String apiUrl = getString(R.string.api_url, city, apiKey);

                // Open connection
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(getString(R.string.http_method_get));

                // Read response
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());

                // Extract relevant information
                double longitude = jsonResponse.getJSONObject(getString(R.string.coord_key)).getDouble(getString(R.string.longitude_key));
                double latitude = jsonResponse.getJSONObject(getString(R.string.coord_key)).getDouble(getString(R.string.latitude_key));
                String country = jsonResponse.getJSONObject(getString(R.string.sys_key)).getString(getString(R.string.country_key));
                int humidity = jsonResponse.getJSONObject(getString(R.string.main_key)).getInt(getString(R.string.humidity_key));
                String cityName = jsonResponse.getString(getString(R.string.name_key));
                String weatherDescription = jsonResponse.getJSONArray(getString(R.string.weather_key)).getJSONObject(0).getString(getString(R.string.weather_description_key));
                double temperatureKelvin = jsonResponse.getJSONObject(getString(R.string.main_key)).getDouble(getString(R.string.temperature_key));

                // Get the selected temperature unit from SharedPreferences
                String temperatureUnit = sharedPrefs.getString(getString(R.string.temp_unit_key), getString(R.string.unit_metric));

                // Declare the temperature variable
                double temperature;

                // Convert temperature to the selected unit
                if (temperatureUnit.equals(getString(R.string.unit_metric))) {
                    // Convert Kelvin to Celsius
                    temperature = Math.round((temperatureKelvin - 273.15) * 10) / 10.0;
                } else {
                    // Convert Kelvin to Fahrenheit
                    temperature = Math.round(((temperatureKelvin - 273.15) * 9 / 5 + 32) * 10) / 10.0;
                }

                // Update UI on the main thread
                requireActivity().runOnUiThread(() -> {
                    longitudeTV.setText(getString(R.string.longitude_label, String.valueOf(longitude)));
                    latitudeTV.setText(getString(R.string.latitude_label, String.valueOf(latitude)));
                    countryTV.setText(getString(R.string.country_label, country));
                    humidityTV.setText(getString(R.string.humidity_label, humidity));
                    cityTV.setText(getString(R.string.city_label, cityName));
                    descriptionTV.setText(getString(R.string.description_label, weatherDescription));
                    temperatureTV.setText(getString(R.string.temperature_format, temperature, temperatureUnit.equals(getString(R.string.unit_metric)) ? getString(R.string.unit_celsius) : getString(R.string.unit_fahrenheit)));

                });

                // Close connections
                bufferedReader.close();
                inputStream.close();
                urlConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
