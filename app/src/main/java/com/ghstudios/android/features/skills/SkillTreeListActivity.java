package com.ghstudios.android.features.skills;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ghstudios.android.features.armorsetbuilder.detail.ASBPagerActivity;
import com.ghstudios.android.mhgendatabase.R;
import com.ghstudios.android.GenericActivity;
import com.ghstudios.android.MenuSection;

public class SkillTreeListActivity extends GenericActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.skills);

        // Tag as top level activity (if not coming from asb)
        boolean fromAsb = getIntent().getBooleanExtra(ASBPagerActivity.EXTRA_FROM_TALISMAN_EDITOR, false);
        if (!fromAsb) {
            super.setAsTopLevel();
        }
    }

    @Override
    protected int getSelectedSection() {
        return MenuSection.SKILL_TREES;
    }

    @Override
    protected Fragment createFragment() {
        return new SkillTreeListFragment();
    }

}
