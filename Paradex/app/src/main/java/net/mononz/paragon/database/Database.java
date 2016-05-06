package net.mononz.paragon.database;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "database.sqlite3";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 2);
        setForcedUpgrade();
    }

    public static final class ability_types implements BaseColumns {
        public static final String TABLE_NAME = "ability_types";

        public static final String _id = "_id";
        public static final String name = "ability_type_name";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

    public static final class affinities implements BaseColumns {
        public static final String TABLE_NAME = "affinities";

        public static final String _id = "_id";
        public static final String name = "affinity_name";
        public static final String image = "affinity_image";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

    public static final class attacks implements BaseColumns {
        public static final String TABLE_NAME = "attacks";

        public static final String _id = "_id";
        public static final String name = "attack_name";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

    public static final class hero_abilities implements BaseColumns {
        public static final String TABLE_NAME = "hero_abilities";

        public static final String _id = "_id";
        public static final String heroes_id = "heroes_id";
        public static final String type = "type";
        public static final String name = "name";
        public static final String description = "description";
        public static final String image = "image";
        public static final String sort = "sort";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
        public static final String FULL_HEROES_ID = TABLE_NAME + "." + heroes_id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero_affinities implements BaseColumns {
        public static final String TABLE_NAME = "hero_affinities";

        public static final String _id = "_id";
        public static final String heroes_id = "heroes_id";
        public static final String affinity_id = "affinity_id";

        public static final String FULL_HEROES_ID = TABLE_NAME + "." + heroes_id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class hero_traits implements BaseColumns {
        public static final String TABLE_NAME = "hero_traits";

        public static final String _id = "_id";
        public static final String attack = "attack";
        public static final String ability = "ability";
        public static final String durability = "durability";
        public static final String mobility = "mobility";
        public static final String difficulty = "difficulty";

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class heroes implements BaseColumns {
        public static final String TABLE_NAME = "heroes";

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

        public static final String FULL_ID = TABLE_NAME + "." + _id;

        public static final Uri CONTENT_URI = Provider.BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Provider.CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class roles implements BaseColumns {
        public static final String TABLE_NAME = "roles";

        public static final String _id = "_id";
        public static final String name = "role_name";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

    public static final class scalings implements BaseColumns {
        public static final String TABLE_NAME = "scalings";

        public static final String _id = "_id";
        public static final String name = "scaling_name";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

    public static final class types implements BaseColumns {
        public static final String TABLE_NAME = "types";

        public static final String _id = "_id";
        public static final String name = "type_name";
        public static final String image = "type_image";

        public static final String FULL_ID = TABLE_NAME + "." + _id;
    }

}