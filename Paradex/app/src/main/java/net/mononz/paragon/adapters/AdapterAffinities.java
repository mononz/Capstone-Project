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

import net.mononz.paragon.Paragon;
import net.mononz.paragon.R;
import net.mononz.paragon.library.CursorRecAdapter;
import net.mononz.paragon.database.Database;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdapterAffinities extends CursorRecAdapter<AdapterAffinities.ShowViewHolder> {

    private Context mContext;

    public AdapterAffinities(Context mContext, Cursor cursor) {
        super(cursor);
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(ShowViewHolder viewHolder, final Cursor cursor) {

        int idx_name = cursor.getColumnIndex(Database.affinities.name);
        int idx_image = cursor.getColumnIndex(Database.affinities.image);

        Glide.with(mContext)
                .load(Uri.parse(Paragon.ASSET_PATH + cursor.getString(idx_image)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .crossFade()
                .into(viewHolder.vImage);

        viewHolder.vName.setText(cursor.getString(idx_name));
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_affinities, viewGroup, false);
        return new ShowViewHolder(itemView);
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.affinity_image) ImageView vImage;
        @Bind(R.id.affinity_name) TextView vName;

        public ShowViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

}