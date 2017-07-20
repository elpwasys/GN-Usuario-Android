package br.com.wasys.gn.usuario.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.wasys.gn.usuario.R;

import br.com.elp.library.fragment.AppFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnicoFragment extends AppFragment {


    private int opcaoMenu;
    private String colaboradorId;

    public UnicoFragment() {

    }

    public static UnicoFragment newInstance(int opcaoMenu, String colaboradorId) {
        UnicoFragment fragment = new UnicoFragment();
        fragment.opcaoMenu = opcaoMenu;
        fragment.colaboradorId = colaboradorId;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unico, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        IdaEVolta fragment = new IdaEVolta();
        fragment.opcao_menu = opcaoMenu;
        fragment.colaborador_final_id = colaboradorId;
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //transaction.addToBackStack(null);
        transaction.replace(R.id.layout_unico, fragment);
        transaction.commit();
    }
}
