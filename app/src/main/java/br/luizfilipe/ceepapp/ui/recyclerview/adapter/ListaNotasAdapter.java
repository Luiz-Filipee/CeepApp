package br.luizfilipe.ceepapp.ui.recyclerview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.luizfilipe.ceepapp.R;
import br.luizfilipe.ceepapp.model.Nota;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {
    private final List<Nota> notas;
    private final Context context;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    @NonNull
    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflando uma view para ser mostrada ne tela, neste caso a item_nota sera mostrado
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotasAdapter.NotaViewHolder holder, int position) {
        Nota notaSelecionada = notas.get(position); // Pegando uma nota refernte a posição
        holder.vincula(notaSelecionada); // chamando metodo para setar os textos dos campos dos textView
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    // Essa clase e necessario pois e ela que sera criada no onCreateViewHolder()
    // sendo assim possivel reutilizar as views de maneira performatica
    class NotaViewHolder extends RecyclerView.ViewHolder {
        private final TextView titulo;
        private final TextView descricao;
        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        public void vincula(Nota nota){
            preencheCampos(nota);
        }

        private void preencheCampos(Nota nota) {
            titulo.setText(nota.getTitulo()); // setando o titulo da recyclerview
            descricao.setText(nota.getDescricao()); // setando o titulo da recyclerview
        }
    }

    public void adiciona(Nota nota){
        notas.add(nota);
        notifyDataSetChanged();
    }
}
