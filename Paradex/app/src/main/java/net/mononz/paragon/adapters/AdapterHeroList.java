package net.mononz.paragon.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import net.mononz.paragon.Paradex;
import net.mononz.paragon.R;
import net.mononz.paragon.library.CursorRecAdapter;
import net.mononz.paragon.database.Database;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdapterHeroList extends CursorRecAdapter<AdapterHeroList.ShowViewHolder> {

    private Context mContext;
    private Callback mCallback;

    public AdapterHeroList(Context mContext, Cursor cursor, Callback callback) {
        super(cursor);
        this.mContext = mContext;
        this.mCallback = callback;
    }

    @Override
    public void onBindViewHolder(final ShowViewHolder viewHolder, final Cursor cursor) {

        int idx_id = cursor.getColumnIndex(Database.hero._id);
        int idx_name = cursor.getColumnIndex(Database.hero.name);
        int idx_thumb = cursor.getColumnIndex(Database.hero.thumb);
        int idx_type = cursor.getColumnIndex(Database.type.name);

        final long _id = cursor.getLong(idx_id);
        final String thumb = cursor.getString(idx_thumb);
        final String type = cursor.getString(idx_type);
        final String name = cursor.getString(idx_name);

        viewHolder.vPortrait.setContentDescription(name);

        Glide.with(mContext)
                .load(Uri.parse(Paradex.ASSET_PATH + thumb))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .into(viewHolder.vPortrait);

        viewHolder.vType.setText(type);
        viewHolder.vName.setText(name);

        viewHolder.vView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.openHero(_id, name, viewHolder.vPortrait);
            }
        });
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_hero, viewGroup, false);
        return new ShowViewHolder(itemView);
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        View vView;
        @Bind(R.id.portrait) ImageView vPortrait;
        @Bind(R.id.type) TextView vType;
        @Bind(R.id.name) TextView vName;

        public ShowViewHolder(View v) {
            super(v);
            vView = v;
            ButterKnife.bind(this, v);
        }
    }

    public interface Callback {
        void openHero(long _id, String name, View view);
    }

}