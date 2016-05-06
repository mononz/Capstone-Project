package net.mononz.paragon.sync;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import net.mononz.paragon.database.Database;

public class Provider extends ContentProvider {

    public static final String CONTENT_AUTHORITY = "net.mononz.paragon";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private Database mOpenHelper;

    private static final int ABILITY_TYPE = 1100;
    private static final int AFFINITY = 1200;
    private static final int ATTACK = 13000;
    private static final int HERO = 1400;
    private static final int HERO_ABILITY = 1500;
    private static final int HERO_AFFINITY = 1600;
    private static final int HERO_TRAIT = 1700;
    private static final int ROLE = 1800;
    private static final int SCALING = 1900;
    private static final int TYPE = 2000;

    private static final int HEROES_BY_ID = 1410;
    private static final int HERO_TRAITS_BY_ID = 1710;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, Database.ability_type.TABLE_NAME, ABILITY_TYPE);
        matcher.addURI(authority, Database.affinity.TABLE_NAME, AFFINITY);
        matcher.addURI(authority, Database.attack.TABLE_NAME, ATTACK);
        matcher.addURI(authority, Database.hero.TABLE_NAME, HERO);
        matcher.addURI(authority, Database.hero_ability.TABLE_NAME, HERO_ABILITY);
        matcher.addURI(authority, Database.hero_affinity.TABLE_NAME, HERO_AFFINITY);
        matcher.addURI(authority, Database.hero_trait.TABLE_NAME, HERO_TRAIT);
        matcher.addURI(authority, Database.role.TABLE_NAME, ROLE);
        matcher.addURI(authority, Database.scaling.TABLE_NAME, SCALING);
        matcher.addURI(authority, Database.type.TABLE_NAME, TYPE);

        matcher.addURI(authority, Database.hero.TABLE_NAME + "/#", HEROES_BY_ID);
        matcher.addURI(authority, Database.hero_trait.TABLE_NAME + "/#", HERO_TRAITS_BY_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new Database(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ABILITY_TYPE:
                return Database.ability_type.CONTENT_DIR_TYPE;
            case AFFINITY:
                return Database.affinity.CONTENT_DIR_TYPE;
            case ATTACK:
                return Database.attack.CONTENT_DIR_TYPE;
            case HERO:
                return Database.hero.CONTENT_DIR_TYPE;
            case HERO_ABILITY:
                return Database.hero_ability.CONTENT_DIR_TYPE;
            case HERO_TRAIT:
                return Database.hero_trait.CONTENT_DIR_TYPE;
            case ROLE:
                return Database.role.CONTENT_DIR_TYPE;
            case SCALING:
                return Database.scaling.CONTENT_DIR_TYPE;
            case TYPE:
                return Database.type.CONTENT_DIR_TYPE;

            case HEROES_BY_ID:
                return Database.hero.CONTENT_ITEM_TYPE;
            case HERO_TRAITS_BY_ID:
                return Database.hero_trait.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        Log.d("query", uri.toString());
        switch (sUriMatcher.match(uri)) {
            case HERO:
                SQLiteQueryBuilder HEROES = new SQLiteQueryBuilder();
                HEROES.setTables(Database.hero.TABLE_NAME +
                        " LEFT JOIN " + Database.type.TABLE_NAME + " ON " + Database.hero.type_id + " = " + Database.type.FULL_ID);
                retCursor = HEROES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HEROES_BY_ID:
                SQLiteQueryBuilder HEROES_BY_ID = new SQLiteQueryBuilder();
                HEROES_BY_ID.setTables(Database.hero.TABLE_NAME +
                        " LEFT JOIN " + Database.type.TABLE_NAME + " ON " + Database.hero.type_id + " = " + Database.type.FULL_ID +
                        " LEFT JOIN " + Database.role.TABLE_NAME + " ON " + Database.hero.role_id + " = " + Database.role.FULL_ID +
                        " LEFT JOIN " + Database.attack.TABLE_NAME + " ON " + Database.hero.attack_id + " = " + Database.attack.FULL_ID +
                        " LEFT JOIN " + Database.scaling.TABLE_NAME + " ON " + Database.hero.scaling_id + " = " + Database.scaling.FULL_ID);
                HEROES_BY_ID.appendWhere(Database.hero.FULL_ID + " = " + uri.getLastPathSegment());
                retCursor = HEROES_BY_ID.query(mOpenHelper.getReadableDatabase(),
                        new String[]{Database.hero.FULL_ID, Database.hero.name, Database.hero.tagline, Database.hero.thumb,
                                Database.hero.image,  Database.hero.url, Database.hero.video, Database.type.name,
                                Database.type.image,  Database.role.name,  Database.attack.name, Database.scaling.name},
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_ABILITY:
                SQLiteQueryBuilder HERO_ABILITIES = new SQLiteQueryBuilder();
                HERO_ABILITIES.setTables(Database.hero_ability.TABLE_NAME +
                        " LEFT JOIN " + Database.ability_type.TABLE_NAME + " ON " + Database.ability_type.FULL_ID + " = " + Database.hero_ability.type);
                retCursor = HERO_ABILITIES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_AFFINITY:
                SQLiteQueryBuilder HERO_AFFINITIES = new SQLiteQueryBuilder();
                HERO_AFFINITIES.setTables(Database.hero_affinity.TABLE_NAME +
                        " LEFT JOIN " + Database.affinity.TABLE_NAME + " ON " + Database.affinity.FULL_ID + " = " + Database.hero_affinity.affinity_id);
                retCursor = HERO_AFFINITIES.query(mOpenHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case HERO_TRAITS_BY_ID:
                SQLiteQueryBuilder HERO_TRAITS_BY_ID = new SQLiteQueryBuilder();
                HERO_TRAITS_BY_ID.setTables(Database.hero_trait.TABLE_NAME);
                HERO_TRAITS_BY_ID.appendWhere(Database.hero_trait.FULL_ID + " = " + uri.getLastPathSegment());
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
        int numReplaced;
        switch (sUriMatcher.match(uri)) {
            case ABILITY_TYPE:
                numReplaced = replaceStub(values, Database.ability_type.TABLE_NAME);
                break;
            case AFFINITY:
                numReplaced = replaceStub(values, Database.affinity.TABLE_NAME);
                break;
            case ATTACK:
                numReplaced = replaceStub(values, Database.attack.TABLE_NAME);
                break;
            case HERO:
                numReplaced = replaceStub(values, Database.hero.TABLE_NAME);
                break;
            case HERO_ABILITY:
                numReplaced = replaceStub(values, Database.hero_ability.TABLE_NAME);
                break;
            case HERO_AFFINITY:
                numReplaced = replaceStub(values, Database.hero_affinity.TABLE_NAME);
                break;
            case HERO_TRAIT:
                numReplaced = replaceStub(values, Database.hero_trait.TABLE_NAME);
                break;
            case ROLE:
                numReplaced = replaceStub(values, Database.role.TABLE_NAME);
                break;
            case SCALING:
                numReplaced = replaceStub(values, Database.scaling.TABLE_NAME);
                break;
            case TYPE:
                numReplaced = replaceStub(values, Database.type.TABLE_NAME);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numReplaced > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numReplaced;
    }

    private int replaceStub(ContentValues[] values, String table_name) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        int numReplaced = 0;
        try {
            for(ContentValues value : values){
                if (value == null) {
                    throw new IllegalArgumentException("Cannot have null content values");
                }
                long _id = 0;
                try {
                    _id = db.replace(table_name, null, value);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
                if (_id != 0) {
                    numReplaced++;
                }
            }
            if (numReplaced > 0) {
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        return numReplaced;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

}