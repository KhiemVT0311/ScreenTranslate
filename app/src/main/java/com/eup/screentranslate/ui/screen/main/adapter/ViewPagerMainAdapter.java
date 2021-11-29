package com.eup.screentranslate.ui.screen.main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.eup.screentranslate.ui.screen.conversation.ConversationFragment;
import com.eup.screentranslate.ui.screen.more.MoreFragment;
import com.eup.screentranslate.ui.screen.screentrans.ScreenTransFragment;
import com.eup.screentranslate.ui.screen.translate.TranslateFragment;

import java.util.List;
import java.util.Vector;

public class ViewPagerMainAdapter extends FragmentStatePagerAdapter {
    List<Fragment> registeredFragments;
    FragmentManager fm;
    public ViewPagerMainAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
        registeredFragments = new Vector<>(4);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ScreenTransFragment();
            case 1:
                return new TranslateFragment();
            case 2:
                return new ConversationFragment();
            case 3:
                return new MoreFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public Fragment getRegisteredFragment(int position){
        return registeredFragments.get(position);
    }
}
