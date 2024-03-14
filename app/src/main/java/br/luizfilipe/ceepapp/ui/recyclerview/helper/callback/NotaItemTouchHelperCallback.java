package br.luizfilipe.ceepapp.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import br.luizfilipe.ceepapp.dao.NotaDAO;
import br.luizfilipe.ceepapp.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ListaNotasAdapter adapter;
    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter){
        this.adapter = adapter;
    }

    // Defini oque permitimos de animaçao, movimentar para a esquerda direita
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(0, marcacoesDeDeslize);
    }

    // é uma chamada quando o elemento for arrastado
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    // quando o movimento for de deslize este metodo sera chamado
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        new NotaDAO().remove(posicaoDaNotaDeslizada);
        adapter.remove(posicaoDaNotaDeslizada);
    }
}
