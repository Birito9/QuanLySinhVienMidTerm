package vn.edu.stu.doangiuaky;

import static android.Manifest.permission.CALL_PHONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;

import vn.edu.stu.doangiuaky.R;

public class AboutMeActivity extends AppCompatActivity {
    Button btnBack;
    TextView tvPhoneNum;

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        requestPermissions(new String[]{CALL_PHONE}, 1);
        addControls();
        addEvents();

    }

    private void addEvents() {
        tvPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhoneNum();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void callPhoneNum() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:(+84)938929526"));
        startActivity(callIntent);
    }

    private void addControls() {
        btnBack = findViewById(R.id.btnBack);
        tvPhoneNum = findViewById(R.id.tvPhoneNum);
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(
                Style.SATELLITE,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        thietLapBanDo();
                    }
                }
        );
    }

    private void thietLapBanDo() {
        // STU: 10.738102290467015, 106.67772674813946
        Point pointSTU = Point.fromLngLat(106.67772674813946, 10.738102290467015);
        AnnotationPlugin plugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager manager = PointAnnotationManagerKt.createPointAnnotationManager(
                plugin,
                new AnnotationConfig()
        );
        PointAnnotationOptions optionsSTU = new PointAnnotationOptions()
                .withPoint(pointSTU)
                .withTextField("STU")
                .withIconImage(BitmapFactory.decodeResource(this.getResources(), R.drawable.red_marker));
        manager.create(optionsSTU);

        CameraOptions cameraOptions = new CameraOptions.Builder()
                .center(pointSTU)
                .zoom(16.0)
                .bearing(0.0)
                .pitch(0.0)
                .build();

        mapView.getMapboxMap().setCamera(cameraOptions);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                openMain();
                break;
            case R.id.item_login:
                Intent intent = new Intent(AboutMeActivity.this, Activity_DangNhap.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMain() {
        Intent intent = new Intent(AboutMeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}