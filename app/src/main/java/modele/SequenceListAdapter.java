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

public class SequenceListAdapter extends ArrayAdapter<SequenceAvecCycles> {


    public SequenceListAdapter(Context mCtx, List<SequenceAvecCycles> objects) {
        super(mCtx, R.layout.adapter_view_layout_sequence, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //récupération de la séquence
        final SequenceAvecCycles sequenceAvecCycles = getItem(position);

        //on charge le xml à inflate
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.adapter_view_layout_sequence, parent, false);

        //récupération des objets graphiques
        TextView textViewNom = (TextView) rowView.findViewById(R.id.adapter_nomSequence);
        TextView textViewDescription = (TextView) rowView.findViewById(R.id.adapter_descriptionSequence);
        TextView textViewTempsReposLong = (TextView) rowView.findViewById(R.id.adapter_tempsReposLong);
        TextView textViewNbRepet = (TextView) rowView.findViewById(R.id.adapter_nbRepet);
        TextView textViewCycles = (TextView) rowView.findViewById(R.id.adapter_nomsCycles);

        //récupération des strings en ressources
        String strTemps = getContext().getString(R.string.temps);
        String strRepos = getContext().getString(R.string.reposLong);
        String strSecondes = getContext().getString(R.string.secondes);
        String space = " ";
        String strRepetition= getContext().getString(R.string.repetition);
        String strCycles = getContext().getString(R.string.cycle);
        //on récupère les infos et on remplit de les textView
        Sequence sequence = sequenceAvecCycles.getSequence();

        textViewNom.setText(sequence.getNom());
        textViewDescription.setText(sequence.getDescription());
        textViewTempsReposLong.setText(strTemps + space + strRepos + space + sequence.getTempsReposLong() + space + strSecondes);

        //récupérations des noms des cycles pour les afficher
        String strListesCycles = strCycles + space;

        for (int i = 0; i < sequenceAvecCycles.getCycles().size(); i++){

            strListesCycles += sequenceAvecCycles.getCycles().get(i).getNom();
            strListesCycles += space;
        }

        textViewCycles.setText(strListesCycles);


        //récupération du nombre de répétitions en fonction de certains Intent ou rl'info ne passe pas
        if(sequence.getRepetition() != 0){
            textViewNbRepet.setText(strRepetition + space + sequence.getRepetition());
        }else{
            textViewNbRepet.setText(strRepetition + space + sequenceAvecCycles.getNbRepet());
        }




        return rowView;
    }
}
