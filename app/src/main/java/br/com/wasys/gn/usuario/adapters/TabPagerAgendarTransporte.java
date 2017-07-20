package br.com.wasys.gn.usuario.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.wasys.gn.usuario.fragments.Multiplos;
import br.com.wasys.gn.usuario.fragments.UnicoFragment;

/**
 * Created by fernandamoncores on 4/3/16.
 */
public class TabPagerAgendarTransporte extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    int opcao_menu;
    String colaborador_id;

    public TabPagerAgendarTransporte(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    public TabPagerAgendarTransporte(FragmentManager fm, int NumOfTabs, int opcao, String colaborador_id) {
        super(fm);
        this.opcao_menu = opcao;
        this.mNumOfTabs = NumOfTabs;
        this.colaborador_id = colaborador_id;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                UnicoFragment fragment = UnicoFragment.newInstance(opcao_menu, colaborador_id);
                return fragment;
            case 1:
                Multiplos obj_m = new Multiplos();
                obj_m.colaborador_final_id = this.colaborador_id;
                return obj_m;

        }
        return null;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNumOfTabs; //No of Tabs
    }

}

