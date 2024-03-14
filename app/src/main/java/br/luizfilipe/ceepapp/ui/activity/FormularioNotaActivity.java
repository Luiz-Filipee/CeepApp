package br.luizfilipe.ceepapp.ui.activity;

import static br.luizfilipe.ceepapp.ui.activity.Constantes.CHAVE_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CHAVE_POSICAO;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.POSICAO_INVALIDA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.TITULO_APPBAR_ALTERA_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.TITULO_APPBAR_INSERE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.luizfilipe.ceepapp.R;
import br.luizfilipe.ceepapp.model.Nota;

public class FormularioNotaActivity extends AppCompatActivity {
    // Por padrao o java inicializa 0, no caso com uma posiçao valida, por isso devemos inicializar com um
    // numero que referencia uma posicao invalida
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle(TITULO_APPBAR_INSERE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializaCampos();

        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_NOTA)) {
            setTitle(TITULO_APPBAR_ALTERA_NOTA);
            Nota nota = (Nota) dados.getSerializableExtra(CHAVE_NOTA);
            posicaoRecebida = dados.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            preencheCampos(nota);
        }

    }

    private void inicializaCampos() {
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    private void preencheCampos(Nota nota) {
        titulo.setText(nota.getTitulo());
        descricao.setText(nota.getDescricao());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (ehMenuSalvaNota(item)) {
            Nota notaCriada = criaNota();
            retornaNota(notaCriada);
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);
        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    @NonNull
    private Nota criaNota() {
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }

    private boolean ehMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.formulario_item_salva;
    }
}
