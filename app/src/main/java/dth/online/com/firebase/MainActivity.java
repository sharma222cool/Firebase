package dth.online.com.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private EditText editText;
    private FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put("overlay_default_max", 5);
//        firebaseRemoteConfig.setDefaults(defaults);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        Task<Void> fetch = firebaseRemoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                firebaseRemoteConfig.activateFetched();
                updateMaxLength();
            }
        });

        editText = (EditText) findViewById(R.id.edit_text);
        updateMaxLength();

    }

    private void updateMaxLength()
    {
        int max = (int) firebaseRemoteConfig.getLong("overlay_default_max");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }
}
