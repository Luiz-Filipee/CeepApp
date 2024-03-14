package br.luizfilipe.ceepapp.ui.activity;

import static br.luizfilipe.ceepapp.ui.activity.Constantes.CHAVE_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CHAVE_POSICAO;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.POSICAO_INVALIDA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.TITULO_APPBAR_LISTA_NOTAS_ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.luizfilipe.ceepapp.R;
import br.luizfilipe.ceepapp.dao.NotaDAO;
import br.luizfilipe.ceepapp.model.Nota;
import br.luizfilipe.ceepapp.ui.recyclerview.adapter.ListaNotasAdapter;
import br.luizfilipe.ceepapp.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.luizfilipe.ceepapp.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

public class ListaNotasActivity extends AppCompatActivity {
    private ListaNotasAdapter adapter;
    private static final String CHAVE_CONTADOR = "contador";
    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR_LISTA_NOTAS_ACTIVITY);


        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        condifuraBotaoInsereNota();

    }


    private void condifuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        FloatingActionButton botaoInsere = findViewById(R.id.lista_notas_botao_insere);
        botaoInsere.setOnClickListener(v -> {
            vaiParaFormularioNotaActivityInsere();
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent intent =
                new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private static List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoInsereNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                adiciona(notaRecebida);
            }
        }
        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private static boolean ehResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode)
                && temNota(data);
    }

    private static boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }


    private static boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && temNota(data);
    }

    private static boolean temNota(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private static boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private static boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }


    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas); // anexa os comportamentos ao recyclerView
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

}