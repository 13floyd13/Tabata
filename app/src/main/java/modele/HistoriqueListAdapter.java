package modele;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tabata.R;

import java.util.List;

public class HistoriqueListAdapter extends ArrayAdapter<Historique> {


    public HistoriqueListAdapter(Context mCtx, List<Historique> objects) {
        super(mCtx, R.layout.adapter_view_layout_historique, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){


        // récupération de l'historique
        final Historique historique = getItem(position);

        //on charge le xml qu'on va inflate
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.adapter_view_layout_historique, parent, false);
        //récupération des objets graphiques
        TextView tvDate = (TextView) rowView.findViewById(R.id.adapter_date);
        TextView tvNomEntrainement = (TextView) rowView.findViewById(R.id.adapter_nomEntrainement);

        tvDate.setText(historique.getDate());
        tvNomEntrainement.setText(historique.getNomEntrainement());

        return rowView;
    }
}
