package net.mononz.paragon.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import net.mononz.paragon.Paradex;
import net.mononz.paragon.R;
import net.mononz.paragon.database.Database;
import net.mononz.paragon.gson.GsAbilityType;
import net.mononz.paragon.gson.GsAffinity;
import net.mononz.paragon.gson.GsAttack;
import net.mononz.paragon.gson.GsHero;
import net.mononz.paragon.gson.GsHeroAbility;
import net.mononz.paragon.gson.GsHeroAffinity;
import net.mononz.paragon.gson.GsHeroTrait;
import net.mononz.paragon.gson.GsRole;
import net.mononz.paragon.gson.GsScaling;
import net.mononz.paragon.gson.GsType;

import retrofit2.Call;
import retrofit2.Response;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String LOG_TAG = SyncAdapter.class.getSimpleName();

    private static final int notification_id = 0;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mNotifyManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getContext());
        mBuilder.setContentTitle(getContext().getString(R.string.app_name))
                .setContentText(getContext().getString(R.string.sync_database))
                .setColor(ContextCompat.getColor(getContext(), R.color.colorAccent))
                .setSmallIcon(R.mipmap.ic_notification);
        mBuilder.setProgress(0, 0, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        mNotifyManager.notify(notification_id, mBuilder.build());

        syncType();
        syncAbilityTypes();
        syncAffinity();
        syncAttack();
        syncHeroAbility();
        syncHeroAffinity();
        syncHeroTrait();
        syncRole();
        syncScaling();
        syncHero(); // do hero last

        Log.d("mNotifyManager", "done");

        SharedPreferences sharedpreferences = getContext().getSharedPreferences(Paradex.SyncPreferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(Paradex.LastUpdated, System.currentTimeMillis());
        editor.apply();

        mNotifyManager.cancel(notification_id);
    }

    private void syncAbilityTypes() {
        //updateNotification("AbilityType");
        Call<GsAbilityType[]> call = Paradex.network.AbilityType();
        try {
            Response<GsAbilityType[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsAbilityType gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.ability_type._id, gs._id);
                    values.put(Database.ability_type.name, gs.ability_type_name);
                    values.put(Database.ability_type.created_at, gs.created_at);
                    values.put(Database.ability_type.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.ability_type.CONTENT_URI, contentValues);
                Log.d("updated", "ability_type: " + numBulkInserted);
            } else {
                Log.e("ability_type", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncAffinity() {
        //updateNotification("Affinity");
        Call<GsAffinity[]> call = Paradex.network.Affinity();
        try {
            Response<GsAffinity[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsAffinity gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.affinity._id, gs._id);
                    values.put(Database.affinity.name, gs.affinity_name);
                    values.put(Database.affinity.image, gs.affinity_image);
                    values.put(Database.affinity.created_at, gs.created_at);
                    values.put(Database.affinity.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.affinity.CONTENT_URI, contentValues);
                Log.d("updated", "affinity: " + numBulkInserted);
            } else {
                Log.e("affinity", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncAttack() {
        //updateNotification("Attack");
        Call<GsAttack[]> call = Paradex.network.Attack();
        try {
            Response<GsAttack[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsAttack gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.attack._id, gs._id);
                    values.put(Database.attack.name, gs.attack_name);
                    values.put(Database.attack.created_at, gs.created_at);
                    values.put(Database.attack.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.attack.CONTENT_URI, contentValues);
                Log.d("updated", "attack: " + numBulkInserted);
            } else {
                Log.e("attack", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncHero() {
        //updateNotification("Hero");
        Call<GsHero[]> call = Paradex.network.Hero();
        try {
            Response<GsHero[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsHero gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.hero._id, gs._id);
                    values.put(Database.hero.name, gs.name);
                    values.put(Database.hero.tagline, gs.tagline);
                    values.put(Database.hero.thumb, gs.thumb);
                    values.put(Database.hero.image, gs.image);
                    values.put(Database.hero.video, gs.video);
                    values.put(Database.hero.url, gs.url);
                    values.put(Database.hero.sort, gs.sort);
                    values.put(Database.hero.type_id, gs.type_id);
                    values.put(Database.hero.role_id, gs.role_id);
                    values.put(Database.hero.attack_id, gs.attack_id);
                    values.put(Database.hero.scaling_id, gs.scaling_id);
                    values.put(Database.hero.created_at, gs.created_at);
                    values.put(Database.hero.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.hero.CONTENT_URI, contentValues);
                Log.d("updated", "hero: " + numBulkInserted);
            } else {
                Log.e("hero", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncHeroAbility() {
        //updateNotification("HeroAbility");
        Call<GsHeroAbility[]> call = Paradex.network.HeroAbility();
        try {
            Response<GsHeroAbility[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsHeroAbility gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.hero_ability._id, gs._id);
                    values.put(Database.hero_ability.heroes_id, gs.heroes_id);
                    values.put(Database.hero_ability.type, gs.type);
                    values.put(Database.hero_ability.name, gs.name);
                    values.put(Database.hero_ability.description, gs.description);
                    values.put(Database.hero_ability.image, gs.image);
                    values.put(Database.hero_ability.key_pc, gs.key_pc);
                    values.put(Database.hero_ability.key_ps, gs.key_ps);
                    values.put(Database.hero_ability.sort, gs.sort);
                    values.put(Database.hero_ability.created_at, gs.created_at);
                    values.put(Database.hero_ability.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.hero_ability.CONTENT_URI, contentValues);
                Log.d("updated", "hero_ability: " + numBulkInserted);
            } else {
                Log.e("hero_ability", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncHeroAffinity() {
        //updateNotification("HeroAffinity");
        Call<GsHeroAffinity[]> call = Paradex.network.HeroAffinity();
        try {
            Response<GsHeroAffinity[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsHeroAffinity gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.hero_affinity._id, gs._id);
                    values.put(Database.hero_affinity.heroes_id, gs.heroes_id);
                    values.put(Database.hero_affinity.affinity_id, gs.affinity_id);
                    values.put(Database.hero_affinity.created_at, gs.created_at);
                    values.put(Database.hero_affinity.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.hero_affinity.CONTENT_URI, contentValues);
                Log.d("updated", "hero_ability: " + numBulkInserted);
            } else {
                Log.e("hero_ability", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncHeroTrait() {
        //updateNotification("HeroTrait");
        Call<GsHeroTrait[]> call = Paradex.network.HeroTrait();
        try {
            Response<GsHeroTrait[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsHeroTrait gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.hero_trait._id, gs._id);
                    values.put(Database.hero_trait.attack, gs.attack);
                    values.put(Database.hero_trait.ability, gs.ability);
                    values.put(Database.hero_trait.durability, gs.durability);
                    values.put(Database.hero_trait.mobility, gs.mobility);
                    values.put(Database.hero_trait.difficulty, gs.difficulty);
                    values.put(Database.hero_trait.created_at, gs.created_at);
                    values.put(Database.hero_trait.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.hero_trait.CONTENT_URI, contentValues);
                Log.d("updated", "hero_trait: " + numBulkInserted);
            } else {
                Log.e("hero_trait", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncRole() {
        //updateNotification("Role");
        Call<GsRole[]> call = Paradex.network.Role();
        try {
            Response<GsRole[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsRole gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.role._id, gs._id);
                    values.put(Database.role.name, gs.role_name);
                    values.put(Database.role.created_at, gs.created_at);
                    values.put(Database.role.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.role.CONTENT_URI, contentValues);
                Log.d("updated", "role: " + numBulkInserted);
            } else {
                Log.e("role", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncScaling() {
        //updateNotification("Scaling");
        Call<GsScaling[]> call = Paradex.network.Scaling();
        try {
            Response<GsScaling[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsScaling gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.scaling._id, gs._id);
                    values.put(Database.scaling.name, gs.scaling_name);
                    values.put(Database.scaling.created_at, gs.created_at);
                    values.put(Database.scaling.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.scaling.CONTENT_URI, contentValues);
                Log.d("updated", "scaling: " + numBulkInserted);
            } else {
                Log.e("scaling", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncType() {
        //updateNotification("Type");
        Call<GsType[]> call = Paradex.network.Type();
        try {
            Response<GsType[]> response = call.execute();
            if (response.isSuccessful()) {
                ContentValues[] contentValues = new ContentValues[response.body().length];
                for (int i=0; i<response.body().length; i++) {
                    GsType gs = response.body()[i];
                    ContentValues values = new ContentValues();
                    values.put(Database.type._id, gs._id);
                    values.put(Database.type.name, gs.type_name);
                    values.put(Database.type.image, gs.type_image);
                    values.put(Database.type.created_at, gs.created_at);
                    values.put(Database.type.updated_at, gs.updated_at);
                    contentValues[i] = values;
                }
                int numBulkInserted = getContext().getContentResolver().bulkInsert(Database.type.CONTENT_URI, contentValues);
                Log.d("updated", "type: " + numBulkInserted);
            } else {
                Log.e("type", response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncImmediately(Context context) {
        Log.d(LOG_TAG, "syncImmediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;
            onAccountCreated(newAccount);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount) {
        Log.d(LOG_TAG, "Account created - " + newAccount.name);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    private void updateNotification(String text) {
        mBuilder.setContentText("Updating " + text);
        mNotifyManager.notify(notification_id, mBuilder.build());
    }

}