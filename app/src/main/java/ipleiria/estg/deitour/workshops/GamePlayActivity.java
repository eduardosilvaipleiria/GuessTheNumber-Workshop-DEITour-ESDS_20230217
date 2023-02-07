package ipleiria.estg.deitour.workshops;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class GamePlayActivity extends AppCompatActivity {

    //Variáveis funcionais da aplicação
    private static final Random gerador = new Random();
    private int numGerado, numTentativas;
    private static final int MIN = 1, MAX = 9, TENTATIVAS_MAX = 5, ONE_SEC = 1000;

    //Variáveis da vista
    private EditText editTextNumber;
    private Button btnVerificar;
    private TextView textViewResultado, textViewNumTentativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String playerName = extras.getString("playerName");

            Snackbar.make(findViewById(android.R.id.content), "Bem-vindo e bom-jogo, " + playerName + "! Neste nível tens tentativas ilimitadas para descobrir o número mágico gerado pela aplicação", Snackbar.LENGTH_LONG).show();
        }

        //Gerar número aleatório
        numGerado = gerarValorAleatorio();

        //Associar as variáveis locais (do código Java) com os elementos da vista
        btnVerificar = findViewById(R.id.btnVerificar);
        editTextNumber = findViewById(R.id.editTextNumber);
        textViewResultado = findViewById(R.id.textViewResultado);
        textViewNumTentativas = findViewById(R.id.textViewNumTentativas);
    }

    private int gerarValorAleatorio() {
        //A função nextInt() gera um número aleatório entre 0 (inclusive) e o valor definido aplicando a fórmula (MAX-MIN+1)+MIN
        int valor = gerador.nextInt(MAX - MIN + 1) + MIN;
        return valor;
    }

    public void onClickBtnVerificar(View view) {
        //Verificar se o utilizador clicou no botão sem introduzir um número
        if (editTextNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Não foi introduzido um número!", Toast.LENGTH_LONG).show();
            return;
        }
        else if (Integer.parseInt(editTextNumber.getText().toString()) < MIN || Integer.parseInt(editTextNumber.getText().toString()) > MAX) {
            Toast.makeText(this, "Introduza um número entre 1 e 9", Toast.LENGTH_LONG).show();
            return;
        }

        //Esconder o teclado
        hideSoftKeyboard(this);

        //Obter o número introduzido pelo utilizador
        int numUser = Integer.parseInt(editTextNumber.getText().toString());

        //Verificar se atingiu o máximo de tentativas e terminar o jogo
        if (numTentativas == TENTATIVAS_MAX) {
            btnVerificar.setEnabled(false);
            editTextNumber.setEnabled(false);
            textViewNumTentativas.setTextColor(Color.RED);
            textViewNumTentativas.setText("Excedeu o número de tentativas");
        }
        else {
            //Incrementar o número de tentativas utilizadas e atualizar o texto
            numTentativas++;

            //Atualizar o número de tentativas restantes
            textViewNumTentativas.setTextColor(Color.BLACK);
            textViewNumTentativas.setText("Número de tentativas restantes: " + (TENTATIVAS_MAX - numTentativas));

            //Veriricar se o número está correto
            if (numUser == numGerado) {
                textViewResultado.setTextColor(Color.rgb(79,143,0));
                textViewResultado.setText("PARABÉNS, conseguiste acertar no número mágico em " + numTentativas + " tentativas!");
                textViewNumTentativas.setText("Para jogar novamente volta à janela anterior e inicia novo jogo");
                editTextNumber.setEnabled(false);
                btnVerificar.setEnabled(false);
            } else {
                //Verificar se o número é superior
                if (numUser > numGerado)
                    countdownToast("Ops! O número mágico é mais baixo... Tenta novamente em ", 3000);
                else
                    countdownToast("Ups! O número mágico é mais alto... Tenta novamente em ", 3000);

                //Bloquear o editTextNumber por 5 segundos
                btnVerificar.setEnabled(false);
                editTextNumber.setEnabled(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        editTextNumber.setText("");
                        editTextNumber.setEnabled(true);
                        btnVerificar.setEnabled(true);
                    }
                }, 3000);   //3 segundos
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void countdownToast(String text, int time) {
        time += 1000;
        final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), text + time, Snackbar.LENGTH_INDEFINITE);
        new CountDownTimer(time, 1000) {
            public void onTick(long m) {
                if(m/1000==0) {
                    snackbar.dismiss();
                    editTextNumber.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editTextNumber, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    snackbar.setText(text + m/1000);
                    snackbar.show();
                }
            }
            public void onFinish() {
                snackbar.dismiss();
            }
        }.start();
    }
}