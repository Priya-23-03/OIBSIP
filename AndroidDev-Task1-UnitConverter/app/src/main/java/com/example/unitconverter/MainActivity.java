package com.example.unitconverter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner;
    private Spinner fromSpinner;
    private Spinner toSpinner;

    private EditText inputValue;
    private TextView resultText;
    private TextView emptyHistoryText;

    private Button convertButton;
    private Button swapButton;
    private Button clearHistoryButton;

    private LinearLayout historyList;

    private final List<String> historyItems = new ArrayList<>();

    private SharedPreferences preferences;

    private static final String PREFS_NAME = "UnitConverterPrefs";
    private static final String HISTORY_KEY = "history";
    private static final int MAX_HISTORY_ITEMS = 50;

    private static final int NAVY = Color.rgb(23, 37, 84);
    private static final int BLUE = Color.rgb(37, 99, 235);
    private static final int WHITE = Color.WHITE;

    private static final double ABSOLUTE_ZERO_C = -273.15;
    private static final double TEMPERATURE_TOLERANCE = 1e-9;

    private final DecimalFormat decimalFormat =
            new DecimalFormat("#,##0.########");

    private final String[] categories = {
            "Length",
            "Weight",
            "Temperature",
            "Area",
            "Volume",
            "Speed",
            "Time"
    };

    private final String[][] units = {
            {"Meter", "Kilometer", "Centimeter", "Inch"},
            {"Kilogram", "Gram", "Pound"},
            {"Celsius", "Fahrenheit", "Kelvin"},
            {
                    "Square Meter",
                    "Square Kilometer",
                    "Square Centimeter",
                    "Hectare",
                    "Acre"
            },
            {"Liter", "Milliliter", "Cubic Meter", "US Gallon"},
            {"Meters/Second", "Kilometers/Hour", "Miles/Hour"},
            {"Second", "Minute", "Hour", "Day"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Display the Android splash screen.
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        bindViews();
        setupCategorySpinner();
        setupButtons();
        loadHistory();
        displayHistory();
    }

    private void bindViews() {
        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);

        inputValue = findViewById(R.id.inputValue);
        resultText = findViewById(R.id.resultText);
        emptyHistoryText = findViewById(R.id.emptyHistoryText);

        convertButton = findViewById(R.id.convertButton);
        swapButton = findViewById(R.id.swapButton);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);

        historyList = findViewById(R.id.historyList);

        inputValue.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | InputType.TYPE_NUMBER_FLAG_SIGNED
        );

        inputValue.setTextColor(NAVY);
        resultText.setTextColor(NAVY);

        // Keep the Swap units text visible.
        swapButton.setTextColor(Color.rgb(29, 78, 216));
        swapButton.setBackgroundTintList(null);
        swapButton.setAlpha(1.0f);
        swapButton.setAllCaps(false);

        convertButton.setAllCaps(false);
        clearHistoryButton.setAllCaps(false);
    }

    private void setupCategorySpinner() {
        categorySpinner.setAdapter(
                createSpinnerAdapter(categories)
        );

        categorySpinner.setSelection(0);
        updateUnitSpinners(0);

        categorySpinner.setOnItemSelectedListener(
                new android.widget.AdapterView
                        .OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {
                        updateUnitSpinners(position);
                        resultText.setText(
                                "Your result will appear here"
                        );
                        inputValue.setError(null);
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent
                    ) {
                        // Nothing to do.
                    }
                }
        );
    }

    private void updateUnitSpinners(int categoryPosition) {
        if (categoryPosition < 0
                || categoryPosition >= units.length) {
            return;
        }

        String[] selectedUnits = units[categoryPosition];

        ArrayAdapter<String> adapter =
                createSpinnerAdapter(selectedUnits);

        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        fromSpinner.setSelection(0);

        toSpinner.setSelection(
                selectedUnits.length > 1 ? 1 : 0
        );
    }

    private ArrayAdapter<String> createSpinnerAdapter(
            String[] values
    ) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        values
                ) {

                    @Override
                    public View getView(
                            int position,
                            View convertView,
                            ViewGroup parent
                    ) {
                        TextView textView = (TextView)
                                super.getView(
                                        position,
                                        convertView,
                                        parent
                                );

                        textView.setTextColor(NAVY);
                        textView.setTextSize(14);
                        textView.setGravity(
                                Gravity.CENTER_VERTICAL
                        );

                        return textView;
                    }

                    @Override
                    public View getDropDownView(
                            int position,
                            View convertView,
                            ViewGroup parent
                    ) {
                        TextView textView = (TextView)
                                super.getDropDownView(
                                        position,
                                        convertView,
                                        parent
                                );

                        textView.setTextColor(NAVY);
                        textView.setTextSize(14);
                        textView.setPadding(
                                dp(16),
                                dp(12),
                                dp(16),
                                dp(12)
                        );
                        textView.setBackgroundColor(WHITE);

                        return textView;
                    }
                };

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        return adapter;
    }

    private void setupButtons() {
        convertButton.setOnClickListener(
                v -> convertValue()
        );

        swapButton.setOnClickListener(
                v -> swapUnits()
        );

        clearHistoryButton.setOnClickListener(
                v -> clearHistory()
        );
    }

    private void swapUnits() {
        int fromPosition =
                fromSpinner.getSelectedItemPosition();

        int toPosition =
                toSpinner.getSelectedItemPosition();

        if (fromPosition < 0 || toPosition < 0) {
            return;
        }

        fromSpinner.setSelection(toPosition);
        toSpinner.setSelection(fromPosition);

        swapButton.setTextColor(Color.rgb(29, 78, 216));
        swapButton.setBackgroundTintList(null);
        swapButton.setAlpha(1.0f);
    }

    private void convertValue() {
        inputValue.setError(null);

        String input =
                inputValue.getText().toString().trim();

        if (input.isEmpty()) {
            showInputError("Please enter a value.");
            return;
        }

        final double value;

        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException exception) {
            showInputError(
                    "Enter a valid number, for example 12.5."
            );
            return;
        }

        if (Double.isNaN(value) || Double.isInfinite(value)) {
            showInputError(
                    "The value must be a finite number."
            );
            return;
        }

        int categoryIndex =
                categorySpinner.getSelectedItemPosition();

        int fromIndex =
                fromSpinner.getSelectedItemPosition();

        int toIndex =
                toSpinner.getSelectedItemPosition();

        if (categoryIndex < 0
                || categoryIndex >= categories.length
                || fromIndex < 0
                || toIndex < 0
                || fromIndex >= units[categoryIndex].length
                || toIndex >= units[categoryIndex].length) {

            Toast.makeText(
                    this,
                    "Please select a category and both units.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String category = categories[categoryIndex];
        String fromUnit = units[categoryIndex][fromIndex];
        String toUnit = units[categoryIndex][toIndex];

        try {
            // Validate temperatures before conversion.
            if ("Temperature".equals(category)) {
                double celsius = toCelsius(value, fromIndex);

                if (celsius < ABSOLUTE_ZERO_C
                        - TEMPERATURE_TOLERANCE) {

                    showInputError(
                            "Temperature cannot be below "
                                    + "absolute zero (-273.15 °C)."
                    );
                    return;
                }
            }

            double result = convert(
                    category,
                    value,
                    fromIndex,
                    toIndex
            );

            if (Double.isNaN(result)
                    || Double.isInfinite(result)) {

                showInputError(
                        "This value is too large to convert."
                );
                return;
            }

            if (Math.abs(result) < 1e-10) {
                result = 0.0;
            }

            String formattedInput = formatNumber(value);
            String formattedResult = formatNumber(result);

            resultText.setText(
                    formattedInput + " " + fromUnit
                            + " = "
                            + formattedResult + " " + toUnit
            );

            String historyEntry =
                    category + " | "
                            + formattedInput + " " + fromUnit
                            + " → "
                            + formattedResult + " " + toUnit;

            addHistory(historyEntry);

        } catch (IllegalArgumentException exception) {
            showInputError(exception.getMessage());
        }
    }

    private void showInputError(String message) {
        inputValue.setError(message);
        inputValue.requestFocus();

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    private double convert(
            String category,
            double value,
            int from,
            int to
    ) {
        switch (category) {
            case "Length": {
                double[] factors = {
                        1.0, 1000.0, 0.01, 0.0254
                };

                return value * factors[from] / factors[to];
            }

            case "Weight": {
                double[] factors = {
                        1.0, 0.001, 0.45359237
                };

                return value * factors[from] / factors[to];
            }

            case "Temperature": {
                double celsius = toCelsius(value, from);
                return fromCelsius(celsius, to);
            }

            case "Area": {
                double[] factors = {
                        1.0,
                        1000000.0,
                        0.0001,
                        10000.0,
                        4046.8564224
                };

                return value * factors[from] / factors[to];
            }

            case "Volume": {
                double[] factors = {
                        1.0, 0.001, 1000.0, 3.785411784
                };

                return value * factors[from] / factors[to];
            }

            case "Speed": {
                double[] factors = {
                        1.0, 1.0 / 3.6, 0.44704
                };

                return value * factors[from] / factors[to];
            }

            case "Time": {
                double[] factors = {
                        1.0, 60.0, 3600.0, 86400.0
                };

                return value * factors[from] / factors[to];
            }

            default:
                throw new IllegalArgumentException(
                        "Unsupported conversion category."
                );
        }
    }

    private double toCelsius(double value, int from) {
        switch (from) {
            case 0:
                return value;

            case 1:
                return (value - 32.0) * 5.0 / 9.0;

            case 2:
                return value - 273.15;

            default:
                throw new IllegalArgumentException(
                        "Please select a valid temperature unit."
                );
        }
    }

    private double fromCelsius(double celsius, int to) {
        switch (to) {
            case 0:
                return celsius;

            case 1:
                return celsius * 9.0 / 5.0 + 32.0;

            case 2:
                return celsius + 273.15;

            default:
                throw new IllegalArgumentException(
                        "Please select a valid temperature unit."
                );
        }
    }

    private String formatNumber(double value) {
        if (Math.abs(value) < 1e-10) {
            value = 0.0;
        }

        return decimalFormat.format(value);
    }

    private void addHistory(String entry) {
        historyItems.add(0, entry);

        while (historyItems.size() > MAX_HISTORY_ITEMS) {
            historyItems.remove(historyItems.size() - 1);
        }

        persistHistory();
        displayHistory();
    }

    private void persistHistory() {
        JSONArray array = new JSONArray();

        for (String item : historyItems) {
            array.put(item);
        }

        preferences.edit()
                .putString(HISTORY_KEY, array.toString())
                .apply();
    }

    private void loadHistory() {
        historyItems.clear();

        String savedHistory =
                preferences.getString(HISTORY_KEY, "[]");

        try {
            JSONArray array = new JSONArray(savedHistory);

            for (int i = 0; i < array.length(); i++) {
                String item = array.optString(i, "");

                if (!item.isEmpty()) {
                    historyItems.add(item);
                }
            }
        } catch (JSONException exception) {
            historyItems.clear();
        }
    }

    private void displayHistory() {
        historyList.removeAllViews();

        if (historyItems.isEmpty()) {
            emptyHistoryText.setVisibility(View.VISIBLE);
            historyList.setVisibility(View.GONE);
            return;
        }

        emptyHistoryText.setVisibility(View.GONE);
        historyList.setVisibility(View.VISIBLE);

        for (String entry : historyItems) {
            TextView itemView = new TextView(this);

            itemView.setText(entry);
            itemView.setTextColor(NAVY);
            itemView.setTextSize(14);
            itemView.setGravity(Gravity.CENTER_VERTICAL);

            itemView.setPadding(
                    dp(16),
                    dp(14),
                    dp(16),
                    dp(14)
            );

            itemView.setBackground(
                    roundedBackground(
                            Color.rgb(248, 250, 252),
                            Color.rgb(226, 232, 240),
                            14,
                            1
                    )
            );

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

            params.bottomMargin = dp(10);

            historyList.addView(itemView, params);
        }
    }

    private void clearHistory() {
        historyItems.clear();

        preferences.edit()
                .remove(HISTORY_KEY)
                .apply();

        displayHistory();

        Toast.makeText(
                this,
                "History cleared",
                Toast.LENGTH_SHORT
        ).show();
    }

    private GradientDrawable roundedBackground(
            int backgroundColor,
            int strokeColor,
            int cornerRadiusDp,
            int strokeWidthDp
    ) {
        GradientDrawable drawable = new GradientDrawable();

        drawable.setColor(backgroundColor);
        drawable.setCornerRadius(dp(cornerRadiusDp));
        drawable.setStroke(
                dp(strokeWidthDp),
                strokeColor
        );

        return drawable;
    }

    private int dp(float value) {
        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density + 0.5f
        );
    }
}
