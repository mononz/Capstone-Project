package net.mononz.paragon.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import net.mononz.paragon.sync.Provider;

public class Database extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "database.sqlite3";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 3);
        // 1 - Initial non sync'd
        // 2 - Iggy Added
        // 3 - Syncable
        setForcedUpgrade();
    }

    public static final class ability_type implements BaseColumns {
        public static final String TABLE_NAME = "ability_type";

        public static final String _id = "_id";
        public static final String name = "ability_type_name";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class affinity implements BaseColumns {
        public static final String TABLE_NAME = "affinity";

        public static final String _id = "_id";
        public static final String name = "affinity_name";
        public static final String image = "affinity_image";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class attack implements BaseColumns {
        public static final String TABLE_NAME = "attack";

        public static final String _id = "_id";
        public static final String name = "attack_name";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero implements BaseColumns {
        public static final String TABLE_NAME = "hero";

        public static final String _id = "_id";
        public static final String name = "name";
        public static final String tagline = "tagline";
        public static final String thumb = "thumb";
        public static final String image = "image";
        public static final String video = "video";
        public static final String url = "url";
        public static final String sort = "sort";
        public static final String type_id = "type_id";
        public static final String role_id = "role_id";
        public static final String attack_id = "attack_id";
        public static final String scaling_id = "scaling_id";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero_ability implements BaseColumns {
        public static final String TABLE_NAME = "hero_ability";

        public static final String _id = "_id";
        public static final String heroes_id = "heroes_id";
        public static final String type = "type";
        public static final String name = "name";
        public static final String description = "description";
        public static final String image = "image";
        public static final String key_pc = "key_pc";
        public static final String key_ps = "key_ps";
        public static final String sort = "sort";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
        public static final String FULL_HEROES_ID = TABLE_NAME + "." + heroes_id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero_affinity implements BaseColumns {
        public static final String TABLE_NAME = "hero_affinity";

        public static final String _id = "_id";
        public static final String heroes_id = "heroes_id";
        public static final String affinity_id = "affinity_id";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_HEROES_ID = TABLE_NAME + "." + heroes_id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero_trait implements BaseColumns {
        public static final String TABLE_NAME = "hero_trait";

        public static final String _id = "_id";
        public static final String attack = "attack";
        public static final String ability = "ability";
        public static final String durability = "durability";
        public static final String mobility = "mobility";
        public static final String difficulty = "difficulty";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class role implements BaseColumns {
        public static final String TABLE_NAME = "role";

        public static final String _id = "_id";
        public static final String name = "role_name";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class scaling implements BaseColumns {
        public static final String TABLE_NAME = "scaling";

        public static final String _id = "_id";
        public static final String name = "scaling_name";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class type implements BaseColumns {
        public static final String TABLE_NAME = "type";

        public static final String _id = "_id";
        public static final String name = "type_name";
        public static final String image = "type_image";
        public static final String created_at = "created_at";
        public static final String updated_at = "updated_at";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

}