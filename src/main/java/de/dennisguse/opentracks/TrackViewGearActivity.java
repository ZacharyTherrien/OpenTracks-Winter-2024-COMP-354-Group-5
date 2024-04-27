package de.dennisguse.opentracks;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import de.dennisguse.opentracks.databinding.TrackViewGearBinding;

/**
 * Allow the user to select their gear for the resort they went to at what date.
 *
 * @author Sean Gregory, Zachary Therrien
 * */
public class TrackViewGearActivity extends AbstractActivity{

    private TrackViewGearBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ski Gear Tracker");

        //get the spinner from the xml.
        Spinner skiDropdown = findViewById(R.id.skiDropDown);
        //create a list of items for the spinner.
        String[] items = new String[]{"coolSki", "epicSki", "ski3"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        skiDropdown.setAdapter(adapter);

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected View getRootView() {
        viewBinding = TrackViewGearBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
