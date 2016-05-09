package net.mononz.paragon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.mononz.paragon.sync.SyncAdapter;

public class Fragment_Settings extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference pref_sync = findPreference(getString(R.string.pref_sync_key));
        pref_sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                SyncAdapter.initializeSyncAdapter(getActivity());
                SyncAdapter.syncImmediately(getActivity());
                return true;
            }
        });

        Preference pref_memory = findPreference(getString(R.string.pref_images_key));
        pref_memory.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Glide.get(getActivity()).clearMemory();
                Toast.makeText(getActivity(), getString(R.string.pref_images_toast), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        Preference pref_legal = findPreference(getString(R.string.pref_legal_key));
        pref_legal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.pref_legal_summary))
                        .setMessage(getString(R.string.pref_legal_toast))
                        .setPositiveButton(getString(R.string.okay),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.pref_legal_download),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.pref_legal_download_link))));
                            }
                        })
                        .show();
                return true;
            }
        });

        Preference pref_email = findPreference(getString(R.string.pref_email_key));
        pref_email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.settings_email_address)});
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_email_subject));
                email.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_email_body));
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, getString(R.string.settings_email_choose)));
                return true;
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(android.R.id.list).setVerticalScrollBarEnabled(false);
    }

}