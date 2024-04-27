package de.dennisguse.opentracks;

import android.os.Bundle;
import android.view.View;

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

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected View getRootView() {
        viewBinding = TrackViewGearBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}
