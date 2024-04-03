package de.dennisguse.opentracks.settings;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.dennisguse.opentracks.R;

public class UserProfilePictureEditActivity extends AppCompatActivity {

    ImageView imageView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laprofile_picture_preference_layout);
        Log.d("onCreate", "OnCreate is executed");

        imageView = findViewById(R.id.profileImageView);
        fab = findViewById(R.id.changeProfilePicFAB);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ButtonClicked", "Floating action button clicked");
                ImagePicker.with(UserProfilePictureEditActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activityResult", "reach check");
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageView.setImageURI(uri);
        }
    }
}
