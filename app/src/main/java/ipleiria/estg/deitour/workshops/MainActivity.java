package ipleiria.estg.deitour.workshops;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ipleiria.estg.deitour.workshops.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    // String para graver o nome do jogador
    private String playerName;

    // Variáveis da vista:
    // Introdução da do nome do jogador
    private EditText editTextTextPersonName;
    // Botão Limpar
    private Button btnStartGame;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "App/jogo desenvolvido numa atividade DEI@Tour na Escola Secundária Domingos Sequeira", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.btnStartGame = findViewById(R.id.btnStartGame);
        this.editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClickBtnStartGame(View view) {
        // Cria a intenção de iniciar a atividade GamePlayActivity
        Intent intent = new Intent(MainActivity.this, GamePlayActivity.class);

        //Obter o texto preenchido no campo do Nome do Jogador
        playerName = this.editTextTextPersonName.getText().toString().trim();

        //Verificar se o texto do Nome do Jogador foi preenchido, e executar ação
        if(!playerName.isEmpty()) {

            //Enviar o nome para a GamePlayActivity
            intent.putExtra("playerName", playerName);

            // Começa a nova atividade
            startActivity(intent);
        }
        // Caso algum esteja vazio, é mostrada uma mensagem de erro
        else{
            Toast.makeText(this, "Preenche o teu nome para iniciar o jogo", Toast.LENGTH_LONG).show();
        }

    }
}