package br.com.wasys.gn.usuario.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.adapters.TabPagerAgendarTransporte;
import br.com.wasys.gn.usuario.helpers.Helper;

import butterknife.ButterKnife;

public class EscolherTransporteActivity extends AppCompatActivity  {

    private EditText txt_data_agendamento;
    private EditText txt_horario_agendamento;
    private int opcao_menu = 0;
    private String colaborador_final_id = "";
    public static int MULIPLOS = 1;
    public static int IDA_E_VOLTA = 0;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleActionBar();
        setContentView(R.layout.activity_choose_transport);
        ButterKnife.bind(this);
        this.ctx = this;
        Bundle b = getIntent().getExtras();
        opcao_menu = b.getInt("opcao");
        colaborador_final_id = b.getString("colaborador_id", "");
        setupTabs();
    }

    public void setupTabs()
    {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        final TabLayout.Tab ida_volta = tabLayout.newTab().setText("Ida e Volta");
        tabLayout.addTab(ida_volta);

        final TabLayout.Tab multiplos = tabLayout.newTab().setText("Múltiplos");
        tabLayout.addTab(multiplos);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabPagerAgendarTransporte adapter;
        if(colaborador_final_id.isEmpty())
            colaborador_final_id = Helper.current_user(this).getColaborador();
        adapter = new TabPagerAgendarTransporte(getSupportFragmentManager(), tabLayout.getTabCount(),opcao_menu, colaborador_final_id);
        viewPager.setAdapter(adapter);


        if(opcao_menu == Helper.PERNOITE) {
            viewPager.setCurrentItem(1);
            multiplos.select();
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(((opcao_menu == Helper.DIARIA || opcao_menu == Helper.MEIA_DIARIA || opcao_menu == Helper.TRANSLADO) && position == 0) || (opcao_menu == Helper.PERNOITE && position == 1 ) )
                {
                    viewPager.setCurrentItem(tab.getPosition());
                }else
                {

                    if(position == EscolherTransporteActivity.MULIPLOS)
                        ida_volta.select();
                    else
                        multiplos.select();

                    Toast.makeText(ctx,"Opção inválida para o tipo de gn.",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Agendar gn");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager manager = getSupportFragmentManager();
                int entryCount = manager.getBackStackEntryCount();
                if (entryCount == 0) {
                    finish();
                }
                else {
                    manager.popBackStack();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
