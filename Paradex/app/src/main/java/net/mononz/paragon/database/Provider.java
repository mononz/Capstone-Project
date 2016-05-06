package net.mononz.paragon.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class Provider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "net.mononz.paragon";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private Database mOpenHelper;

    private static final int HEROES = 100;
    private static final int HEROES_BY_ID = 200;
    private static final int HERO_ABILITIES = 300;
    private static final int HERO_AFFINITIES = 400;
    private static final int HERO_TRAITS_BY_ID = 500;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, Database.heroes.TABLE_NAME, HEROES);
        matcher.addURI(authority, Database.heroes.TABLE_NAME + "/#", HEROES_BY_ID);
        matcher.addURI(authority, Database.hero_abilities.TABLE_NAME, HERO_ABILITIES);
        matcher.addURI(authority, Database.hero_affinities.TABLE_NAME, HERO_AFFINITIES);
        matcher.addURI(authority, Database.hero_traits.TABLE_NAME + "/#", HERO_TRAITS_BY_ID);
        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new Database(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri){
        final int match = sUriMatcher.match(uri);
        Log.d("Match", "" + match);
        switch (match) {
            case HEROES:
                return Database.heroes.CONTENT_DIR_TYPE;
            case HEROES_BY_ID:
                return Database.heroes.CONTENT_ITEM_TYPE;
            case HERO_ABILITIES:
                return Database.hero_abilities.CONTENT_DIR_TYPE;
            case HERO_AFFINITIES:
                return Database.hero_affinities.CONTENT_DIR_TYPE;
            case HERO_TRAITS_BY_ID:
                return Database.hero_traits.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor retCursor;
        Log.d("query", uri.toString());
        switch (sUriMatcher.match(uri)) {
            case HEROES:
                SQLiteQueryBuilder HEROES = new SQLiteQueryBuilder();
                HEROES.setTables(Database.heroes.TABLE_NAME +
                        " LEFT JOIN " + Database.types.TABLE_NAME + " ON " + Database.heroes.type_id + " = " + Database.types.FULL_ID);
                retCursor = HEROES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HEROES_BY_ID:
                SQLiteQueryBuilder HEROES_BY_ID = new SQLiteQueryBuilder();
                HEROES_BY_ID.setTables(Database.heroes.TABLE_NAME +
                        " LEFT JOIN " + Database.types.TABLE_NAME + " ON " + Database.heroes.type_id + " = " + Database.types.FULL_ID +
                        " LEFT JOIN " + Database.roles.TABLE_NAME + " ON " + Database.heroes.role_id + " = " + Database.roles.FULL_ID +
                        " LEFT JOIN " + Database.attacks.TABLE_NAME + " ON " + Database.heroes.attack_id + " = " + Database.attacks.FULL_ID +
                        " LEFT JOIN " + Database.scalings.TABLE_NAME + " ON " + Database.heroes.scaling_id + " = " + Database.scalings.FULL_ID);
                HEROES_BY_ID.appendWhere(Database.heroes.FULL_ID + " = " + uri.getLastPathSegment());
                retCursor = HEROES_BY_ID.query(mOpenHelper.getReadableDatabase(),
                        new String[]{Database.heroes.FULL_ID, Database.heroes.name, Database.heroes.tagline, Database.heroes.thumb,
                                Database.heroes.image,  Database.heroes.url, Database.heroes.video, Database.types.name,
                                Database.types.image,  Database.roles.name,  Database.attacks.name, Database.scalings.name},
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_ABILITIES:
                SQLiteQueryBuilder HERO_ABILITIES = new SQLiteQueryBuilder();
                HERO_ABILITIES.setTables(Database.hero_abilities.TABLE_NAME +
                        " LEFT JOIN " + Database.ability_types.TABLE_NAME + " ON " + Database.ability_types.FULL_ID + " = " + Database.hero_abilities.type);
                retCursor = HERO_ABILITIES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_AFFINITIES:
                SQLiteQueryBuilder HERO_AFFINITIES = new SQLiteQueryBuilder();
                HERO_AFFINITIES.setTables(Database.hero_affinities.TABLE_NAME +
                        " LEFT JOIN " + Database.affinities.TABLE_NAME + " ON " + Database.affinities.FULL_ID + " = " + Database.hero_affinities.affinity_id);
                retCursor = HERO_AFFINITIES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_TRAITS_BY_ID:
                SQLiteQueryBuilder HERO_TRAITS_BY_ID = new SQLiteQueryBuilder();
                HERO_TRAITS_BY_ID.setTables(Database.hero_traits.TABLE_NAME);
                HERO_TRAITS_BY_ID.appendWhere(Database.hero_traits.FULL_ID + " = " + uri.getLastPathSegment());
                retCursor = HERO_TRAITS_BY_ID.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

}