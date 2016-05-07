package net.mononz.paragon;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.mononz.paragon.adapters.AdapterAbilities;
import net.mononz.paragon.adapters.AdapterAffinities;
import net.mononz.paragon.database.Database;
import net.mononz.paragon.library.svg.SvgDecoder;
import net.mononz.paragon.library.svg.SvgDrawableTranscoder;
import net.mononz.paragon.library.svg.SvgSoftwareLayerSetter;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Nullable @Bind(R.id.collapsing_toolbar) protected CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Nullable @Bind(R.id.hero_image) protected ImageView hero_image;
    @Bind(R.id.tagline_text) protected TextView tagline_text;
    @Bind(R.id.type_text) protected TextView type_text;
    @Bind(R.id.type_image) protected ImageView type_image;
    @Bind(R.id.role_text) protected TextView role_text;
    @Bind(R.id.attack_text) protected TextView attack_text;
    @Bind(R.id.scaling_text) protected TextView scaling_text;
    @Bind(R.id.trait_1_progress) protected ProgressBar trait_1_progress;
    @Bind(R.id.trait_2_progress) protected ProgressBar trait_2_progress;
    @Bind(R.id.trait_3_progress) protected ProgressBar trait_3_progress;
    @Bind(R.id.trait_4_progress) protected ProgressBar trait_4_progress;
    @Bind(R.id.trait_5_progress) protected ProgressBar trait_5_progress;
    @Bind(R.id.adview) protected AdView adView;

    @Bind(R.id.recycler_abilities) protected RecyclerView recycler_abilities;
    @Bind(R.id.recycler_affinities) protected RecyclerView recycler_affinities;

    private static final int LOADER_HEROES_BASE = 1;
    private static final int LOADER_HEROES_AFFINITIES = 2;
    private static final int LOADER_HEROES_TRAITS = 3;
    private static final int LOADER_HEROES_ABILITIES = 4;

    private AdapterAbilities mAdapterAbilities;
    private AdapterAffinities mAdapterAffinities;

    public static final String HERO_ID = "Hero_ID";
    public static final String HERO_Name = "Hero_Name";

    private String mExternal = null;
    private String mYouTube = null;

    private long mHeroID = -1;
    private String mHeroName = null;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        requestBuilder = Glide.with(DetailActivity.this)
                .using(Glide.buildStreamModelLoader(Uri.class, DetailActivity.this), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeroID = extras.getLong(HERO_ID, -1);
            mHeroName = extras.getString(HERO_Name, null);
            setTitle("");
            getSupportLoaderManager().initLoader(LOADER_HEROES_BASE, null, DetailActivity.this);
            getSupportLoaderManager().initLoader(LOADER_HEROES_ABILITIES, null, DetailActivity.this);
            getSupportLoaderManager().initLoader(LOADER_HEROES_AFFINITIES, null, DetailActivity.this);
            getSupportLoaderManager().initLoader(LOADER_HEROES_TRAITS, null, DetailActivity.this);
        }

        mAdapterAbilities = new AdapterAbilities(DetailActivity.this, null);
        mAdapterAffinities = new AdapterAffinities(DetailActivity.this, null);

        GridLayoutManager llm1 = new GridLayoutManager(this, getResources().getInteger(R.integer.ability_grid_columns), LinearLayoutManager.VERTICAL, false);
        recycler_abilities.setHasFixedSize(true);
        recycler_abilities.setLayoutManager(llm1);
        recycler_abilities.setNestedScrollingEnabled(false);
        recycler_abilities.setAdapter(mAdapterAbilities);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device))
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        Paradex.sendScreen(getString(R.string.analytics_screen_detail_) + ((mHeroName == null) ? Long.toString(mHeroID) : mHeroName));
        if (adView != null) {
            adView.resume();
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
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_external).setVisible(mExternal != null);
        menu.findItem(R.id.menu_youtube).setVisible(mYouTube != null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_external:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mExternal)));
                return true;
            case R.id.menu_youtube:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mYouTube)));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_HEROES_BASE:
                return new CursorLoader(this, ContentUris.withAppendedId(Database.hero.CONTENT_URI, mHeroID), null, null, null, null);
            case LOADER_HEROES_ABILITIES:
                return new CursorLoader(this, Database.hero_ability.CONTENT_URI,
                        new String[]{Database.hero_ability.FULL_ID, Database.hero_ability.name, Database.hero_ability.description, Database.hero_ability.image, Database.hero_ability.key_pc, Database.hero_ability.key_ps, Database.ability_type.name},
                        Database.hero_ability.FULL_HEROES_ID + "=?", new String[]{Long.toString(mHeroID)}, null);
            case LOADER_HEROES_TRAITS:
                return new CursorLoader(this, ContentUris.withAppendedId(Database.hero_trait.CONTENT_URI, mHeroID), null, null, null, null);
            case LOADER_HEROES_AFFINITIES:
                return new CursorLoader(this, Database.hero_affinity.CONTENT_URI, new String[]{Database.affinity.FULL_ID, Database.affinity.name, Database.affinity.image},
                        Database.hero_affinity.FULL_HEROES_ID + "=?", new String[]{Long.toString(mHeroID)}, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {

            case LOADER_HEROES_BASE:
                if (data.moveToFirst()) {
                    int idx_name = data.getColumnIndex(Database.hero.name);
                    int idx_tagline = data.getColumnIndex(Database.hero.tagline);
                    int idx_image = data.getColumnIndex(Database.hero.image);
                    int idx_type = data.getColumnIndex(Database.type.name);
                    int idx_type_image = data.getColumnIndex(Database.type.image);
                    int idx_role = data.getColumnIndex(Database.role.name);
                    int idx_attack = data.getColumnIndex(Database.attack.name);
                    int idx_scaling = data.getColumnIndex(Database.scaling.name);

                    int idx_external = data.getColumnIndex(Database.hero.url);
                    int idx_youtube = data.getColumnIndex(Database.hero.video);

                    type_text.setText(data.getString(idx_type));
                    role_text.setText(data.getString(idx_role));
                    attack_text.setText(data.getString(idx_attack));
                    scaling_text.setText(data.getString(idx_scaling));

                    setTitle(data.getString(idx_name));
                    if (collapsingToolbarLayout != null) {
                        collapsingToolbarLayout.setTitle(data.getString(idx_name));
                    }

                    if (tagline_text != null) {
                        tagline_text.setText("\"" + data.getString(idx_tagline) + "''");
                    }

                    if (hero_image != null) {
                        Glide.with(DetailActivity.this)
                                .load(Uri.parse(Paradex.ASSET_PATH + data.getString(idx_image)))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .crossFade()
                                .centerCrop()
                                .into(hero_image);
                    }

                    mExternal = data.getString(idx_external);
                    mYouTube = data.getString(idx_youtube);
                    invalidateOptionsMenu();

                    requestBuilder
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(Uri.parse(Paradex.ASSET_PATH + data.getString(idx_type_image)))
                            .into(type_image);
                }
                break;

            case LOADER_HEROES_ABILITIES:
                mAdapterAbilities.changeCursor(data);
                break;

            case LOADER_HEROES_TRAITS:
                if (data.moveToFirst()) {
                    int idx_attack = data.getColumnIndex(Database.hero_trait.attack);
                    int idx_ability = data.getColumnIndex(Database.hero_trait.ability);
                    int idx_durability = data.getColumnIndex(Database.hero_trait.durability);
                    int idx_mobility = data.getColumnIndex(Database.hero_trait.mobility);
                    int idx_difficulty = data.getColumnIndex(Database.hero_trait.difficulty);

                    trait_1_progress.setProgress(data.getInt(idx_attack));
                    trait_2_progress.setProgress(data.getInt(idx_ability));
                    trait_3_progress.setProgress(data.getInt(idx_durability));
                    trait_4_progress.setProgress(data.getInt(idx_mobility));
                    trait_5_progress.setProgress(data.getInt(idx_difficulty));
                }
                break;

            case LOADER_HEROES_AFFINITIES:
                GridLayoutManager llm2 = new GridLayoutManager(this, data.getCount(), LinearLayoutManager.VERTICAL, false);
                recycler_affinities.setHasFixedSize(true);
                recycler_affinities.setLayoutManager(llm2);
                recycler_affinities.setNestedScrollingEnabled(false);
                recycler_affinities.setAdapter(mAdapterAffinities);
                mAdapterAffinities.changeCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_HEROES_ABILITIES:
                mAdapterAbilities.swapCursor(null);
                break;
            case LOADER_HEROES_AFFINITIES:
                mAdapterAffinities.swapCursor(null);
                break;
        }
    }

}