package com.ghstudios.android.features.monsters.detail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ghstudios.android.data.classes.Monster;
import com.ghstudios.android.data.classes.MonsterDamage;
import com.ghstudios.android.data.database.DataManager;
import com.ghstudios.android.loader.MonsterLoader;
import com.ghstudios.android.mhgendatabase.R;

public class MonsterDamageFragment extends Fragment {
    private static final String ARG_MONSTER_ID = "MONSTER_ID";

    private Bundle mBundle;
    
    private Monster mMonster;
    
    private TextView mMonsterLabelTextView;
    private ImageView mMonsterIconImageView;
    
    private LinearLayout mWeaponDamageTL, mElementalDamageTL;
    private View mDividerView;

    private ImageView mCutImageView, mImpactImageView, mShotImageView, mKOImageView;
    private ImageView mFireImageView, mWaterImageView, mIceImageView, 
        mThunderImageView, mDragonImageView;
    
    public static MonsterDamageFragment newInstance(long monsterId) {
        Bundle args = new Bundle();
        args.putLong(ARG_MONSTER_ID, monsterId);
        MonsterDamageFragment f = new MonsterDamageFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        
        // Check for a Monster ID as an argument, and find the monster
        Bundle args = getArguments();
        if (args != null) {
            long monsterId = args.getLong(ARG_MONSTER_ID, -1);
            if (monsterId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(R.id.monster_detail_fragment, args, new MonsterLoaderCallbacks());
            }
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monster_damage, container, false);
        
        mMonsterLabelTextView = (TextView) view.findViewById(R.id.detail_monster_label);
        mMonsterIconImageView = (ImageView) view.findViewById(R.id.detail_monster_image);

        mCutImageView = (ImageView) view.findViewById(R.id.cut);
        mImpactImageView = (ImageView) view.findViewById(R.id.impact);
        mShotImageView = (ImageView) view.findViewById(R.id.shot);
        mKOImageView = (ImageView) view.findViewById(R.id.ko);
        mFireImageView = (ImageView) view.findViewById(R.id.fire);
        mWaterImageView = (ImageView) view.findViewById(R.id.water);
        mIceImageView = (ImageView) view.findViewById(R.id.ice);
        mThunderImageView = (ImageView) view.findViewById(R.id.thunder);
        mDragonImageView = (ImageView) view.findViewById(R.id.dragon);
    
        mWeaponDamageTL = (LinearLayout) view.findViewById(R.id.weapon_damage);
        mElementalDamageTL = (LinearLayout) view.findViewById(R.id.elemental_damage);

        mDividerView = view.findViewById(R.id.divider);
        
        return view;
    }
    
