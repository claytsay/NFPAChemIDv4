package com.tsaysoft.nfpachemidv4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tsaysoft.nfpachemidv3.ChemProp;
import com.tsaysoft.nfpachemidv3.ChemSpecial;

import static com.tsaysoft.nfpachemidv3.ChemSpecial.*;
import com.tsaysoft.nfpachemidv3.Chemical;

import java.util.EnumMap;
import java.util.HashMap;

import static com.tsaysoft.nfpachemidv3.ChemProp.FLAMMABILITY;
import static com.tsaysoft.nfpachemidv3.ChemProp.HEALTH;
import static com.tsaysoft.nfpachemidv3.ChemProp.REACTIVITY;

public class MainActivity extends AppCompatActivity {

    private Spinner spHealth;
    private Spinner spFlammability;
    private Spinner spReactivity;

    private CheckBox cbOX;
    private CheckBox cbSA;
    private CheckBox cbW;

    private EnumMap<ChemProp, Integer> properties = new EnumMap<>(ChemProp.class);
    private EnumMap<ChemSpecial, Boolean> specials = new EnumMap<>(ChemSpecial.class);
    //private HashMap<String, Object> queryData;

    public static final Gson gsonProps = new GsonBuilder().registerTypeAdapter(
                new TypeToken<EnumMap<ChemProp, Integer>>() {}.getType(),
                new EnumMapInstanceCreator<ChemProp, Integer>(ChemProp.class)).create();
    public static final Gson gsonSpecs = new GsonBuilder().registerTypeAdapter(
            new TypeToken<EnumMap<ChemSpecial, Boolean>>() {}.getType(),
            new EnumMapInstanceCreator<ChemSpecial, Boolean>(ChemSpecial.class)).create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeInput();
    }

    /**
     * Submits the entered NFPA 704 information.
     * @param view
     */
    public void onSubmit(View view) {
        // Collect data and get variables ready
        compileInput();
        Chemical queryChemical = new Chemical(null, properties, specials);
        //queryChemical.instantiateChemID(); // EXPERIMENTAL "PROCEDURE"

        /*String resultsSerial = new Gson().toJson(queryChemical);
        Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
        intent.putExtra("QUERY", resultsSerial);
        startActivity(intent);*/

        /*queryData = new HashMap<>();
        queryData.put("props", properties);
        queryData.put("specs", specials);*/

        /*String resultsSerial = new Gson().toJson(queryData);
        Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
        intent.putExtra("QUERY", resultsSerial);
        startActivity(intent);*/

        // Package the data using the specially-made Gsons
        String propsString = gsonProps.toJson(properties);
        String specsString = gsonSpecs.toJson(properties);

        // Finalize the Intent
        Intent intent = new Intent(getBaseContext(), ResultsActivity.class);
        intent.putExtra("PROPERTIES", propsString);
        intent.putExtra("SPECIALS", specsString);

        startActivity(intent);

    }

    /**
     * Instantiates <code>Spinner</code>s and <code>CheckBox</code>es.
     */
    private void initializeInput() {
        spHealth = (Spinner)findViewById(R.id.spinner_health);
        spFlammability = (Spinner)findViewById(R.id.spinner_flammability);
        spReactivity = (Spinner)findViewById(R.id.spinner_reactivity);

        cbOX = (CheckBox)findViewById(R.id.checkBox_ox);
        cbSA = (CheckBox) findViewById(R.id.checkBox_sa);
        cbW = (CheckBox) findViewById(R.id.checkBox_w);

    }

    /**
     * Compiles the inputs from the <code>Spinner</code>s and <code>CheckBox</code>es.
     */
    private void compileInput() {
        properties.put(HEALTH, getIntSpinnerValue(spHealth));
        properties.put(FLAMMABILITY, getIntSpinnerValue(spFlammability));
        properties.put(REACTIVITY, getIntSpinnerValue(spReactivity));

        specials.put(OXIDIZER, cbOX.isChecked());
        specials.put(SIMPLE_ASPHYXIANT, cbSA.isChecked());
        specials.put(WATER_REACT, cbW.isChecked());
    }

    /**
     * Returns the <code>int</code> value of a <code>Spinner</code>.
     * @param spinner
     * @return
     */
    private int getIntSpinnerValue(Spinner spinner) {
        return Integer.parseInt(spinner.getSelectedItem().toString());
    }

}
