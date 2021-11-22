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

public class CycleListAdapter extends ArrayAdapter<CycleAvecTravails> {


    public CycleListAdapter(Context mCtx, List<CycleAvecTravails> objects) {
        super(mCtx, R.layout.adapter_view_layout_cycle, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //récupération du cycle
        final CycleAvecTravails cycleavecTravails = getItem(position);

        //on charge le xml qu'on va inflate
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_view_layout_cycle, parent, false);

        //récupération des objets graphiques
        TextView textViewNom = (TextView) rowView.findViewById(R.id.adapter_nomCycle);
        TextView textViewNbRepet = (TextView) rowView.findViewById(R.id.adapter_nbRepet);

        String strRepetition= getContext().getString(R.string.repetition);
        String space = " ";
        //on remplit le text
        Cycle cycle = cycleavecTravails.getCycle();
        textViewNom.setText(cycle.getNom());
        textViewNbRepet.setText(strRepetition + space +cycle.getRepetition());

        return rowView;
    }
}
