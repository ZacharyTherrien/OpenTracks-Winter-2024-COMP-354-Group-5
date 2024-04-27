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

        // Credit: https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
        Spinner skiDropdown = findViewById(R.id.skiDropDown);
        skiDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Cross-Country Skis", "Downhill Skis", "Miscellaneous Skis", "Child Skis"}));

        Spinner bootDropdown = findViewById(R.id.bootDropDown);
        bootDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Snow Boots", "Spiky Boots", "Leather Boots", "Iron Boots"}));

        Spinner poleDropdown = findViewById(R.id.poleDropDown);
        poleDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Cool Poles", "Grounded Poles", "/pol/"}));

        Spinner resortDropdown = findViewById(R.id.resortDropDown);
        resortDropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Printre Noi Resort", "Big White Ski Resort", "Jackson Hole Resort", "Lake Louise Ski Resort", "Romanian Resort"}));

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected View getRootView() {
        viewBinding = TrackViewGearBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
