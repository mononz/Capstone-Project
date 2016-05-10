package net.mononz.paragon;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.mononz.paragon.adapters.AdapterHeroList;
import net.mononz.paragon.database.Database;
import net.mononz.paragon.sync.SyncAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.recycler) protected RecyclerView recycler;
    @Bind(R.id.adview) protected AdView adView;

    private static final int CURSOR_LOADER_ID = 1;
    private AdapterHeroList mAdapterHeroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mAdapterHeroes = new AdapterHeroList(MainActivity.this, null, new AdapterHeroList.Callback() {
            @Override
            public void openHero(long _id, String name, View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.HERO_ID, _id);
                intent.putExtra(DetailActivity.HERO_Name, name);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        GridLayoutManager llm = new GridLayoutManager(this, getResources().getInteger(R.integer.hero_grid_columns), LinearLayoutManager.VERTICAL, false);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(llm);
        recycler.setAdapter(mAdapterHeroes);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device))
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, MainActivity.this);
        Paradex.sendScreen(getString(R.string.analytics_screen_main));
        if (adView != null) {
            adView.resume();
        }
        // update database if greater than sync threshold
        if (((Paradex) getApplication()).timeForSync()) {
            if (Paradex.isNetworkConnected(this)) {
                SyncAdapter.initializeSyncAdapter(this);
                SyncAdapter.syncImmediately(this);
            } else {
                final Snackbar snackBar = Snackbar.make(findViewById(R.id.main_coordinator), getString(R.string.snack_no_internet), Snackbar.LENGTH_LONG);
                snackBar.show();
            }
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_coming_soon:
                startActivity(new Intent(MainActivity.this, Activity_Soon.class));
                return true;
            case R.id.menu_disclaimer:
                startActivity(new Intent(MainActivity.this, Activity_Settings.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Database.hero.CONTENT_URI,
                new String[]{Database.hero.FULL_ID, Database.hero.name, Database.hero.thumb, Database.type.name},
                null, null, Database.hero.sort + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapterHeroes.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterHeroes.swapCursor(null);
    }

}