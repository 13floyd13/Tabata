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
        TextView textViewTravails = (TextView) rowView.findViewById(R.id.adapter_nomsTravails);

        String strRepetition= getContext().getString(R.string.repetition);
        String space = " ";
        String strTravail = getContext().getString(R.string.travail);
        //on remplit le text
        Cycle cycle = cycleavecTravails.getCycle();
        textViewNom.setText(cycle.getNom());


        //récupération des noms des travails pour les afficher
        String strListeTravails = strTravail + "\n";

        for (int i = 0; i < cycleavecTravails.getTravails().size(); i++){

            strListeTravails += cycleavecTravails.getTravails().get(i).getNom();
            strListeTravails += space;
        }

        textViewTravails.setText(strListeTravails);


        //récupération du nombre de répétitions en fonction de certains Intent ou l'info ne passe pas
        if(cycle.getRepetition() != 0){
            textViewNbRepet.setText(strRepetition + space +cycle.getRepetition());
        }else{
            textViewNbRepet.setText(strRepetition + space + cycleavecTravails.getNbRepet());
        }

        return rowView;
    }
}
