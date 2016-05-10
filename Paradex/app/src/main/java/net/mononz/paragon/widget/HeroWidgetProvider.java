package net.mononz.paragon.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import net.mononz.paragon.Paradex;
import net.mononz.paragon.R;
import net.mononz.paragon.database.Database;

public class HeroWidgetProvider extends AppWidgetProvider {

    private String lastHeroID = "0";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_hero);

            // lets pull out a random row from the hero table
            Cursor cursor = context.getApplicationContext().getContentResolver().query(Database.hero.CONTENT_URI,
                    new String[]{Database.hero.FULL_ID, Database.hero.name, Database.hero.thumb, Database.hero.image, Database.type.name },
                    Database.hero.FULL_ID + " IS NOT ?", new String[]{ lastHeroID }, "RANDOM() LIMIT 1");

            //DatabaseUtils.dumpCursor(cursor);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        // store last hero id so the next random row won't be the same hero
                        int idx_id = cursor.getColumnIndex(Database.hero.FULL_ID);
                        lastHeroID = cursor.getString(idx_id);

                        int idx_name = cursor.getColumnIndex(Database.hero.name);
                        int idx_type = cursor.getColumnIndex(Database.type.name);
                        // randomly use either the avatar thumb or teh full banner hero image
                        int idx_image = (Paradex.getRandomBoolean())
                                ? cursor.getColumnIndex(Database.hero.thumb)
                                : cursor.getColumnIndex(Database.hero.image);
                        remoteViews.setTextViewText(R.id.name, cursor.getString(idx_name));
                        remoteViews.setTextViewText(R.id.type, cursor.getString(idx_type));

                        AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, remoteViews, R.id.portrait, appWidgetIds);
                        Glide.with(context.getApplicationContext())
                                .load(Paradex.ASSET_PATH + cursor.getString(idx_image))
                                .asBitmap()
                                .into(appWidgetTarget);
                    }
                }
                cursor.close();
            }

            Intent intent = new Intent(context, HeroWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.portrait, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}