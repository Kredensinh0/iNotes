package com.example.pokrz.inotes2.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pokrz.inotes2.CategoryViewModel;
import com.example.pokrz.inotes2.NoteViewModel;
import com.example.pokrz.inotes2.R;
import com.example.pokrz.inotes2.SectionsStatePagerAdapter;
import com.example.pokrz.inotes2.entities.Category;
import com.example.pokrz.inotes2.entities.Note;
import com.example.pokrz.inotes2.ui.adapters.CategoryDrawerAdapter;
import com.example.pokrz.inotes2.ui.adapters.NoteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    public static final int FRAGMENT_NOTES = 0;
    public static final int FRAGMENT_MAP = 1;

    public static Location location;
    private LocationManager locationManager;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList;
    public static CategoryDrawerAdapter categoryAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);

        ViewPager viewPager = findViewById(R.id.fragment_container);
        viewPagerSetup(viewPager);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationListenerGPS);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        isLocationEnabled();

        NavigationView navigationView = findViewById(R.id.nav_view);
        RecyclerView recyclerViewNavigation = navigationView.findViewById(R.id.recycler_view_navigation);
        recyclerViewNavigation.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryDrawerAdapter();
        recyclerViewNavigation.setAdapter(categoryAdapter);

        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;
            categoryAdapter.setCategories(categories);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Category category = categoryList.get(viewHolder.getAdapterPosition());
                categoryViewModel.delete(category);
                Snackbar.make(findViewById(R.id.fragment_container), "Category has been deleted!", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerViewNavigation);

    }

    private void viewPagerSetup(ViewPager viewPager) {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentNotes(), "Fragment with notes"); // inflated by default
        adapter.addFragment(new FragmentMap(), "Fragment with map of notes");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == FRAGMENT_MAP) {
                    FloatingActionButton fab = findViewById(R.id.fab);
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                    lp.anchorGravity = Gravity.BOTTOM | GravityCompat.START;
                    fab.setLayoutParams(lp);
                } else {
                    FloatingActionButton fab = findViewById(R.id.fab);
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                    lp.anchorGravity = Gravity.BOTTOM | GravityCompat.END;
                    fab.setLayoutParams(lp);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_menu_add_category:
                new LovelyTextInputDialog(this)
                        .setTopColorRes(R.color.colorPrimary)
                        .setTitle("New category title: ")
                        .setIcon(R.drawable.ic_add_to_collection)
                        .setConfirmButton(android.R.string.ok, text -> {
                            if (!text.trim().isEmpty()) {
                                for (Category cat : categoryList) {
                                    if (cat.getTitle().trim().toLowerCase().equals(text.trim().toLowerCase())) {
                                        Toast.makeText(this, "This category already exists!", Toast.LENGTH_LONG).show();
                                    }
                                }
                                categoryViewModel.insert(new Category(text));
                            } else {
                                Toast.makeText(this, "Category title cannot be empty!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                break;
        }
        return true;
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkLocationPermission())
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } else {
                    location = null;
                }
                return;
            }
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            MainActivity.location = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void isLocationEnabled() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(getString(R.string.enable_location_title));
            alertDialog.setMessage(getString(R.string.location_not_enabled));
            alertDialog.setPositiveButton(getString(R.string.location_settings), (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            });
            alertDialog.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }
}

//TODO: add onClickListener to categories and adjust filter in notes adapter