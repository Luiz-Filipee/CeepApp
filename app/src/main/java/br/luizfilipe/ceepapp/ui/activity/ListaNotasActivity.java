package br.luizfilipe.ceepapp.ui.activity;

import static br.luizfilipe.ceepapp.ui.activity.Constantes.CHAVE_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static br.luizfilipe.ceepapp.ui.activity.Constantes.TITULO_APPBAR_LISTA_NOTAS_ACTIVITY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import br.luizfilipe.ceepapp.R;
import br.luizfilipe.ceepapp.dao.NotaDAO;
import br.luizfilipe.ceepapp.model.Nota;
import br.luizfilipe.ceepapp.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {
    private ListaNotasAdapter adapter;


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
        botaoInsereNota.setOnClickListener(v -> {
            abreFormularioNotaActivity();
        });
    }

    private void abreFormularioNotaActivity() {
        Intent intent =
                new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(intent, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private static List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        List<Nota> todasNotas;
        todasNotas = dao.todos();
        return todasNotas;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoComNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private static boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && ehCodigoRequestInsereNota(resultCode) && temNota(data);
    }

    private static boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private static boolean ehCodigoRequestInsereNota(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private static boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }


    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }

}