    private void updateUI() {
        String cellText = mMonster.getName();
        String cellImage = "icons_monster/" + mMonster.getFileLocation();
        
        mMonsterLabelTextView.setText(cellText);
        AssetManager manager = getActivity().getAssets();
        try {
            InputStream open = manager.open(cellImage);
            Bitmap bitmap = BitmapFactory.decodeStream(open);
            // Assign the bitmap to an ImageView in this layout
            mMonsterIconImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Draw Drawable
        mCutImageView.setImageResource(R.drawable.cut);
        mImpactImageView.setImageResource(R.drawable.impact);
        mShotImageView.setImageResource(R.drawable.shot);
        mKOImageView.setImageResource(R.drawable.stun);
        mFireImageView.setImageResource(R.drawable.fire);
        mWaterImageView.setImageResource(R.drawable.water);
        mIceImageView.setImageResource(R.drawable.ice);
        mThunderImageView.setImageResource(R.drawable.thunder);
        mDragonImageView.setImageResource(R.drawable.dragon);
        
        ArrayList<MonsterDamage> damages = 
                DataManager.get(getActivity()).queryMonsterDamageArray(mMonster.getId());

        MonsterDamage damage = null;
        String body_part, cut, impact, shot, ko, fire, water, ice, thunder, dragon;
        
        LayoutInflater inflater = getLayoutInflater(mBundle);

        // build each row of both tables per record
        for(int i = 0; i < damages.size(); i++) {
            LinearLayout wdRow = (LinearLayout) inflater.inflate(
                    R.layout.fragment_monster_damage_listitem, mWeaponDamageTL, false);
            LinearLayout edRow = (LinearLayout) inflater.inflate(
                    R.layout.fragment_monster_damage_listitem, mElementalDamageTL, false);
              
            damage = damages.get(i);
            
            body_part = damage.getBodyPart();

            // Table 1
            TextView body_part_tv1 = (TextView) wdRow.findViewById(R.id.body_part);
            TextView dummy_tv = (TextView) wdRow.findViewById(R.id.dmg1);
            TextView cut_tv = (TextView) wdRow.findViewById(R.id.dmg2);
            TextView impact_tv = (TextView) wdRow.findViewById(R.id.dmg3);
            TextView shot_tv = (TextView) wdRow.findViewById(R.id.dmg4);
            TextView ko_tv = (TextView) wdRow.findViewById(R.id.dmg5);

            // Table 2
            TextView body_part_tv2 = (TextView) edRow.findViewById(R.id.body_part);
            TextView fire_tv = (TextView) edRow.findViewById(R.id.dmg1);
            TextView water_tv = (TextView) edRow.findViewById(R.id.dmg2);
            TextView ice_tv = (TextView) edRow.findViewById(R.id.dmg3);
            TextView thunder_tv = (TextView) edRow.findViewById(R.id.dmg4);
            TextView dragon_tv = (TextView) edRow.findViewById(R.id.dmg5);


            SpannableString s = new SpannableString(body_part);

            if(body_part.contains("("))
            {
                int start = body_part.indexOf("(");
                int end = body_part.length();
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.text_color_secondary)),start,end,0);
                s.setSpan(new RelativeSizeSpan(.8f),start,end,0);
            }


            body_part_tv1.setText(s);
            body_part_tv2.setText(s);


            checkDamageValue(damage.getCut(),cut_tv,false,false);
            checkDamageValue(damage.getImpact(),impact_tv,false,false);
            checkDamageValue(damage.getShot(),shot_tv,false,false);
            checkDamageValue(damage.getKo(),ko_tv,false,true);

            checkDamageValue(damage.getFire(),fire_tv,true,false);
            checkDamageValue(damage.getWater(),water_tv,true,false);
            checkDamageValue(damage.getIce(),ice_tv,true,false);
            checkDamageValue(damage.getThunder(),thunder_tv,true,false);
            checkDamageValue(damage.getDragon(),dragon_tv,true,false);

            dummy_tv.setText("");

            mWeaponDamageTL.addView(wdRow);
            mElementalDamageTL.addView(edRow);
        }
    }

    // Loader to load data for this monster
    private class MonsterLoaderCallbacks implements LoaderCallbacks<Monster> {
        
        @Override
        public Loader<Monster> onCreateLoader(int id, Bundle args) {
            return new MonsterLoader(getActivity(), args.getLong(ARG_MONSTER_ID));
        }
        
        @Override
        public void onLoadFinished(Loader<Monster> loader, Monster run) {
            mMonster = run;
            LoaderManager lm = getLoaderManager();
            Bundle args = new Bundle();
            args.putLong(ARG_MONSTER_ID, run.getId());

            updateUI();
        }
        
        @Override
        public void onLoaderReset(Loader<Monster> loader) {
            // Do nothing
        }
    }
    
    private String checkDamageValue(int damage,TextView tv, boolean element, boolean isKO) {
        String ret = Integer.toString(damage);
        if(damage<=0)
            ret = "-";

        tv.setText(ret);

        if(!isKO && (!element && damage>=45) || (element && damage>=25))
            tv.setTypeface(null,Typeface.BOLD);

        return ret;
    }
}
