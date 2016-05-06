package net.mononz.paragon.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import net.mononz.paragon.Paradex;
import net.mononz.paragon.R;
import net.mononz.paragon.library.CursorRecAdapter;
import net.mononz.paragon.database.Database;
import net.mononz.paragon.library.svg.SvgDecoder;
import net.mononz.paragon.library.svg.SvgDrawableTranscoder;
import net.mononz.paragon.library.svg.SvgSoftwareLayerSetter;

import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdapterAbilities extends CursorRecAdapter<AdapterAbilities.ShowViewHolder> {

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public AdapterAbilities(Context mContext, Cursor cursor) {
        super(cursor);

        requestBuilder = Glide.with(mContext)
                .using(Glide.buildStreamModelLoader(Uri.class, mContext), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    @Override
    public void onBindViewHolder(ShowViewHolder viewHolder, final Cursor cursor) {

        int idx_name = cursor.getColumnIndex(Database.hero_ability.name);
        int idx_type = cursor.getColumnIndex(Database.ability_type.name);
        int idx_description = cursor.getColumnIndex(Database.hero_ability.description);
        int idx_image = cursor.getColumnIndex(Database.hero_ability.image);

        final String image = cursor.getString(idx_image);
        final String name = cursor.getString(idx_name);
        final String type = cursor.getString(idx_type);
        final String description = cursor.getString(idx_description);

        requestBuilder
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(Uri.parse(Paradex.ASSET_PATH + image))
                .into(viewHolder.vImage);

        viewHolder.vType.setText(type);
        viewHolder.vName.setText(name);
        viewHolder.vDescription.setText(description);
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_abilities, viewGroup, false);
        return new ShowViewHolder(itemView);
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        View vView;
        @Bind(R.id.ability_image) ImageView vImage;
        @Bind(R.id.ability_name) TextView vName;
        @Bind(R.id.ability_type) TextView vType;
        @Bind(R.id.ability_description) TextView vDescription;

        public ShowViewHolder(View v) {
            super(v);
            vView = v;
            ButterKnife.bind(this, v);
        }
    }

}