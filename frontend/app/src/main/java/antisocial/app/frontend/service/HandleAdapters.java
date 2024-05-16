package antisocial.app.frontend.service;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import antisocial.app.frontend.adapter.IAdapter;

public class HandleAdapters {

    private List<IAdapter> adapters;

    public HandleAdapters(List<IAdapter> adapters) {
        this.adapters = adapters;
    }

    public void setAdapter(RecyclerView recyclerView, String type, Context context){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.Adapter adapter = (RecyclerView.Adapter) adapters.stream().filter(adapterNeeded
                -> adapterNeeded.isNeeded(type)).collect(Collectors.toList()).get(0);
        recyclerView.setAdapter(adapter);
    }
}
