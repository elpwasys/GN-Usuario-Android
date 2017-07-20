package br.com.wasys.gn.usuario.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.usuario.R;
import br.com.wasys.gn.usuario.fragments.ScheduledTransport;
import br.com.wasys.gn.usuario.fragments.SchedulerTransport;
import br.com.wasys.gn.usuario.helpers.Helper;
import br.com.wasys.gn.usuario.services.AvaliacaoService;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    SharedPreferences settings;
    public static final String PREFS_NAME = "AOP_PREFS";
    boolean possui_avaliacao = false;
    ProgressDialog progress;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_name);
        TextView menu_title = (TextView)hView.findViewById(R.id.menu_title);
        settings = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        JSONObject response_json = null;
        try {
            response_json = new JSONObject(json_string);
            nav_user.setText(response_json.getJSONObject("usuario").getString("nome"));
            menu_title.setText(response_json.getJSONObject("usuario").getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setupTabs();
        verificaAvaliacao();

    }

    public void showProgressDialog()
    {
        progress = new ProgressDialog(this);
        progress.setMessage("Carregando....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progress.setIndeterminate(true);
        progress.show();

    }


    public void verificaAvaliacao()
    {

        showProgressDialog();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AvaliacaoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AvaliacaoService service = retrofit.create(AvaliacaoService.class);

        Call<ResponseBody> response = service.getAvaliacao(Long.parseLong(Helper.current_user(this).getColaborador()));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {

                        JSONObject resposta = response_json.getJSONObject("result");
                        if (resposta.has("solicitacao")) {
                            progress.hide();
                            avaliacaoMotorista(resposta);

                        } else {
                            progress.hide();
                        }

                    } else {
                        progress.hide();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress.hide();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.hide();
            }

        });

    }

    public void avaliacaoMotorista(final JSONObject obj_json)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_avaliacao_motorista);
        dialog.setTitle("Title...");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        TextView motorista = (TextView) dialog.findViewById(R.id.txt_motorista);
        TextView carro = (TextView) dialog.findViewById(R.id.txt_carro);
        try{
            motorista.setText(obj_json.getJSONObject("motorista").getString("nome"));
            carro.setText(obj_json.getJSONObject("carro").getString("modelo"));
        }catch (Exception e)
        {

        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(mContext,AvaliacaoMotoristaActivity.class);
                Bundle b= new Bundle();
                b.putString("obj_json", obj_json.toString());
                i.putExtras(b);
                startActivity(i);
            }
        });
        dialog.show();
    }


    public void setupTabs() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int i = mViewPager.getCurrentItem();
            if (i == 0) {
                drawer.openDrawer(GravityCompat.START);
            }
            else {
                i--;
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent i;
        int id = item.getItemId();
        if (id == R.id.nav_termos_de_uso) {
            Bundle extras = new Bundle();
            extras.putBoolean("from_termo",true);
            Intent intent = new Intent(this, TermosDeUsoActivity.class);
            intent.putExtras(extras);
            startActivity(intent);

        }else if (id == R.id.nav_historicos)
        {
            i = new Intent(this, HistoricoTransportesActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_ajuda) {
            i = new Intent(this, TutorialActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sobre) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_sobre);
            dialog.setTitle("Title...");
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
        }else if(id == R.id.nav_sair)
        {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_sair);
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();

        }else if(id == R.id.nav_avaliacoes)
        {
            progress.hide();
            verificaAvaliacao();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout()
    {

        SharedPreferences preferences = getSharedPreferences("AOP_PREFS", 0);
        preferences.edit().remove("login").commit();

        SharedPreferences settings = getSharedPreferences("appDataStorage", Context.MODE_PRIVATE);
        settings.edit().remove("trechos").commit();

        SharedPreferences clear_preferences = getPreferences(0);
        SharedPreferences.Editor editor = clear_preferences.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                //Fragement for Android Tab
                return new SchedulerTransport();
                case 1:
                    //Fragment for Ios Tab
                    return new ScheduledTransport();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
            }
            return null;
        }
    }
}
