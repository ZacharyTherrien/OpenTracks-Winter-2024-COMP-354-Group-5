package de.dennisguse.opentracks;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import de.dennisguse.opentracks.databinding.ActivitySeasonalBinding;
import de.dennisguse.opentracks.databinding.ActivitySeasonalPerSeasonBinding;
import de.dennisguse.opentracks.ui.aggregatedStatistics.DummyDataGenerator;

public class SeasonalActivityPerSeason extends AbstractActivity
{
    private ActivitySeasonalPerSeasonBinding viewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle data = getIntent().getExtras();
        if (data != null)
        {
            changeTitle(data.getString("seasonTitle"));
        }

        TextView distanceTraveledTextView = findViewById(R.id.distanceTraveled);
        TextView totalTimeTextView = findViewById(R.id.totalTime);
        TextView averageSpeedTextView = findViewById(R.id.avgSpeed);
        TextView maxSpeedTextView = findViewById(R.id.maxSpeed);



        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }
    private void MakeYesNoButton(Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Heading to " + button.getText()).setMessage("Is this okay?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Do stuff
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
    }
    private void changeTitle(String newTitle) {
        MaterialToolbar materialToolbar = (MaterialToolbar)findViewById(R.id.season_materialtoolbar);
        materialToolbar.setTitle(newTitle);
    }

    @Override
    protected View getRootView() {
        viewBinding = ActivitySeasonalPerSeasonBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }
}