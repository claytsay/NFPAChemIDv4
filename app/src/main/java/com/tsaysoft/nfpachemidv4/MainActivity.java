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

    public static final DataConverter DATA_CONVERTER = new DataConverter();



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
        Intent intent = new Intent(getBaseContext(), ResultsActivity.class);

        // Let's just go to the basics: perhaps that will be easiest.
        for (ChemProp e : ChemProp.values()) {
            intent.putExtra(e.toString(), properties.get(e));
        }

        for (ChemSpecial e : ChemSpecial.values()) {
            intent.putExtra(e.toString(), specials.get(e));
        }

        startActivity(intent);

    }

    /**
     * Instantiates <code>Spinner</code>s and <code>CheckBox</code>es.
     */
    private void initializeInput() {
        spHealth        = (Spinner)findViewById(R.id.spinner_health);
        spFlammability  = (Spinner)findViewById(R.id.spinner_flammability);
        spReactivity    = (Spinner)findViewById(R.id.spinner_reactivity);

        cbOX    =  (CheckBox)findViewById(R.id.checkBox_ox);
        cbSA    =  (CheckBox)findViewById(R.id.checkBox_sa);
        cbW     =  (CheckBox)findViewById(R.id.checkBox_w);
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
